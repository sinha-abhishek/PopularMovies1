package services;

import java.util.concurrent.Callable;


import models.DiscoverResponseModel;
import models.MovieDataModel;
import models.ReviewResponseModel;
import models.VideoResponseModel;
import retrofit.http.GET;
import retrofit.Callback;
import retrofit.http.Path;
import retrofit.http.Query;

import java.util.List;

/**
 * Created by abhishek on 17/02/16.
 */
public interface ApiService {
    @GET("/discover/movie")
    public void getMovies(@Query("api_key") String apiKey, @Query("sort_by") String sortString, Callback<DiscoverResponseModel> callback);

    @GET("/movie/{id}/videos")
    public void getTrailers(@Path("id") String id, @Query("api_key") String apiKey, Callback<VideoResponseModel> callback);

    @GET("/movie/{id}/reviews")
    public void getReviews(@Path("id") String id, @Query("api_key") String apiKey, Callback<ReviewResponseModel> callback);
}
