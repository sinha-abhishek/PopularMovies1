package com.example.abhisheksinha.listviewexample.providers;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.List;

import models.MovieDBModel;

/**
 * Created by abhishek on 12/03/16.
 */
public class MovieProvider extends ContentProvider {
    static final String PROVIDER_NAME = "com.example.abhisheksinha.Movies";
    static final String URL = "content://" + PROVIDER_NAME ;
    public static final Uri CONTENT_URI = Uri.parse(URL);

    static final String ID = "_id";
    static final String NAME = "name";
    static final String FAV = "isFav";
    static final String POPULAR = "isPopular";
    static final String HIGHRATED = "isHighRated";


    static final int MOVIES = 1;
    static final int MOVIES_TYPE = 2;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME,"movies",MOVIES);
        uriMatcher.addURI(PROVIDER_NAME,"movies/*", MOVIES_TYPE);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case MOVIES:
                List<MovieDBModel> models = MovieDBModel.GetAll();
                return MovieDBModel.ToCursor(models);
            case MOVIES_TYPE:
                String col = uri.getPathSegments().get(1);
                if (col.compareToIgnoreCase("popular") == 0) {
                    return MovieDBModel.ToCursor(MovieDBModel.Fetch(POPULAR));
                } else if (col.compareToIgnoreCase("highestrated") == 0) {
                    return MovieDBModel.ToCursor(MovieDBModel.Fetch(HIGHRATED));
                } else if (col.compareToIgnoreCase("fav") == 0) {
                    return MovieDBModel.ToCursor(MovieDBModel.Fetch(FAV));
                } else {
                    throw new IllegalArgumentException("Unknown URI " + uri);
                }
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case MOVIES:
                return ContentResolver.CURSOR_DIR_BASE_TYPE+ "/models.MovieDBModel";
            case MOVIES_TYPE:
                return ContentResolver.CURSOR_DIR_BASE_TYPE+ "/models.MovieDBModel";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
//        String movieId = values.getAsString("movieId");
//        String movieName = values.getAsString("movieName")
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
