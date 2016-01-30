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

/**
 * Created by abhisheksinha on 1/30/16.
 */
public class ImageWithTextAdapter extends BaseAdapter {
    private List<String> posterPaths;
    private List<String> titles;
    Context context;
    final String BASE_URL = "http://image.tmdb.org/t/p/w92";
    private static LayoutInflater inflater=null;
    public ImageWithTextAdapter(Context context, List<String> posterPaths, List<String> titles) {
        this.posterPaths = posterPaths;
        this.titles = titles;
        this.context = context;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void clear() {
        posterPaths.clear();
        titles.clear();
        this.notifyDataSetChanged();
    }

    public void addAll(List<String> posterPaths, List<String> titles) {
        this.posterPaths = posterPaths;
        this.titles = titles;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
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
        textView.setText(titles.get(position));
        String imgUrl = BASE_URL + "/" + posterPaths.get(position);
        Picasso.with(context).load(imgUrl).into(imageView);
        return  view;

    }
}
