package com.es.phoneshop.model.product;

import com.es.phoneshop.exception.ProductDaoException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testSaveNewProduct() throws ProductDaoException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product(19L, "test", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        Product foundProduct = productDao.getProduct(product.getId());
        assertNotNull(foundProduct);
        assertEquals("test", foundProduct.getCode());
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testFindProductWithZeroStock() throws ProductDaoException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product(23L, "test", "Samsung Galaxy S", new BigDecimal(100), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        List<Product> productList = productDao.findProducts();
        assertFalse(productList.contains(product));
    }

    @Test
    public void testFindProductWithNullPrice() throws ProductDaoException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product(24L, "test", "Samsung Galaxy S", null, usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        List<Product> productList = productDao.findProducts();
        assertFalse(productList.contains(product));
    }


    @Test
    public void testDeleteProduct() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product(25L, "testProductToDelete", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        try {
            productDao.save(product);
            productDao.delete(25L);
            Product foundProduct = productDao.getProduct(25L);
            fail("Expected ProductDaoException");
        } catch (ProductDaoException e) {
            assertEquals(e.getMessage(), new ProductDaoException("Product with id 25 not found").getMessage());
        }

    }

    @Test
    public void testSaveProductWithNullId() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product(null, "testProductToDelete", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        try {
            productDao.save(product);
            fail("Expected ProductDaoException");
        } catch (ProductDaoException e) {
            assertEquals(e.getMessage(), new ProductDaoException("Product id is not initialized").getMessage());
        }

    }

    @Test
    public void testGetProduct() throws ProductDaoException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product(31L,"testProductToGet", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        productDao.save(product);
        assertEquals(productDao.getProduct(product.getId()), product);
    }

}
