package com.gmeloni.shelly.service;

import com.gmeloni.shelly.dto.DeviceLocalIPAddressAndChannel;
import com.gmeloni.shelly.dto.rest.GetShellyEMStatusResponse;
import com.gmeloni.shelly.dto.rest.GetShellyProEMStatusResponse;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static com.gmeloni.shelly.Constants.DATE_AND_TIME_FORMAT;

@Service
public class ScheduledService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final DateTimeFormatter sampleFormatter = DateTimeFormatter.ofPattern(DATE_AND_TIME_FORMAT);
    private final DateTimeFormatter hourlyAggregationFormatter = DateTimeFormatter.ofPattern(DATE_AND_TIME_FORMAT);
    @Value("#{'${shelly.em.ip.addresses}'.split(',')}")
    private List<String> shellyEMIpAddresses;
    @Value("#{'${shelly.pro.em.ip.addresses}'.split(',')}")
    private List<String> shellyProEMIpAddresses;
    @Value("${raw-em.sampling.period.milliseconds}")
    private String samplingPeriodInMilliseconds;
    @Value("${raw-em.sampling.threshold}")
    private Double samplingThreshold;

    @Autowired
    private RawEMDataService rawEMDataService;
    @Autowired
    private RawEMDataRepository rawEmDataRepository;
    @Autowired
    private HourlyEMEnergyRepository hourlyEMEnergyRepository;

    @Scheduled(fixedRateString = "${raw-em.sampling.period.milliseconds}")
    public void processRawEMData() {
        for (String shellyEMIpAddress : shellyEMIpAddresses) {
            GetShellyEMStatusResponse shellyEMStatusResponse = rawEMDataService.getShellyEMRawSamples(shellyEMIpAddress);
            LocalDateTime sampleDateTime = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(shellyEMStatusResponse.getUnixTime()),
                    TimeZone.getTimeZone("Europe/Rome").toZoneId()
            );
            String deviceId = "SHELLY_EM_" + shellyEMStatusResponse.getMacAddress();
            Double channel0Power = shellyEMStatusResponse.getRawEMSamples().get(0).getPower();
            Double channel1Power = shellyEMStatusResponse.getRawEMSamples().get(1).getPower();
            rawEmDataRepository.save(new RawEMData(
                    deviceId,
                    0,
                    sampleDateTime,
                    shellyEMIpAddress,
                    channel0Power
            ));
            log.info("Saved raw EM data: [{}, {}, {}, {}, {}]", sampleFormatter.format(sampleDateTime), deviceId, 0, shellyEMIpAddress, String.format("%.2f", channel0Power));
            rawEmDataRepository.save(new RawEMData(
                    deviceId,
                    1,
                    sampleDateTime,
                    shellyEMIpAddress,
                    channel1Power
            ));
            log.info("Saved raw EM data: [{}, {}, {}, {}, {}]", sampleFormatter.format(sampleDateTime), deviceId, 1, shellyEMIpAddress, String.format("%.2f", channel1Power));
        }
        for (String shellyProEMIpAddress : shellyProEMIpAddresses) {
            GetShellyProEMStatusResponse shellyProEMStatusResponse = rawEMDataService.getShellyProEMRawSamples(shellyProEMIpAddress);
            LocalDateTime sampleDateTime = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(shellyProEMStatusResponse.getShellyProEMSys().getUnixTime()),
                    TimeZone.getTimeZone("Europe/Rome").toZoneId()
            );
            String deviceId = "SHELLY_EM_" + shellyProEMStatusResponse.getShellyProEMSys().getMacAddress();
            Double channel0Power = shellyProEMStatusResponse.getShellyProEMRawSampleChannel0().getPower();
            Double channel1Power = shellyProEMStatusResponse.getShellyProEMRawSampleChannel1().getPower();
            rawEmDataRepository.save(new RawEMData(
                    deviceId,
                    0,
                    sampleDateTime,
                    shellyProEMIpAddress,
                    channel0Power
            ));
            log.info("Saved raw EM data: [{}, {}, {}, {}, {}]", sampleFormatter.format(sampleDateTime), deviceId, 0, shellyProEMIpAddress, String.format("%.2f", channel0Power));
            rawEmDataRepository.save(new RawEMData(
                    deviceId,
                    1,
                    sampleDateTime,
                    shellyProEMIpAddress,
                    channel1Power
            ));
            log.info("Saved raw EM data: [{}, {}, {}, {}, {}]", sampleFormatter.format(sampleDateTime), deviceId, 1, shellyProEMIpAddress, String.format("%.2f", channel1Power));
        }
    }

    @Scheduled(cron = "${raw-em.hourly-aggregate.cron.schedule}", zone = "Europe/Rome")
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
