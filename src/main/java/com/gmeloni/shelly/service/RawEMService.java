package com.gmeloni.shelly.service;

import com.gmeloni.shelly.dto.rest.GetEMStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RawEMService {

    private static final Logger log = LoggerFactory.getLogger(RawEMService.class);

    @Autowired private RestTemplate restTemplate;

    public GetEMStatusResponse getRawEMSamples() {
        return restTemplate.getForObject("http://192.168.1.15/status", GetEMStatusResponse.class);
    }

}