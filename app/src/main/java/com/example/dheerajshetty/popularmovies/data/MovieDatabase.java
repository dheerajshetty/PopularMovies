package com.example.dheerajshetty.popularmovies.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by dheerajshetty on 3/19/16.
 */

@Database(version = MovieDatabase.VERSION)
public class MovieDatabase {
    public static final int VERSION = 2;

    @Table(MovieColumns.class) public static final String MOVIES = "movies";
    @Table(MovieColumns.class) public static final String FAVORITE_MOVIES = "favorite_movies";
    @Table(TrailerColumns.class) public static final String MOVIE_TRAILERS = "movie_trailers";
    @Table(ReviewColumns.class) public static final String MOVIE_REVIEWS = "movie_reviews";
}
