package models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abhishek on 05/03/16.
 */
public class VideoModel {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("type")
    public String type;
    @SerializedName("site")
    public String site;
    @SerializedName("key")
    public String key;
}
