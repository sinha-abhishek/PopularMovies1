package managers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by abhisheksinha on 1/28/16.
 */
public class MovieDataManager implements Serializable {
    private String posterPath;
//    private boolean isAdult;
    private String overView;
    private String releaseDate;
//    private int[] genereIds;
//    private long id;
//    private String originalTitle;
    private String title;
//    private String originalLanguage;
//    private String backDropPath;
//    private double popularity;
//    private long voteCount;
//    private boolean hasVideo;
    private double voteAverage;
    private JSONObject movieObj;

    public MovieDataManager(JSONObject obj) throws JSONException{
        movieObj = obj;
        this.title = (String) movieObj.get("title");
        posterPath = (String) movieObj.get("poster_path");
        overView = (String) movieObj.get("overview");
        voteAverage = (double) movieObj.get("vote_average");
        releaseDate = (String) movieObj.get("release_date");

    }

    public String GetTitle() {
        return title;
    }

    public String GetPosterPath() {
        return posterPath;
    }

    public String GetJson() throws JSONException {
        String json = movieObj.toString();
        return json;
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
