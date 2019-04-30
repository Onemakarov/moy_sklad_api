package com.makarov.moyskladapi.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makarov.moyskladapi.utils.HttpUtils;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class JsonDeserializeFilter extends Filter {

    private final Class<?> deserializeObjectClass;

    private final ObjectMapper objectMapper;

    private final HttpUtils httpUtils;

    public JsonDeserializeFilter(Class deserializeObjectClass, ObjectMapper objectMapper, HttpUtils httpUtils) {
        this.deserializeObjectClass = deserializeObjectClass;
        this.objectMapper = objectMapper;
        this.httpUtils = httpUtils;
    }

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        String body = getBodyAsString(new InputStreamReader(exchange.getRequestBody()));
        try {
            Object object = objectMapper.readValue(body, deserializeObjectClass);
            exchange.setAttribute("object", object);
        } catch (IOException e) {
            httpUtils.sendBadRequest(exchange, "bad request");
            return;
        }
        chain.doFilter(exchange);
    }

    @Override
    public String description() {
        return null;
    }

    private String getBodyAsString(InputStreamReader inputStreamReader) throws IOException {
        BufferedReader br = new BufferedReader(inputStreamReader);
        String requestBody = br.lines().collect(Collectors.joining());
        inputStreamReader.close();
        return requestBody;
    }
}
