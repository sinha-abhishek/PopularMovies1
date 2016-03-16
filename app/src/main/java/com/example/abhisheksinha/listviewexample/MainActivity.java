package com.example.abhisheksinha.listviewexample;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import models.MovieDataModel;

public class MainActivity extends ActionBarActivity implements  MainFragment.Callback{
    private boolean mHasTwoFragments;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.fragment_detail) != null) {
            mHasTwoFragments = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_detail, new DetailActivityFragment(),DetailActivity.DETAILFRAGMENT_TAG)
                        .commit();
            }
            DetailActivityFragment df = (DetailActivityFragment) getSupportFragmentManager().findFragmentByTag(DetailActivity.DETAILFRAGMENT_TAG);
            if (df != null) {
                df.SetShareProvider(mShareActionProvider);
            }
        } else {
            mHasTwoFragments = false;
        }
//        Intent intent = new Intent(this,
//                NetworkService.class);
//        startService(intent);

    }


    @Override
    public void onItemSelected(MovieDataModel m) {
        if (!mHasTwoFragments) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, m);
            startActivity(intent);
            return;
        }
        //DetailActivityFragment df = (DetailActivityFragment)getSupportFragmentManager().findFragmentByTag(DetailActivity.DETAILFRAGMENT_TAG);
        Bundle args = new Bundle();
        args.putParcelable(DetailActivityFragment.MOVIE_DETAIL_KEY,m);
        DetailActivityFragment fragment = new DetailActivityFragment();
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_detail, fragment, DetailActivity.DETAILFRAGMENT_TAG)
                .commit();
        fragment.SetShareProvider(mShareActionProvider);
        return;
    }

    @Override
    public void onOtherItemSelected(MenuItem item) {

    }

    @Override
    public void onCreateOptions(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_share);
        if (item != null) {
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
            DetailActivityFragment df = (DetailActivityFragment)getSupportFragmentManager().findFragmentByTag(DetailActivity.DETAILFRAGMENT_TAG);
            df.SetShareProvider(mShareActionProvider);
        }
    }
}
