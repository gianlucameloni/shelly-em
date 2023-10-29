package com.gmeloni.shelly.controller;

import com.gmeloni.shelly.dto.DailyAggregate;
import com.gmeloni.shelly.dto.GetLastReadingResponse;
import com.gmeloni.shelly.dto.HourlyAggregate;
import com.gmeloni.shelly.dto.MonthlyAggregate;
import com.gmeloni.shelly.service.RawEMService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EMController {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private RawEMService rawEMService;

    @GetMapping(
            value = "/aggregate/hourly",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<HourlyAggregate> findHourlyAggregated(
            @RequestParam("year") String year,
            @RequestParam("month") String month,
            @RequestParam("day") String day
    ) {
        String filterDate = String.format("%s-%s-%s", year, month, day);
        return entityManager
                .createNamedQuery("SelectHourlyAggregateByYearMonthDay")
                .setParameter("filterDate", filterDate)
                .getResultList();
    }

    @GetMapping(
            value = "/aggregate/daily",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<DailyAggregate> findDailyAggregated(
            @RequestParam("year") String year,
            @RequestParam("month") String month
    ) {
        String filterDate = String.format("%s-%s-%s", year, month, "01");
        return entityManager
                .createNamedQuery("SelectDailyAggregateByYearMonth")
                .setParameter("filterDate", filterDate)
                .getResultList();
    }

    @GetMapping(
            value = "/aggregate/monthly",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<MonthlyAggregate> findMonthlyAggregated(
            @RequestParam("year") String year
    ) {
        String filterDate = String.format("%s-%s-%s", year, "01", "01");
        return entityManager
                .createNamedQuery("SelectMonthlyAggregateByYear")
                .setParameter("filterDate", filterDate)
                .getResultList();
    }

    @GetMapping(
            value = "/totals",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<DailyAggregate> findTotals(
    ) {
        return entityManager
                .createNamedQuery("SelectTotals")
                .getResultList();
    }

    @GetMapping(
            value = "/last",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public GetLastReadingResponse findLastReading(
    ) {
        return new GetLastReadingResponse(rawEMService.getLastReading());
    }

}
