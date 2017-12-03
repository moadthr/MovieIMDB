package com.example.moad.myapplicationtest.model;

/**
 * Created by moad on 07/11/2017.
 */

public class NavItem {
    int mTitle;
    int  mSubtitle;
    int mIcon;

    public int getmTitle() {
        return mTitle;
    }

    public int getmSubtitle() {
        return mSubtitle;
    }

    public int getmIcon() {
        return mIcon;
    }

    public NavItem(int title, int subtitle, int icon) {
        mTitle = title;
        mSubtitle = subtitle;
        mIcon = icon;
    }
}
