package com.makarov.moyskladapi.utils;

import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.OutputStream;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HttpUtilsTest {

    @InjectMocks
    private HttpUtils httpUtils;

    @Mock
    private HttpExchange exchangeMock;

    @Mock
    private OutputStream outputStreamMock;

    @Test
    void testSendBadResponseWriteInResponseBodyCorrect() throws IOException {
        String message = "message";
        when(exchangeMock.getResponseBody()).thenReturn(outputStreamMock);

        httpUtils.sendBadRequest(exchangeMock, message);

        inOrder(outputStreamMock).verify(outputStreamMock, calls(1)).write(message.getBytes());
        inOrder(outputStreamMock).verify(outputStreamMock).close();
    }
}