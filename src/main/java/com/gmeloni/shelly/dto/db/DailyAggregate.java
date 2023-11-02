package com.gmeloni.shelly.dto.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmeloni.shelly.Utilities;
import lombok.Data;

import java.text.DecimalFormat;

import static com.gmeloni.shelly.Constants.ENERGY_DECIMAL_FORMAT;

@Data
public class DailyAggregate {

    @JsonProperty("day")
    private String day;
    @JsonProperty("grid_energy_in")
    private String gridEnergyIn;
    @JsonProperty("grid_energy_out")
    private String gridEnergyOut;
    @JsonProperty("pv_energy_in")
    private String pvEnergyIn;
    @JsonProperty("pv_energy_out")
    private String pvEnergyOut;

    public DailyAggregate(String day, Double gridEnergyIn, Double gridEnergyOut, Double pvEnergyIn, Double pvEnergyOut) {
        DecimalFormat dataFormat = new DecimalFormat(ENERGY_DECIMAL_FORMAT);
        this.day = Utilities.padLeftWithZeros(day, 2);
        this.gridEnergyIn = gridEnergyIn == null ? ENERGY_DECIMAL_FORMAT : dataFormat.format(gridEnergyIn);
        this.gridEnergyOut = gridEnergyOut == null ? ENERGY_DECIMAL_FORMAT : dataFormat.format(gridEnergyOut);
        this.pvEnergyIn = pvEnergyIn == null ? ENERGY_DECIMAL_FORMAT : dataFormat.format(pvEnergyIn);
        this.pvEnergyOut = pvEnergyOut == null ? ENERGY_DECIMAL_FORMAT : dataFormat.format(pvEnergyOut);
    }

}
