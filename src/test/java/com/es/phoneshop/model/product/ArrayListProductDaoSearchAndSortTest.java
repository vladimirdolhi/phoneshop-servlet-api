package com.es.phoneshop.model.product;

import com.es.phoneshop.exception.ProductDaoException;
import com.es.phoneshop.exception.ProductNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArrayListProductDaoSearchAndSortTest {


    @Mock
    private Product product1;
    @Mock
    private Product product2;
    @Mock
    private Product product3;
    @Mock
    private Product product4;
    @Mock
    private Product product5;
    @Mock
    private Product product6;


    private static ArrayListProductDao productDao;

    @Before
    public void setup() {

        productDao = (ArrayListProductDao) ArrayListProductDao.getInstance();
        productDao.getProducts().addAll(Arrays.asList(product1, product2, product3, product4, product5, product6));

        when(product1.getPrice()).thenReturn(new BigDecimal(450));
        when(product1.getStock()).thenReturn(25);
        when(product1.getDescription()).thenReturn("Samsung A72");

        when(product2.getPrice()).thenReturn(new BigDecimal(1000));
        when(product2.getStock()).thenReturn(5);
        when(product2.getDescription()).thenReturn("Google Pixel 7 Pro");

        when(product3.getPrice()).thenReturn(new BigDecimal(1300));
        when(product3.getStock()).thenReturn(10);
        when(product3.getDescription()).thenReturn("Google Pixel 7");

        when(product4.getPrice()).thenReturn(new BigDecimal(250));
        when(product4.getStock()).thenReturn(285);
        when(product4.getDescription()).thenReturn("Siemens C56");

        when(product5.getPrice()).thenReturn(new BigDecimal(250));
        when(product5.getStock()).thenReturn(285);
        when(product5.getDescription()).thenReturn("Nokia 3310");

        when(product6.getPrice()).thenReturn(new BigDecimal(1100));
        when(product6.getStock()).thenReturn(285);
        when(product6.getDescription()).thenReturn("Samsung Galaxy S");
        
    }

    @After
    @Test
    public void clear() {
        productDao.getProducts().clear();
    }

    @Test
    public void testDefaultFindProducts() {
        List<Product> result = productDao.findProducts("", null, null);
        assertTrue(result.size() == 6);
    }


    @Test
    public void testFindProductWithDefiniteQuery() {
        List<Product> result = productDao.findProducts("Nokia 3310", null, null);
        assertTrue(result.get(0).getDescription().equals("Nokia 3310"));
    }

    @Test
    public void testFindProductWithDefiniteNonExistQuery() {
        List<Product> result = productDao.findProducts("qgwvhwj", null, null);
        assertTrue(result.size() == 0);
    }

    @Test
    public void testFindProductWithQuery() {
        List<Product> result = productDao.findProducts("Google Pixel 7", null, null);
        assertEquals(Arrays.asList(product2, product3, product1), result);
    }

    @Test
    public void testFindProductWithSortAscByPrice() {
        List<Product> result = productDao.findProducts("", SortField.PRICE, SortOrder.ASC);
        assertEquals(productDao.getProducts().stream().
                sorted(Comparator.comparing(Product::getPrice))
                .collect(Collectors.toList()), result);
    }

    @Test
    public void testFindProductWithSortDescByPrice() {
        List<Product> result = productDao.findProducts("", SortField.PRICE, SortOrder.DESC);
        assertEquals(productDao.getProducts().stream().
                sorted(Comparator.comparing(Product::getPrice).reversed())
                .collect(Collectors.toList()), result);
    }


    @Test
    public void testFindProductWithSortAscByDescription() {
        List<Product> result = productDao.findProducts("", SortField.DESCRIPTION, SortOrder.ASC);
        assertEquals(productDao.getProducts().stream().
                sorted(Comparator.comparing(Product::getDescription))
                .collect(Collectors.toList()), result);
    }

    @Test
    public void testFindProductWithSortDescByDescription() {
        List<Product> result = productDao.findProducts("", SortField.DESCRIPTION, SortOrder.DESC);
        assertEquals(productDao.getProducts().stream().
                sorted(Comparator.comparing(Product::getDescription).reversed())
                .collect(Collectors.toList()), result);
    }


    @Test
    public void testFindProductWithQueryAndSort() {
        List<Product> result = productDao.findProducts("7 3", SortField.PRICE, SortOrder.DESC);
        assertEquals(Arrays.asList(product3, product2, product1, product5), result);
    }


}
