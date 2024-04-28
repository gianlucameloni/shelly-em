package com.gmeloni.shelly.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeviceLocalIPAddressAndChannel {
    private String deviceLocalIPAddress;
    private String deviceChannel;
}
