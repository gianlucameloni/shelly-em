package com.gmeloni.shelly.dto.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RawEMSample {
    @JsonProperty("power")
    private Double power;
    @JsonProperty("voltage")
    private Double voltage;
}
