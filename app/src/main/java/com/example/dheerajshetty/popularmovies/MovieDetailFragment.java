package com.example.dheerajshetty.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailFragment extends Fragment {

    private static final String MAX_RATING = "10";
    private OnFragmentInteractionListener mListener;
    Movie mMovie;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        //Set title
        TextView title = (TextView) rootView.findViewById(R.id.movie_title_textview);
        title.setText(mMovie.getTitle());

        //Set movie thumbnail
        ImageView movieImage = (ImageView) rootView.findViewById(R.id.movie_detail_imageview);
        Picasso.with(getContext()).load(mMovie.getPosterPath()).into(movieImage);

        //Set release date
        TextView releaseDate = (TextView) rootView.findViewById(R.id.release_date_textview);
        releaseDate.setText(mMovie.getReleaseDate());

        //Set user rating
        TextView userRating = (TextView) rootView.findViewById(R.id.user_rating_textview);
        userRating.setText(mMovie.getUserRating() + "/" + MAX_RATING);

        //Set movie plot
        TextView moviePlot = (TextView) rootView.findViewById(R.id.movie_plot_textview);
        moviePlot.setText(mMovie.getPlot());

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
}
