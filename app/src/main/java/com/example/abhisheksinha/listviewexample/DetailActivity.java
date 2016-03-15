package com.example.abhisheksinha.listviewexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import models.MovieDataModel;

public class DetailActivity extends ActionBarActivity {

    public static final String DETAILFRAGMENT_TAG = "detail_fragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            MovieDataModel data = getIntent().getParcelableExtra(Intent.EXTRA_TEXT);
            Bundle args = new Bundle();
            args.putParcelable(DetailActivityFragment.MOVIE_DETAIL_KEY, data);
            DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
            detailActivityFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail, detailActivityFragment, DETAILFRAGMENT_TAG)
                    .commit();
             }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(DetailActivityFragment.class.getSimpleName(), "clicked " + item.getItemId());
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return true;
    }



}
