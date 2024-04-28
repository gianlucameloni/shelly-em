package com.gmeloni.shelly.dto.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetShellyProEMStatusResponse {
    @JsonProperty("em1:0")
    private ShellyProEMRawSample shellyProEMRawSampleChannel0;
    @JsonProperty("em1:1")
    private ShellyProEMRawSample shellyProEMRawSampleChannel1;
    @JsonProperty("sys")
    private ShellyProEMSys shellyProEMSys;
}
