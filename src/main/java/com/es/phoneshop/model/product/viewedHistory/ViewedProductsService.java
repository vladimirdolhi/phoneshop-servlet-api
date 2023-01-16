package com.es.phoneshop.model.product.viewedHistory;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;

public interface ViewedProductsService {
    ViewedProductsHistory getViewedProducts(HttpServletRequest request);

    void add(ViewedProductsHistory viewedProducts, Product product);
}
