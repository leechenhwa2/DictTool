package com.uniuwo.dict.model;

import java.util.ArrayList;
import java.util.List;

public class SearchIndex {
    private String file;
    private List<String> searchWords;

    public SearchIndex() {
    }

    public SearchIndex(String file, List<String> searchWords) {
        this.file = file;
        this.searchWords = searchWords;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public List<String> getSearchWords() {
        if (searchWords == null) searchWords = new ArrayList<>();

        return searchWords;
    }

    public void setSearchWords(List<String> searchWords) {
        this.searchWords = searchWords;
    }
}
