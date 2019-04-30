package com.makarov.moyskladapi.service;

import com.makarov.moyskladapi.dao.PurchaseDao;
import com.makarov.moyskladapi.dto.Purchase;
import com.makarov.moyskladapi.transaction.Transactional;

import java.util.Date;

public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseDao purchaseDao;

    public PurchaseServiceImpl(PurchaseDao purchaseDao) {
        this.purchaseDao = purchaseDao;
    }

    @Override
    @Transactional
    public Purchase savePurchase(Purchase purchase) {
        if (purchase.getPrice() <= 0) {
            throw new IllegalArgumentException("price less than zero");
        }
        return purchaseDao.savePurchase(purchase);
    }

    @Override
    @Transactional
    public int getCountOfPurchaseProduct(Date date, long productId) {
        return purchaseDao.getCountOfPurchaseProductByDate(date, productId);
    }

    @Override
    @Transactional
    public long getSumOfPurchaseProductsByDate(Date date, long productId) {
        return purchaseDao.getSumOfPurchaseProductsPriceByDate(date, productId);
    }
}
