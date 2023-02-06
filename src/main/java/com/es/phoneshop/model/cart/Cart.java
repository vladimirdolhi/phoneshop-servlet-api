package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.dao.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class Cart extends Entity implements Serializable {
    private List<CartItem> items;

    public Cart() {
        super(null);
        this.totalCost = BigDecimal.ZERO;
        this.currency = Currency.getInstance("USD");
        this.items = new ArrayList<>();
    }

    private int totalQuantity;
    private BigDecimal totalCost;

    private Currency currency;

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

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "items=" + items.toString() +
                '}';
    }
}
