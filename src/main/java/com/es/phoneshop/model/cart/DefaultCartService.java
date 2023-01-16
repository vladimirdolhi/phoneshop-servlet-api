package com.es.phoneshop.model.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultCartService implements CartService{

    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock writeLock = readWriteLock.writeLock();
    private Lock readLock = readWriteLock.readLock();

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
    public Cart getCart(HttpServletRequest request) {
        readLock.lock();
        try {
            Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
            if(cart == null){
                request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
            }
            return cart;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        writeLock.lock();
        try {
            Product product = productDao.getProduct(productId);
            if (product.getStock() < quantity) {
                throw new OutOfStockException(product, quantity, product.getStock());
            }

            Optional<CartItem> addedItem = cart.getItems().stream()
                    .filter(item -> product.getId().equals(item.getProduct().getId()))
                    .findAny();

            if (addedItem.isPresent()) {
                CartItem cartItem = addedItem.get();
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartItem.getProduct().setStock(cartItem.getProduct().getStock() - quantity);
            } else {
                cart.getItems().add(new CartItem(product, quantity));
                product.setStock(product.getStock() - quantity);
            }

        } finally {
            writeLock.unlock();
        }

    }
}
