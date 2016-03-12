package Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhisheksinha.listviewexample.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import models.MovieDBModel;
import models.MovieDataModel;

/**
 * Created by abhishek on 12/03/16.
 */
public class MovieDataAdapter extends CursorAdapter {

    final String BASE_URL = "http://image.tmdb.org/t/p/w185";
    public MovieDataAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.grid_item, null);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv= (TextView) view.findViewById(R.id.movieTitle);
        ImageView iv = (ImageView) view.findViewById(R.id.moviePoster);

        MovieDBModel model = MovieDBModel.fromCursor(cursor);
        tv.setText(model.name);
        String imgUrl = BASE_URL + "/" + model.posterPath;
        Picasso.with(context).load(imgUrl).into(iv);
    }

}
