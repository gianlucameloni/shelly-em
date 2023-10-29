package com.gmeloni.shelly.service;

import com.gmeloni.shelly.dto.rest.GetEMStatusResponse;
import com.gmeloni.shelly.model.RawEMData;
import com.gmeloni.shelly.repository.RawEMDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RawEMService {

    private static final Logger log = LoggerFactory.getLogger(RawEMService.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RawEMDataRepository rawEMDataRepository;
    @Value("${shelly.em.ip}")
    private String shellyEMAddress;

    public GetEMStatusResponse getRawEMSamples() {
        return restTemplate.getForObject("http://" + shellyEMAddress + "/status", GetEMStatusResponse.class);
    }

    public RawEMData getLastReading() {
        return rawEMDataRepository.findTopByOrderBySampleTimestampDesc();
    }

}
