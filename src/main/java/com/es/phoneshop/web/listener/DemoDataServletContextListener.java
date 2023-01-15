package com.es.phoneshop.web.listener;

import com.es.phoneshop.exception.ProductDaoException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.PriceHistory;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;

public class DemoDataServletContextListener implements ServletContextListener {

    private ProductDao productDao;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        this.productDao = ArrayListProductDao.getInstance();

        boolean insertDemoData = Boolean.valueOf(event.getServletContext().getInitParameter("insertDemoData"));

        if (insertDemoData){
            try {
                initProducts();
            } catch (ProductDaoException e) {
                throw new RuntimeException("Filed to insert demo data");
            }
         }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    private void initProducts() throws ProductDaoException {
        Currency usd = Currency.getInstance("USD");


        productDao.save(new Product( "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", generateRandomPriceHistory()));
        productDao.save(new Product( "sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg",generateRandomPriceHistory()));
        productDao.save(new Product( "sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg", generateRandomPriceHistory()));
        productDao.save(new Product( "iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg",generateRandomPriceHistory()));
        productDao.save(new Product( "iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg",generateRandomPriceHistory()));
        productDao.save(new Product( "htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg",generateRandomPriceHistory()));
        productDao.save(new Product( "sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg", generateRandomPriceHistory()));
        productDao.save(new Product( "xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg", generateRandomPriceHistory()));
        productDao.save(new Product( "nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg", generateRandomPriceHistory()));
        productDao.save(new Product( "palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg", generateRandomPriceHistory()));
        productDao.save(new Product( "simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg", generateRandomPriceHistory()));
        productDao.save(new Product( "simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg", generateRandomPriceHistory()));
        productDao.save(new Product( "simsxg75", "Siemens SXG751", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg",generateRandomPriceHistory()));

    }

    List <PriceHistory> generateRandomPriceHistory(){
        Random random = new SecureRandom();
        List<String> monthList = Arrays.asList("jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec");
        List<String> yearList = Arrays.asList("2019", "2020", "2021", "2022", "2023");


        Currency usd = Currency.getInstance("USD");
        int len = random.nextInt(5) + 1;


        List <PriceHistory> priceHistoryList = new ArrayList<>();

        for(int i = 0; i < len; i++){
            String randomMonth = monthList.get(random.nextInt(monthList.size()));
            String randomYear = yearList.get(random.nextInt(yearList.size()));
            BigDecimal price = BigDecimal.valueOf( random.nextInt(1200) + 100);
            String date = randomMonth + " " + randomYear;
            priceHistoryList.add(new PriceHistory(date, price, usd));
        }
        return priceHistoryList;
    }

}
