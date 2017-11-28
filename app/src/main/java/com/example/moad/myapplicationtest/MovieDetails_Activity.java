package com.example.moad.myapplicationtest;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.moad.myapplicationtest.model.NavItem;
import com.example.moad.myapplicationtest.model.Result;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetails_Activity extends BaseDrawerActivity {
    TextView titleMovie ;
    ImageView imageView;
    TextView DescriptionMovie ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       getLayoutInflater().inflate(R.layout.activity_movie_details_, frameLayout);

        titleMovie =  (TextView) findViewById(R.id.titleMovie);
        DescriptionMovie =  (TextView) findViewById(R.id.DescriptionMovie);
        imageView =  (ImageView) findViewById(R.id.imgMovie);

        Intent intent = this.getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");

        Result result =  (Result) args.getSerializable("result");

        if(result.getTitle()!= null)
            titleMovie.setText(result.getTitle());
        if(result.getOriginalName()!= null)
            titleMovie.setText(result.getOriginalName());

        DescriptionMovie.setText(result.getOverview());
        String test = result.getPosterPath();
        String url = ApiKey.urlImage+""+result.getPosterPath();


        Picasso.with(imageView.getContext()).load(url).fit().centerInside().into(imageView);

    }

}
