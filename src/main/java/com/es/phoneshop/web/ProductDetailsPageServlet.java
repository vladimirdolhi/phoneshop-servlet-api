package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.product.*;
import com.es.phoneshop.model.product.viewedHistory.DefaultViewedProductsService;
import com.es.phoneshop.model.product.viewedHistory.ViewedProductsHistory;
import com.es.phoneshop.model.product.viewedHistory.ViewedProductsService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;

    private CartService cartService;

    private ViewedProductsService viewedProductsService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        viewedProductsService = DefaultViewedProductsService.getInstance();

    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long productId = parseProductId(request);
        Product currentProduct = productDao.getById(productId);
        request.setAttribute("product", currentProduct);
        request.setAttribute("cart", cartService.getCart(request));
        ViewedProductsHistory viewedProductsHistory = viewedProductsService.getViewedProducts(request);
        viewedProductsService.add(viewedProductsHistory, currentProduct);
        request.setAttribute("viewedProducts", viewedProductsHistory);
        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quantityStr = request.getParameter("quantity");
        Long productId = parseProductId(request);

        int quantity;
        try {
            request.getLocale();
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            quantity = format.parse(quantityStr).intValue();
        } catch (ParseException ex){
            request.setAttribute("error", "Not a number");
            doGet(request, response);
            return;
        }
        Cart cart = cartService.getCart(request);
        try {
            cartService.add(cart, productId, quantity);
        } catch (OutOfStockException e) {
            request.setAttribute("error", "Out of stock, available " + e.getStockAvailable());
            doGet(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/products/" + productId + "?message=Product added to cart");
    }

    private long parseProductId(HttpServletRequest request){
        return Long.valueOf(request.getPathInfo().substring(1));
    }
}
