package com.es.phoneshop.web;

import com.es.phoneshop.model.product.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdvancedSearchPageServlet extends HttpServlet {
    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, String> errors = new HashMap<>();

        String description = request.getParameter("description");
        String searchOption = request.getParameter("searchOption");

        request.setAttribute("searchOptions", SearchOption.getSearchOptions());

        BigDecimal minPrice = parsePriceFromRequest(request, "minPrice", errors);
        BigDecimal maxPrice = parsePriceFromRequest(request, "maxPrice", errors);

        if (errors.isEmpty()) {
            ArrayList<Product> products = (ArrayList<Product>) productDao.findProducts(description, searchOption, minPrice, maxPrice);
            request.setAttribute("products", products);
            request.setAttribute("message", "Found " + products.size() + " products");
        } else {
            request.setAttribute("products", new ArrayList<Product>());
            request.setAttribute("errors", errors);
        }
        request.getRequestDispatcher("/WEB-INF/pages/search.jsp").forward(request, response);
    }

    private BigDecimal parsePriceFromRequest(HttpServletRequest request, String parameter, Map<String, String> errors) {
        String priceString = request.getParameter(parameter);
        BigDecimal price = null;
        try {
            if (priceString == null || priceString.isEmpty()) {
                if(parameter.equals("minPrice")) {
                    price = new BigDecimal(0);
                } else {
                    price = new BigDecimal(Double.MAX_VALUE);
                }
            } else if (!priceString.matches("[0-9,.]+")) {
                throw new NumberFormatException("Not a number");
            } else {
                price = new BigDecimal(priceString);
            }
        } catch (NumberFormatException e) {
            errors.put(parameter, "Not a number");
        }
        return price;
    }

}
