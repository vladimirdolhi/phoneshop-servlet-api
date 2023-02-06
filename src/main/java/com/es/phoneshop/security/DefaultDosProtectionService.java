package com.es.phoneshop.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService{

    private static final long TIME_INTERVAL_MILLS = 60_000;
    private static final long MAX_LEGAL_REQUEST_COUNT = 20;

    private Map<String, Long> countMap = new ConcurrentHashMap<>();
    private Map<String, Long> timeMap = new ConcurrentHashMap<>();

    private DefaultDosProtectionService() {
    }

    private static DosProtectionService service;

    public static DosProtectionService getInstance(){
        DosProtectionService localService = service;
        if(localService == null){
            synchronized (DefaultDosProtectionService.class){
                localService = service;
                if (localService == null){
                    service = localService = new DefaultDosProtectionService();
                }
            }
        }
        return localService;
    }
    @Override
    public boolean isAllowed(String ip) {
        Long currentTime = System.currentTimeMillis();
        Long lastRequestTime = timeMap.get(ip);
        if(lastRequestTime != null && currentTime - lastRequestTime > TIME_INTERVAL_MILLS){
            countMap.remove(ip);
            timeMap.remove(ip);
        }
        Long requestCount = countMap.get(ip);
        if(requestCount == null){
            requestCount = 0L;
        } else if (requestCount > MAX_LEGAL_REQUEST_COUNT){
            return false;
        }
        countMap.put(ip, ++requestCount);
        timeMap.put(ip, System.currentTimeMillis());
        return true;
    }
}
