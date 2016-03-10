package models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by abhishek on 05/03/16.
 */
public class ReviewResponseModel {
    @SerializedName("id")
    String id;
    @SerializedName("results")
    public ArrayList<ReviewModel> reviews;

    @SerializedName("total_results")
    public int totalResults;

}
