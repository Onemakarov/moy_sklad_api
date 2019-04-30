package com.makarov.moyskladapi.handler;

import com.makarov.moyskladapi.dto.Demand;
import com.makarov.moyskladapi.service.DemandService;
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
class DemandHandlerTest {

    @InjectMocks
    private DemandHandler demandHandler;

    @Mock
    private DemandService demandServiceMock;

    @Mock
    private HttpUtils httpUtilsMock;

    @Mock
    private HttpExchange exchangeMock;

    @Test
    void testHandleSendBadResponseIfProductInvalid() throws IOException {
        Demand demand = new Demand();
        when(exchangeMock.getAttribute("object")).thenReturn(demand);
        when(demandServiceMock.saveDemand(demand)).thenThrow(new IllegalArgumentException("message"));

        demandHandler.handle(exchangeMock);

        verify(httpUtilsMock, times(1)).sendBadRequest(exchangeMock, "message");
    }
}