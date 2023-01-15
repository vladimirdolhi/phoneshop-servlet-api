package com.es.phoneshop.model.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

public class DefaultCartService implements CartService{

    private Cart cart = new Cart();

    private ProductDao productDao;

    private DefaultCartService() {
        this.productDao = ArrayListProductDao.getInstance();
    }

    private static CartService cartService;

    public static CartService getInstance(){
        CartService localDefaultCartService = cartService;
        if(localDefaultCartService == null){
            synchronized (ArrayListProductDao.class){
                localDefaultCartService = cartService;
                if (localDefaultCartService == null){
                    cartService = localDefaultCartService = new DefaultCartService();
                }
            }
        }
        return localDefaultCartService;
    }
    @Override
    public Cart getCart() {
        return this.cart;
    }

    @Override
    public void add(Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(productId);
        if (product.getStock() < quantity){
            throw new OutOfStockException(product, quantity, product.getStock());
        }
        cart.getItems().add(new CartItem(product, quantity));
    }
}
