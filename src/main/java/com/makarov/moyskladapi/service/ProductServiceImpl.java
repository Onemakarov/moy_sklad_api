package com.makarov.moyskladapi.service;

import com.makarov.moyskladapi.dao.ProductDao;
import com.makarov.moyskladapi.dto.Product;
import com.makarov.moyskladapi.exeption.UniqueException;
import com.makarov.moyskladapi.transaction.Transactional;

public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;

    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Product saveProduct(Product product) {
        try {
            return productDao.saveProduct(product);
        } catch (UniqueException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
