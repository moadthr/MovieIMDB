package com.example.moad.myapplicationtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.moad.myapplicationtest.model.PopularTvShows;
import com.example.moad.myapplicationtest.model.Result;
import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TVShowsActivity extends BaseDrawerActivity implements ListItemClickListener {

    public static RecyclerView mNameList;
    static int showGrid;
    static int layoutcard;
    PaginationTvshowAdapter adapterPagination;
    ProgressBar progressBar;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 10;
    private int currentPage = PAGE_START;
    private MovieService movieService;
    static String language;
    SharedPreferences sharedPreferences;
    LinearLayoutManager layoutManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.TvShows);
        getLayoutInflater().inflate(R.layout.activity_tvshows, frameLayout);

        layoutcard = R.layout.cell_cards;
        showGrid = 1;
        load();
        layoutManager = new LinearLayoutManager(this);
        mNameList = (RecyclerView) findViewById(R.id.rv_names);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        movieService = MovieApi.getClient().create(MovieService.class);    //1
    }

    public void load() {
        sharedPreferences = getBaseContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        if (sharedPreferences.contains("lang")) {
            language = sharedPreferences.getString("lang", null);
        } else {
            sharedPreferences
                    .edit()
                    .putString("lang", "en-EN")
                    .apply();
            language = sharedPreferences.getString("lang", null);
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

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private Call<PopularTvShows> callPopularTvShowsApi() {    //2
        return movieService.getTvSeries(
                getString(R.string.my_api_key), language,
                currentPage
        );
    }

    private List<Result> fetchResults(Response<PopularTvShows> response) {    //3
        PopularTvShows populartvShows = response.body();
        return populartvShows.getResults();
    }

    public void changeAdapter(int layout) {

        mNameList.setLayoutManager(layoutManager);
        if (layout == R.layout.cell_cards_3) {
            mNameList.setLayoutManager(new GridLayoutManager(this, 3));
        }

        adapterPagination = new PaginationTvshowAdapter(TVShowsActivity.this, this, layout);
        mNameList.setItemAnimator(new DefaultItemAnimator());
        mNameList.setAdapter(adapterPagination);
        mNameList.setHasFixedSize(true);

        mNameList.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

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

        int id = item.getItemId();

        if (id == R.id.showGrid) {
            if (showGrid % 3 == 0) {
                layoutcard = R.layout.cell_cards;
                changeLayout();
                showGrid++;
            } else if (showGrid % 3 == 1) {
                layoutcard = R.layout.cell_cards_2;
                changeLayout();
                showGrid++;
            } else if (showGrid % 3 == 2) {
                layoutcard = R.layout.cell_cards_3;
                changeLayout();
                showGrid++;
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeLayout(){

        mNameList.setLayoutManager(layoutManager);
        if (layoutcard == R.layout.cell_cards_3) {
            mNameList.setLayoutManager(new GridLayoutManager(this, 3));
        }

        mNameList.setAdapter(adapterPagination);
        mNameList.setHasFixedSize(true);
        mNameList.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

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
    }

    @Override
    public void onListItemClick(Result tvshow) {
        Intent intent = new Intent(this, MovieDetails_Activity.class);
        Bundle args = new Bundle();
        tvshow.setType("tvshow");
        args.putSerializable("result", (Serializable) tvshow);
        intent.putExtra("BUNDLE", args);
        startActivity(intent);
    }
}
