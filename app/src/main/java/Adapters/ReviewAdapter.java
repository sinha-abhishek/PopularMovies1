package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.abhisheksinha.listviewexample.R;

import java.util.List;

import models.ReviewModel;
import models.VideoModel;

/**
 * Created by abhishek on 15/03/16.
 */
public class ReviewAdapter extends BaseAdapter {

    private List<ReviewModel> modelList;
    private Context context;
    private static LayoutInflater inflater;

    public ReviewAdapter(Context context, List<ReviewModel> modelList ) {
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

    public void addAll(List<ReviewModel> movieDetails) {
        this.modelList = movieDetails;
        this.notifyDataSetChanged();
    }

    public class ViewHolder {
        public TextView textView;

        public ViewHolder(View view) {

            textView = (TextView) view.findViewById(R.id.review_summary);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView  = ReviewAdapter.inflater.inflate(R.layout.review_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        ReviewModel model = modelList.get(position);
        holder.textView.setText(model.content);
        return convertView;
    }

}
