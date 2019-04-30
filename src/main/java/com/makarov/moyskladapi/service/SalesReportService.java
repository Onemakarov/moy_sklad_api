package com.makarov.moyskladapi.service;

import com.makarov.moyskladapi.dto.SalesReport;

import java.util.Date;

public interface SalesReportService {

    SalesReport getSalesReportByDate(Date date, long productId);
}
