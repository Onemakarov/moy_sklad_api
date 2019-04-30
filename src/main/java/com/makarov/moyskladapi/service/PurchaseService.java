package com.makarov.moyskladapi.service;

import com.makarov.moyskladapi.dto.Purchase;

import java.util.Date;

public interface PurchaseService {

    Purchase savePurchase(Purchase operation);

    int getCountOfPurchaseProduct(Date date, long productId);

    long getSumOfPurchaseProductsByDate(Date date, long productId);
}
