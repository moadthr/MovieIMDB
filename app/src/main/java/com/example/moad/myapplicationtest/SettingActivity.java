package com.example.moad.myapplicationtest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Locale;

public class SettingActivity extends BaseDrawerActivity {
    Spinner spinnerLanguages ;
    Spinner spinnerSizeImg ;
    static boolean initialDisplay = true ;
    SharedPreferences sharedPreferences;
    String language ;
    String activity ;
    public Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.Setting);
        context= this;
        getLayoutInflater().inflate(R.layout.activity_setting, frameLayout);
        initialDisplay = true ;
        spinnerLanguages = (Spinner) findViewById(R.id.language);
        spinnerSizeImg = (Spinner) findViewById(R.id.imgSize);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterimgSize = ArrayAdapter.createFromResource(this, R.array.imgSize, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLanguages.setAdapter(adapter);
        spinnerSizeImg.setAdapter(adapterimgSize);
        sharedPreferences = getBaseContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        language = sharedPreferences.getString("lang",null);

        if(language.equals("en-EN")) {
            setLocale("en",SettingActivity.this);
            spinnerLanguages.setSelection(0);
        }
        if(language.equals("fr-FR")) {
            setLocale("fr",SettingActivity.this);
            spinnerLanguages.setSelection(1);
        }
        Intent intent = new Intent();

        activity = intent.getStringExtra("activity");

        spinnerLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                String language = spinnerLanguages.getSelectedItem().toString();

                if (!initialDisplay) {

                    if (language.equals("English")) {
                        sharedPreferences
                                .edit()
                                .putString("lang", "en-EN")
                                .apply();
                        setLocale("en",SettingActivity.this);
                        finish();
                        startActivity(new Intent(SettingActivity.this, SettingActivity.class));

                    }
                    if (language.equals("French")) {
                        sharedPreferences
                                .edit()
                                .putString("lang", "fr-FR")
                                .apply();
                        setLocale("fr",SettingActivity.this);
                        finish();
                        startActivity( new Intent(SettingActivity.this, SettingActivity.class));


                    }


                }
                initialDisplay = false;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }

    public static  void setLocale(String lang, Context contextt) {
        Locale myLocale = new Locale(lang);
        Resources res = contextt.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

}
