package com.example.abhisheksinha.listviewexample;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.pref_final, false);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        spinner = (ProgressBar) getActivity().findViewById(R.id.spinnerView);
        spinner.setVisibility(View.VISIBLE);
        //FetchTask t = new FetchTask();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String syncConnPref = sharedPref.getString(SettingsActivity.SORT_PREF_KEY, "");
        Log.i(MainFragment.class.getSimpleName(), syncConnPref);
        //t.execute(syncConnPref);

        RestClient c = new RestClient();
        ApiService apiService = c.getApiService();
        apiService.getMovies(API_KEY, syncConnPref, new Callback<DiscoverResponseModel>() {
            @Override
            public void success(DiscoverResponseModel dataModels, Response response) {
                List<MovieDataModel> movieDataModels = dataModels.getMovieDataModels();
                spinner.setVisibility(View.GONE);
                imageWithTextAdapter.addAll(movieDataModels);
                for (MovieDataModel m:movieDataModels) {
                    Log.i("RETRO",m.GetTitle()+" "+m.GetVoteAvg());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("RETRO", error.toString());
            }
        });
    }



    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_example, container, false);

//        FetchTask t = new FetchTask();
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        String syncConnPref = sharedPref.getString(SettingsActivity.SORT_PREF_KEY, "");
//        Log.i(MainFragment.class.getSimpleName(), syncConnPref);
//        t.execute("popularity.desc");
        List<MovieDataModel> dummyList = new ArrayList<MovieDataModel>();
        imageWithTextAdapter = new ImageWithTextAdapter(getActivity(),dummyList);
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

    public class FetchTask extends AsyncTask<String, Void, List<MovieDataManager>> {
        @Override
        protected List<MovieDataManager> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String LOG_TAG = FetchTask.class.getSimpleName();

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                Uri uri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter("api_key", API_KEY)
                        .appendQueryParameter(SORT_QUERY_PARAM, params[0])
                        .appendQueryParameter("language","en")
                        .build();
                URL url = new URL(uri.toString());
                Log.i(LOG_TAG,url.toString());
                // new URL("https://api.themoviedb.org/3/discover/movie?api_key=6fbc4c1792dbc14aba698926dfadf31f&sort_by=popularity.desc");
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                Log.i("FetchTask", forecastJsonStr);
                List<MovieDataManager> results = parseJson(forecastJsonStr);
                //Log.i("FetchTask", results.get(0));
                return results;
                //Log.e("FetchTask", "Info: ", );
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } catch (JSONException e) {
                Log.e("PlaceholderFragment", "Error ", e);
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return null;
        }
        private List<MovieDataManager> parseJson(String jData) throws JSONException{
            JSONObject j = new JSONObject(jData);
            List<MovieDataManager> results = new ArrayList<MovieDataManager>();
            JSONArray resultArr = (JSONArray)j.getJSONArray("results");
            for (int i = 0 ; i < resultArr.length() ; i++) {
                try {
                    JSONObject res = (JSONObject) resultArr.get(i);
                    //String title = (String) res.get("title");
                    MovieDataManager m = new MovieDataManager(res);
                    results.add(m);
                } catch (JSONException e) {
                    Log.e(FetchTask.class.getSimpleName(),e.getMessage(), e);
                } catch (Exception e) {
                    Log.e(FetchTask.class.getSimpleName(),e.getMessage(), e);
                }
            }
            return results;
        }

        @Override
        protected void onPostExecute(List<MovieDataManager> mgrs) {
            imageWithTextAdapter.clear();

            try {
                //imageWithTextAdapter.addAll(mgrs);
                //spinner = (ProgressBar) getActivity().findViewById(R.id.spinnerView);
                spinner.setVisibility(View.GONE);
            } catch (Exception e){
                Log.e("FetchData",e.getMessage(),e);
            }
            //spinner.setVisibility(View.VISIBLE);
            super.onPostExecute(mgrs);
        }
    }
}
