package com.es.phoneshop.web.filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DosFilterTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterConfig filterConfig;
    @Mock
    private FilterChain chain;

    private final DosFilter filter = new DosFilter();

    @Before
    public void setup() throws ServletException {
        filter.init(filterConfig);
        when(request.getRemoteAddr()).thenReturn("21321");
    }

    @Test
    public void testDoFilter() throws ServletException, IOException {
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }

}