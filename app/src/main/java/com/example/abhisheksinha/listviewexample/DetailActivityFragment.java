package com.example.abhisheksinha.listviewexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail, menu);
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
                String img_url = "http://image.tmdb.org/t/p/w185/"+m.GetPosterPath();
                Log.i(DetailActivityFragment.class.getSimpleName(),"load url "+img_url);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(DetailActivityFragment.class.getSimpleName(),"clicked "+item.getItemId());
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(getActivity());
            return true;
        }
        return true;
    }
}
