package com.gmeloni.shelly.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "raw_em_data")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@EqualsAndHashCode
public class RawEMData implements Serializable {
    @Id @Column(name = "sample_timestamp") private LocalDateTime sampleTimestamp;
    @Column(name = "grid_power") private Double gridPower;
    @Column(name = "pv_power") private Double pvPower;
}
