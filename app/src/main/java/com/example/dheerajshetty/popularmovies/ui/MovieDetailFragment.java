package com.example.dheerajshetty.popularmovies.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dheerajshetty.popularmovies.BuildConfig;
import com.example.dheerajshetty.popularmovies.R;
import com.example.dheerajshetty.popularmovies.data.MovieColumns;
import com.example.dheerajshetty.popularmovies.data.MoviesProvider;
import com.example.dheerajshetty.popularmovies.data.ReviewColumns;
import com.example.dheerajshetty.popularmovies.data.TrailerColumns;
import com.example.dheerajshetty.popularmovies.model.Movie;
import com.example.dheerajshetty.popularmovies.model.MovieQuestion;
import com.example.dheerajshetty.popularmovies.model.Review;
import com.example.dheerajshetty.popularmovies.model.Trailer;
import com.example.dheerajshetty.popularmovies.model.TrailerQuestion;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailFragment extends Fragment {

    private static final String MAX_RATING = "10";
    private static final String DETAIL_VIDEOS = "videos";
    private static final String DETAIL_REVIEW = "reviews";
    private OnFragmentInteractionListener mListener;
    Movie mMovie;
    private Call<TrailerQuestion> mTrailerCall;
    private Call<MovieQuestion<Review>> mReviewCall;
    private Button mFavButton;
    private ShareActionProvider mShareActionProvider;
    private List<Trailer> mTrailers;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param args Bundle with the selected movie.
     *
     * @return A new instance of fragment MovieDetailFragment.
     */
    public static MovieDetailFragment newInstance(Bundle args) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovie = getArguments().getParcelable(Movie.MOVIE_BUNDLE_KEY);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        //Set title
        final TextView title = (TextView) rootView.findViewById(R.id.movie_title_textview);
        title.setText(mMovie.getTitle());

        //Set movie thumbnail
        String moviePosterPath;
        moviePosterPath = Movie.makePosterPath(mMovie.getPosterPath());
        ImageView movieImage = (ImageView) rootView.findViewById(R.id.movie_detail_imageview);
        Picasso.with(getContext()).load(moviePosterPath).into(movieImage);

        //Set release date
        TextView releaseDate = (TextView) rootView.findViewById(R.id.release_date_textview);
        releaseDate.setText(mMovie.getReleaseDate());

        //Set user rating
        final TextView userRating = (TextView) rootView.findViewById(R.id.user_rating_textview);
        userRating.setText(mMovie.getVoteAverage() + "/" + MAX_RATING);

        //Set movie plot
        TextView moviePlot = (TextView) rootView.findViewById(R.id.movie_plot_textview);
        moviePlot.setText(mMovie.getOverview());

        fetchTrailers();
        fetchReviews();

        mFavButton = (Button) rootView.findViewById(R.id.fav_button);
        mFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                cv.put(MovieColumns.TITLE, mMovie.getTitle());
                cv.put(MovieColumns.PLOT, mMovie.getOverview());
                cv.put(MovieColumns.POSTER, mMovie.getPosterPath());
                cv.put(MovieColumns.RELEASE_DATE, mMovie.getReleaseDate());
                cv.put(MovieColumns.USER_RATING, mMovie.getVoteAverage());
                cv.put(MovieColumns.MOVIE_ID, mMovie.getId());
                try {
                    getActivity().getContentResolver().insert(MoviesProvider.Movies.CONTENT_URI, cv);
                } catch (SQLiteConstraintException e) {
                    Toast.makeText(getContext(), "Already a favorite", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    private void fetchReviews() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Movie.BASE_URL_STRING)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MoviesService.MovieReviewAPI movieReviewAPI =
                retrofit.create(MoviesService.MovieReviewAPI.class);

        mReviewCall = movieReviewAPI.getQuestions(mMovie.getId(), BuildConfig.TMDB_API_KEY);

        mReviewCall.enqueue(new Callback<MovieQuestion<Review>>() {
            @Override
            public void onResponse(Call<MovieQuestion<Review>> call,
                                   Response<MovieQuestion<Review>> response) {
                addReviews(response.body().getResults());
            }

            @Override
            public void onFailure(Call<MovieQuestion<Review>> call, Throwable t) {

            }
        });
    }

    private void addReviews(List<Review> results) {
        for (Review review : results) {
            LayoutInflater li = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = li.inflate(R.layout.review_list_item, null);
            TextView author = (TextView) view.findViewById(R.id.author);
            author.append(review.getAuthor());
            TextView content = (TextView) view.findViewById(R.id.content);
            content.setText(review.getContent());
            addViewToDetailView(view, R.id.review_container);

            ContentValues cv = new ContentValues();
            cv.put(ReviewColumns.MOVIE_ID, mMovie.getId());
            cv.put(ReviewColumns.REVIEW_AUTHOR, review.getAuthor());
            cv.put(ReviewColumns.REVIEW_CONTENT, review.getContent());

            getActivity().getContentResolver().insert(MoviesProvider.Reviews.CONTENT_URI, cv);
        }
    }

    private void fetchTrailers() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Movie.BASE_URL_STRING)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MoviesService.MovieTrailerAPI movieTrailerAPI =
                retrofit.create(MoviesService.MovieTrailerAPI.class);

        mTrailerCall = movieTrailerAPI.getQuestions(mMovie.getId(), BuildConfig.TMDB_API_KEY);

        mTrailerCall.enqueue(new Callback<TrailerQuestion>() {
            @Override
            public void onResponse(Call<TrailerQuestion> call, Response<TrailerQuestion> response) {
                mTrailers = response.body().getResults();
                addTrailers(mTrailers);

                if (mTrailers != null && mShareActionProvider != null) {
                    mShareActionProvider.setShareIntent(createShareForecastIntent());
                }
            }

            @Override
            public void onFailure(Call<TrailerQuestion> call, Throwable t) {

            }
        });
    }

    private void addTrailers(List<Trailer> results) {
        for (final Trailer t: results) {
            LayoutInflater li = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = li.inflate(R.layout.trailer_list_item, null);
            TextView trailer_name = (TextView) view.findViewById(R.id.trailer_name);
            trailer_name.setText(t.getName());
            addViewToDetailView(view, R.id.trailer_container);
            view.findViewById(R.id.trailer_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + t.getKey())));
                }
            });

            ContentValues cv = new ContentValues();
            cv.put(TrailerColumns.MOVIE_ID, mMovie.getId());
            cv.put(TrailerColumns.TRAILER_KEY, t.getKey());
            //getActivity().getContentResolver().insert(MoviesProvider.Trailers.CONTENT_URI, cv);
        }
    }

    private void addViewToDetailView(View view, int resId) {
        ViewGroup parent = (ViewGroup) getActivity().findViewById(resId);
        parent.addView(view, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_movie_detail, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (mTrailers != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" +
                mTrailers.get(0).getKey());
        return shareIntent;
    }
}
