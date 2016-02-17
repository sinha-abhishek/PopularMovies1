package services;

import java.util.concurrent.Callable;


import models.DiscoverResponseModel;
import models.MovieDataModel;
import retrofit.http.GET;
import retrofit.Callback;
import retrofit.http.Query;

import java.util.List;

/**
 * Created by abhishek on 17/02/16.
 */
public interface ApiService {
    @GET("/3/discover/movie")
    public void getMovies(@Query("api_key") String apiKey, @Query("sort_by") String sortString, Callback<DiscoverResponseModel> callback);
}
