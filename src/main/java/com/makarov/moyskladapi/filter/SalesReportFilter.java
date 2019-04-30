package com.makarov.moyskladapi.filter;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.Entry;

public class SalesReportFilter extends Filter {
    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        Map<String, String> parameters = queryToMap(exchange.getRequestURI().getQuery());
        for (Entry<String, String> parameter : parameters.entrySet()) {
            exchange.setAttribute(parameter.getKey(), parameter.getValue());
        }
        chain.doFilter(exchange);
    }

    @Override
    public String description() {
        return null;
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }
}
