package com.es.phoneshop.model.product;

import com.es.phoneshop.exception.ProductDaoException;
import com.es.phoneshop.exception.ProductNotFoundException;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {

    private static ProductDao productDao;

    public static ProductDao getInstance(){
        ProductDao localProductDaoInstance = productDao;
        if(localProductDaoInstance == null){
            synchronized (ArrayListProductDao.class){
                localProductDaoInstance = productDao;
                if (localProductDaoInstance == null){
                    productDao = localProductDaoInstance = new ArrayListProductDao();
                }
            }
        }
        return localProductDaoInstance;
    }
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock readLock = readWriteLock.readLock();
    private Lock writeLock = readWriteLock.writeLock();

    private long maxId = 0;

    private List<Product> products;

    private ArrayListProductDao() {
        this.products = new ArrayList<>();
    }

    @Override
    public Product getProduct(Long id) throws ProductNotFoundException {
        readLock.lock();
        try {
            return products.stream()
                    .filter(p -> id.equals(p.getId()))
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundException(id, "Product with id " + id + " not found"));
        } finally {
            readLock.unlock();
        }

    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        readLock.lock();

        try {
            List<Product> availableProducts = products.stream()
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .collect(Collectors.toList());
            if (query != null){
                return sortProductList(findProductsByQuery(query, availableProducts), sortField, sortOrder);
            }
            return sortProductList(availableProducts, sortField, sortOrder);

        } finally {
            readLock.unlock();
        }

    }

    private List<Product> findProductsByQuery(String query, List<Product> products) {
        String[] queryParts = query.split(" ");
        return products.stream()
                .filter(product -> Arrays.stream(queryParts).anyMatch(p ->
                        product.getDescription().contains(p)))
                .sorted(Comparator.comparing(p -> findMatchCount(p, queryParts), Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    private int findMatchCount(Product product, String[] queryParts){
        return  (int) Arrays.stream(queryParts)
                .filter(product.getDescription()::contains)
                .count();
    }

    private Comparator<Product> getComparator(SortField sortField, SortOrder sortOrder){
        if (sortField == SortField.PRICE){
            if (sortOrder == SortOrder.ASC){
                return  Comparator.comparing(Product::getPrice);
            }
            else {
                return Comparator.comparing(Product::getPrice, Comparator.reverseOrder());
            }
        }

        if (sortField == SortField.DESCRIPTION){
            if (sortOrder == SortOrder.ASC){
                return  Comparator.comparing(Product::getDescription);
            }
            else {
                return Comparator.comparing(Product::getDescription, Comparator.reverseOrder());
            }
        }
        return null;
    }

    private List<Product> sortProductList(List<Product> products, SortField sortField, SortOrder sortOrder){

        Comparator<Product> comparator = getComparator(sortField, sortOrder);

        if (comparator != null){
            return products.stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());
        }

        return products;
    }

    @Override
    public void save(Product product) throws ProductDaoException {
        writeLock.lock();
        try {
            if (product.getId() == null){
                product.setId(++maxId);
                products.add(product);
            } else {
                try {
                    Product productToUpdate = getProduct(product.getId());
                    products.set(products.indexOf(productToUpdate), product);
                } catch (ProductNotFoundException e){
                    throw new ProductDaoException("Product with id " + product.getId() + " doesn't exists");
                }
            }

        } finally {
            writeLock.unlock();
        }

    }

    @Override
    public void delete(Long id) throws ProductDaoException {
        writeLock.lock();
        try {
            products.stream()
                    .filter(product -> product.getId().equals(id))
                    .findFirst()
                    .map(product -> products.remove(product))
                    .orElseThrow(() -> new ProductDaoException("Product with id " + id + " not found"));
        } finally {
            writeLock.unlock();
        }
    }

    public List<Product> getProducts() {
        return products;
    }

}
