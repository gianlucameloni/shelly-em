package com.gmeloni.shelly.dto.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmeloni.shelly.dto.db.EnergyTotals;
import com.gmeloni.shelly.dto.db.MonthlyAggregate;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetMonthlyAggregatedResponse {
    @JsonProperty("totals")
    private EnergyTotals totals;
    @JsonProperty("aggregates")
    private List<MonthlyAggregate> monthlyAggregates;
}
