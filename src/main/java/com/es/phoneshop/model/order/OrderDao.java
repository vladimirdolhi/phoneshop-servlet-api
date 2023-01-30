package com.es.phoneshop.model.order;

import com.es.phoneshop.exception.OrderNotFoundException;


public interface OrderDao {
    Order getById(Long id) throws OrderNotFoundException;
    Order getOrderBySecureId(String secureId) throws OrderNotFoundException;
    void save(Order order) throws OrderNotFoundException;
}
