package com.gmeloni.shelly.dto.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmeloni.shelly.dto.RawEMSample;
import lombok.Data;

import java.util.List;

@Data
public class GetEMStatusResponse {
    @JsonProperty("unixtime")
    private Long unixTime;
    @JsonProperty("emeters")
    private List<RawEMSample> rawEMSamples;
}
