package com.es.phoneshop.model.product.viewedHistory;

import com.es.phoneshop.model.product.Product;

import java.util.ArrayDeque;
import java.util.Deque;


public class ViewedProductsHistory {

    private Deque<Product> recentlyViewedProducts;

    public ViewedProductsHistory() {
        this.recentlyViewedProducts = new ArrayDeque<>();
    }

    public void addViewedProductFirst(Product product){
        recentlyViewedProducts.addFirst(product);
    }

    public void deleteViewedProduct(Product product){
        recentlyViewedProducts.remove(product);
    }

    public void deleteLastViewedProduct(){
        recentlyViewedProducts.removeLast();
    }

    public void deleteViewedProduct(int productIndex){
        recentlyViewedProducts.remove(productIndex);
    }

    public Deque<Product> getRecentlyViewedProducts() {
        return recentlyViewedProducts;
    }



}
