package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhisheksinha.listviewexample.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import managers.MovieDataManager;
import models.MovieDataModel;

/**
 * Created by abhisheksinha on 1/30/16.
 */
public class ImageWithTextAdapter extends BaseAdapter {

    private List<MovieDataModel> movieDetails;
    Context context;
    final String BASE_URL = "http://image.tmdb.org/t/p/w185";
    private static LayoutInflater inflater=null;
    public ImageWithTextAdapter(Context context, List<MovieDataModel> movieDetails) {
        this.movieDetails = movieDetails;
        this.context = context;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void clear() {
        movieDetails.clear();
        this.notifyDataSetChanged();
    }

    public void addAll(List<MovieDataModel> movieDetails) {
        this.movieDetails = movieDetails;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return movieDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return movieDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //View view;


        Holder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item, null);
            holder = new Holder();
            holder.tv = (TextView) convertView.findViewById(R.id.movieTitle);
            holder.img = (ImageView) convertView.findViewById(R.id.moviePoster);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        MovieDataModel m = movieDetails.get(position);
        holder.tv.setText(m.GetTitle());
        String imgUrl = BASE_URL + "/" + m.GetPosterPath();
        Picasso.with(context).load(imgUrl).into(holder.img);
        return  convertView;

    }
}
