package com.es.phoneshop.model.product;

import com.es.phoneshop.exception.ProductDaoException;

import java.util.List;

public interface ProductDao {
    Product getProduct(Long id) throws ProductDaoException;
    List<Product> findProducts();
    void save(Product product) throws ProductDaoException;
    void delete(Long id) throws ProductDaoException;
}
