package com.example.moad.myapplicationtest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Locale;

public class SettingActivity extends BaseDrawerActivity {
    Spinner spinnerLanguages;
    static boolean initialDisplay = true;
    SharedPreferences sharedPreferences;
    String language;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.Setting);
        context = this;
        getLayoutInflater().inflate(R.layout.activity_setting, frameLayout);
        initialDisplay = true;
        spinnerLanguages = (Spinner) findViewById(R.id.language);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguages.setAdapter(adapter);

        sharedPreferences = getBaseContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        language = sharedPreferences.getString("lang", null);

        if (language.equals("en-EN")) {
            setLocale("en", SettingActivity.this);
            spinnerLanguages.setSelection(0);
        }
        if (language.equals("fr-FR")) {
            setLocale("fr", SettingActivity.this);
            spinnerLanguages.setSelection(1);
        }
        spinnerLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                String language = spinnerLanguages.getSelectedItem().toString();

                if (!initialDisplay) {

                    if (language.equals("English") || language.equals("Anglais")) {
                        sharedPreferences
                                .edit()
                                .putString("lang", "en-EN")
                                .apply();
                        setLocale("en", SettingActivity.this);
                        finish();
                        startActivity(new Intent(SettingActivity.this, SettingActivity.class));

                    }
                    if (language.equals("French") || language.equals("Francais")) {
                        sharedPreferences
                                .edit()
                                .putString("lang", "fr-FR")
                                .apply();
                        setLocale("fr", SettingActivity.this);
                        finish();
                        startActivity(new Intent(SettingActivity.this, SettingActivity.class));
                    }
                }
                initialDisplay = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }

    public static void setLocale(String lang, Context contextt) {
        Locale myLocale = new Locale(lang);
        Resources res = contextt.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }


}
