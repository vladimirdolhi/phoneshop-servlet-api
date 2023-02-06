package com.es.phoneshop.model.product;

import java.util.Arrays;
import java.util.List;

public enum SearchOption {
    ALL_WORDS,
    ANY_WORDS;

    public static List<SearchOption> getSearchOptions() {
        return Arrays.asList(SearchOption.values());
    }
}
