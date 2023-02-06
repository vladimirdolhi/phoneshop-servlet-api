package com.es.phoneshop.model.product;

import com.es.phoneshop.exception.DaoNotFoundException;
import com.es.phoneshop.exception.ProductDaoException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.dao.GenericArrayListDao;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ArrayListProductDao extends GenericArrayListDao<Product> implements ProductDao {

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

    private ArrayListProductDao() {
    }

    @Override
    public Product getById(Long id) throws ProductNotFoundException {
        try {
            return super.get(id);
        } catch (DaoNotFoundException e) {
            throw new ProductNotFoundException(id, "Product with id " + id + " not found");
        }
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        getLock().readLock().lock();

        try {
            List<Product> availableProducts = getItems().stream()
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .collect(Collectors.toList());
            if (query != null){
                return sortProductList(findProductsByQuery(query, availableProducts), sortField, sortOrder);
            }
            return sortProductList(availableProducts, sortField, sortOrder);

        } finally {
            getLock().readLock().unlock();
        }

    }

    @Override
    public List<Product> findProducts(String description, String searchOption, BigDecimal minPrice, BigDecimal maxPrice) {
        if (description == null && searchOption == null) {
            return new ArrayList<>();
        }
        List<Product> availableProducts = getItems().stream()
                .filter(product -> product.getPrice() != null)
                .filter(product -> product.getStock() > 0)
                .collect(Collectors.toList());

        if (searchOption == null || SearchOption.valueOf(searchOption) == SearchOption.ANY_WORDS) {
            return findProductsByQuery(description, availableProducts).stream()
                    .filter(product -> product.getPrice().compareTo(minPrice) >= 0)
                    .filter(product -> product.getPrice().compareTo(maxPrice) <= 0)
                    .collect(Collectors.toList());
        } else {
            return findProductsByQueryAllMatch(description , availableProducts).stream()
                    .filter(product -> product.getPrice().compareTo(minPrice) >= 0)
                    .filter(product -> product.getPrice().compareTo(maxPrice) <= 0)
                    .collect(Collectors.toList());
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

    private List<Product> findProductsByQueryAllMatch(String query, List<Product> products) {
        String[] queryParts = query.split(" ");
        return products.stream()
                .filter(product -> Arrays.stream(queryParts).allMatch(p ->
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
        try {
            super.save(product);
        } catch (DaoNotFoundException e){
            throw new ProductNotFoundException("Product with id " + product.getId() + " doesn't exists");
        }
    }

    @Override
    public void delete(Long id) throws ProductDaoException {
        try {
            super.delete(id);
        } catch (DaoNotFoundException e){
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }
    }

    public List<Product> getProducts() {
        return super.getItems();
    }



}
