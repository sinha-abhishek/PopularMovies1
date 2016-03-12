package com.example.abhisheksinha.listviewexample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import models.DiscoverResponseModel;
import models.MovieDBModel;
import models.MovieDataModel;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import services.ApiService;
import services.RestClient;

/**
 * Created by abhishek on 11/03/16.
 */
public class NetworkService extends Service {
    private static final String LOG_TAG = "NetworkService";
    public static final String NETWORK_ACTION = "network_action";
    public static final String POPULAR_UPDATED = "popular_updated";
    public static final String RATED_UPDATED = "rated_updated";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        RestClient restClient = new RestClient();
        ApiService apiService = restClient.getApiService();
        apiService.getMovies(MainFragment.API_KEY, getString(R.string.sort_popularity_val), new Callback<DiscoverResponseModel>() {
            @Override
            public void success(DiscoverResponseModel discoverResponseModel, Response response) {
                Log.i(LOG_TAG, "got " + discoverResponseModel.toString());
                List<MovieDataModel> models = discoverResponseModel.getMovieDataModels();
                MovieDBModel.UpdatePopular(models);
                Intent intent1 = new Intent(NETWORK_ACTION);
                intent1.putExtra(POPULAR_UPDATED, true);
                sendBroadcast(intent1);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        apiService.getMovies(MainFragment.API_KEY, getString(R.string.sort_vote_val), new Callback<DiscoverResponseModel>() {
            @Override
            public void success(DiscoverResponseModel discoverResponseModel, Response response) {
                Log.i(LOG_TAG, "got "+discoverResponseModel.toString());
                List<MovieDataModel> models = discoverResponseModel.getMovieDataModels();
                MovieDBModel.UpdateRated(models);
                Intent intent1 = new Intent(NETWORK_ACTION);
                intent1.putExtra(RATED_UPDATED, true);
                sendBroadcast(intent1);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        return super.onStartCommand(intent, flags, startId);
    }
}
