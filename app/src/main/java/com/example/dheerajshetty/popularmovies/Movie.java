package com.example.dheerajshetty.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This represents a Movie.
 *
 * Created by dheerajshetty on 1/10/16.
 */
public class Movie implements Parcelable {

    //Keys used in the API query
    public static final String MOVIE_USER_RATING_KEY = "vote_average";
    static final String MOVIE_POSTER_PATH_KEY = "poster_path";
    static final String MOVIE_TITLE_KEY = "title";
    static final String MOVIE_PLOT_KEY = "overview";
    public static final String MOVIE_RELEASE_DATE_KEY = "release_date";
    //Key used when passing a movie from one fragment to the other
    public static final String MOVIE_BUNDLE_KEY = "movies";
    String title;
    String plot;
    String userRating;
    String releaseDate;
    String moviePosterPath;

    protected Movie(Parcel in) {
        this.title = in.readString();
        this.plot = in.readString();
        this.userRating = in.readString();
        this.releaseDate = in.readString();
        this.moviePosterPath = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie(String title, String plot, String userRating, String releaseDate, String moviePosterPath) {
        this.title = title;
        this.plot = plot;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.moviePosterPath = moviePosterPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(plot);
        dest.writeString(userRating);
        dest.writeString(releaseDate);
        dest.writeString(moviePosterPath);
    }

    public String getPosterPath() {
        return moviePosterPath;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getUserRating() {
        return userRating;
    }

    public String getPlot() {
        return plot;
    }
}
