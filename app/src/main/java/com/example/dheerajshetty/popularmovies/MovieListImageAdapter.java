package com.example.dheerajshetty.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

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
    private Movie mMovies[];

    public MovieListImageAdapter(Context context, Movie movies[]) {
        mContext = context;
        mMovies = movies;
    }

    @Override
    public int getCount() { return mMovies.length; }

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
        Picasso.with(mContext).load(mMovies[position].getPosterPath()).into(imageView);
        return imageView;
    }
}
