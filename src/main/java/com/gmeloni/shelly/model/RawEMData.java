package com.gmeloni.shelly.model;

import com.gmeloni.shelly.model.pk.RawEMDataPK;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "raw_em_data")
@IdClass(RawEMDataPK.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode
@Getter
public class RawEMData implements Serializable {
    @Id
    @Column(name = "device_local_ip_address")
    private String deviceLocalIPAddress;
    @Id
    @Column(name = "device_channel")
    private Integer deviceChannel;
    @Id
    @Column(name = "sample_timestamp")
    private LocalDateTime sampleTimestamp;
    @Column(name = "device_id")
    private String deviceId;
    @Column(name = "power")
    private Double power;
}
