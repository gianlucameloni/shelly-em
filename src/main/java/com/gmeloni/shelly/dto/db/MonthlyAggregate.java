package com.gmeloni.shelly.dto.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmeloni.shelly.Utilities;

import java.text.DecimalFormat;

import static com.gmeloni.shelly.Constants.ENERGY_DECIMAL_FORMAT;

public class MonthlyAggregate {

    @JsonProperty("month")
    private final String month;
    @JsonProperty("grid_energy_in")
    private final String gridEnergyIn;
    @JsonProperty("grid_energy_out")
    private final String gridEnergyOut;
    @JsonProperty("pv_energy_in")
    private final String pvEnergyIn;
    @JsonProperty("pv_energy_out")
    private final String pvEnergyOut;

    public MonthlyAggregate(String month, Double gridEnergyIn, Double gridEnergyOut, Double pvEnergyIn, Double pvEnergyOut) {
        DecimalFormat dataFormat = new DecimalFormat(ENERGY_DECIMAL_FORMAT);
        this.month = Utilities.padLeftWithZeros(month, 2);
        this.gridEnergyIn = gridEnergyIn == null ? ENERGY_DECIMAL_FORMAT : dataFormat.format(gridEnergyIn);
        this.gridEnergyOut = gridEnergyOut == null ? ENERGY_DECIMAL_FORMAT : dataFormat.format(gridEnergyOut);
        this.pvEnergyIn = pvEnergyIn == null ? ENERGY_DECIMAL_FORMAT : dataFormat.format(pvEnergyIn);
        this.pvEnergyOut = pvEnergyOut == null ? ENERGY_DECIMAL_FORMAT : dataFormat.format(pvEnergyOut);
    }

}
