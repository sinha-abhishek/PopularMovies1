package com.example.abhisheksinha.listviewexample;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
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
import managers.MovieDataManager;
import models.DiscoverResponseModel;
import models.MovieDataModel;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import services.ApiService;
import services.RestClient;

/**
 * Created by abhisheksinha on 12/31/15.
 */
public class MainFragment extends Fragment{
    private ProgressBar spinner;
    final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
    final String API_KEY = "xxxxxxxxxxx";
    final String SORT_QUERY_PARAM = "sort_by";
    final String MOVIE_DATA = "movie_data";
    private ImageWithTextAdapter imageWithTextAdapter;
    private ArrayList<MovieDataModel> movieDataModelList;
    private String sortPreference;
   // private HashMap<S>
    final String MOVIE_LIST_DATA = "movie_list_data";
    private boolean showSpinner;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String syncConnPref = sharedPref.getString(SettingsActivity.SORT_PREF_KEY, "");
        outState.putString(SettingsActivity.SORT_PREF_KEY, syncConnPref);
        outState.putParcelableArrayList(MOVIE_LIST_DATA, movieDataModelList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.pref_final, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String syncConnPref = sharedPref.getString(SettingsActivity.SORT_PREF_KEY, "");
        sortPreference = syncConnPref;
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_LIST_DATA) &&
                savedInstanceState.containsKey(SettingsActivity.SORT_PREF_KEY) &&
                sortPreference == savedInstanceState.getString(SettingsActivity.SORT_PREF_KEY)) {
            showSpinner = false;
            movieDataModelList = savedInstanceState.getParcelableArrayList(MOVIE_LIST_DATA);
        } else {
            showSpinner = true;
            movieDataModelList = new ArrayList<MovieDataModel>();


            Log.i(MainFragment.class.getSimpleName(), syncConnPref);
            RestClient c = new RestClient();
            ApiService apiService = c.getApiService();
            apiService.getMovies(API_KEY, syncConnPref, new Callback<DiscoverResponseModel>() {
                @Override
                public void success(DiscoverResponseModel dataModels, Response response) {
                    ArrayList<MovieDataModel> movieDataModels = dataModels.getMovieDataModels();
                    spinner.setVisibility(View.GONE);
                    movieDataModelList = movieDataModels;
                    imageWithTextAdapter.addAll(movieDataModels);
                    showSpinner = false;
                    if (spinner != null) {
                        spinner.setVisibility(View.GONE);
                    }
                    removeSpinner();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("RETRO", error.toString());
                }
            });
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

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String syncConnPref = sharedPref.getString(SettingsActivity.SORT_PREF_KEY, "");
        if (movieDataModelList.size() == 0 || syncConnPref != sortPreference) {
            sortPreference = syncConnPref;
            spinner = (ProgressBar) getActivity().findViewById(R.id.spinnerView);
            spinner.setVisibility(View.VISIBLE);
            imageWithTextAdapter.clear();
            Log.i(MainFragment.class.getSimpleName(), syncConnPref);
            RestClient c = new RestClient();
            ApiService apiService = c.getApiService();
            apiService.getMovies(API_KEY, syncConnPref, new Callback<DiscoverResponseModel>() {
                @Override
                public void success(DiscoverResponseModel dataModels, Response response) {
                    ArrayList<MovieDataModel> movieDataModels = dataModels.getMovieDataModels();
                    spinner.setVisibility(View.GONE);
                    imageWithTextAdapter.addAll(movieDataModels);
                    movieDataModelList = movieDataModels;
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("RETRO", error.toString());
                }
            });
        } else {
            spinner = (ProgressBar) getActivity().findViewById(R.id.spinnerView);
            spinner.setVisibility(View.GONE);
            imageWithTextAdapter.addAll(movieDataModelList);
        }
//        if (showSpinner) {
//            spinner = (ProgressBar) getActivity().findViewById(R.id.spinnerView);
//            spinner.setVisibility(View.VISIBLE);
//        } else {
//            spinner = (ProgressBar) getActivity().findViewById(R.id.spinnerView);
//            spinner.setVisibility(View.GONE);
//        }
//        spinner = (ProgressBar) getActivity().findViewById(R.id.spinnerView);
//        spinner.setVisibility(View.VISIBLE);
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        String syncConnPref = sharedPref.getString(SettingsActivity.SORT_PREF_KEY, "");
//        Log.i(MainFragment.class.getSimpleName(), syncConnPref);
//        RestClient c = new RestClient();
//        ApiService apiService = c.getApiService();
//        apiService.getMovies(API_KEY, syncConnPref, new Callback<DiscoverResponseModel>() {
//            @Override
//            public void success(DiscoverResponseModel dataModels, Response response) {
//                List<MovieDataModel> movieDataModels = dataModels.getMovieDataModels();
//                spinner.setVisibility(View.GONE);
//                imageWithTextAdapter.addAll(movieDataModels);
//                for (MovieDataModel m:movieDataModels) {
//                    Log.i("RETRO",m.GetTitle()+" "+m.GetVoteAvg());
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Log.e("RETRO", error.toString());
//            }
//        });
    }



    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_example, container, false);
        imageWithTextAdapter = new ImageWithTextAdapter(getActivity(),movieDataModelList);
        GridView lv = (GridView) rootView;
        lv.setAdapter(imageWithTextAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieDataModel m = (MovieDataModel) parent.getAdapter().getItem(position);
                try {
                    Intent movieDetailIntent = new Intent(getActivity(), DetailActivity.class)
                            .putExtra(Intent.EXTRA_TEXT, m);
                           // .putExtra(Intent.EXTRA_TEXT, m.GetJson());
                    startActivity(movieDetailIntent);
                } catch (Exception e) {
                    Log.e("MainFragment",e.getMessage(),e);
                    Toast.makeText(getActivity(), "Can't load this time", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        }
        return true;
    }

}
