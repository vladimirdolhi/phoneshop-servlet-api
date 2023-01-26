package com.es.phoneshop.model.cart;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {
    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    private int totalQuantity;
    private BigDecimal totalCost;

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public List<CartItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "items=" + items.toString() +
                '}';
    }
}
