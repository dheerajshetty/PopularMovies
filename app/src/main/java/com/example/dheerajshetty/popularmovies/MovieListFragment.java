package com.example.dheerajshetty.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


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
    private static final String BASE_URL_STRING = "http://api.themoviedb.org/3/";
    private static final String DISCOVER_MOVIE_ENDPOINT = "discover/movie";
    private static final String SORT_ORDER = "sort_by";
    private static final String POPULARITY_DESC = "popularity.desc";
    private static final String RATING_DESC = "vote_average.desc";
    private static final String API_KEY_PARAMETER = "api_key";
    private static final String TMDB_API_KEY = "<TMDB API KEY HERE>";
    private static final int MOST_POP_INDEX = 0;
    private static final int RATING_INDEX = 1;
    private static final String VOTE_COUNT_GT = "vote_count.gte";
    private static final String ADULT_INCLUDE = "include_adult";
    private OnFragmentInteractionListener mListener;
    private MovieListImageAdapter mImageAdapter;
    private GridView mMovieListGridView;
    private Movie mMovies[];
    //Default sort order is based on popularity
    private String mSortOrder = POPULARITY_DESC;

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
                R.array.order, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] orderStringArray = getResources().getStringArray(R.array.order);
                String itemText = spinner.getSelectedItem().toString();
                if(itemText.equals(orderStringArray[MOST_POP_INDEX]) && mSortOrder.equals(RATING_DESC)) {
                    mSortOrder = POPULARITY_DESC;
                    new FetchMovieListTask().execute(mSortOrder);
                } else if(itemText.equals(orderStringArray[RATING_INDEX]) && mSortOrder.equals(POPULARITY_DESC)) {
                    mSortOrder = RATING_DESC;
                    new FetchMovieListTask().execute(mSortOrder);
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

        new FetchMovieListTask().execute(mSortOrder);

        mMovieListGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Movie.MOVIE_BUNDLE_KEY, mMovies[position]);
                mListener.onFragmentInteraction(MovieListFragment.class.getSimpleName(), bundle);
            }
        });

        return rootView;

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

    /**
     * This class is used to fetch popular movie list from the
     * <a href="http://themoviedb.org">The movies database.</>
     * */
    class FetchMovieListTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected Movie[] doInBackground(String... params) {

            URL popularMoviesUrl;
            String popularMoviesResult;

            Uri popularMoviesUri = Uri.parse(BASE_URL_STRING).buildUpon()
                    .appendEncodedPath(DISCOVER_MOVIE_ENDPOINT)
                    .appendQueryParameter(SORT_ORDER, params[0])
                    .appendQueryParameter(API_KEY_PARAMETER, TMDB_API_KEY)
                    //Avoid movies with low vote count
                    .appendQueryParameter(VOTE_COUNT_GT, "50")
                    //Avoid adult movies
                    .appendQueryParameter(ADULT_INCLUDE, "false")
                    .build();

            try {
                popularMoviesUrl = new URL(popularMoviesUri.toString());
                //Execute query
                popularMoviesResult = readFromUrl(popularMoviesUrl);
                //Extract movies from result
                mMovies = getMovieDataFromJson(popularMoviesResult);

                return mMovies;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return new Movie[0];
        }

        /**
         * Method to extract movie object from the query result in JSON format.
         *
         * @param popularMoviesResult TMDB query result in JSON format.
         *
         * @return Extracted array of movies.
         *
         * */
        private Movie[] getMovieDataFromJson(String popularMoviesResult) {

            JSONObject moviesJson = null;
            final String IMAGE_QUERY_BASE_PATH = "http://image.tmdb.org/t/p/w185";
            Movie movieResult[];
            final String TMDB_POPULAR_RESULTS = "results";

            try {
                moviesJson = new JSONObject(popularMoviesResult);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (moviesJson != null) {

                try {
                    JSONArray results = moviesJson.getJSONArray(TMDB_POPULAR_RESULTS);
                    movieResult = new Movie[results.length()];

                    for(int i = 0; i < results.length(); ++i) {
                        String title = ((JSONObject) results.get(i)).getString(Movie.MOVIE_TITLE_KEY);
                        String plot = ((JSONObject) results.get(i)).getString(Movie.MOVIE_PLOT_KEY);
                        String userRating = ((JSONObject) results.get(i)).getString(Movie.MOVIE_USER_RATING_KEY);
                        String releaseDate = ((JSONObject) results.get(i)).getString(Movie.MOVIE_RELEASE_DATE_KEY);

                        //Append the base path for movie poster query
                        //Used when loading using Picasso
                        String moviePosterPath;
                        StringBuilder sb = new StringBuilder();
                        sb.append(IMAGE_QUERY_BASE_PATH);
                        sb.append(((JSONObject) results.get(i)).getString(Movie.MOVIE_POSTER_PATH_KEY));
                        moviePosterPath = sb.toString();

                        movieResult[i] = new Movie(title, plot, userRating, releaseDate, moviePosterPath);
                    }
                        return movieResult;


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;

            } else {
                Log.e(LOG_TAG, "Movies json object is null");
                return null;
            }

        }

        /**
         * Method to execute the query on the movies database.
         *
         * @param url Query url.
         *
         * @return Query result.
         *
         * */
        private String readFromUrl(URL url) {
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                if (inputStream == null) {
                    //Stream is null, return null
                    return null;
                }

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            mImageAdapter = new MovieListImageAdapter(getActivity(), movies);
            mMovieListGridView.setAdapter(mImageAdapter);
            mImageAdapter.notifyDataSetChanged();
        }
    }
}
