package com.example.dheerajshetty.popularmovies.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import com.example.dheerajshetty.popularmovies.BuildConfig;
import com.example.dheerajshetty.popularmovies.R;
import com.example.dheerajshetty.popularmovies.model.Movie;
import com.example.dheerajshetty.popularmovies.model.MovieQuestion;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieListFragment#newInstance} factory method to
 * create an instance of this fragment.
 * This fragment is used to retrieve the list of movies based on a
 * sort order.
 */
public class MovieListFragment extends Fragment {
    private static final String LOG_TAG = MovieListFragment.class.getSimpleName();

    private static final String SORT_ORDER = "sort_by";
    private static final String POPULARITY_DESC = "popularity.desc";
    private static final String RATING_DESC = "vote_average.desc";
    private static final String API_KEY_PARAMETER = "api_key";
    //private static final String TMDB_API_KEY = "74adaaa6f7589aba774f9789855b9dc2";
    private static final int MOST_POP_INDEX = 0;
    private static final int RATING_INDEX = 1;
    private static final int FAV_INDEX = 2;
    private static final String VOTE_COUNT_GT = "vote_count.gte";
    private static final String ADULT_INCLUDE = "include_adult";
    private static final String SPINNER_KEY = "spinner_key";
    private OnFragmentInteractionListener mListener;
    private MovieListImageAdapter mImageAdapter;
    private GridView mMovieListGridView;
    //Default sort order is based on popularity
    private String mSortOrder = POPULARITY_DESC;
    private MovieQuestion<Movie> mMoviesList;
    private Call<MovieQuestion<Movie>> call;
    private static boolean mFav = false;
    private String mSpinnerText;
    private int mSpinnerSelectedItem = 0;

    public MovieListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MovieListFragment.
     */
    public static MovieListFragment newInstance() {
        MovieListFragment fragment = new MovieListFragment();
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movie_list_options, menu);

        //Set up spinner view to select sort order
        MenuItem menuItem = menu.findItem(R.id.sort_order);
        final Spinner spinner = (Spinner) menuItem.getActionView();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.order, R.layout.spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(mSpinnerSelectedItem);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] orderStringArray = getResources().getStringArray(R.array.order);
                mSpinnerText = spinner.getSelectedItem().toString();
                mSpinnerSelectedItem = spinner.getSelectedItemPosition();
                if(mSpinnerText.equals(orderStringArray[MOST_POP_INDEX]) &&
                        (mSortOrder.equals(RATING_DESC) || mFav)) {
                    mSortOrder = POPULARITY_DESC;
                    mFav = false;
                    //new FetchMovieListTask().execute(mSortOrder);
                    fetchMovies(mSortOrder);
                } else if(mSpinnerText.equals(orderStringArray[RATING_INDEX]) &&
                        (mSortOrder.equals(POPULARITY_DESC) || mFav)) {
                    mSortOrder = RATING_DESC;
                    mFav = false;
                    //new FetchMovieListTask().execute(mSortOrder);
                    fetchMovies(mSortOrder);
                } else if(mSpinnerText.equals(orderStringArray[FAV_INDEX])) {
                    mImageAdapter.getMoviesFromFavorites();
                    mMoviesList = mImageAdapter.getMovies();
                    mFav = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        mMovieListGridView = (GridView) rootView.findViewById(R.id.movies_gridview);

        //Fetch movies using retrofit
        fetchMovies(mSortOrder);

        //new FetchMovieListTask().execute(mSortOrder);

        mMovieListGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Movie.MOVIE_BUNDLE_KEY,
                        mMoviesList.getResults().get(position));
                mListener.onFragmentInteraction(MovieListFragment.class.getSimpleName(), bundle);
            }
        });

        return rootView;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SPINNER_KEY, mSpinnerSelectedItem);
    }

    @Override
    public void onStop() {
        super.onStop();

        call.cancel();
    }

    private void fetchMovies(String mSortOrder) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Movie.BASE_URL_STRING)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MoviesService.MoviesAPI moviesAPI = retrofit.create(MoviesService.MoviesAPI.class);
        call = moviesAPI.getQuestions("discover",
                BuildConfig.TMDB_API_KEY,
                "50",
                "false",
                mSortOrder);


        call.enqueue(new Callback<MovieQuestion<Movie>>() {
            @Override
            public void onResponse(Call<MovieQuestion<Movie>> call,
                                   Response<MovieQuestion<Movie>> response) {
                mMoviesList = response.body();
                mImageAdapter = new MovieListImageAdapter(getActivity(), mMoviesList);
                mMovieListGridView.setAdapter(mImageAdapter);
                mImageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieQuestion<Movie>> call, Throwable t) {

            }
        });
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
}
