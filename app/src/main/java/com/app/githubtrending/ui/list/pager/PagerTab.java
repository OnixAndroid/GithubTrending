package com.app.githubtrending.ui.list.pager;

import androidx.fragment.app.Fragment;

import com.app.githubtrending.R;
import com.app.githubtrending.ui.list.RepoListFragment;

enum PagerTab {
    Trending(R.string.trending_tab_name),
    Favourites(R.string.favourites_tab_name);
    
    PagerTab(int tabNameRes) {
        this.tabNameRes = tabNameRes;
    }

    private final int tabNameRes;

    public Fragment getFragment() {
        if (this == PagerTab.Trending) {
            return RepoListFragment.newInstance(RepoListFragment.TYPE_ALL);
        } else if (this == PagerTab.Favourites) {
            return RepoListFragment.newInstance(RepoListFragment.TYPE_FAVOURITES);
        } else {
            throw new IllegalArgumentException();
        }
    }
    public int getTabName() {
        return tabNameRes;
    }

    
}
