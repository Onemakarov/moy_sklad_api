package com.makarov.moyskladapi.dao;

import com.makarov.moyskladapi.datasource.ConnectionManager;
import com.makarov.moyskladapi.dto.Product;
import com.makarov.moyskladapi.exeption.UniqueException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDao {

    private final ConnectionManager connectionManager;

    public ProductDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public Product saveProduct(Product product) {
        final String sql = "INSERT INTO products (name) " +
                "VALUES (?) RETURNING id";

        Connection connection = connectionManager.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, product.getName());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            product.setId(resultSet.getLong("id"));
            return product;
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new UniqueException("product already exists");
            }
            throw new RuntimeException(e);
        }
    }
}
