package com.example.moad.myapplicationtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moad.myapplicationtest.model.NavItem;
import com.example.moad.myapplicationtest.model.Result;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MovieDetails_Activity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener  {

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;

    TextView titleMovie ;
    ImageView imageView;
    TextView DescriptionMovie ;
    ImageView imgfavoris ;
    SharedPreferences sharedPreferences;
    List<Result> favoisList ;
    String jsonFavoris ;
    Result result ;
    boolean AddOrRemove ;
    boolean existe  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Details");
        setContentView(R.layout.activity_movie_details_);
//       getLayoutInflater().inflate(R.layout.activity_movie_details_, frameLayout);
        sharedPreferences   = getBaseContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        titleMovie =  (TextView) findViewById(R.id.titleMovie);
        DescriptionMovie =  (TextView) findViewById(R.id.DescriptionMovie);
        imageView =  (ImageView) findViewById(R.id.imgMovie);
        imgfavoris= (ImageView) findViewById(R.id.imgFavoris);
        existe = false ;

        Intent intent = this.getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");

         result =  (Result) args.getSerializable("result");

        if(result.getTitle()!= null)
            titleMovie.setText(result.getTitle());
        if(result.getOriginalName()!= null)
            titleMovie.setText(result.getOriginalName());

        DescriptionMovie.setText(result.getOverview());
        String test = result.getPosterPath();
        String url = ApiKey.urlImage+""+result.getPosterPath();


        Picasso.with(imageView.getContext()).load(url).fit().centerInside().into(imageView);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Result>>(){}.getType();




        if (sharedPreferences.contains("favoris")){

            jsonFavoris = sharedPreferences.getString("favoris",null);
            favoisList = gson.fromJson(jsonFavoris, type);
            int r = 0;
            for(Result res:favoisList){
                if(res.getId().equals(result.getId())){
                    existe = true;
                    break;
                }
            }
            if(existe)
                imgfavoris.setImageResource(R.drawable.ic_favorite_black_24dp);
            else
                imgfavoris.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
        else{
            favoisList= new ArrayList<Result>();
            String json = gson.toJson(favoisList);
            sharedPreferences
                    .edit()
                    .putString("favoris", json)
                    .apply();
        }

        imgfavoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();


                if(existe){
                    for (Iterator<Result> iter = favoisList.listIterator(); iter.hasNext(); ) {
                        Result a = iter.next();
                        if (a.getId().equals(result.getId())) {
                            iter.remove();
                        }
                    }
                    imgfavoris.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    existe =false;

                }
                else{
                    imgfavoris.setImageResource(R.drawable.ic_favorite_black_24dp);
                    favoisList.add(result);
                    existe =true;
                }
                String json = gson.toJson(favoisList);
                sharedPreferences
                        .edit()
                        .putString("favoris", json)
                        .apply();
            }
        });

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(getString(R.string.YoutubeApi), this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo("fhWaJi1Hsfo"); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }
}
