package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;


import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AddItemToCartServlet extends HttpServlet {

    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productIds =request.getParameterValues("productId");
        String[] quantities =request.getParameterValues("quantity");
        Long productId = Long.valueOf(request.getPathInfo().substring(1));

        Map<Long, String> errors = new HashMap<>();

        int index = Arrays.stream(productIds)
                .map(Long::valueOf)
                .collect(Collectors.toList())
                .indexOf(productId);

        try {
            int quantity = getQuantity(quantities[index], request);
            Cart cart = cartService.getCart(request);
            cartService.add(cart, productId, quantity);

        } catch (ParseException | OutOfStockException e) {
            handleError(errors, productId, e);
        }

        if(errors.isEmpty()){
            response.sendRedirect(request.getContextPath() + "/products?message=Product added successfully");
        } else {
            request.setAttribute("errors", errors);
            response.sendRedirect(request.getContextPath() + "/products?errors=" + errors.get(productId)+ "&id=" + productId);
        }
    }

    private int getQuantity(String quantityString, HttpServletRequest request) throws ParseException {
        NumberFormat numberFormat = NumberFormat.getInstance(request.getLocale());
        return numberFormat.parse(quantityString).intValue();
    }

    private void handleError(Map<Long, String> errors, Long productId, Exception e){
        if(e.getClass().equals(ParseException.class)){
            errors.put(productId, "Not a number");
        } else {
            if (((OutOfStockException) e).getStockRequested() <= 0){
                errors.put(productId, "Quantity can't be negative or zero");
            } else {
                errors.put(productId, "Out of stock, max available " + ((OutOfStockException) e).getStockAvailable());
            }
        }

    }
}
