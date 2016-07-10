package com.example.dheerajshetty.popularmovies.model;

import java.util.List;

/**
 * Created by dheerajshetty on 3/26/16.
 */
public class TrailerQuestion {
    int id;
    List<Trailer> results;

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
