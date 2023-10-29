package com.gmeloni.shelly.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmeloni.shelly.Utilities;

import java.text.DecimalFormat;

import static com.gmeloni.shelly.Constants.ENERGY_DECIMAL_FORMAT;

public class MonthlyAggregate {

    @JsonProperty("month")
    private String month;
    @JsonProperty("gridEnergyIn")
    private String gridEnergyIn;
    @JsonProperty("gridEnergyOut")
    private String gridEnergyOut;
    @JsonProperty("pvEnergyIn")
    private String pvEnergyIn;
    @JsonProperty("pvEnergyOut")
    private String pvEnergyOut;

    public MonthlyAggregate(String month, Double gridEnergyIn, Double gridEnergyOut, Double pvEnergyIn, Double pvEnergyOut) {
        DecimalFormat dataFormat = new DecimalFormat(ENERGY_DECIMAL_FORMAT);
        this.month = Utilities.padLeftWithZeros(month,2);
        this.gridEnergyIn = dataFormat.format(gridEnergyIn);
        this.gridEnergyOut = dataFormat.format(gridEnergyOut);
        this.pvEnergyIn = dataFormat.format(pvEnergyIn);
        this.pvEnergyOut = dataFormat.format(pvEnergyOut);
    }

}
