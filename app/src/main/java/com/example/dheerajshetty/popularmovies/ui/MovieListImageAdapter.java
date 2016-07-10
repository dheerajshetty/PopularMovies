package com.example.dheerajshetty.popularmovies.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.dheerajshetty.popularmovies.R;
import com.example.dheerajshetty.popularmovies.data.MovieColumns;
import com.example.dheerajshetty.popularmovies.data.MoviesProvider;
import com.example.dheerajshetty.popularmovies.model.Movie;
import com.example.dheerajshetty.popularmovies.model.MovieQuestion;
import com.squareup.picasso.Picasso;

/**
 * Image adapter used to populate the move list.
 * Uses the picasso library to load the movie poster image into the imageview.
 *
 * Created by dheerajshetty on 1/2/16.
 */
public class MovieListImageAdapter extends BaseAdapter {

    private final String LOG_TAG = MovieListImageAdapter.class.getSimpleName();

    private Context mContext;
    private MovieQuestion<Movie> mMovies;

    public MovieListImageAdapter(Context context, MovieQuestion question) {
        mContext = context;
        mMovies = question;
    }

    @Override
    public int getCount() {
        if(mMovies == null) {
            return 0;
        }
        return mMovies.getResults().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;

        if(convertView == null) {

            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(0, 0);
            layoutParams.height = parent.getMeasuredHeight() / parent.getResources().getInteger(R.integer.gridview_num_rows);
            layoutParams.width = parent.getMeasuredWidth() / ((GridView) parent).getNumColumns();
            imageView.setLayoutParams(layoutParams);
        }
        else {
            imageView = (ImageView) convertView;
        }

        String moviePosterPath;
        moviePosterPath = Movie.makePosterPath(
                mMovies.getResults().get(position).getPosterPath());

        Picasso.with(mContext).load(moviePosterPath).into(imageView);
        return imageView;
    }

    public void getMoviesFromFavorites() {
        mMovies = new MovieQuestion<Movie>();

        String[] projection =
                {
                        MovieColumns.MOVIE_ID,
                        MovieColumns.TITLE,
                        MovieColumns.POSTER,
                        MovieColumns.RELEASE_DATE,
                        MovieColumns.PLOT,
                        MovieColumns.USER_RATING
                };
        Cursor c = mContext.getContentResolver().query(MoviesProvider.Movies.CONTENT_URI,
                projection,
                null,
                null,
                null);

        cursorToMovies(c);
        notifyDataSetChanged();
    }

    private void cursorToMovies(Cursor c) {
        while (c.moveToNext()) {
            Movie m = new Movie();
            m.setId(c.getInt(c.getColumnIndex(MovieColumns.MOVIE_ID)));
            m.setTitle(c.getString(c.getColumnIndex(MovieColumns.TITLE)));
            m.setPosterPath(c.getString(c.getColumnIndex(MovieColumns.POSTER)));
            m.setReleaseDate(c.getString(c.getColumnIndex(MovieColumns.RELEASE_DATE)));
            m.setVoteAverage(Double.parseDouble(c.getString(c.getColumnIndex(
                    MovieColumns.USER_RATING))));
            m.setOverview(c.getString(c.getColumnIndex(MovieColumns.PLOT)));
            mMovies.getResults().add(m);
        }
    }

    public MovieQuestion<Movie> getMovies() {
        return mMovies;
    }
}
