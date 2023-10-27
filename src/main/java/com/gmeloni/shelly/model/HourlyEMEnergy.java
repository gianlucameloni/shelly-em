package com.gmeloni.shelly.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "hourly_em_energy")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@EqualsAndHashCode
public class HourlyEMEnergy implements Serializable {
    @Id
    @Column(name = "from_timestamp")
    private LocalDateTime fromTimestamp;
    @Column(name = "to_timestamp")
    private LocalDateTime toTimestamp;
    @Column(name = "grid_energy_in")
    private Double gridEnergyIn;
    @Column(name = "grid_energy_out")
    private Double gridEnergyOut;
    @Column(name = "pv_energy_in")
    private Double pvEnergyIn;
    @Column(name = "pv_energy_out")
    private Double pvEnergyOut;
    @Column(name = "max_grid_voltage")
    private Double maxGridVoltage;
    @Column(name = "min_grid_voltage")
    private Double minGridVoltage;
    @Column(name = "max_pv_voltage")
    private Double maxPvVoltage;
    @Column(name = "min_pv_voltage")
    private Double minPvVoltage;
}
