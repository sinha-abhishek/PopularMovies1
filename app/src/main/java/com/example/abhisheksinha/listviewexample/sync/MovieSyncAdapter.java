package com.example.abhisheksinha.listviewexample.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.example.abhisheksinha.listviewexample.MainFragment;
import com.example.abhisheksinha.listviewexample.R;

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
 * Created by abhishek on 13/04/16.
 */
public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {
    ContentResolver mContentResolver;
    public static final String NETWORK_ACTION = "network_action";
    public static final String POPULAR_UPDATED = "popular_updated";
    public static final String RATED_UPDATED = "rated_updated";
    private static final String LOG_TAG = "MovieSyncAdapter";

    public MovieSyncAdapter(Context context, boolean autoInitialize) {

        super(context, autoInitialize);

        mContentResolver = context.getContentResolver();
    }

    public MovieSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        RestClient restClient = new RestClient();
        ApiService apiService = restClient.getApiService();
        apiService.getMovies(MainFragment.API_KEY, getContext().getString(R.string.sort_popularity_val), new Callback<DiscoverResponseModel>() {
            @Override
            public void success(DiscoverResponseModel discoverResponseModel, Response response) {
                Log.i(LOG_TAG, "got " + discoverResponseModel.toString());
                List<MovieDataModel> models = discoverResponseModel.getMovieDataModels();
                MovieDBModel.UpdatePopular(models);
                Intent intent1 = new Intent(NETWORK_ACTION);
                intent1.putExtra(POPULAR_UPDATED, true);
                getContext().sendBroadcast(intent1);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        apiService.getMovies(MainFragment.API_KEY, getContext().getString(R.string.sort_vote_val), new Callback<DiscoverResponseModel>() {
            @Override
            public void success(DiscoverResponseModel discoverResponseModel, Response response) {
                Log.i(LOG_TAG, "got " + discoverResponseModel.toString());
                List<MovieDataModel> models = discoverResponseModel.getMovieDataModels();
                MovieDBModel.UpdateRated(models);
                Intent intent1 = new Intent(NETWORK_ACTION);
                intent1.putExtra(RATED_UPDATED, true);
                getContext().sendBroadcast(intent1);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
