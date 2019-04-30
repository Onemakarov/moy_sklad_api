package com.makarov.moyskladapi.dao;

import com.makarov.moyskladapi.datasource.ConnectionManager;
import com.makarov.moyskladapi.dto.Demand;
import com.makarov.moyskladapi.exeption.DataException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@SuppressWarnings("Duplicates")
public class DemandDao {

    private final ConnectionManager connectionManager;

    public DemandDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public Demand saveDemand(Demand demand) {
        final String sql = "INSERT INTO demand (product_id, price, count, date) " +
                "VALUES (?, ?, ?, ?) RETURNING id;";

        Connection connection = connectionManager.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, demand.getProductId());
            statement.setLong(2, demand.getPrice());
            statement.setInt(3, demand.getCount());
            statement.setDate(4, new java.sql.Date(demand.getDate().getTime()));
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            demand.setId(resultSet.getLong("id"));
            resultSet.close();
            return demand;
        } catch (SQLException e) {
            throw new DataException(e.getMessage());
        }
    }

    public int getCountOfDemandProduct(Date date, Long productId) {
        final String sql = "SELECT SUM(count) FROM demand WHERE date <= ? AND product_id = ? GROUP BY product_id;";

        Connection connection = connectionManager.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, new java.sql.Date(date.getTime()));
            statement.setLong(2, productId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("sum");
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new DataException(e.getMessage());
        }
        return 0;
    }

    public long getSumOfDemandProductsPriceByDate(java.util.Date date, long productId) {
        final String sql = "SELECT SUM(count * price) FROM demand WHERE date <= ? AND product_id = ? GROUP BY product_id";

        Connection connection = connectionManager.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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
