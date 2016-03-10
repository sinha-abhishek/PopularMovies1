package models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abhishek on 05/03/16.
 */
public class ReviewModel {
    @SerializedName("id")
    public String id;
    @SerializedName("author")
    public String author;
    @SerializedName("content")
   public  String content;
    @SerializedName("url")
    public String url;
}
