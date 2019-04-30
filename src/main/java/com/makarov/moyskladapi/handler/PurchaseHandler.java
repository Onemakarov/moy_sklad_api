package com.makarov.moyskladapi.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makarov.moyskladapi.dto.Purchase;
import com.makarov.moyskladapi.service.PurchaseService;
import com.makarov.moyskladapi.utils.HttpUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class PurchaseHandler implements HttpHandler {

    private final PurchaseService purchaseService;

    private final HttpUtils httpUtils;

    private final ObjectMapper objectMapper;

    public PurchaseHandler(PurchaseService purchaseService, HttpUtils httpUtils, ObjectMapper objectMapper) {
        this.purchaseService = purchaseService;
        this.httpUtils = httpUtils;
        this.objectMapper = objectMapper;
    }

    @SuppressWarnings("Duplicates")
    public void handle(HttpExchange exchange) throws IOException {
        Purchase operation = (Purchase) exchange.getAttribute("object");

        try {
            purchaseService.savePurchase(operation);
        } catch (IllegalArgumentException e) {
            httpUtils.sendBadRequest(exchange, e.getMessage());
            return;
        }

        String responseBody = objectMapper.writeValueAsString(operation);
        exchange.sendResponseHeaders(201, responseBody.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseBody.getBytes());
        outputStream.close();
    }
}
