package com.makarov.moyskladapi.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makarov.moyskladapi.dto.Demand;
import com.makarov.moyskladapi.service.DemandService;
import com.makarov.moyskladapi.utils.HttpUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class DemandHandler implements HttpHandler {

    private final DemandService demandService;

    private final HttpUtils httpUtils;

    private final ObjectMapper objectMapper;

    public DemandHandler(DemandService demandService, HttpUtils httpUtils, ObjectMapper objectMapper) {
        this.demandService = demandService;
        this.httpUtils = httpUtils;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Demand demand = (Demand) exchange.getAttribute("object");

        try {
            demandService.saveDemand(demand);
        } catch (IllegalArgumentException e) {
            httpUtils.sendBadRequest(exchange, e.getMessage());
            return;
        }

        String responseBody = objectMapper.writeValueAsString(demand);
        exchange.sendResponseHeaders(201, responseBody.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseBody.getBytes());
        outputStream.close();
    }
}
