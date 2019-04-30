package com.makarov.moyskladapi.service;

import com.makarov.moyskladapi.dto.Demand;
import com.makarov.moyskladapi.transaction.Transactional;

import java.util.Date;

public interface DemandService {

    Demand saveDemand(Demand operation);

    @Transactional
    boolean enoughProduct(Date date, long productId, Demand demand);

    long getSumOfDemandProductsByDate(Date date, long productId);
}
