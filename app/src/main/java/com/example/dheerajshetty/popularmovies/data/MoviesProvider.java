package com.example.dheerajshetty.popularmovies.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by dheerajshetty on 3/19/16.
 */

@ContentProvider(authority = MoviesProvider.AUTHORITY, database = MovieDatabase.class)
public class MoviesProvider {
    public static final String AUTHORITY = "com.example.dheerajshetty.popularmovies.data.MoviesProvider";

    interface Path {
        String MOVIES = "movies";
        String REVIEWS = "movie_reviews";
        String TRAILERS = "movie_trailers";
    }

    @TableEndpoint(table = MovieDatabase.MOVIES) public static class Movies {
        @ContentUri(
                path = Path.MOVIES,
                type = "vnd.android.cursor.dir/movie",
                defaultSort = MovieColumns.TITLE + " ASC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" +
                Path.MOVIES);
    }

    @TableEndpoint(table = MovieDatabase.MOVIE_REVIEWS) public static class Reviews {
        @ContentUri(
                path = Path.REVIEWS,
                type = "vnd.android.cursor.dir/movie_reviews",
                defaultSort = ReviewColumns._ID + " ASC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" +
                Path.REVIEWS);
    }

    @TableEndpoint(table = MovieDatabase.MOVIE_TRAILERS) public static class Trailers {
        @ContentUri(
                path = Path.TRAILERS,
                type = "vnd.android.cursor.dir/movie_trailers",
                defaultSort = TrailerColumns._ID + " ASC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" +
                Path.TRAILERS);
    }
}
