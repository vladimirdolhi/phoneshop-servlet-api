package com.es.phoneshop.exception;

public class DaoNotFoundException extends Exception{

    private Long id;
    public DaoNotFoundException(Long id, String message) {
        super(message);
        this.id = id;
    }

    public DaoNotFoundException(String message) {
        super(message);
    }

    public DaoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoNotFoundException(Throwable cause) {
        super(cause);
    }

    public Long getId() {
        return id;
    }
}
