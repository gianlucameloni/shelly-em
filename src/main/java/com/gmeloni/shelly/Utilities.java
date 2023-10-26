package com.gmeloni.shelly;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utilities {

    public static String toPrettyJson(
            Object value
    ) {
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(value).replaceAll("\\s+", "");
        } catch (JsonProcessingException e) {
            return "Could not print pretty JSON from input object!";
        }
    }

}
