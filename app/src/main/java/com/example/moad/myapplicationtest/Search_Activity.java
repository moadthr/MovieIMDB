package com.example.moad.myapplicationtest;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.moad.myapplicationtest.model.Result;
import com.example.moad.myapplicationtest.model.SearchResult;
import com.example.moad.myapplicationtest.model.TopRatedMovies;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search_Activity extends BaseDrawerActivity implements ListItemClickListener {

    Button btnsearch;
    public static RecyclerView mNameList;
    static int showGrid;
    static int layoutcard;
    PaginationSearchAdapter adapterPagination;
    ProgressBar progressBar;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    private MovieService movieService;
    static String language;
    SharedPreferences sharedPreferences;
    static String query;
    SearchView searchView;
    boolean isInitialise = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.Search);
        getLayoutInflater().inflate(R.layout.activity_search_, frameLayout);
        searchView = (SearchView) findViewById(R.id.searchview);
        btnsearch = (Button) findViewById(R.id.btnsearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                callSearch(newText);
                return true;
            }

            public void callSearch(String q) {
                query = q;
            }

        });

        layoutcard = R.layout.cell_cards_2;
        showGrid = 1;
        load();

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
        if (isInitialise) {
            changeAdapter(layoutcard);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.showGrid).setVisible(false);
        return true;
    }

    private Call<SearchResult> callTopRatedMoviesApi() {    //2
        return movieService.searchMovie(query,
                getString(R.string.my_api_key), language,
                currentPage
        );
    }

    private List<Result> fetchResults(Response<SearchResult> response) {    //3
        SearchResult searchresult = response.body();
        return searchresult.getResults();
    }


    public void changeAdapter(int layout) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mNameList.setLayoutManager(layoutManager);

        adapterPagination = new PaginationSearchAdapter(Search_Activity.this, this, layout);
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

        callTopRatedMoviesApi().enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                List<Result> results = fetchResults(response);
                if (results.size() != 0) {
                    if (currentPage == TOTAL_PAGES) {
                        adapterPagination.addAll(results);
                        isLastPage = true;
                    } else {
                        progressBar.setVisibility(View.GONE);
                        adapterPagination.addAll(results);
                        if (currentPage <= TOTAL_PAGES) adapterPagination.addLoadingFooter();
                        else isLastPage = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Toast.makeText(Search_Activity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadNextPage() {

        callTopRatedMoviesApi().enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                adapterPagination.removeLoadingFooter();
                isLoading = false;

                List<Result> results = fetchResults(response);
                adapterPagination.addAll(results);

                if (currentPage != TOTAL_PAGES)
                    adapterPagination.addLoadingFooter();
                else
                    isLastPage = true;
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                t.printStackTrace();
                // TODO: 08/11/16 handle failure
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.showGrid) {

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(Result result) {
        Intent intent = new Intent(this, MovieDetails_Activity.class);
        Bundle args = new Bundle();
        result.setType("movie");
        args.putSerializable("result", (Serializable) result);
        intent.putExtra("BUNDLE", args);
        startActivity(intent);

    }

    public void lookfor(View view) {
        isLoading = false;
        isLastPage = false;
        currentPage = 1;
        isInitialise = true;
        if (query != null && !query.equals("")) {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            changeAdapter(layoutcard);
        }

    }
}
