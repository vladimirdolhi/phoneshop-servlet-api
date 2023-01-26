package com.es.phoneshop.web;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MiniCartServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;


    private final String MINICART_PAGE_SERVLET = "/WEB-INF/pages/minicart.jsp";
    private MiniCartServlet servlet = new MiniCartServlet();

    @Test(expected = NullPointerException.class)
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(request).getRequestDispatcher(eq(MINICART_PAGE_SERVLET));
        verify(requestDispatcher).include(request, response);
    }
}