package com.makarov.moyskladapi.service;

import com.makarov.moyskladapi.dao.PurchaseDao;
import com.makarov.moyskladapi.dto.Purchase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceImplTest {

    @InjectMocks
    private PurchaseServiceImpl purchaseService;

    @Mock
    private PurchaseDao purchaseDaoMock;

    @Test
    void testSavePurchaseThrowExceptionIfPriceLessThanZero() {
        Purchase purchase = createPurchase(-1L);

        assertThrows(IllegalArgumentException.class,
                () -> purchaseService.savePurchase(purchase));
    }

    @Test
    void testSavePurchaseSuccess() {
        Purchase purchase = createPurchase(1L);

        purchaseService.savePurchase(purchase);

        verify(purchaseDaoMock, times(1)).savePurchase(purchase);
    }

    private Purchase createPurchase(long price) {
        Purchase purchase = new Purchase();
        purchase.setPrice(price);
        return purchase;
    }
}