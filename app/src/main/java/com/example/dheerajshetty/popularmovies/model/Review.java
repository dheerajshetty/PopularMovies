package com.example.dheerajshetty.popularmovies.model;

/**
 * Created by dheerajshetty on 4/4/16.
 */
public class Review {
    String id;
    String author;
    String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
