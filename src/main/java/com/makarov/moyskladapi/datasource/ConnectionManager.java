package com.makarov.moyskladapi.datasource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {

    private ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

    private final DataSource dataSource;

    private ThreadLocal<Object> transaction = new ThreadLocal<>();

    public ConnectionManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() {
        Connection connection = connectionThreadLocal.get();
        try {
            if (connection == null) {
                connection = dataSource.getConnection();
                connectionThreadLocal.set(connection);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public void closeConnection() {
        Connection connection = connectionThreadLocal.get();
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            connectionThreadLocal.remove();
        }
    }

    public boolean isTransaction() {
        return transaction.get() != null;
    }

    public void startTransaction() {
        transaction.set(new Object());
    }

    public void commit() throws SQLException {
        getConnection().commit();
        transaction.remove();
    }

    public void rollback() throws SQLException {
        getConnection().rollback();
        transaction.remove();
    }
}
