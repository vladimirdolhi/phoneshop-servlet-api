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
        Product product = new Product("test", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        Product foundProduct = productDao.getProduct(product.getId());
        assertNotNull(foundProduct);
        assertEquals("test", foundProduct.getCode());
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testFindProductWithZeroStock() throws ProductDaoException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("test", "Samsung Galaxy S", new BigDecimal(100), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        List<Product> productList = productDao.findProducts();
        assertFalse(productList.contains(product));
    }

    @Test
    public void testFindProductWithNullPrice() throws ProductDaoException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("test", "Samsung Galaxy S", null, usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        List<Product> productList = productDao.findProducts();
        assertFalse(productList.contains(product));
    }


    @Test
    public void testDeleteProduct() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product( "testProductToDelete", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Long id = null;
        try {
            productDao.save(product);
            id = Long.valueOf(productDao.findProducts().indexOf(product));
            productDao.delete(id);
            Product foundProduct = productDao.getProduct(id);
            fail("Expected ProductDaoException");
        } catch (ProductDaoException e) {
            assertEquals(new ProductDaoException("Product with id " + id + " not found").getMessage(), e.getMessage());
        }

    }


    @Test
    public void testGetProduct() throws ProductDaoException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("testProductToGet", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        System.out.println(productDao.getProduct(product.getId()));
        assertNotNull(productDao.getProduct(product.getId()));
    }

    @Test
    public void testUpdateProduct() throws ProductDaoException {
        Currency usd = Currency.getInstance("USD");
        Product productBeforeUpdate = new Product("testProductBeforeUpdate", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(productBeforeUpdate);
        long id = productBeforeUpdate.getId();
        Product updatedProduct = new Product(id, "testProductUpdated", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(updatedProduct);
        assertEquals("testProductUpdated", productDao.getProduct(id).getCode());
    }

    @Test
    public void testUpdateNonExistProduct() throws ProductDaoException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product(50L,"testProductBeforeUpdate", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

        try {
            productDao.save(product);
            fail("Expected ProductDaoException");
        } catch (ProductDaoException e) {
            assertEquals(new ProductDaoException("Product with id 50 doesn't exists").getMessage(), e.getMessage());
        }
    }

}
