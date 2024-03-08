package com.app.githubtrending.ui.list.pager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class RepoPagerAdapter extends FragmentStateAdapter {
    public RepoPagerAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return PagerTab.Trending.getFragment();
        } else if (position == 1) {
            return PagerTab.Favourites.getFragment();
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int getItemCount() {
        return PagerTab.values().length;
    }


}