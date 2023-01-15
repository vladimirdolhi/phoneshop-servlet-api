package com.es.phoneshop.model.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultCartService implements CartService{

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock writeLock = readWriteLock.writeLock();
    private Lock readLock = readWriteLock.readLock();
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
        readLock.lock();
        try {
            return this.cart;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void add(Long productId, int quantity) throws OutOfStockException {
        writeLock.lock();
        try{
            Product product = productDao.getProduct(productId);
            if (product.getStock() < quantity){
                throw new OutOfStockException(product, quantity, product.getStock());
            }
            cart.getItems().add(new CartItem(product, quantity));
        }
        finally {
            writeLock.unlock();
        }

    }
}
