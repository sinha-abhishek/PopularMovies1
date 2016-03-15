package models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek on 05/03/16.
 */
public class VideoResponseModel {
    @SerializedName("id")
    String id;

    @SerializedName("results")
    public List<VideoModel> results;
}
