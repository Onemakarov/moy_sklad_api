package com.makarov.moyskladapi.utils;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class HttpUtils {

    public void sendBadRequest(HttpExchange exchange, String message) throws IOException {
        exchange.sendResponseHeaders(400, message.getBytes().length);
        exchange.getResponseBody().write(message.getBytes());
        exchange.getResponseBody().close();
    }
}
