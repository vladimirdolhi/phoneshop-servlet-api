package com.es.phoneshop.model.dao;

import com.es.phoneshop.exception.DaoNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GenericArrayListDao<T extends Entity>{
    private List<T> items;
    private long maxId;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock readLock = readWriteLock.readLock();
    private Lock writeLock = readWriteLock.writeLock();

    public GenericArrayListDao() {
        items = new ArrayList<>();
    }

    public T get(Long id) throws DaoNotFoundException {
        readLock.lock();
        try {
            return items.stream()
                    .filter(p -> id.equals(p.getId()))
                    .findAny()
                    .orElseThrow(() -> new DaoNotFoundException(id, "Item with id " + id + " not found"));
        } finally {
            readLock.unlock();
        }
    }

    public void save(T item) throws DaoNotFoundException {
        writeLock.lock();
        try {
            if (item.getId() == null){
                item.setId(++maxId);
                items.add(item);
            } else {
                T itemToUpdate = get(item.getId());
                items.set(items.indexOf(itemToUpdate), item);
            }
        } finally {
            writeLock.unlock();
        }
    }

    public void delete(Long id) throws DaoNotFoundException {
        writeLock.lock();
        try {
            items.stream()
                    .filter(product -> product.getId().equals(id))
                    .findFirst()
                    .map(product -> items.remove(product))
                    .orElseThrow(() -> new DaoNotFoundException("Item with id " + id + " not found"));
        } finally {
            writeLock.unlock();
        }
    }
    public List<T> getItems() {
        return items;
    }

    public ReadWriteLock getLock() {
        return readWriteLock;
    }
}
