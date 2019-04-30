package com.makarov.moyskladapi.service;

import com.makarov.moyskladapi.dao.DemandDao;
import com.makarov.moyskladapi.dto.Demand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DemandServiceImplTest {

    @InjectMocks
    private DemandServiceImpl demandService;

    @Mock
    private PurchaseService purchaseServiceMock;

    @Mock
    private DemandDao demandDaoMock;

    @Mock
    private Date dateMock;

    @Test
    void testSaveDemandThrowExceptionIfPriceLessThanZero() {
        Demand demand  = new Demand();
        demand.setPrice(-1L);
        lenient().when(purchaseServiceMock.getCountOfPurchaseProduct(dateMock, 1L)).thenReturn(2);
        lenient().when(demandDaoMock.getCountOfDemandProduct(dateMock, 1L)).thenReturn(1);

        assertThrows(IllegalArgumentException.class,
                () -> demandService.saveDemand(demand));
    }

    @Test
    void testSaveDemandSuccess() {
        Demand demand = createDemand();
        lenient().when(purchaseServiceMock.getCountOfPurchaseProduct(dateMock, 1L)).thenReturn(2);
        lenient().when(demandDaoMock.getCountOfDemandProduct(dateMock, 1L)).thenReturn(1);

        demandService.saveDemand(demand);

        verify(demandDaoMock, times(1)).saveDemand(demand);
    }

    private Demand createDemand() {
        Demand demand = new Demand();
        demand.setPrice(1L);
        demand.setDate(dateMock);
        demand.setCount(1);
        demand.setProductId(1L);
        return demand;
    }
}