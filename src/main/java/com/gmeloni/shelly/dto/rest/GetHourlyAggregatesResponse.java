package com.gmeloni.shelly.dto.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmeloni.shelly.dto.db.EnergyTotals;
import com.gmeloni.shelly.dto.db.HourlyAggregate;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetHourlyAggregatesResponse {
    @JsonProperty("totals")
    private EnergyTotals totals;
    @JsonProperty("aggregates")
    private List<HourlyAggregate> hourlyAggregates;
}
