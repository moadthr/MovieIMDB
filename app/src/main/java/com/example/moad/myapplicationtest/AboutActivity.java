package com.example.moad.myapplicationtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AboutActivity extends BaseDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_about, frameLayout);
        setTitle(R.string.About);
    }

}
