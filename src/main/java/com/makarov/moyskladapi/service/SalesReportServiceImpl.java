package com.makarov.moyskladapi.service;

import com.makarov.moyskladapi.dto.SalesReport;
import com.makarov.moyskladapi.transaction.Transactional;

import java.util.Date;

public class SalesReportServiceImpl implements SalesReportService {

    private final PurchaseService purchaseService;

    private final DemandService demandService;

    public SalesReportServiceImpl(PurchaseService purchaseService, DemandService demandService) {
        this.purchaseService = purchaseService;
        this.demandService = demandService;
    }

    @Override
    @Transactional
    public SalesReport getSalesReportByDate(Date date, long productId) {
        SalesReport salesReport = new SalesReport();
        salesReport.setProductId(productId);
        salesReport.setDate(date);
        long purchaseSum = purchaseService.getSumOfPurchaseProductsByDate(date, productId);
        salesReport.setPurchaseSum(purchaseSum);
        long demandSum = demandService.getSumOfDemandProductsByDate(date, productId);
        salesReport.setDemandSum(demandSum);
        salesReport.setProfit(demandSum - purchaseSum);
        return salesReport;
    }
}
