package com.es.phoneshop.model.order;

import com.es.phoneshop.exception.OrderNotFoundException;


public interface OrderDao {
    Order getOrder(Long id) throws OrderNotFoundException;
    void save(Order order) throws OrderNotFoundException;
}
