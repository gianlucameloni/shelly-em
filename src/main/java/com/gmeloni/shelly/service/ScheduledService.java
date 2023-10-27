package com.gmeloni.shelly.service;

import com.gmeloni.shelly.dto.rest.GetEMStatusResponse;
import com.gmeloni.shelly.model.HourlyEMEnergy;
import com.gmeloni.shelly.model.RawEMData;
import com.gmeloni.shelly.repository.HourlyEMEnergyRepository;
import com.gmeloni.shelly.repository.RawEMDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.TimeZone;

@Service
public class ScheduledService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final DateTimeFormatter sampleFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DateTimeFormatter hourlyAggregationFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    private RawEMService rawEMService;
    @Autowired
    private RawEMDataRepository rawEmDataRepository;
    @Autowired
    private HourlyEMEnergyRepository hourlyEMEnergyRepository;
    @Value("${raw-em.sampling.period.milliseconds}")
    private String samplingPeriodInMilliseconds;
    @Value("${raw-em.sampling.threshold}")
    private Double samplingThreshold;

    @Scheduled(fixedRateString = "${raw-em.sampling.period.milliseconds}")
    public void processRawEMData() {
        GetEMStatusResponse getEMStatusResponse = rawEMService.getRawEMSamples();
        LocalDateTime sampleDateTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(getEMStatusResponse.getUnixTime()),
                TimeZone.getTimeZone("Europe/Rome").toZoneId()
        );
        Double gridPower = getEMStatusResponse.getRawEMSamples().get(0).getPower();
        Double pvPower = getEMStatusResponse.getRawEMSamples().get(1).getPower();
        Double gridVoltage = getEMStatusResponse.getRawEMSamples().get(0).getVoltage();
        Double pvVoltage = getEMStatusResponse.getRawEMSamples().get(1).getVoltage();
        RawEMData rawEmData = new RawEMData(
                sampleDateTime,
                gridPower,
                pvPower,
                gridVoltage,
                pvVoltage
        );
        rawEmDataRepository.save(rawEmData);
        log.info("Saved raw EM data: [{}, {}, {}, {}, {}]", sampleFormatter.format(sampleDateTime), String.format("%.2f", gridPower), String.format("%.2f", pvPower), String.format("%.2f", gridVoltage), String.format("%.2f", pvVoltage));
    }

    @Scheduled(cron = "${raw-em.hourly-aggregation.cron.schedule}", zone = "Europe/Rome")
    public void processHourlyAggregatedData() {
        final double dt = Double.parseDouble(samplingPeriodInMilliseconds) / 1000 / 3600;
        LocalDateTime fromTimestamp = LocalDateTime.now().minusHours(1).truncatedTo(ChronoUnit.HOURS);
        LocalDateTime toTimestamp = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        List<RawEMData> rawEMDataSamples = rawEmDataRepository.findAllBySampleTimestampBetween(fromTimestamp, toTimestamp);
        Double gridEnergyIn = rawEMDataSamples
                .stream()
                .filter(s -> (s.getGridPower() > samplingThreshold))
                .mapToDouble(d -> d.getGridPower() * dt)
                .sum();
        Double gridEnergyOut = rawEMDataSamples
                .stream()
                .filter(s -> (s.getGridPower() < -1 * samplingThreshold))
                .mapToDouble(d -> Math.abs(d.getGridPower()) * dt)
                .sum();
        Double pvEnergyIn = rawEMDataSamples
                .stream()
                .filter(s -> (s.getPvPower() > samplingThreshold))
                .mapToDouble(d -> d.getPvPower() * dt)
                .sum();
        Double pvEnergyOut = rawEMDataSamples
                .stream()
                .filter(s -> (s.getPvPower() < -1 * samplingThreshold))
                .mapToDouble(d -> Math.abs(d.getPvPower()) * dt)
                .sum();
        Double maxGridVoltage = rawEMDataSamples
                .stream()
                .mapToDouble(d -> d.getGridVoltage())
                .max()
                .getAsDouble();
        Double minGridVoltage = rawEMDataSamples
                .stream()
                .mapToDouble(d -> d.getGridVoltage())
                .min()
                .getAsDouble();
        Double maxPvVoltage = rawEMDataSamples
                .stream()
                .mapToDouble(d -> d.getPvVoltage())
                .max()
                .getAsDouble();
        Double minPvVoltage = rawEMDataSamples
                .stream()
                .mapToDouble(d -> d.getPvVoltage())
                .min()
                .getAsDouble();
        hourlyEMEnergyRepository.save(new HourlyEMEnergy(fromTimestamp, toTimestamp, gridEnergyIn, gridEnergyOut, pvEnergyIn, pvEnergyOut, maxGridVoltage, minGridVoltage, maxPvVoltage, minPvVoltage));
        log.info("Saved aggregated EM data: [{}, {}, {}, {}, {}, {}, {}, {}, {}, {}]", hourlyAggregationFormatter.format(fromTimestamp), hourlyAggregationFormatter.format(toTimestamp), String.format("%.2f", gridEnergyIn), String.format("%.2f", gridEnergyOut), String.format("%.2f", pvEnergyIn), String.format("%.2f", pvEnergyOut), String.format("%.2f", maxGridVoltage), String.format("%.2f", minGridVoltage), String.format("%.2f", maxPvVoltage), String.format("%.2f", minPvVoltage));
        for (RawEMData r : rawEMDataSamples) {
            rawEmDataRepository.delete(r);
        }
        log.info("Removed old raw EM data");
    }

}
