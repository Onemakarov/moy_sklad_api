package com.makarov.moyskladapi.datasource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConnectionManagerTest {

    @Mock
    private DataSource dataSourceMock;

    @Mock
    private Connection connectionMock;

    @InjectMocks
    private ConnectionManager connectionManager;

    @Test
    void testGetConnectionSuccess() throws SQLException {
        when(dataSourceMock.getConnection()).thenReturn(connectionMock);

        Connection connection = connectionManager.getConnection();

        assertNotNull(connection);
        assertEquals(connectionMock, connection);
    }

    @Test
    void testGetConnectionFailed() throws SQLException {
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());

        assertThrows(RuntimeException.class,
                () -> connectionManager.getConnection());
    }
}
