package com.example.moad.myapplicationtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.moad.myapplicationtest.model.Result;
import com.example.moad.myapplicationtest.model.SearchResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Favoris_Activity extends BaseDrawerActivity  implements ListItemClickListener {
    SharedPreferences sharedPreferences;
    List<Result> favoisList ;
    String jsonFavoris ;
    PaginationAdapter adapterPagination;
    ProgressBar progressBar;
    static int layoutcard ;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    public static RecyclerView mNameList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.Favoris);
        getLayoutInflater().inflate(R.layout.activity_favoris_, frameLayout);
        layoutcard = R.layout.cell_cards_2;
        mNameList = (RecyclerView) findViewById(R.id.rv_names);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);

    }

    public void load (){

        Gson gson = new Gson();
        Type type = new TypeToken<List<Result>>(){}.getType();
        sharedPreferences = getBaseContext().getSharedPreferences("MyPref",MODE_PRIVATE);
        if (sharedPreferences.contains("favoris")){
            jsonFavoris = sharedPreferences.getString("favoris",null);
            favoisList = gson.fromJson(jsonFavoris, type);
        }
        else{
            favoisList= new ArrayList<Result>();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
        changeAdapter(layoutcard);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.showGrid).setVisible(false);
        return true;
    }

    public void changeAdapter (int layout ){

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mNameList.setLayoutManager(layoutManager);

        adapterPagination = new PaginationAdapter(Favoris_Activity.this,this,layout);
        mNameList.setItemAnimator(new DefaultItemAnimator());
        mNameList.setAdapter(adapterPagination);
        mNameList.setHasFixedSize(true);

        // mocking network delay for API call
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadFirstPage();
            }
        }, 1000);

    }

    private void loadFirstPage() {
                List<Result> results = favoisList;
                adapterPagination.addAll(results);
                 isLastPage = true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.showGrid ) {
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(Result result) {
        Intent intent = new Intent(this,MovieDetails_Activity.class);
        Bundle args = new Bundle();
        args.putSerializable("result",(Serializable)result);
        intent.putExtra("BUNDLE",args);
        startActivity(intent);
    }

}
