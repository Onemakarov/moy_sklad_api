package com.makarov.moyskladapi.handler;

import com.makarov.moyskladapi.dto.Purchase;
import com.makarov.moyskladapi.service.PurchaseService;
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
class PurchaseHandlerTest {

    @InjectMocks
    private PurchaseHandler purchaseHandler;

    @Mock
    private PurchaseService purchaseServiceMock;

    @Mock
    private HttpUtils httpUtilsMock;

    @Mock
    private HttpExchange exchangeMock;

    @Test
    void testHandleSendBadResponseIfProductInvalid() throws IOException {
        Purchase purchase = new Purchase();
        when(exchangeMock.getAttribute("object")).thenReturn(purchase);
        when(purchaseServiceMock.savePurchase(purchase)).thenThrow(new IllegalArgumentException("message"));

        purchaseHandler.handle(exchangeMock);

        verify(httpUtilsMock, times(1)).sendBadRequest(exchangeMock, "message");
    }
}