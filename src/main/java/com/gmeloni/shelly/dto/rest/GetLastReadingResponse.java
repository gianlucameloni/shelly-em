package com.gmeloni.shelly.dto.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmeloni.shelly.model.RawEMData;
import lombok.Data;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

import static com.gmeloni.shelly.Constants.DATE_AND_TIME_FORMAT;
import static com.gmeloni.shelly.Constants.POWER_AND_VOLTAGE_DECIMAL_FORMAT;

@Data
public class GetLastReadingResponse {

    @JsonProperty("sample_timestamp")
    private String sampleTimestamp;
    @JsonProperty("grid_power")
    private String gridPower;
    @JsonProperty("pv_power")
    private String pvPower;
    @JsonProperty("grid_voltage")
    private String gridVoltage;
    @JsonProperty("pv_voltage")
    private String pvVoltage;

    public GetLastReadingResponse(RawEMData rawEMData) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_AND_TIME_FORMAT);
        DecimalFormat dataFormat = new DecimalFormat(POWER_AND_VOLTAGE_DECIMAL_FORMAT);
        this.sampleTimestamp = dateTimeFormatter.format(rawEMData.getSampleTimestamp());
        this.gridPower = dataFormat.format(rawEMData.getGridPower());
        this.pvPower = dataFormat.format(rawEMData.getPvPower());
        this.gridVoltage = dataFormat.format(rawEMData.getGridVoltage());
        this.pvVoltage = dataFormat.format(rawEMData.getPvVoltage());
    }

}
