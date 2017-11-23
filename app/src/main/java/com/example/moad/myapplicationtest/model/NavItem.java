package com.example.moad.myapplicationtest.model;

/**
 * Created by moad on 07/11/2017.
 */

public class NavItem {
    String mTitle;
    String mSubtitle;
    int mIcon;

    public String getmTitle() {
        return mTitle;
    }

    public String getmSubtitle() {
        return mSubtitle;
    }

    public int getmIcon() {
        return mIcon;
    }

    public NavItem(String title, String subtitle, int icon) {
        mTitle = title;
        mSubtitle = subtitle;
        mIcon = icon;
    }
}
