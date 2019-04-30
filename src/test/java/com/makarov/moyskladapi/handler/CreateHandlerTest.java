package com.makarov.moyskladapi.handler;

import com.makarov.moyskladapi.dto.Product;
import com.makarov.moyskladapi.service.ProductServiceImpl;
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
class CreateHandlerTest {

    @InjectMocks
    private CreateHandler createHandler;

    @Mock
    private ProductServiceImpl productServiceMock;

    @Mock
    private HttpUtils httpUtilsMock;

    @Mock
    private HttpExchange exchangeMock;

    @Test
    void testHandleSendBadResponseIfProductIsInvalid() throws IOException {
        Product product = new Product();
        when(exchangeMock.getAttribute("object")).thenReturn(product);
        when(productServiceMock.saveProduct(product)).thenThrow(new IllegalArgumentException("message"));

        createHandler.handle(exchangeMock);

        verify(httpUtilsMock, times(1)).sendBadRequest(exchangeMock, "message");
    }
}