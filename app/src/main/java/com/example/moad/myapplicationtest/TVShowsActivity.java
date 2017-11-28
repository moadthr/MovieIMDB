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

import com.example.moad.myapplicationtest.model.PopularTvShows;
import com.example.moad.myapplicationtest.model.Result;
import com.example.moad.myapplicationtest.model.TopRatedMovies;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TVShowsActivity extends BaseDrawerActivity implements ListItemClickListener {

    public static RecyclerView mNameList ;
    static int  showGrid    ;
    static int layoutcard ;
    PaginationAdapter adapterPagination;
    ProgressBar progressBar;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    private MovieService movieService;
    static String language  ;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_tvshows, frameLayout);

        layoutcard = R.layout.cell_cards;
        showGrid = 1 ;
        sharedPreferences = getBaseContext().getSharedPreferences("MyPref",MODE_PRIVATE);
        if (sharedPreferences.contains("lang")){
            language = sharedPreferences.getString("lang",null);
        }
        else{
            sharedPreferences
                    .edit()
                    .putString("lang", "en-EN")
                    .apply();
            language = sharedPreferences.getString("lang",null);
        }


        mNameList = (RecyclerView) findViewById(R.id.rv_names);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        changeAdapter(layoutcard);
        movieService = MovieApi.getClient().create(MovieService.class);	//1
    }
    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    private Call<PopularTvShows> callPopularTvShowsApi() {	//2
        return movieService.getTvSeries(
                getString(R.string.my_api_key),language,
                currentPage
        );
    }
    private List<Result> fetchResults(Response<PopularTvShows> response) {	//3
        PopularTvShows populartvShows = response.body();
        return populartvShows.getResults();
    }


    public void changeAdapter (int layout ){

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mNameList.setLayoutManager(layoutManager);
        if(layout == R.layout.cell_cards_3){
            mNameList.setLayoutManager(new GridLayoutManager(this, 3));
        }

        adapterPagination = new PaginationAdapter(TVShowsActivity.this,this,layout);
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

        callPopularTvShowsApi().enqueue(new Callback<PopularTvShows>() {
            @Override
            public void onResponse(Call<PopularTvShows> call, Response<PopularTvShows> response) {
                // Got data. Send it to adapter

                List<Result> results = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                adapterPagination.addAll(results);

                if (currentPage <= TOTAL_PAGES) adapterPagination.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<PopularTvShows> call, Throwable t) {
                Toast.makeText(TVShowsActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void loadNextPage() {

        callPopularTvShowsApi().enqueue(new Callback<PopularTvShows>() {
            @Override
            public void onResponse(Call<PopularTvShows> call, Response<PopularTvShows> response) {
                adapterPagination.removeLoadingFooter();
                isLoading = false;

                List<Result> results = fetchResults(response);
                adapterPagination.addAll(results);

                if (currentPage != TOTAL_PAGES) adapterPagination.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<PopularTvShows> call, Throwable t) {
                t.printStackTrace();
                // TODO: 08/11/16 handle failure
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        int test = showGrid ;
        if (id == R.id.showGrid ) {
            if(showGrid%3 == 0) {
                layoutcard = R.layout.cell_cards;
                changeAdapter(layoutcard);
                showGrid++;
                return true;
            }
            else if(showGrid%3 == 1){
                layoutcard = R.layout.cell_cards_2;
                changeAdapter(layoutcard);
                showGrid++;
                return true;
            }
            else if(showGrid%3 == 2) {
                layoutcard = R.layout.cell_cards_3;
                changeAdapter(layoutcard);
                showGrid++;
                return true;
            }

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onListItemClick(Result movie) {
        Intent intent = new Intent(this,MovieDetails_Activity.class);
        // intent.putExtra("list", list);

        Bundle args = new Bundle();
        args.putSerializable("result",(Serializable)movie);
        intent.putExtra("BUNDLE",args);
        //intent.putStringArrayListExtra(EXTRA_CARS,cars);
        startActivity(intent);


    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(TVShowsActivity.this, MainActivity.class));
        finish();
    }

}
