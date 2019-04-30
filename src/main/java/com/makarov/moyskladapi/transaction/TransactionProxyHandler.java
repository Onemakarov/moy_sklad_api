package com.makarov.moyskladapi.transaction;

import com.makarov.moyskladapi.datasource.ConnectionManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class TransactionProxyHandler implements InvocationHandler {

    private final Map<String, Method> methods = new HashMap<>();

    private final ConnectionManager connectionManager;

    private final Object target;

    public TransactionProxyHandler(Object target, ConnectionManager connectionManager) {
        this.target = target;
        this.connectionManager = connectionManager;

        for(Method method: target.getClass().getDeclaredMethods()) {
            this.methods.put(method.getName(), method);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (methods.get(method.getName()).isAnnotationPresent(Transactional.class)) {
            return handleTransactionalMethod(method, args);
        } else {
            return handle(method, args);
        }
    }

    public Object handle(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(target, args);
    }

    private Object handleTransactionalMethod(Method method, Object[] args) throws Throwable {
        if (connectionManager.isTransaction()) {
            return handle(method, args);
        }
        Connection connection = connectionManager.getConnection();
        connection.setAutoCommit(false);
        Object result = null;
        try {
            connectionManager.startTransaction();
            result = methods.get(method.getName()).invoke(target, args);
            connectionManager.commit();
        } catch (Exception e) {
            connectionManager.rollback();
            throw e.getCause();
        } finally {
            connectionManager.closeConnection();
        }
        return result;
    }
}
