package com.es.phoneshop.model.order;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.cart.DefaultCartService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultOrderService implements OrderService{

    private static OrderService orderService;
    private OrderDao orderDao;

    public static OrderService getInstance() {
        OrderService localDefaultOrderService = orderService;
        if (localDefaultOrderService == null) {
            synchronized (OrderService.class) {
                localDefaultOrderService = orderService;
                if (localDefaultOrderService == null) {
                    orderService = localDefaultOrderService = new DefaultOrderService();
                }
            }
        }
        return localDefaultOrderService;
    }

    private DefaultOrderService() {
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    public Order getOrder(Cart cart) {
        Order order = new Order();
        order.setItems(cart.getItems().stream().map(item -> {
            try{
                return (CartItem) item.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));
        order.setSubtotal(cart.getTotalCost());
        order.setDeliveryCost(calculateDeliveryCost());
        order.setTotalCost(order.getSubtotal().add(order.getDeliveryCost()));
        order.setTotalQuantity(cart.getTotalQuantity());

        return order;
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }

    @Override
    public void placeOrder(Order order) {
        orderDao.save(order);
    }

    private BigDecimal calculateDeliveryCost(){
        return new BigDecimal(5);
    }


}
