package com.gmeloni.shelly.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RawEMSample {
    @JsonProperty("power") private Double power;
}
