package com.gmeloni.shelly.model.pk;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode
public class RawEMDataPK implements Serializable {
    private String deviceLocalIPAddress;
    private Integer deviceChannel;
    private LocalDateTime sampleTimestamp;
}
