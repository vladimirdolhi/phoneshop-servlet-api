package com.es.phoneshop.web;

import com.es.phoneshop.model.order.DefaultOrderService;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderService;
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

import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderOverviewPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;

    private final OrderOverviewPageServlet servlet = new OrderOverviewPageServlet();
    private final OrderService orderService = DefaultOrderService.getInstance();
    private final Order order = new Order();

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        orderService.placeOrder(order);
        String secureId = order.getSecureId();
        when(request.getPathInfo()).thenReturn("/" + secureId);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGet() throws IOException, ServletException {
        servlet.doGet(request, response);
        verify(request).setAttribute(eq("order"), any());
        verify(requestDispatcher).forward(request, response);
    }

}