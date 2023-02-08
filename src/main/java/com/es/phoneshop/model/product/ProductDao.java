package com.es.phoneshop.model.product;

import com.es.phoneshop.exception.ProductDaoException;
import com.es.phoneshop.exception.ProductNotFoundException;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao {
    Product getById(Long id) throws ProductNotFoundException;
    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);
    List<Product> findProducts(String description, String searchOption, BigDecimal minPrice, BigDecimal maxPrice);
    void save(Product product) throws ProductDaoException;
    void delete(Long id) throws ProductDaoException;

}
