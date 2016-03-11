package models;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek on 10/03/16.
 */

public class MovieDBModel extends Model {
    @Column(name = "movieId" , unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public double id;

   @Column(name = "name" )
    public String name;

    @Column (name = "overview")
    public String overview;

    @Column(name = "releaseDate")
    public String releaseDate;

    @Column(name = "voteAverage")
    public double voteAverage;

    @Column(name = "posterPath")
    public String posterPath;

    @Column (name = "isFav")
    public boolean isFav;

    @Column (name = "isPopular")
    public boolean isPopular;

    @Column (name = "isHighRated")
    public boolean isHighRated;

    public MovieDBModel(){
        super();
    }

    public MovieDBModel(MovieDataModel model, boolean isFav, boolean isPopular, boolean isHighRated) {
        this.id = model.GetId();
        this.name = model.GetTitle();
        this.overview = model.GetSynopsis();
        this.releaseDate = model.GetReleaseDate();
        this.voteAverage = model.GetVoteAvg();
        this.isFav = isFav;
        this.posterPath = model.GetPosterPath();
        this.isPopular = isPopular;
        this.isHighRated = isHighRated;

    }

    public static void UpdatePopular(List<MovieDataModel> models) {
        ActiveAndroid.beginTransaction();
        try {
            for (MovieDataModel model :
                    models) {

                MovieDBModel dbModel = FetchModelById(model.GetId());
                if (dbModel == null) {
                    dbModel = new MovieDBModel(model, false, true, false);
                } else {
                    dbModel.isPopular = true;
                }
                dbModel.save();

            }
            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ActiveAndroid.endTransaction();
        }

    }

   public static List<MovieDataModel> ConvertList(List<MovieDBModel> models) {
       List<MovieDataModel> movieDataModels = new ArrayList<MovieDataModel>();
       for (MovieDBModel model:
            models) {
            MovieDataModel movieModel = new MovieDataModel(model);
           movieDataModels.add(movieModel);
       }
       return movieDataModels;
   }

    public static void UpdateRated(List<MovieDataModel> models) {
        ActiveAndroid.beginTransaction();
        try {
            for (MovieDataModel model :
                    models) {

                MovieDBModel dbModel = FetchModelById(model.GetId());
                if (dbModel == null) {
                    dbModel = new MovieDBModel(model, false, false, true);
                } else {
                    dbModel.isHighRated = true;
                }
                dbModel.save();

            }
            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ActiveAndroid.endTransaction();
        }

    }

    public static MovieDBModel FetchModelById(double id){
        Select select = new Select();
        MovieDBModel model = select.from(MovieDBModel.class).where("movieId = ?",id).executeSingle();
        return model;
    }

    public static void ClearAllPopular() {
        Select select = new Select();
        List<MovieDBModel> models = select.from(MovieDBModel.class).where("isPopular = ?", true).execute();
        ActiveAndroid.beginTransaction();
        try {
            for (MovieDBModel model :
                    models) {
                model.isPopular = false;
                if (!model.isHighRated && !model.isFav) {
                    model.delete();
                } else {
                    model.save();
                }

            }
            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            ActiveAndroid.endTransaction();
        }

    }

    public static void ClearAllRated() {
        Select select = new Select();
        List<MovieDBModel> models = select.from(MovieDBModel.class).where("isHighRated = ?", true).execute();
        ActiveAndroid.beginTransaction();
        try {
            for (MovieDBModel model :
                    models) {
                model.isHighRated = false;
                if (!model.isPopular && !model.isFav) {
                    model.delete();
                } else {
                    model.save();
                }
            }
            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ActiveAndroid.endTransaction();
        }

    }

    public static List<MovieDBModel> FetchModels(boolean popular, boolean highrated, boolean fav) {
        Select select = new Select();
        List<MovieDBModel> models = select.from(MovieDBModel.class).where("isHighRated = ? AND isPopular= ? " +
                "AND isFav=?", highrated,popular,fav).execute();
        return models;
    }
}
