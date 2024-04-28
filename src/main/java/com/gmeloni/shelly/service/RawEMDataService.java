package com.gmeloni.shelly.service;

import com.gmeloni.shelly.dto.rest.GetShellyEMStatusResponse;
import com.gmeloni.shelly.dto.rest.GetShellyProEMStatusResponse;
import com.gmeloni.shelly.model.RawEMData;
import com.gmeloni.shelly.repository.RawEMDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RawEMDataService {

    private static final Logger log = LoggerFactory.getLogger(RawEMDataService.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RawEMDataRepository rawEMDataRepository;

    public GetShellyEMStatusResponse getShellyEMRawSamples(
            String shellyEMIpAddress
    ) {
        return restTemplate.getForObject("http://" + shellyEMIpAddress + "/status", GetShellyEMStatusResponse.class);
    }

    public GetShellyProEMStatusResponse getShellyProEMRawSamples(
            String shellyProEMIpAddress
    ) {
        return restTemplate.getForObject("http://" + shellyProEMIpAddress + "/rpc/Shelly.GetStatus", GetShellyProEMStatusResponse.class);
    }

    public RawEMData getLastReading() {
        return rawEMDataRepository.findTopByOrderBySampleTimestampDesc();
    }

}
