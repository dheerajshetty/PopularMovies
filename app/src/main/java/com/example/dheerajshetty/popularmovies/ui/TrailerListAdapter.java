package com.example.dheerajshetty.popularmovies.ui;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dheerajshetty.popularmovies.R;
import com.example.dheerajshetty.popularmovies.model.Trailer;

import java.util.List;

/**
 * Created by dheerajshetty on 3/22/16.
 */
public class TrailerListAdapter extends BaseAdapter {

    Context mContext;
    List<Trailer> mTrailers;

    public TrailerListAdapter(Context context, List<Trailer> trailers) {
        mContext = context;
        mTrailers = trailers;
    }

    @Override
    public int getCount() {
        if (mTrailers == null) {
            return 0;
        }
        Log.e("DHEERAJ", "" + mTrailers.size());
        return mTrailers.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            Log.e("DHEERAJ", mTrailers.get(position).getName());
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.trailer_list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.trailer_image);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.trailer_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(mTrailers.get(position).getName());

        return convertView;
    }

    public void swapList(List<Trailer> trailers) {
        mTrailers = trailers;
        notifyDataSetChanged();
    }
}
