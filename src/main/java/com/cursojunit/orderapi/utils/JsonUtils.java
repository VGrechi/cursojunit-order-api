package com.cursojunit.orderapi.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class JsonUtils {

    public static String convertToJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }

    public static Object convertToObject(String json, Class classType) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, classType);
    }

    public static Object convertToList(String json, Class classType) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, classType));
    }
}
