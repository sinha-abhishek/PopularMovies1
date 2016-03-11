package models;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.parceler.Parcel;

/**
 * Created by abhishek on 17/02/16.
 */

public class MovieDataModel implements Parcelable{
    @SerializedName("poster_path")
    private String posterPath;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeDouble(id);
        dest.writeString(title);
        dest.writeString(overView);
        dest.writeString(releaseDate);
        dest.writeDouble(voteAverage);
        dest.writeString(posterPath);
    }

    public MovieDataModel(MovieDBModel model) {
        this.id = model.id;
        this.title = model.name;
        this.overView = model.overview;
        this.releaseDate = model.releaseDate;
        this.voteAverage = model.voteAverage;
        this.posterPath = model.posterPath;
    }

    @SerializedName("id")
    private double id;

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

    public double GetId() { return id;}
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

    private MovieDataModel(android.os.Parcel in) {
        this.id = in.readDouble();
        this.title = in.readString();
        this.overView = in.readString();
        this.releaseDate = in.readString();
        this.voteAverage = in.readDouble();
        this.posterPath = in.readString();
    }

    public static final Parcelable.Creator<MovieDataModel> CREATOR = new Parcelable.Creator<MovieDataModel>() {

        @Override
        public MovieDataModel createFromParcel(android.os.Parcel source) {
            return new MovieDataModel(source);
        }

        @Override
        public MovieDataModel[] newArray(int size) {
            return new MovieDataModel[size];
        }
    };
}


