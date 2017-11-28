package com.example.moad.myapplicationtest;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.moad.myapplicationtest.model.NavItem;

import java.util.ArrayList;

public class BaseDrawerActivity extends AppCompatActivity {

    private ActionBarDrawerToggle mDrawerToggle;
    protected DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base_drawer);

        frameLayout = (FrameLayout) findViewById(R.id.content_frame);

        mNavItems.add(new NavItem("Home", "Movies", R.drawable.ic_home_black_24dp));
        mNavItems.add(new NavItem("TvShows", "series", R.drawable.ic_live_tv_black_24dp));
        mNavItems.add(new NavItem("Setting", "change your preferences", R.drawable.ic_settings_black_24dp));
        mNavItems.add(new NavItem("Search", "Look for a movie", R.drawable.ic_search_black_24dp));
        mNavItems.add(new NavItem("About", "Know more about us", R.drawable.ic_info_black_24dp));

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
                selectItemFromDrawer(position);
            }
        });

    }

    private void selectItemFromDrawer(int position) {

        if( mNavItems.get(position).getmTitle().equals("Setting")){
            Intent intent = new Intent(this,SettingActivity.class);
            intent.putExtra("activity","Setting");
            startActivity(intent);
            finish();
        }
        if( mNavItems.get(position).getmTitle().equals("Home")){
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("activity","MovieDetails_activity");
            startActivity(intent);
            finish();
        }
        if( mNavItems.get(position).getmTitle().equals("TvShows")){
            Intent intent = new Intent(this,TVShowsActivity.class);
            intent.putExtra("activity","TvShowsActivity");
            startActivity(intent);
            finish();
        }
        if( mNavItems.get(position).getmTitle().equals("Search")){
            Intent intent = new Intent(this,Search_Activity.class);
            intent.putExtra("activity","Search_Activity");
            startActivity(intent);
            finish();
        }

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
