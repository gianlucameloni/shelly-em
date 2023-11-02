package com.gmeloni.shelly.controller;

import com.gmeloni.shelly.dto.db.DailyAggregate;
import com.gmeloni.shelly.dto.db.EnergyTotals;
import com.gmeloni.shelly.dto.db.HourlyAggregate;
import com.gmeloni.shelly.dto.db.MonthlyAggregate;
import com.gmeloni.shelly.dto.rest.GetDailyAggregatesResponse;
import com.gmeloni.shelly.dto.rest.GetHourlyAggregatesResponse;
import com.gmeloni.shelly.dto.rest.GetLastReadingResponse;
import com.gmeloni.shelly.dto.rest.GetMonthlyAggregatedResponse;
import com.gmeloni.shelly.service.RawEMDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MonitoringController {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private RawEMDataService rawEMDataService;

    @Tag(name = "Aggregates")
    @Operation(summary = "Retrieve hourly aggregates by year, month and day of interest")
    @GetMapping(
            value = "/aggregates/hourly",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public GetHourlyAggregatesResponse findHourlyAggregates(
            @Parameter(description = "Year of interest for the aggregates", required = true) @RequestParam(value = "year") String year,
            @Parameter(description = "Month of interest for the aggregates", required = true) @RequestParam(value = "month") String month,
            @Parameter(description = "Day of interest for the aggregates", required = true) @RequestParam("day") String day
    ) {
        String filterDate = String.format("%s-%s-%s", year, month, day);
        List<HourlyAggregate> hourlyAggregates = (List<HourlyAggregate>) entityManager
                .createNamedQuery("SelectHourlyAggregateByYearMonthDay")
                .setParameter("filterDate", filterDate)
                .getResultList();
        EnergyTotals totals = (EnergyTotals) entityManager
                .createNamedQuery("SelectTotalsByYearMonthDay")
                .setParameter("filterDate", filterDate)
                .getSingleResult();
        return new GetHourlyAggregatesResponse(totals, hourlyAggregates);
    }

    @Tag(name = "Aggregates")
    @Operation(summary = "Retrieve daily aggregates by year and month of interest")
    @GetMapping(
            value = "/aggregates/daily",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public GetDailyAggregatesResponse findDailyAggregates(
            @Parameter(description = "Year of interest for the aggregates", required = true) @RequestParam("year") String year,
            @Parameter(description = "Month of interest for the aggregates", required = true) @RequestParam("month") String month
    ) {
        String filterDate = String.format("%s-%s-%s", year, month, "01");
        List<DailyAggregate> dailyAggregates = (List<DailyAggregate>) entityManager
                .createNamedQuery("SelectDailyAggregateByYearMonth")
                .setParameter("filterDate", filterDate)
                .getResultList();
        EnergyTotals totals = (EnergyTotals) entityManager
                .createNamedQuery("SelectTotalsByYearMonth")
                .setParameter("filterDate", filterDate)
                .getSingleResult();
        return new GetDailyAggregatesResponse(totals, dailyAggregates);
    }

    @Tag(name = "Aggregates")
    @Operation(summary = "Retrieve monthly aggregates by year of interest")
    @GetMapping(
            value = "/aggregates/monthly",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public GetMonthlyAggregatedResponse findMonthlyAggregates(
            @Parameter(description = "Year of interest for the aggregates", required = true) @RequestParam("year") String year
    ) {
        String filterDate = String.format("%s-%s-%s", year, "01", "01");
        List<MonthlyAggregate> monthlyAggregates = (List<MonthlyAggregate>) entityManager
                .createNamedQuery("SelectMonthlyAggregateByYear")
                .setParameter("filterDate", filterDate)
                .getResultList();
        EnergyTotals totals = (EnergyTotals) entityManager
                .createNamedQuery("SelectTotalsByYear")
                .setParameter("filterDate", filterDate)
                .getSingleResult();
        return new GetMonthlyAggregatedResponse(totals, monthlyAggregates);
    }

    @Tag(name = "Aggregates")
    @Operation(summary = "Retrieve all-times aggregates")
    @GetMapping(
            value = "/aggregates/all",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public EnergyTotals findAllTimesAggregates(
    ) {
        return (EnergyTotals) entityManager
                .createNamedQuery("SelectTotals")
                .getSingleResult();
    }

    @Tag(name = "Readings")
    @Operation(summary = "Retrieve the last power readings")
    @GetMapping(
            value = "/readings/last",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public GetLastReadingResponse findLastReading(
    ) {
        return new GetLastReadingResponse(rawEMDataService.getLastReading());
    }

}
