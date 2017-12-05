package com.example.moad.myapplicationtest;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;



import com.example.moad.myapplicationtest.model.NavItem;
import com.example.moad.myapplicationtest.model.Result;
import com.example.moad.myapplicationtest.model.TopRated;
import com.example.moad.myapplicationtest.model.TopRatedMovies;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class MainActivity extends BaseDrawerActivity implements ListItemClickListener{


    public static RecyclerView mNameList ;
    static int  showGrid    ;
    static int layoutcard ;
    PaginationAdapter adapterPagination;
    ProgressBar progressBar;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 15;
    private int currentPage = PAGE_START;
    private MovieService movieService;
    static String language  ;
    LinearLayoutManager layoutManager;
    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.Home);
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);
        layoutcard = R.layout.cell_cards;
        showGrid = 1 ;
        load();
        mNameList = (RecyclerView) findViewById(R.id.rv_names);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        layoutManager = new LinearLayoutManager(this);
        mNameList.setLayoutManager(layoutManager);

        movieService = MovieApi.getClient().create(MovieService.class);	//1

    }
    public void load (){

        sharedPreferences = getBaseContext().getSharedPreferences("MyPref",MODE_PRIVATE);
        if (sharedPreferences.contains("lang")){
            language = sharedPreferences.getString("lang",null);
            if(language.equals("en-EN"))
                SettingActivity.setLocale("en",this);
            else
                SettingActivity.setLocale("fr",this);
        }
        else{
            sharedPreferences
                    .edit()
                    .putString("lang", "en-EN")
                    .apply();
            language = sharedPreferences.getString("lang",null);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
        int a= currentPage ;
        boolean b=isLoading;
        changeAdapter(layoutcard);
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private Call<TopRatedMovies> callTopRatedMoviesApi() {	//2
        return movieService.getTopRatedMovies(
                getString(R.string.my_api_key),language,
                currentPage
        );
    }
    private List<Result> fetchResults(Response<TopRatedMovies> response) {	//3
        TopRatedMovies topRatedMovies = response.body();
        return topRatedMovies.getResults();
    }

    public RecyclerView getmNameList() {
        return mNameList;
    }

    public void changeAdapter (int layout ){

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mNameList.setLayoutManager(layoutManager);
        if (layout == R.layout.cell_cards_3) {
            mNameList.setLayoutManager(new GridLayoutManager(this, 3));
        }

         adapterPagination = new PaginationAdapter(MainActivity.this,this,layout);
         mNameList.setItemAnimator(new DefaultItemAnimator());
         mNameList.setAdapter(adapterPagination);
         mNameList.setHasFixedSize(true);

          mNameList.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        // mocking network delay for API call
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                     loadFirstPage();
            }
        }, 1000);
    }
    private void loadFirstPage() {

        callTopRatedMoviesApi().enqueue(new Callback<TopRatedMovies>() {
            @Override
            public void onResponse(Call<TopRatedMovies> call, Response<TopRatedMovies> response) {
                // Got data. Send it to adapter

                if (currentPage == TOTAL_PAGES) {
                    List<Result> results = fetchResults(response);
                    adapterPagination.addAll(results);
                    isLastPage = true;
                }
                else {
                    List<Result> results = fetchResults(response);
                    progressBar.setVisibility(View.GONE);
                    adapterPagination.addAll(results);

                    if (currentPage <= TOTAL_PAGES)
                        adapterPagination.addLoadingFooter();
                    else {
                        isLastPage = true;
                    }
                }
            }
            @Override
            public void onFailure(Call<TopRatedMovies> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadNextPage() {

        callTopRatedMoviesApi().enqueue(new Callback<TopRatedMovies>() {
            @Override
            public void onResponse(Call<TopRatedMovies> call, Response<TopRatedMovies> response) {
                adapterPagination.removeLoadingFooter();
                isLoading = false;

                List<Result> results = fetchResults(response);
                adapterPagination.addAll(results);

                if (currentPage != TOTAL_PAGES) adapterPagination.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<TopRatedMovies> call, Throwable t) {
                t.printStackTrace();
                // TODO: 08/11/16 handle failure
            }
        });
    }
        @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.showGrid ) {
              if (showGrid % 3 == 0) {
                layoutcard = R.layout.cell_cards;
                changeAdapter(layoutcard);
                showGrid++;
            } else if (showGrid % 3 == 1) {
                layoutcard = R.layout.cell_cards_2;
                changeAdapter(layoutcard);
                showGrid++;
            } else if (showGrid % 3 == 2) {
                layoutcard = R.layout.cell_cards_3;
                changeAdapter(layoutcard);
                showGrid++;
            }
            return true;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onListItemClick(Result result) {
        Intent intent = new Intent(this,MovieDetails_Activity.class);
        Bundle args = new Bundle();
        result.setType("movie");
        args.putSerializable("result",(Serializable)result);
        intent.putExtra("BUNDLE",args);
        startActivity(intent);

    }

}
