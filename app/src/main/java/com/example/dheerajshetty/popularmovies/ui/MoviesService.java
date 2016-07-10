package com.example.dheerajshetty.popularmovies.ui;

import com.example.dheerajshetty.popularmovies.model.Movie;
import com.example.dheerajshetty.popularmovies.model.MovieQuestion;
import com.example.dheerajshetty.popularmovies.model.Review;
import com.example.dheerajshetty.popularmovies.model.TrailerQuestion;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by dheerajshetty on 3/19/16.
 */
public class MoviesService {
    public interface MoviesAPI {
        @GET("{endpoint}/movie?")
        Call<MovieQuestion<Movie>> getQuestions(
                @Path("endpoint") String endpoint,
                @Query("api_key") String api_key_param,
                @Query("vote_count.gte") String vote_count_gte,
                @Query("include_adult") String adult,
                @Query("sort_by") String sort_order);
    }
    public  interface MovieTrailerAPI {
        @GET("movie/{id}/videos?")
        Call<TrailerQuestion> getQuestions(
                @Path("id") int id,
                @Query("api_key") String api_key_param);
    }
    public  interface MovieReviewAPI {
        @GET("movie/{id}/reviews?")
        Call<MovieQuestion<Review>> getQuestions(
                @Path("id") int id,
                @Query("api_key") String api_key_param);
    }
}
