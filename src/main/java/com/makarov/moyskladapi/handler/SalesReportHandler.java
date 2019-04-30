package com.makarov.moyskladapi.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makarov.moyskladapi.dto.SalesReport;
import com.makarov.moyskladapi.exeption.DataException;
import com.makarov.moyskladapi.service.SalesReportService;
import com.makarov.moyskladapi.utils.HttpUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SalesReportHandler implements HttpHandler {

    private final SalesReportService salesReportService;

    private final HttpUtils httpUtils;

    private final ObjectMapper objectMapper;

    public SalesReportHandler(SalesReportService salesReportService, HttpUtils httpUtils, ObjectMapper objectMapper) {
        this.salesReportService = salesReportService;
        this.httpUtils = httpUtils;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Long productId = null;
        Date date = null;

        try {
            productId = Long.valueOf((String) exchange.getAttribute("productId"));
            String dateString = (String) exchange.getAttribute("date");
            date = dateFormat.parse(dateString);
        } catch (Exception e) {
            httpUtils.sendBadRequest(exchange, "bad request");
            return;
        }

        SalesReport salesReport = null;
        try {
            salesReport = salesReportService.getSalesReportByDate(date, productId);
        } catch (DataException e) {
            httpUtils.sendBadRequest(exchange, "bad request");
        }
        String responseBody = objectMapper.writeValueAsString(salesReport);
        exchange.sendResponseHeaders(200, responseBody.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseBody.getBytes());
        outputStream.close();
        System.out.println(salesReport);
    }
}
