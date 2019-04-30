package com.makarov.moyskladapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makarov.moyskladapi.dao.DemandDao;
import com.makarov.moyskladapi.dao.ProductDao;
import com.makarov.moyskladapi.dao.PurchaseDao;
import com.makarov.moyskladapi.datasource.ConnectionManager;
import com.makarov.moyskladapi.dto.Demand;
import com.makarov.moyskladapi.dto.Product;
import com.makarov.moyskladapi.dto.Purchase;
import com.makarov.moyskladapi.filter.ContentTypeValidationFilter;
import com.makarov.moyskladapi.filter.JsonDeserializeFilter;
import com.makarov.moyskladapi.filter.SalesReportFilter;
import com.makarov.moyskladapi.handler.CreateHandler;
import com.makarov.moyskladapi.handler.DemandHandler;
import com.makarov.moyskladapi.handler.PurchaseHandler;
import com.makarov.moyskladapi.handler.SalesReportHandler;
import com.makarov.moyskladapi.service.*;
import com.makarov.moyskladapi.transaction.TransactionProxyHandler;
import com.makarov.moyskladapi.utils.HttpUtils;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpServer;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import static java.lang.reflect.Proxy.newProxyInstance;

public class Main {

    public static void main(String[] args) throws IOException {
        ConnectionManager connectionManager = new ConnectionManager(getDataSource());
        initDb(connectionManager);

        HttpUtils httpUtils = new HttpUtils();
        ObjectMapper objectMapper = creatObjectMapper();

        ProductDao productDao = new ProductDao(connectionManager);
        PurchaseDao purchaseDao = new PurchaseDao(connectionManager);
        DemandDao demandDao = new DemandDao(connectionManager);

        ProductService productService = createProxyInstance(ProductService.class, new ProductServiceImpl(productDao), connectionManager);
        PurchaseService purchaseService = createProxyInstance(PurchaseService.class, new PurchaseServiceImpl(purchaseDao), connectionManager);
        DemandService demandService = createProxyInstance(DemandService.class, new DemandServiceImpl(purchaseService, demandDao), connectionManager);
        SalesReportService salesReportService = createProxyInstance(SalesReportService.class, new SalesReportServiceImpl(purchaseService, demandService), connectionManager);

        Filter contentTypeFilter = new ContentTypeValidationFilter(httpUtils);
        Filter salesReportFilter = new SalesReportFilter();
        List<Filter> productFilters = getProductFilters(contentTypeFilter, objectMapper, httpUtils);
        List<Filter> purchaseFilters = getPurchaseFilters(contentTypeFilter, objectMapper, httpUtils);
        List<Filter> demandFilters = getDemandFilters(contentTypeFilter, objectMapper, httpUtils);

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.setExecutor(Executors.newFixedThreadPool(10));
        server.createContext("/newproduct", new CreateHandler(productService, httpUtils, objectMapper)).getFilters().addAll(productFilters);
        server.createContext("/purchase", new PurchaseHandler(purchaseService, httpUtils, objectMapper)).getFilters().addAll(purchaseFilters);
        server.createContext("/demand", new DemandHandler(demandService, httpUtils, objectMapper)).getFilters().addAll(demandFilters);
        server.createContext("/salesreport", new SalesReportHandler(salesReportService, httpUtils, objectMapper)).getFilters().add(salesReportFilter);
        server.start();
    }

    private static List<Filter> getProductFilters(Filter contentTypeFilter, ObjectMapper objectMapper, HttpUtils httpUtils) {
        List<Filter> productFilters = new ArrayList<>();
        Filter productJsonFilter = new JsonDeserializeFilter(Product.class, objectMapper, httpUtils);
        productFilters.add(contentTypeFilter);
        productFilters.add(productJsonFilter);
        return productFilters;
    }

    private static List<Filter> getDemandFilters(Filter contentTypeFilter, ObjectMapper objectMapper, HttpUtils httpUtils) {
        List<Filter> demandFilters = new ArrayList<>();
        Filter demandJsonFilter = new JsonDeserializeFilter(Demand.class, objectMapper, httpUtils);
        demandFilters.add(contentTypeFilter);
        demandFilters.add(demandJsonFilter);
        return demandFilters;
    }

    private static List<Filter> getPurchaseFilters(Filter contentTypeFilter, ObjectMapper objectMapper, HttpUtils httpUtils) {
        List<Filter> purchaseFilters = new ArrayList<>();
        Filter purchaseJsonFilter = new JsonDeserializeFilter(Purchase.class, objectMapper, httpUtils);
        purchaseFilters.add(contentTypeFilter);
        purchaseFilters.add(purchaseJsonFilter);
        return purchaseFilters;
    }

    private static ObjectMapper creatObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        objectMapper.setDateFormat(format);
        return objectMapper;
    }

    private static void initDb(ConnectionManager connectionManager) {
        final String sql = "CREATE TABLE IF NOT EXISTS products" +
                "(" +
                "id BIGSERIAL NOT NULL " +
                "CONSTRAINT product_pkey " +
                "PRIMARY KEY, " +
                "name VARCHAR(255) UNIQUE NOT NULL" +
                ");" +
                "" +
                "CREATE TABLE IF NOT EXISTS purchase " +
                "( " +
                "id BIGSERIAL NOT NULL " +
                "CONSTRAINT purchase_pkey " +
                "PRIMARY KEY, " +
                "product_id BIGINT NOT NULL " +
                "CONSTRAINT product_fk " +
                "REFERENCES products, " +
                "price BIGINT NOT NULL, " +
                "count INT NOT NULL, " +
                "date DATE NOT NULL " +
                ");" +
                "" +
                "CREATE TABLE IF NOT EXISTS demand " +
                "( " +
                "id BIGSERIAL NOT NULL " +
                "CONSTRAINT demand_pkey " +
                "PRIMARY KEY, " +
                "product_id BIGINT NOT NULL " +
                "CONSTRAINT product_fk " +
                "REFERENCES products, " +
                "price BIGINT NOT NULL, " +
                "count INT NOT NULL, " +
                "date DATE NOT NULL " +
                ");";
        try {
            Connection connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static DataSource getDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUsername("postgres");
        dataSource.setPassword("root");
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/moy-sklad-api");
        dataSource.setMinimumIdle(5);
        dataSource.setMaximumPoolSize(10);
        return dataSource;
    }

    private static <T> T createProxyInstance(Class<T> proxyClass, Object target, ConnectionManager connectionManager) {
        T proxyInstance = (T) newProxyInstance(proxyClass.getClassLoader(),
                new Class[]{proxyClass},
                new TransactionProxyHandler(target, connectionManager));
        return proxyInstance;
    }

}
