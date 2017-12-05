package com.example.moad.myapplicationtest;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by moad on 25/11/2017.
 */

public abstract class PaginationScrollListener  extends RecyclerView.OnScrollListener {

    LinearLayoutManager layoutManager;
    GridLayoutManager layoutGridManager ;

    public PaginationScrollListener(LinearLayoutManager layoutManager) {

        this.layoutManager = layoutManager;
        if(MainActivity.layoutcard == R.layout.cell_cards_3){
            this.layoutManager = (GridLayoutManager) MainActivity.mNameList.getLayoutManager();
        }
        if(TVShowsActivity.layoutcard == R.layout.cell_cards_3)
            this.layoutManager = (GridLayoutManager) TVShowsActivity.mNameList.getLayoutManager();
    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                loadMoreItems();
            }
        }
    }

    protected abstract void loadMoreItems();

    public abstract int getTotalPageCount();

    public abstract boolean isLastPage();

    public abstract boolean isLoading();
}