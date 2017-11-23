package com.example.moad.myapplicationtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.moad.myapplicationtest.model.Movie;
import com.example.moad.myapplicationtest.model.NavItem;
import com.example.moad.myapplicationtest.model.Result;
import com.example.moad.myapplicationtest.model.TopRated;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class MainActivity extends AppCompatActivity  implements ListItemClickListener{

    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private RecyclerViewAdapter recyclerViewAdapter ;
    private RecyclerView mNameList ;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    static int  showGrid = 1   ;
    static int layoutcard ;
    TopRated topRated ;
    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        layoutcard = R.layout.cell_cards;
        showGrid = 1 ;


        mNavItems.add(new NavItem("Home", "Movies", R.drawable.ic_home_black_24dp));
        mNavItems.add(new NavItem("TvSeries", "series", R.drawable.ic_settings_applications_black_24dp));
        mNavItems.add(new NavItem("Setting", "change your preferences", R.drawable.ic_dashboard_black_24dp));
        mNavItems.add(new NavItem("About", "Know more about us", R.drawable.ic_dashboard_black_24dp));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //    selectItemFromDrawer(position);
            }
        });

        mNameList = (RecyclerView) findViewById(R.id.rv_names);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mNameList.setLayoutManager(layoutManager);

        okhtttp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void okhtttp (){

        RestAdapter retrofit = new RestAdapter.Builder()
                .setEndpoint("https://api.themoviedb.org/3")
                .build();

        apiService api = retrofit.create(apiService.class);

        api.getTopRated(new retrofit.Callback<TopRated>() {
            @Override
            public void success(TopRated res, retrofit.client.Response response) {
                topRated  =res ;
                changeAdapter(layoutcard,res);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
            }


        });
    }

    public void changeAdapter (int layout , TopRated reponse){



        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mNameList.setLayoutManager(layoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this,reponse.getResults().size(),reponse.getResults(),layout);
        mNameList.setAdapter(recyclerViewAdapter);
        mNameList.setHasFixedSize(true);
        if(layout == R.layout.cell_cards_3){
            mNameList.setLayoutManager(new GridLayoutManager(this, 3));
        }

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
                changeAdapter(layoutcard,topRated);
                showGrid++;
                return true;
            }
            else if(showGrid%3 == 1){
                layoutcard = R.layout.cell_cards_2;
               changeAdapter(layoutcard,topRated);
                showGrid++;
                return true;
            }
            else if(showGrid%3 == 2) {
                layoutcard = R.layout.cell_cards_3;
                changeAdapter(layoutcard,topRated);
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
        args.putSerializable("movie",(Serializable)movie);
        intent.putExtra("BUNDLE",args);
        //intent.putStringArrayListExtra(EXTRA_CARS,cars);
        startActivity(intent);
    }
}
