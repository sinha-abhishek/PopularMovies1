package models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek on 17/02/16.
 */
public class DiscoverResponseModel {
    @SerializedName("page")
    int page;
    @SerializedName("results")
    ArrayList<MovieDataModel> movieDataModels;

    public ArrayList<MovieDataModel> getMovieDataModels(){
        return movieDataModels;
    }


}
