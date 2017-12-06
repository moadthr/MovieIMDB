package com.example.moad.myapplicationtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.moad.myapplicationtest.model.Result;
import com.example.moad.myapplicationtest.model.Video;
import com.example.moad.myapplicationtest.model.Videos;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetails_Activity extends BaseDrawerActivity implements YouTubePlayer.OnInitializedListener {

    TextView titleMovie;
    ImageView imageView;
    ImageView imgshare;
    TextView DescriptionMovie;
    ImageView imgfavoris;
    SharedPreferences sharedPreferences;
    List<Result> favoisList;
    String jsonFavoris;
    Result result;
    String moviekey;
    boolean AddOrRemove;
    boolean existe;
    private MovieService movieService;
    FrameLayout frameLayout;
    YouTubePlayerSupportFragment frag;
    private YouTubePlayer YPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        setTitle(R.string.Details);
        getLayoutInflater().inflate(R.layout.activity_movie_details_, frameLayout);

        sharedPreferences = getBaseContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        titleMovie = (TextView) findViewById(R.id.titleMovie);
        DescriptionMovie = (TextView) findViewById(R.id.DescriptionMovie);
        imageView = (ImageView) findViewById(R.id.imgMovie);
        imgfavoris = (ImageView) findViewById(R.id.imgFavoris);
        imgshare = (ImageView) findViewById(R.id.imageshare);
        imgshare.setImageResource(R.drawable.ic_share_black_24dp);
        existe = false;

        Intent intent = this.getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");

        result = (Result) args.getSerializable("result");
        if (result.getTitle() != null)
            titleMovie.setText(result.getTitle());
        if (result.getOriginalName() != null)
            titleMovie.setText(result.getOriginalName());

        DescriptionMovie.setText(result.getOverview());
        String url = ApiKey.urlImage + "" + result.getBackdropPath();

        Picasso.with(imageView.getContext()).load(url).fit().centerInside().into(imageView);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Result>>() {
        }.getType();


        if (sharedPreferences.contains("favoris")) {

            jsonFavoris = sharedPreferences.getString("favoris", null);
            favoisList = gson.fromJson(jsonFavoris, type);
            for (Result res : favoisList) {
                if (res.getId().equals(result.getId())) {
                    existe = true;
                    break;
                }
            }
            if (existe)
                imgfavoris.setImageResource(R.drawable.ic_favorite_black_24dp);
            else
                imgfavoris.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        } else {
            favoisList = new ArrayList<Result>();
            String json = gson.toJson(favoisList);
            sharedPreferences
                    .edit()
                    .putString("favoris", json)
                    .apply();
            imgfavoris.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
        imgshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = "";
                Intent myintent = new Intent(Intent.ACTION_SEND);
                myintent.setType("text/plain");
                if (result.getType().equals("movie"))
                    shareBody = "you should see this " + result.getType() + " : " + result.getTitle() + " ;)";
                if (result.getType().equals("tvshow"))
                    shareBody = "you should see this " + result.getType() + " : " + result.getOriginalName() + " ;)";
                String objet = "IMDB Movie";
                myintent.putExtra(Intent.EXTRA_SUBJECT, objet);
                myintent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(myintent, "Share Using"));

            }
        });

        imgfavoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                if (existe) {
                    for (Iterator<Result> iter = favoisList.listIterator(); iter.hasNext(); ) {
                        Result a = iter.next();
                        if (a.getId().equals(result.getId())) {
                            iter.remove();
                        }
                    }
                    imgfavoris.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    existe = false;

                } else {
                    imgfavoris.setImageResource(R.drawable.ic_favorite_black_24dp);
                    favoisList.add(result);
                    existe = true;
                }
                String json = gson.toJson(favoisList);
                sharedPreferences
                        .edit()
                        .putString("favoris", json)
                        .apply();
            }
        });

        movieService = MovieApi.getClient().create(MovieService.class);    //1
        if (result.getType().equals("movie"))
            getVideoId();
        if (result.getType().equals("tvshow"))
            getVideoTvshowId();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer
            youTubePlayer, boolean b) {
        if (!b) {
            YPlayer = youTubePlayer;
            YPlayer.cueVideo(moviekey);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider
                                                provider, YouTubeInitializationResult youTubeInitializationResult) {
    }

    private Call<Videos> callVideoApi() {
        return movieService.getVideo(result.getId(), getString(R.string.my_api_key), "en-EN", 1
        );
    }

    private Call<Videos> callVideoTvshowApi() {
        return movieService.getVideoTvshow(result.getId(), getString(R.string.my_api_key), "en-EN", 1
        );
    }

    private List<Video> fetchResults(Response<Videos> response) {
        Videos videos = response.body();
        return videos.getResults();
    }

    private void getVideoId() {
        callVideoApi().enqueue(new Callback<Videos>() {
            @Override
            public void onResponse(Call<Videos> call, Response<Videos> response) {
                List<Video> results = fetchResults(response);
                frag = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
                if (results == null || results.size() == 0) {
                    moviekey = null;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().hide(frag).commit();
                } else {
                    moviekey = results.get(0).getKey();
                    frag.initialize(getString(R.string.YoutubeApi), MovieDetails_Activity.this);
                }

            }

            @Override
            public void onFailure(Call<Videos> call, Throwable t) {
                Toast.makeText(MovieDetails_Activity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getVideoTvshowId() {

        callVideoTvshowApi().enqueue(new Callback<Videos>() {
            @Override
            public void onResponse(Call<Videos> call, Response<Videos> response) {

                frag = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
                List<Video> results = fetchResults(response);
                if (results == null) {
                    moviekey = null;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().hide(frag).commit();
                } else {
                    moviekey = results.get(0).getKey();
                    frag.initialize(getString(R.string.YoutubeApi), MovieDetails_Activity.this);
                }
            }

            @Override
            public void onFailure(Call<Videos> call, Throwable t) {
                Toast.makeText(MovieDetails_Activity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
