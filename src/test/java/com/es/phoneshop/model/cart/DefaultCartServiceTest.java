package com.es.phoneshop.model.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;



import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCartServiceTest {
    private static CartService cartService;
    private static Cart cart;
    private static Product product1;
    private static Product product2;
    private static Product product3;
    private static Product product4;
    private static ProductDao productDao;


    @BeforeClass
    public static void setup() {
        cartService = DefaultCartService.getInstance();
        productDao = ArrayListProductDao.getInstance();
        cart = new Cart();

        Currency usd = Currency.getInstance("USD");

        product1 = new Product( "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        product2 = new Product( "sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
        product3 = new Product( "sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg");
        product4 = new Product("iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");

        productDao.save(product1);
        productDao.save(product2);
        productDao.save(product3);
        productDao.save(product4);

        cart.getItems().add(new CartItem(product1, 3));
        cart.getItems().add(new CartItem(product2, 5));
        cart.getItems().add(new CartItem(product3, 8));

    }


    @Test
    public void testAddToCartExistingProduct() throws OutOfStockException {
        int beforeUpdateSize = cart.getItems().size();
        cartService.add(cart, 1L, 1);
        assertEquals(beforeUpdateSize, cart.getItems().size());
    }

    @Test
    public void testAddProduct() throws OutOfStockException {
        int beforeAddSize = cart.getItems().size();

        cartService.add(cart, 4L, 1);
        assertEquals(beforeAddSize + 1, cart.getItems().size());
    }

    @Test(expected = OutOfStockException.class)
    public void testAddMoreThanAvailable() throws OutOfStockException {
        cartService.add(cart, 1L, 1000);
    }

    @Test
    public void testUpdateCartProduct() throws OutOfStockException {
        int beforeUpdateSize = cart.getItems().size();
        cartService.update(cart, 1L, 50);
        int afterUpdateSize = cart.getItems().size();
        assertEquals(50, cart.getItems().get(0).getQuantity());
        assertEquals(beforeUpdateSize, afterUpdateSize);
    }

    @Test(expected = OutOfStockException.class)
    public void testUpdateCartItemWithOutOfStockQuantity() throws OutOfStockException {
        cartService.update(cart, 1L, 1000);
    }

    @Test
    public void testDeleteNonExistingProducts() {
        int beforeUpdateSize = cart.getItems().size();
        cartService.delete(cart, 10L);
        cartService.delete(cart, 11L);
        int afterUpdateSize = cart.getItems().size();
        assertEquals(beforeUpdateSize, afterUpdateSize);
    }

    @Test
    public void testDeleteProducts() {
        cartService.delete(cart, 1L);
        cartService.delete(cart, 2L);
        cartService.delete(cart, 3L);

        System.out.println(cart.getItems());
        assertTrue(cart.getItems().isEmpty());
    }


}