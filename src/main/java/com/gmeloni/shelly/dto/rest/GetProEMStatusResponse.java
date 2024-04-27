package com.gmeloni.shelly.dto.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetProEMStatusResponse {
    @JsonProperty("em1:0")
    private RawProEMSample rawProEMSampleId0;
    @JsonProperty("em1:1")
    private RawProEMSample rawProEMSampleId1;
}
