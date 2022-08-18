package com.uniuwo.dict.model;

public class WordCount implements Comparable<WordCount> {
    private String word;
    private Integer count;

    public WordCount() {
    }

    public WordCount(String word, Integer count) {
        this.word = word;
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer increase(){
        count++;
        return  count;
    }

    public Integer increase(Integer countAdd){
        count+= countAdd;
        return  count;
    }

    @Override
    public String toString() {
        return word + '=' + count;
    }

    @Override
    public int compareTo(WordCount o) {
        return this.word.compareTo(o.word);
    }

    public static WordCount fromString(String raw){
        if(raw == null || raw.isBlank()) return null;

        if(raw.contains("=")){
            String[] splits = raw.split("=");
            if(splits.length == 2){
                return new WordCount(splits[0], Integer.parseInt(splits[1]));
            }
        }

        return null;
    }
}
