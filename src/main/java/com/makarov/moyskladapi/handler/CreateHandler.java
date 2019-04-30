package com.makarov.moyskladapi.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makarov.moyskladapi.dto.Product;
import com.makarov.moyskladapi.service.ProductService;
import com.makarov.moyskladapi.utils.HttpUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class CreateHandler implements HttpHandler {

    private final ProductService productService;

    private final HttpUtils httpUtils;

    private final ObjectMapper objectMapper;

    public CreateHandler(ProductService productService, HttpUtils httpUtils, ObjectMapper objectMapper) {
        this.productService = productService;
        this.httpUtils = httpUtils;
        this.objectMapper = objectMapper;
    }


    public void handle(HttpExchange exchange) throws IOException {
        Product product = (Product) exchange.getAttribute("object");

        try {
            productService.saveProduct(product);
        } catch (IllegalArgumentException e) {
            httpUtils.sendBadRequest(exchange, e.getMessage());
            return;
        }

        String responseBody = objectMapper.writeValueAsString(product);
        exchange.sendResponseHeaders(201, responseBody.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseBody.getBytes());
        outputStream.close();
    }
}
