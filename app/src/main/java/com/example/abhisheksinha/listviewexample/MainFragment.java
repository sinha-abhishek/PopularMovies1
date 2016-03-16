package com.example.abhisheksinha.listviewexample;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.abhisheksinha.listviewexample.providers.MovieProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Adapters.ImageWithTextAdapter;
import Adapters.MovieDataAdapter;
import managers.MovieDataManager;
import models.DiscoverResponseModel;
import models.MovieDBModel;
import models.MovieDataModel;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import services.ApiService;
import services.RestClient;

/**
 * Created by abhisheksinha on 12/31/15.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final String ACTIVE_POS = "active_position";
    private ProgressBar spinner;
    final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
    final String API_KEY = "xxxxxxxxxxx";
    final String SORT_QUERY_PARAM = "sort_by";
    final String MOVIE_DATA = "movie_data";
    private ImageWithTextAdapter imageWithTextAdapter;
    private MovieDataAdapter adapter;
    private ArrayList<MovieDataModel> movieDataModelList;
    private String sortPreference;
   // private HashMap<S>
    final String MOVIE_LIST_DATA = "movie_list_data";
    private boolean showSpinner;
    private static final int DATA_LOADER = 0;
    public static final String IS_FAV = "fav";
    private MyReciever myReciever;
    private int mActivePosition;
    private GridView gridView;

    public interface Callback {

        public void onItemSelected(MovieDataModel m);

        public void onOtherItemSelected(MenuItem item);

        public void onCreateOptions(Menu menu);

        public void onMoviesLoaded(int activePosition, GridView gv);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String syncConnPref = sharedPref.getString(SettingsActivity.SORT_PREF_KEY, "");
        outState.putString(SettingsActivity.SORT_PREF_KEY, syncConnPref);
        //movieDataModelList = MovieDBModel.ConvertList()
        outState.putParcelableArrayList(MOVIE_LIST_DATA, movieDataModelList);
        outState.putInt(ACTIVE_POS,mActivePosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        myReciever = new MyReciever(this);

        PreferenceManager.setDefaultValues(getActivity(), R.xml.pref_final, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String syncConnPref = sharedPref.getString(SettingsActivity.SORT_PREF_KEY, "");
        sortPreference = syncConnPref;
        super.onCreate(savedInstanceState);
        mActivePosition = 0;
        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_LIST_DATA) &&
                savedInstanceState.containsKey(SettingsActivity.SORT_PREF_KEY) &&
                sortPreference == savedInstanceState.getString(SettingsActivity.SORT_PREF_KEY)) {
            mActivePosition = savedInstanceState.getInt(ACTIVE_POS,0);
        } else {
            showSpinner = true;

        }

    }

    public void removeSpinner(){
        if (spinner != null) {
            spinner.setVisibility(View.GONE);
        } else {
            spinner = (ProgressBar) getActivity().findViewById(R.id.spinnerView);
            spinner.setVisibility(View.GONE);
        }
    }

    private void StartFetch() {
        Intent intent = new Intent(getActivity(),
                NetworkService.class);
        getActivity().startService(intent);
        mActivePosition = 0;
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String syncConnPref = sharedPref.getString(SettingsActivity.SORT_PREF_KEY, "");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NetworkService.NETWORK_ACTION);
        getActivity().registerReceiver(myReciever, intentFilter);

        if (MovieDBModel.GetAll().size() == 0){
            StartFetch();
        } else if ( syncConnPref != sortPreference) {
            spinner = (ProgressBar) getActivity().findViewById(R.id.spinnerView);
            spinner.setVisibility(View.VISIBLE);
            getLoaderManager().restartLoader(DATA_LOADER, null, this);
            sortPreference = syncConnPref;
            spinner = (ProgressBar) getActivity().findViewById(R.id.spinnerView);

        } else {
            spinner = (ProgressBar) getActivity().findViewById(R.id.spinnerView);
            spinner.setVisibility(View.GONE);
        }
        getLoaderManager().initLoader(DATA_LOADER, null, this);

    }



    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_example, container, false);
        imageWithTextAdapter = new ImageWithTextAdapter(getActivity(),movieDataModelList);
        GridView lv = (GridView) rootView;
        //lv.setAdapter(imageWithTextAdapter);
        adapter = new MovieDataAdapter(getActivity().getBaseContext(), null , false);
        lv.setAdapter(adapter);
        gridView = lv;
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                mActivePosition = position;
                if (cursor != null) {
                    try {
                        MovieDBModel movieDBModel = MovieDBModel.fromCursor(cursor);
                        MovieDataModel m = new MovieDataModel(movieDBModel);
                        ((Callback)getActivity()).onItemSelected(m);
                    } catch (Exception e) {
                        Log.e("MainFragment", e.getMessage(), e);
                        Toast.makeText(getActivity(), "Can't load this time", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        ((Callback)getActivity()).onCreateOptions(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.refreshBtn) {
            //adapter.
            spinner.setVisibility(View.VISIBLE);
            Intent intent = new Intent(getActivity(),
                    NetworkService.class);
            getActivity().startService(intent);
        } else {
            ((Callback)getActivity()).onOtherItemSelected(item);
        }
        return true;
    }

    private class MyReciever extends BroadcastReceiver {

        private LoaderManager.LoaderCallbacks loaderCallbacks;

        private boolean popularDone;
        private boolean ratedDone;
        MyReciever(LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks){
            this.loaderCallbacks = loaderCallbacks;
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            Loader<Cursor> m = getLoaderManager().getLoader(DATA_LOADER);
//            if (m.isStarted()){
//                m.abandon();
//            }
            if (intent.getBooleanExtra(NetworkService.POPULAR_UPDATED, false)){
                popularDone = true;
            }
            if (intent.getBooleanExtra(NetworkService.RATED_UPDATED,false)){
                ratedDone = true;
            }
            if (popularDone && ratedDone) {
                getLoaderManager().restartLoader(DATA_LOADER, null, loaderCallbacks);
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case DATA_LOADER:
                // Returns a new CursorLoader
                String path = GetQueryString();
                return new CursorLoader(
                        getActivity(),   // Parent activity context
                        MovieProvider.CONTENT_URI.buildUpon().appendPath("movies").appendPath(path).build(),        // Table to query
                        null,     // Projection to return
                        null,            // No selection clause
                        null,            // No selection arguments
                        null             // Default sort order
                );
            default:
                // An invalid id was passed in
                return null;
        }
    }

    private String GetQueryString(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String syncConnPref = sharedPref.getString(SettingsActivity.SORT_PREF_KEY, "");
        if (syncConnPref.compareToIgnoreCase(getString(R.string.sort_vote_val)) == 0) {
            return "highestrated";
        }
        if (syncConnPref.compareToIgnoreCase(getString(R.string.sort_popularity_val)) == 0){
            return "popular";
        }
        return "fav";
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        int movieIndexId = data.getColumnIndex("_id");
        Log.i("MainFragment", String.valueOf(movieIndexId));
        spinner = (ProgressBar) getActivity().findViewById(R.id.spinnerView);
        if(spinner != null) {
            spinner.setVisibility(View.GONE);
        }
        adapter.swapCursor(data);
        if (adapter.getCount() == 0) {

        }
        ((Callback)getActivity()).onMoviesLoaded(mActivePosition,gridView);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        getActivity().unregisterReceiver(myReciever);
        super.onStop();
    }
}
