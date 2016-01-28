package managers;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by abhisheksinha on 1/28/16.
 */
public class MovieDataManager {
//    private String posterPath;
//    private boolean isAdult;
//    private String overView;
//    private String releaseDate;
//    private int[] genereIds;
//    private long id;
//    private String originalTitle;
//    private String title;
//    private String originalLanguage;
//    private String backDropPath;
//    private double popularity;
//    private long voteCount;
//    private boolean hasVideo;
//    private double voteAverage;
    private JSONObject movieObj;

    public MovieDataManager(JSONObject obj)  {
        movieObj = obj;
    }

    public String GetTitle() throws JSONException {
        String title = (String) movieObj.get("title");
        return title;
    }

    public String GetPosterPath() throws JSONException {
        String path = (String) movieObj.get("poster_path");
        return path;
    }
}
