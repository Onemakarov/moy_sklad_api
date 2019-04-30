package com.makarov.moyskladapi.filter;

import com.makarov.moyskladapi.utils.HttpUtils;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class ContentTypeValidationFilter extends Filter {

    private final HttpUtils httpUtils;

    public ContentTypeValidationFilter(HttpUtils httpUtils) {
        this.httpUtils = httpUtils;
    }

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        if (!contentTypeIsApplicationJson(exchange.getRequestHeaders())) {
            httpUtils.sendBadRequest(exchange, "content type is not application/json");
        }
        chain.doFilter(exchange);
    }

    private boolean contentTypeIsApplicationJson(Headers requestHeaders) {
        return requestHeaders.containsKey("Content-type") &&
                requestHeaders.get("Content-type").get(0).equals("application/json");
    }

    @Override
    public String description() {
        return null;
    }
}
