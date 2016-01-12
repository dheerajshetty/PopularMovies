package com.example.dheerajshetty.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * This is the main activity for the popular movies app.
 * It begins with adding a fragment with the list of popular movies
 * in a grid. On selecting a particular movie it replaces the fragment
 * with a detailed view fragment with details of the selected movie,
 */
public class MainActivity extends AppCompatActivity
        implements OnFragmentInteractionListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(findViewById(R.id.fragment_container) != null) {
            //If previous state exists, then do not add fragment
            if (savedInstanceState != null) {
                return;
            }

            //First fragment showing the list of movies in a grid view
            MovieListFragment movieListFragment = MovieListFragment.newInstance();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, movieListFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String TAG, Bundle bundle) {

        //Start detail movie fragment
        //Replace the existing movie grid fragment
        if(TAG.equals(MovieListFragment.class.getSimpleName())) {
            MovieDetailFragment movieDetailFragment = MovieDetailFragment.newInstance(bundle);

            //Phone view
            if(findViewById(R.id.fragment_container) != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, movieDetailFragment)
                        .addToBackStack(null)
                        .commit();
            }

            //Tablet view
            if(findViewById(R.id.movie_detail_container) != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, movieDetailFragment)
                        .commit();
            }
        }
    }
}
