package com.uniuwo.dict.model;

public class DictEntry {
    private int id;

    /**
     * word with accents
     */
    private String word;
    private String content;

    /**
     * Primary search word without accents. Mostly not null.
     */
    private String word1;

    /**
     * Secondary word without accents. May be null.
     */
    private String word2;

    public DictEntry() {
    }

    public DictEntry(String word, String content) {
        this.word = word;
        this.content = content;
    }

    public DictEntry(String word, String content, String word1, String word2) {
        this.word = word;
        this.content = content;
        this.word1 = word1;
        this.word2 = word2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWord1() {
        return word1;
    }

    public void setWord1(String word1) {
        this.word1 = word1;
    }

    public String getWord2() {
        return word2;
    }

    public void setWord2(String word2) {
        this.word2 = word2;
    }

    @Override
    public String toString() {
        return word + " :: " + content;
    }

    /**
     * For HTML content with def.
     * @return
     */
    public String toHtmlString() {
        return "<span class=\"word\"><span class=\"lema\">" + word + "</span> " + content + "</span>";
    }

    public String toHtmlStringWithId(String id) {
        return "<span class=\"word\"><span class=\"lema\" id=\"" + id + "\">" + word + "</span> " + content + "</span>";
    }

    public String toRawHtmlString() {
        return content;
    }
}
