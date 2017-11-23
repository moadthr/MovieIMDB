package com.example.moad.myapplicationtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moad.myapplicationtest.model.Result;
import com.squareup.picasso.Picasso;

public class MovieDetails_Activity extends AppCompatActivity {
    TextView titleMovie ;
    ImageView imageView;
    TextView DescriptionMovie ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details_);

        titleMovie =  (TextView) findViewById(R.id.titleMovie);
        DescriptionMovie =  (TextView) findViewById(R.id.DescriptionMovie);
        imageView =  (ImageView) findViewById(R.id.imgMovie);

        Intent intent = this.getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        Result movie =  (Result) args.getSerializable("movie");

        titleMovie.setText(movie.getTitle());
        DescriptionMovie.setText(movie.getOverview());
        String test = movie.getPosterPath();
        String url = ApiKey.urlImage+""+movie.getPosterPath();


        Picasso.with(imageView.getContext()).load(url).fit().centerInside().into(imageView);

    }
}
