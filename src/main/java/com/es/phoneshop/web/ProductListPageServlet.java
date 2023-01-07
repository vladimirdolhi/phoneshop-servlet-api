package com.es.phoneshop.web;

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

public class ProductListPageServlet extends HttpServlet {
    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String sortFieldStr = request.getParameter("sort");
        String sortOrderStr = request.getParameter("order");
        String query = request.getParameter("query");

        SortField sortField = sortFieldStr == null ? null : SortField.valueOf(sortFieldStr.toUpperCase());
        SortOrder sortOrder = sortOrderStr == null ? null : SortOrder.valueOf(sortOrderStr.toUpperCase());

        request.setAttribute("products", productDao.findProducts(query, sortField, sortOrder));

        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }


}
