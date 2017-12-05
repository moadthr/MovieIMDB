package com.example.moad.myapplicationtest;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.moad.myapplicationtest.model.NavItem;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class BaseDrawerActivity extends AppCompatActivity {

    protected DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    FrameLayout frameLayout;
    DrawerListAdapter adapter;
    boolean openedDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base_drawer);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setLogo(R.drawable.ic_view_headline_black_24dp);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_view_headline_black_24dp);
        openedDrawer = true;

        frameLayout = (FrameLayout) findViewById(R.id.content_frame);

        mNavItems.add(new NavItem(R.string.Home, R.string.HomeDetails, R.drawable.ic_home_black_24dp));
        mNavItems.add(new NavItem(R.string.TvShows, R.string.TvShowsDetails, R.drawable.ic_live_tv_black_24dp));
        mNavItems.add(new NavItem(R.string.Setting, R.string.SettingDetails, R.drawable.ic_settings_black_24dp));
        mNavItems.add(new NavItem(R.string.Search, R.string.SearchDetails, R.drawable.ic_search_black_24dp));
        mNavItems.add(new NavItem(R.string.Favoris, R.string.FavorisDetails, R.drawable.ic_favorite_black_24dp));
        mNavItems.add(new NavItem(R.string.About, R.string.AboutDetails, R.drawable.ic_info_black_24dp));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });

    }

    public void load() {
        adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);
    }

    private void selectItemFromDrawer(int position) {

        if (mNavItems.get(position).getmTitle() == R.string.Setting) {
            Intent intent = new Intent(this, SettingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        if (mNavItems.get(position).getmTitle() == R.string.Home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            startActivity(intent);
        }
        if (mNavItems.get(position).getmTitle() == R.string.TvShows) {
            Intent intent = new Intent(this, TVShowsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            startActivity(intent);
        }
        if (mNavItems.get(position).getmTitle() == R.string.Search) {
            Intent intent = new Intent(this, Search_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            startActivity(intent);
        }
        if (mNavItems.get(position).getmTitle() == R.string.Favoris) {
            Intent intent = new Intent(this, Favoris_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            startActivity(intent);
        }
        if (mNavItems.get(position).getmTitle() == R.string.About) {
            Intent intent = new Intent(this, AboutActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            startActivity(intent);
        }
        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }


    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            if (openedDrawer)
                mDrawerLayout.openDrawer(mDrawerPane);
            else
                mDrawerLayout.closeDrawer(mDrawerPane);
            openedDrawer = !openedDrawer;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START))
            mDrawerLayout.closeDrawer(Gravity.START);
        else
            super.onBackPressed();
    }
}
