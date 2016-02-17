package models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by abhishek on 17/02/16.
 */
public class DiscoverResponseModel {
    @SerializedName("page")
    int page;
    @SerializedName("results")
    List<MovieDataModel> movieDataModels;

    public List<MovieDataModel> getMovieDataModels(){
        return movieDataModels;
    }


}
