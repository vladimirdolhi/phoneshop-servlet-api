package com.es.phoneshop.model.product;

import com.es.phoneshop.exception.ProductDaoException;

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
        try {
            initProducts();
        } catch (ProductDaoException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product getProduct(Long id) throws ProductDaoException {
        readLock.lock();
        try {
            return products.stream()
                    .filter(p -> id.equals(p.getId()))
                    .findAny()
                    .orElseThrow(() -> new ProductDaoException("Product with id " + id + " not found"));
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
                } catch (ProductDaoException e){
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

    private void initProducts() throws ProductDaoException {

        Currency usd = Currency.getInstance("USD");
        save(new Product( "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        save(new Product( "sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        save(new Product( "sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        save(new Product( "iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        save(new Product( "iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        save(new Product( "htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        save(new Product( "sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        save(new Product( "xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        save(new Product( "nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        save(new Product( "palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        save(new Product( "simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        save(new Product( "simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        save(new Product( "simsxg75", "Siemens SXG751", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));

    }
}
