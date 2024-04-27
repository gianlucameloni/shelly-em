package com.gmeloni.shelly.dto.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RawProEMSample {
    @JsonProperty("act_power")
    private Double power;
    @JsonProperty("voltage")
    private Double voltage;
}