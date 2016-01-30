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

/**
 * Created by abhisheksinha on 1/30/16.
 */
public class ImageWithTextAdapter extends BaseAdapter {

    private List<MovieDataManager> movieDetails;
    Context context;
    final String BASE_URL = "http://image.tmdb.org/t/p/w185";
    private static LayoutInflater inflater=null;
    public ImageWithTextAdapter(Context context, List<MovieDataManager> movieDetails) {
        this.movieDetails = movieDetails;
        this.context = context;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void clear() {
        movieDetails.clear();
        this.notifyDataSetChanged();
    }

    public void addAll(List<MovieDataManager> movieDetails) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        view = inflater.inflate(R.layout.grid_item, null);
        TextView textView = (TextView) view.findViewById(R.id.movieTitle);
        ImageView imageView = (ImageView) view.findViewById(R.id.moviePoster);
        MovieDataManager m = movieDetails.get(position);
        textView.setText(m.GetTitle());
        String imgUrl = BASE_URL + "/" + m.GetPosterPath();
        Picasso.with(context).load(imgUrl).into(imageView);
        return  view;

    }
}
