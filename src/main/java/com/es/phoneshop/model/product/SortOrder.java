package com.es.phoneshop.model.product;

import com.es.phoneshop.model.order.PaymentMethod;

import java.util.Arrays;
import java.util.List;

public enum SortOrder {
    ASC, DESC;

    public List<PaymentMethod> getPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }
}
