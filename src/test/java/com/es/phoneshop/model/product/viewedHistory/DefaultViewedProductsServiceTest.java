package com.es.phoneshop.model.product.viewedHistory;

import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.SessionCookieConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Deque;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultViewedProductsServiceTest {

    private ViewedProductsService viewedProductsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;
    @Before
    public void setUp() throws Exception {
        viewedProductsService = DefaultViewedProductsService.getInstance();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("viewedProducts")).thenReturn(new ViewedProductsHistory());
    }

    @Test
    public void testNotNullAndEmpty() {
        Deque<Product> result = viewedProductsService.getViewedProducts(request).getRecentlyViewedProducts();
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testAddProductsToHistory() {
        Currency usd = Currency.getInstance("USD");

        Product product1 = new Product( "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product product2 = new Product( "sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
        Product product3 = new Product( "sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg");

        viewedProductsService.add(viewedProductsService.getViewedProducts(request), product1);
        viewedProductsService.add(viewedProductsService.getViewedProducts(request), product2);
        viewedProductsService.add(viewedProductsService.getViewedProducts(request), product3);

        Deque<Product> result = viewedProductsService.getViewedProducts(request).getRecentlyViewedProducts();
        assertNotNull(result);

        assertEquals(result.getFirst(), product3);
        assertEquals(result.getLast(), product1);

    }

    @Test
    public void testAddAlreadyViewedProduct() {
        Currency usd = Currency.getInstance("USD");

        Product product1 = new Product( "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        viewedProductsService.add(viewedProductsService.getViewedProducts(request), product1);
        viewedProductsService.add(viewedProductsService.getViewedProducts(request), product1);
        viewedProductsService.add(viewedProductsService.getViewedProducts(request), product1);

        Deque<Product> result = viewedProductsService.getViewedProducts(request).getRecentlyViewedProducts();

        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(result.getLast(), result.getFirst());

    }

}