package com.makarov.moyskladapi.dto;

import java.util.Date;

public class SalesReport {

    private long productId;

    private long profit;

    private long purchaseSum;

    private long demandSum;

    private Date date;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getProfit() {
        return profit;
    }

    public void setProfit(long profit) {
        this.profit = profit;
    }

    public long getPurchaseSum() {
        return purchaseSum;
    }

    public void setPurchaseSum(long purchaseSum) {
        this.purchaseSum = purchaseSum;
    }

    public long getDemandSum() {
        return demandSum;
    }

    public void setDemandSum(long demandSum) {
        this.demandSum = demandSum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "productId : " + productId + "\n" +
                "profit : " + profit + "\n" +
                "purchase sum : " + purchaseSum + "\n" +
                "demand sum : " + demandSum + "\n" +
                "date : " + date;

    }
}
