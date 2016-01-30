package com.example.abhisheksinha.listviewexample;

import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;

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

/**
 * Created by abhisheksinha on 12/31/15.
 */
public class TestFragment extends Fragment{
    private ArrayAdapter<String> adp;
    private ProgressBar spinner;
    final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
    final String API_KEY = "6fbc4c1792dbc14aba698926dfadf31f";
    final String SORT_QUERY_PARAM = "sort_by";
    private ImageWithTextAdapter imageWithTextAdapter;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_example, container, false);
        FetchTask t = new FetchTask();
        t.execute("popularity.desc");
        List<String> dummyList = new ArrayList<String>();
        List<String> dummyList2 = new ArrayList<String>();

        adp = new ArrayAdapter<String>(getActivity(),R.layout.list_item,R.id.listitem, dummyList);
        imageWithTextAdapter = new ImageWithTextAdapter(getActivity(),dummyList, dummyList2);
        GridView lv = (GridView) rootView;
        lv.setAdapter(imageWithTextAdapter);
        return rootView;
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
                JSONObject res = (JSONObject)resultArr.get(i);
                //String title = (String) res.get("title");
                MovieDataManager m = new MovieDataManager(res);
                results.add(m);
            }
            return results;
        }

        @Override
        protected void onPostExecute(List<MovieDataManager> mgrs) {
            adp.clear();
            imageWithTextAdapter.clear();
            List<String> strings = new ArrayList<String>();
            List<String> posterPaths = new ArrayList<String>();
            try {
                for (MovieDataManager m : mgrs) {
                    String title = m.GetTitle();
                    strings.add(title);
                    String path = m.GetPosterPath();
                    posterPaths.add(path);
                }
                imageWithTextAdapter.addAll(posterPaths,strings);
                adp.addAll(strings);
                spinner = (ProgressBar) getActivity().findViewById(R.id.spinnerView);
                spinner.setVisibility(View.GONE);
            } catch (JSONException e){
                Log.e("FetchData","Jsonexception",e);
            }
            //spinner.setVisibility(View.VISIBLE);
            super.onPostExecute(mgrs);
        }
    }
}
