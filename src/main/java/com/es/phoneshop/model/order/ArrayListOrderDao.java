package com.es.phoneshop.model.order;

import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.exception.ProductDaoException;
import com.es.phoneshop.exception.ProductNotFoundException;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class ArrayListOrderDao implements OrderDao {

    private static OrderDao orderDao;

    public static OrderDao getInstance(){
        OrderDao localProductDaoInstance = orderDao;
        if(localProductDaoInstance == null){
            synchronized (ArrayListOrderDao.class){
                localProductDaoInstance = orderDao;
                if (localProductDaoInstance == null){
                    orderDao = localProductDaoInstance = new ArrayListOrderDao();
                }
            }
        }
        return localProductDaoInstance;
    }
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock readLock = readWriteLock.readLock();
    private Lock writeLock = readWriteLock.writeLock();

    private long maxId = 0;

    private List<Order> orders;

    private ArrayListOrderDao() {
        this.orders = new ArrayList<>();
    }

    @Override
    public Order getOrder(Long id) throws OrderNotFoundException {
        readLock.lock();
        try {
            return orders.stream()
                    .filter(p -> id.equals(p.getId()))
                    .findAny()
                    .orElseThrow(() -> new OrderNotFoundException(id, "Order with id " + id + " not found"));
        } finally {
            readLock.unlock();
        }

    }

    @Override
    public void save(Order order) throws ProductDaoException {
        writeLock.lock();
        try {
            if (order.getId() == null){
                order.setId(++maxId);
                orders.add(order);
            } else {
                try {
                    Order productToUpdate = getOrder(order.getId());
                    orders.set(orders.indexOf(productToUpdate), order);
                } catch (ProductNotFoundException e){
                    throw new ProductDaoException("Order with id " + order.getId() + " doesn't exists");
                }
            }

        } finally {
            writeLock.unlock();
        }

    }

    public List<Order> getOrders() {
        return orders;
    }

}
