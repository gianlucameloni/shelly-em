package com.gmeloni.shelly.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.text.DecimalFormat;

import static com.gmeloni.shelly.Constants.ENERGY_DECIMAL_FORMAT;

@Data
public class EnergyTotals {

    @JsonProperty("gridEnergyIn")
    private String gridEnergyIn;
    @JsonProperty("gridEnergyOut")
    private String gridEnergyOut;
    @JsonProperty("pvEnergyIn")
    private String pvEnergyIn;
    @JsonProperty("pvEnergyOut")
    private String pvEnergyOut;

    public EnergyTotals(Double gridEnergyIn, Double gridEnergyOut, Double pvEnergyIn, Double pvEnergyOut) {
        DecimalFormat dataFormat = new DecimalFormat(ENERGY_DECIMAL_FORMAT);
        this.gridEnergyIn = dataFormat.format(gridEnergyIn);
        this.gridEnergyOut = dataFormat.format(gridEnergyOut);
        this.pvEnergyIn = dataFormat.format(pvEnergyIn);
        this.pvEnergyOut = dataFormat.format(pvEnergyOut);
    }

}
