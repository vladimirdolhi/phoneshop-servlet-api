package com.es.phoneshop.model.order;

import com.es.phoneshop.exception.DaoNotFoundException;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.exception.ProductDaoException;
import com.es.phoneshop.model.dao.GenericArrayListDao;


import java.util.List;

public class ArrayListOrderDao extends GenericArrayListDao<Order> implements OrderDao {

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

    private ArrayListOrderDao() {

    }

    @Override
    public Order getById(Long id) throws OrderNotFoundException {
        getLock().readLock().lock();
        try {
            return super.get(id);
        } catch (DaoNotFoundException e) {
            throw new OrderNotFoundException(e);
        } finally {
            getLock().readLock().unlock();
        }
    }

    @Override
    public Order getOrderBySecureId(String secureId) throws OrderNotFoundException {
        getLock().readLock().lock();
        try {
            return getOrders().stream()
                    .filter(p -> secureId.equals(p.getSecureId()))
                    .findAny()
                    .orElseThrow(() -> new OrderNotFoundException("Order with id " + secureId + " not found"));
        } finally {
            getLock().readLock().unlock();
        }
    }

    @Override
    public void save(Order order) throws ProductDaoException {
        try {
            super.save(order);
        } catch (DaoNotFoundException e){
            throw new ProductDaoException("Order with id " + order.getId() + " doesn't exists");
        }
    }

    public List<Order> getOrders() {
        return super.getItems();
    }

}
