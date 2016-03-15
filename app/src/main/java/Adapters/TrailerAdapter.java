package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.abhisheksinha.listviewexample.R;

import java.util.List;

import models.VideoModel;

/**
 * Created by abhishek on 15/03/16.
 */
public class TrailerAdapter extends BaseAdapter {

    private List<VideoModel> modelList;
    private Context context;
    private static LayoutInflater inflater;

    public TrailerAdapter(Context context, List<VideoModel> modelList ) {
        this.modelList = modelList;
        this.context = context;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clear() {
        modelList.clear();
        this.notifyDataSetChanged();
    }

    public void addAll(List<VideoModel> movieDetails) {
        this.modelList = movieDetails;
        this.notifyDataSetChanged();
    }

    public class ViewHolder {
        public TextView textView;

        public ViewHolder(View view) {

            textView = (TextView) view.findViewById(R.id.trailerText);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView  = TrailerAdapter.inflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.textView.setText("Trailer "+position);
        return convertView;
    }

}
