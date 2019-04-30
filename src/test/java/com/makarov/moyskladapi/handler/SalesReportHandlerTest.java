package com.makarov.moyskladapi.handler;

import com.makarov.moyskladapi.service.SalesReportService;
import com.makarov.moyskladapi.utils.HttpUtils;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalesReportHandlerTest {

    @InjectMocks
    private SalesReportHandler salesReportHandler;

    @Mock
    private SalesReportService salesReportServiceMock;

    @Mock
    private HttpExchange exchangeMock;

    @Mock
    private HttpUtils httpUtilsMock;

    @Test
    void testHandleSendBadResponseIfProductIdIsNotNumber() throws IOException {
        when(exchangeMock.getAttribute("productId")).thenReturn("string");

        salesReportHandler.handle(exchangeMock);

        inOrder(httpUtilsMock).verify(httpUtilsMock, calls(1)).sendBadRequest(exchangeMock, "bad request");
    }

    @Test
    void testHandleSendBadResponseIfNoProductId() throws IOException {
        when(exchangeMock.getAttribute("productId")).thenThrow(new NullPointerException());

        salesReportHandler.handle(exchangeMock);

        inOrder(httpUtilsMock).verify(httpUtilsMock, calls(1)).sendBadRequest(exchangeMock, "bad request");
    }

    @Test
    void testHandleSendBadResponseIfDateNotCorrect() throws IOException {
        when(exchangeMock.getAttribute("productId")).thenReturn("1");
        when(exchangeMock.getAttribute("date")).thenReturn("string");

        salesReportHandler.handle(exchangeMock);

        verify(httpUtilsMock, times(1)).sendBadRequest(exchangeMock, "bad request");
    }

    @Test
    void testHandleSendBadResponseIfNoDate() throws IOException {
        when(exchangeMock.getAttribute("productId")).thenReturn("1");
        when(exchangeMock.getAttribute("date")).thenThrow(new NullPointerException());

        salesReportHandler.handle(exchangeMock);

        verify(httpUtilsMock, times(1)).sendBadRequest(exchangeMock, "bad request");
    }
}