package com.example.abhisheksinha.listviewexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import managers.MovieDataManager;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            try {
                String data = intent.getStringExtra(Intent.EXTRA_TEXT);
                JSONObject j = new JSONObject(data);
                MovieDataManager m = new MovieDataManager(j);
                TextView title = (TextView) view.findViewById(R.id.detailTitle);
                title.setText(m.GetTitle());
                ImageView img = (ImageView) view.findViewById(R.id.detailImage);
                String img_url = "http://image.tmdb.org/t/p/w500/"+m.GetPosterPath();
                Picasso.with(getActivity()).load(img_url).into(img);
                String synopsis = m.GetSynopsis();
                TextView syn = (TextView) view.findViewById(R.id.synopsis);
                syn.setText(synopsis);
                TextView voteAvg = (TextView) view.findViewById(R.id.user_rating_val);
                voteAvg.setText(Double.toString(m.GetVoteAvg()));
                TextView releaseDateVal = (TextView) view.findViewById(R.id.releaseDateVal);
                releaseDateVal.setText(m.GetReleaseDate());
            }catch (JSONException e) {
                Log.e(DetailActivityFragment.class.getSimpleName(),e.getMessage(),e);
            }
        }
        return view;
    }
}
