package com.example.multisearch.model;

public class SearchResult {
    private String word;
    private String definition;
    private String error;
    private String url;

    // 생성자
    public SearchResult() {
    }

    // Getter 및 Setter 메서드
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

