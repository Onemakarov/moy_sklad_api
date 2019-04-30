package com.makarov.moyskladapi.service;

import com.makarov.moyskladapi.dao.ProductDao;
import com.makarov.moyskladapi.dto.Product;
import com.makarov.moyskladapi.exeption.UniqueException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductDao productDaoMock;

    @Test
    void testSaveProductThrowExceptionIfDuplicate() {
        Product product = new Product();
        when(productDaoMock.saveProduct(product)).thenThrow(new UniqueException(""));

        assertThrows(IllegalArgumentException.class,
                () -> productService.saveProduct(product));
    }

    @Test
    void testSaveProductSuccess() {
        Product product = new Product();

        productService.saveProduct(product);

        verify(productDaoMock, times(1)).saveProduct(product);
    }
}