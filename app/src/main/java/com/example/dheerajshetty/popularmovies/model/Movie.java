
package com.example.dheerajshetty.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private String poster_path;
    private String overview;
    private String release_date;
    private String title;
    private double popularity;
    private int vote_count;
    private double vote_average;
    private int id;
    final static String IMAGE_QUERY_BASE_PATH = "http://image.tmdb.org/t/p/w185";
    public static final String MOVIE_BUNDLE_KEY = "movies";
    public static final String BASE_URL_STRING = "http://api.themoviedb.org/3/";
    public static final String DISCOVER_MOVIE_ENDPOINT = "discover/movie";

    public Movie() {}

    public Movie(Parcel in) {
        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
        title = in.readString();
        popularity = in.readDouble();
        vote_count = in.readInt();
        vote_average = in.readDouble();
        id = in.readInt();
    }
    /**
     * 
     * @return
     *     The posterPath
     */
    public String getPosterPath() {
        return poster_path;
    }

    /**
     * 
     * @param posterPath
     *     The poster_path
     */
    public void setPosterPath(String posterPath) {
        this.poster_path = posterPath;
    }

    /**
     *
     * @return
     *     The overview
     */
    public String getOverview() {
        return overview;
    }

    /**
     * 
     * @param overview
     *     The overview
     */
    public void setOverview(String overview) {
        this.overview = overview;
    }

    /**
     * 
     * @return
     *     The releaseDate
     */
    public String getReleaseDate() {
        return release_date;
    }

    /**
     * 
     * @param releaseDate
     *     The release_date
     */
    public void setReleaseDate(String releaseDate) {
        this.release_date = releaseDate;
    }

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The popularity
     */
    public double getPopularity() {
        return popularity;
    }

    /**
     * 
     * @param popularity
     *     The popularity
     */
    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    /**
     * 
     * @return
     *     The voteCount
     */
    public int getVoteCount() {
        return vote_count;
    }

    /**
     * 
     * @param voteCount
     *     The vote_count
     */
    public void setVoteCount(int voteCount) {
        this.vote_count = voteCount;
    }

    /**
     * 
     * @return
     *     The voteAverage
     */
    public double getVoteAverage() {
        return vote_average;
    }

    /**
     * 
     * @param voteAverage
     *     The vote_average
     */
    public void setVoteAverage(double voteAverage) {
        this.vote_average = voteAverage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(title);
        dest.writeDouble(popularity);
        dest.writeInt(vote_count);
        dest.writeDouble(vote_average);
        dest.writeInt(id);
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


    public static String makePosterPath(String posterPath) {
        String moviePosterPath;
        StringBuilder sb = new StringBuilder();
        sb.append(IMAGE_QUERY_BASE_PATH);
        sb.append(posterPath);
        moviePosterPath = sb.toString();
        return moviePosterPath;
    }
}
