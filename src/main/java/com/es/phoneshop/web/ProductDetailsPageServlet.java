package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductDaoException;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;

    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long productId = parseProductId(request);
        request.setAttribute("product", productDao.getProduct(productId));
        request.setAttribute("cart", cartService.getCart());
        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quantityStr = request.getParameter("quantity");
        long productId = parseProductId(request);

        int quantity;
        try {
            quantity = Integer.valueOf(quantityStr);
        } catch (NumberFormatException ex){
            request.setAttribute("error", "Not a number");
            doGet(request, response);
            return;
        }
        try {
            cartService.add(productId, quantity);
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
