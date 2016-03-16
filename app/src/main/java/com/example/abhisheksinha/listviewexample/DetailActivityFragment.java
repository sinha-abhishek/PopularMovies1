package com.example.abhisheksinha.listviewexample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapters.ReviewAdapter;
import Adapters.TrailerAdapter;
import managers.MovieDataManager;
import models.MovieDBModel;
import models.MovieDataModel;
import models.ReviewModel;
import models.ReviewResponseModel;
import models.VideoModel;
import models.VideoResponseModel;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import services.ApiService;
import services.RestClient;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public static final String MOVIE_DETAIL_KEY = "movieDetailKey";

    private List<VideoModel> trailers = new ArrayList<VideoModel>();
    private List<ReviewModel> reviews = new ArrayList<ReviewModel>();

    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    LayoutInflater inflater;
    private ShareActionProvider mShareActionProvider;

    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail2, container, false);
        this.inflater = inflater;
//        Intent intent = getActivity().getIntent();
//        TextView tmp = (TextView) view.findViewById(R.id.detailTitle);
//        tmp.setText("testing...");
        Bundle args = getArguments();

        if (args != null &&  args.containsKey(MOVIE_DETAIL_KEY)) {// intent.hasExtra(Intent.EXTRA_TEXT)) {
            try {
//                String data = intent.getStringExtra(Intent.EXTRA_TEXT);
//                JSONObject j = new JSONObject(data);
//                MovieDataManager m = new MovieDataManager(j);
                trailerAdapter = new TrailerAdapter(getActivity(), trailers);
                reviewAdapter = new ReviewAdapter(getActivity(),reviews);
                final MovieDataModel m = args.getParcelable(MOVIE_DETAIL_KEY);
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
                voteAvg.setText(getString(R.string.user_rating) +  Double.toString(m.GetVoteAvg()));
                TextView releaseDateVal = (TextView) view.findViewById(R.id.releaseDateVal);
                releaseDateVal.setText(getString(R.string.release_date) + m.GetReleaseDate());
                Button button = (Button) view.findViewById(R.id.favBtn);
                MovieDBModel movieDBModel = MovieDBModel.FetchModelById(m.GetId());

                final boolean isFav = movieDBModel.isFav;
                if (isFav){
                    button.setText(getString(R.string.remFav));
                } else{
                    button.setText(getString(R.string.addfav));
                }

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MovieDBModel movieDBModel = MovieDBModel.FetchModelById(m.GetId());
                        if (isFav) {
                            movieDBModel.isFav = false;
                        } else {
                            movieDBModel.isFav = true;
                        }
                        movieDBModel.save();
                    }
                });

//                ListView trailerList = (ListView) view.findViewById(R.id.trailers);
//                trailerList.setAdapter(trailerAdapter);

//                ListView reviewList = (ListView) view.findViewById(R.id.reviews);
//                reviewList.setAdapter(reviewAdapter);

                fetchReviews(reviews, m.GetId());
                fetchTrailers(trailers, m.GetId());

//                trailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        VideoModel model = (VideoModel) parent.getAdapter().getItem(position);
//                        String url = model.getYoutubeURL();
//                        if (url != "") {
//                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
//                            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
//                                startActivity(intent);
//                            } else {
//                                Toast.makeText(getActivity(), "No supported app to view trailer", Toast.LENGTH_SHORT);
//                            }
//                        }
//                    }
//                });

//                reviewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        ReviewModel model = (ReviewModel) parent.getAdapter().getItem(position);
//                        String url = model.url;
//                        if (url != null) {
//                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
//                            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
//                                startActivity(intent);
//                            } else {
//                                Toast.makeText(getActivity(), "No supported app to open link", Toast.LENGTH_SHORT);
//                            }
//                        }
//                    }
//                });



//                TextView trailer = (TextView) view.findViewById(R.id.trailers);
//                trailer.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
            }catch (Exception e) {
                Log.e(DetailActivityFragment.class.getSimpleName(),e.getMessage(),e);
            }
        }
        return view;
    }

    private void fetchTrailers(final List<VideoModel> models, double id)  {
        RestClient client = new RestClient();
        ApiService service = client.getApiService();
        service.getTrailers(String.valueOf(id), MainFragment.API_KEY, new Callback<VideoResponseModel>() {
            @Override
            public void success(VideoResponseModel videoResponseModel, Response response) {
                models.addAll(videoResponseModel.results);
                //trailerAdapter.clear();
                //trailerAdapter.addAll(models);
                LinearLayout list = (LinearLayout) getActivity().findViewById(R.id.trailers);
                int i = 1;
                for (final VideoModel model :
                        models) {
                    View vi = inflater.inflate(R.layout.list_item, null);
                    TextView tv = (TextView) vi.findViewById(R.id.trailerText);
                    tv.setText("Trailer " + i);
                    i++;
                    vi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //VideoModel model = (VideoModel) parent.getAdapter().getItem(position);
                            String url = model.getYoutubeURL();
                            if (url != "") {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
                                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getActivity(), "No supported app to view trailer", Toast.LENGTH_SHORT);
                                }
                            }
                        }
                    });
                    list.addView(vi);

                }
                if (models.size() > 0) {
                    SetShareIntent();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Can't load trailers this time", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchReviews(final List<ReviewModel> models, double id) {
        RestClient client = new RestClient();
        ApiService service = client.getApiService();
        String idString = String.valueOf(id).substring(0, String.valueOf(id).indexOf("."));
        service.getReviews(idString, MainFragment.API_KEY, new Callback<ReviewResponseModel>() {
            @Override
            public void success(ReviewResponseModel responseModel, Response response) {
                models.addAll(responseModel.reviews);
                //reviewAdapter.clear();
                LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.reviews);
                int i = 1;
                for (final ReviewModel model :
                        models) {
                    View vi = inflater.inflate(R.layout.review_list_item, null);
                    TextView tv = (TextView) vi.findViewById(R.id.review_summary);
                    TextView author = (TextView) vi.findViewById(R.id.reviewer);
                    author.setText(model.author);
                    tv.setText(model.content);
                    i++;
                    vi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //VideoModel model = (VideoModel) parent.getAdapter().getItem(position);
                            String url = model.url;
                            if (url != "") {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
                                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getActivity(), "No supported app to open reviews", Toast.LENGTH_SHORT);
                                }
                            }
                        }
                    });
                    ll.addView(vi);
                    //reviewAdapter.addAll(reviews);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Can't load reviews this time", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void SetShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
       // shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        if (trailers.size() > 0) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, trailers.get(0).getYoutubeURL());
            if(mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(shareIntent);
            }
            return ;//shareIntent;
        }
        return ;
    }

    public void SetShareProvider(ShareActionProvider provider) {
        mShareActionProvider = provider;
    }
}
