package com.gmeloni.shelly.dto.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ShellyProEMSys {
    @JsonProperty("mac")
    private String macAddress;
    @JsonProperty("unixtime")
    private Long unixTime;
}
