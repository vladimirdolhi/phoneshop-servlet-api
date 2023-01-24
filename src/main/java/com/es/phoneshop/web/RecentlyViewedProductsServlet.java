package com.es.phoneshop.web;

import com.es.phoneshop.model.product.viewedHistory.DefaultViewedProductsService;
import com.es.phoneshop.model.product.viewedHistory.ViewedProductsService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RecentlyViewedProductsServlet extends HttpServlet {
    private static final String VIEWED_PRODUCTS = "/WEB-INF/pages/recentlyViewedProducts.jsp";
    private ViewedProductsService viewedProductsService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        viewedProductsService = DefaultViewedProductsService.getInstance();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("viewedProducts", viewedProductsService.getViewedProducts(request));
        request.getRequestDispatcher(VIEWED_PRODUCTS).include(request, response);
    }
}
