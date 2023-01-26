package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddItemToCartServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private HttpSession session;

    private ProductDao productDao = ArrayListProductDao.getInstance();

    @Mock
    private ServletConfig servletConfig;

    private AddItemToCartServlet servlet = new AddItemToCartServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init(servletConfig);
        when(request.getSession()).thenReturn(session);
        when(request.getLocale()).thenReturn(new Locale("RU"));
    }

    @Test
    public void testDoPost() throws ServletException, OutOfStockException, IOException {
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"10"});
        Currency usd = Currency.getInstance("USD");
        Product product = new Product( "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        CartService cartService = DefaultCartService.getInstance();
        Cart cart = cartService.getCart(request);
        cartService.add(cart, product.getId(), 1);
        when(request.getParameterValues("productId")).thenReturn(new String[]{product.getId().toString()});
        when(request.getSession().getAttribute(any())).thenReturn(cart);
        when(request.getPathInfo()).thenReturn("/25");
        servlet.doPost(request, response);
        assertEquals(11, cart.getItems().get(0).getQuantity());
        verify(response).sendRedirect(request.getContextPath() + "/products?message=Product added successfully");
    }

    @Test
    public void testDoPostWithError() throws ServletException, OutOfStockException, IOException {
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"101"});
        Currency usd = Currency.getInstance("USD");
        Product product = new Product( "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        CartService cartService = DefaultCartService.getInstance();
        Cart cart = cartService.getCart(request);
        cartService.add(cart, product.getId(), 10);
        when(request.getParameterValues("productId")).thenReturn(new String[]{product.getId().toString()});
        when(request.getSession().getAttribute(any())).thenReturn(cart);
        when(request.getPathInfo()).thenReturn("/24");
        servlet.doPost(request, response);
        assertEquals(10, cart.getItems().get(0).getQuantity());
        verify(response).sendRedirect(request.getContextPath() + "/products?errors=Out of stock, max available 100&id=24");
    }
}