package com.makarov.moyskladapi.service;

import com.makarov.moyskladapi.dao.DemandDao;
import com.makarov.moyskladapi.dto.Demand;
import com.makarov.moyskladapi.transaction.Transactional;

import java.util.Date;

public class DemandServiceImpl implements DemandService {

    private final PurchaseService purchaseService;

    private final DemandDao demandDao;


    public DemandServiceImpl(PurchaseService purchaseService, DemandDao demandDao) {
        this.purchaseService = purchaseService;
        this.demandDao = demandDao;
    }

    @Override
    @Transactional
    public Demand saveDemand(Demand demand) {
        validateDemand(demand);
        return demandDao.saveDemand(demand);
    }

    @Transactional
    private void validateDemand(Demand demand) {
        if (demand.getPrice() <= 0) {
            throw new IllegalArgumentException("price less than zero");
        }
        if (!enoughProduct(demand.getDate(), demand.getProductId(), demand)) {
            throw new IllegalArgumentException("not enough product");
        }
    }

    @Override
    @Transactional
    public boolean enoughProduct(Date date, long productId, Demand demand) {
        return (purchaseService.getCountOfPurchaseProduct(date, productId) -
                getCountOfDemandProduct(date, productId)) >= demand.getCount();
    }

    @Transactional
    private int getCountOfDemandProduct(Date date, long productId) {
        return demandDao.getCountOfDemandProduct(date, productId);
    }

    @Transactional
    public long getSumOfDemandProductsByDate(Date date, long productId) {
        return demandDao.getSumOfDemandProductsPriceByDate(date, productId);
    }
}
