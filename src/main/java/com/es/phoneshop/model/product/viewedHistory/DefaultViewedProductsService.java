package com.es.phoneshop.model.product.viewedHistory;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class DefaultViewedProductsService implements ViewedProductsService{

    private static final String VIEWED_PRODUCTS = "viewedProducts";
    private static final int MAX_VIEWED_PRODUCTS = 4;

    private DefaultViewedProductsService() {
    }

    private static ViewedProductsService service;

    public static ViewedProductsService getInstance(){
        ViewedProductsService localService = service;
        if(localService == null){
            synchronized (DefaultViewedProductsService.class){
                localService = service;
                if (localService == null){
                    service = localService = new DefaultViewedProductsService();
                }
            }
        }
        return localService;
    }
    @Override
    public ViewedProductsHistory getViewedProducts(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        ViewedProductsHistory viewedProductsHistory =
                (ViewedProductsHistory) httpSession.getAttribute(VIEWED_PRODUCTS);
        if(viewedProductsHistory == null){
            viewedProductsHistory = new ViewedProductsHistory();
            httpSession.setAttribute(VIEWED_PRODUCTS, viewedProductsHistory);
        }

        return viewedProductsHistory;
    }

    @Override
    public void add(ViewedProductsHistory viewedProducts, Product product) {

        boolean dequeAlreadyContainsProduct = viewedProducts.getRecentlyViewedProducts()
                .stream()
                .anyMatch(p -> p.equals(product));

        if(dequeAlreadyContainsProduct){
            viewedProducts.deleteViewedProduct(product);
        } else {
            if(viewedProducts.getRecentlyViewedProducts().size() == MAX_VIEWED_PRODUCTS){
                viewedProducts.deleteLastViewedProduct();
            }
        }
        viewedProducts.addViewedProductFirst(product);
    }
}
