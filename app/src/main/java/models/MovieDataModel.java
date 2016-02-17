package models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.parceler.Parcel;

/**
 * Created by abhishek on 17/02/16.
 */
@Parcel
public class MovieDataModel {
    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("title")
    private String title;

    @SerializedName("overview")
    private String overView;

    @SerializedName("release_date")
    private String releaseDate;
    //private boolean isAdult;
    //    private int[] genereIds;
//    private long id;
//    private String originalTitle;
    //    private String originalLanguage;
//    private String backDropPath;
//    private double popularity;
//    private long voteCount;
//    private boolean hasVideo;
    @SerializedName("vote_average")
    private double voteAverage;

    public String GetTitle() {
        return title;
    }

    public String GetPosterPath() {
        return posterPath;
    }


    public String GetSynopsis()  {
        return overView;
    }

    public String GetReleaseDate() {
        return releaseDate;
    }

    public double GetVoteAvg() {
        return voteAverage;
    }
}


