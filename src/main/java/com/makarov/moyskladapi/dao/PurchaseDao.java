package com.makarov.moyskladapi.dao;

import com.makarov.moyskladapi.datasource.ConnectionManager;
import com.makarov.moyskladapi.dto.Purchase;
import com.makarov.moyskladapi.exeption.DataException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@SuppressWarnings("Duplicates")
public class PurchaseDao {

    private final ConnectionManager connectionManager;

    public PurchaseDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public Purchase savePurchase(Purchase purchase) {
        final String sql = "INSERT INTO purchase (product_id, price, count, date) " +
                "VALUES (?, ?, ?, ?) RETURNING id;";

        Connection connection = connectionManager.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1, purchase.getProductId());
            statement.setLong(2, purchase.getPrice());
            statement.setInt(3, purchase.getCount());
            statement.setDate(4, new java.sql.Date(purchase.getDate().getTime()));
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            purchase.setId(resultSet.getLong("id"));
            return purchase;
        } catch (SQLException e) {
            throw new DataException(e.getMessage());
        }
    }

    public int getCountOfPurchaseProductByDate(Date date, Long productId) {
        final String sql = "SELECT SUM(count) FROM purchase WHERE date <= ? AND product_id = ? GROUP BY product_id;";

        Connection connection = connectionManager.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setDate(1, new java.sql.Date(date.getTime()));
            statement.setLong(2, productId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt("sum");
            resultSet.close();
            return count;
        } catch (SQLException e) {
            throw new DataException(e.getMessage());
        }
    }

    public long getSumOfPurchaseProductsPriceByDate(Date date, long productId) {
        final String sql = "SELECT SUM(count * price) FROM purchase WHERE date <= ? AND product_id = ? GROUP BY product_id";

        Connection connection = connectionManager.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setDate(1, new java.sql.Date(date.getTime()));
            statement.setLong(2, productId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int sum = resultSet.getInt("sum");
            resultSet.close();
            return sum;
        } catch (SQLException e) {
            throw new DataException(e.getMessage());
        }
    }
}
