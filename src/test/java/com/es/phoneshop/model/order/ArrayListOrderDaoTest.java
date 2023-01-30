package com.es.phoneshop.model.order;

import com.es.phoneshop.exception.OrderNotFoundException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ArrayListOrderDaoTest {
    private static final OrderDao orderDao = ArrayListOrderDao.getInstance();
    private static final OrderService orderService = DefaultOrderService.getInstance();
    private static Order order1;
    private static Order order2;
    @Before
    public void setup() {
        order1 = new Order();
        order2 = new Order();
        orderService.placeOrder(order1);
        orderService.placeOrder(order2);
    }

    @Test
    public void testGetOrder() {
        Long testId = 1L;
        Order receivedOrder = orderDao.getById(testId);
        assertEquals(testId, receivedOrder.getId());
    }

    @Test(expected = OrderNotFoundException.class)
    public void testNonExistingOrder() {
        orderDao.getById(100L);
    }

    @Test
    public void testGetOrderBySecureId() {
        String secureId = order1.getSecureId();
        Order receivedOrder = orderDao.getOrderBySecureId(secureId);
        assertEquals(secureId, receivedOrder.getSecureId());
    }
}