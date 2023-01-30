package com.es.phoneshop.web;

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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;
    @Mock
    private HttpSession session;

    private final CheckoutPageServlet servlet = new CheckoutPageServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init(config);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
    }


    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("paymentMethods"), any());
        verify(request).setAttribute(eq("order"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostWithCorrectData() throws IOException, ServletException {
        when(request.getParameter("firstName")).thenReturn("Testname");
        when(request.getParameter("lastName")).thenReturn("Testname");
        when(request.getParameter("deliveryAddress")).thenReturn("Test adress");
        when(request.getParameter("phone")).thenReturn("+375 44 234-21-12");
        when(request.getParameter("deliveryDate")).thenReturn("2024-12-23");
        when(request.getParameter("paymentMethod")).thenReturn("CACHE");

        servlet.doPost(request, response);
        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoPostOneParameter() throws IOException, ServletException {
        when(request.getParameter("firstName")).thenReturn("Testname");

        servlet.doPost(request, response);
        verify(request).setAttribute(eq("errors"), any());
        verify(request).setAttribute(eq("order"), any());
        verify(request).setAttribute(eq("paymentMethods"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostInCorrectDate() throws IOException, ServletException {
        when(request.getParameter("deliveryDate")).thenReturn("2018-01-01");
        when(request.getParameter("phone")).thenReturn("kjsfdhbhsdbsdsfd");

        servlet.doPost(request, response);
        verify(request).setAttribute(eq("errors"), any());
        verify(request).setAttribute(eq("order"), any());
        verify(request).setAttribute(eq("paymentMethods"), any());
        verify(requestDispatcher).forward(request, response);
    }

}