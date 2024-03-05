package com.app.githubtrending.ui.navigator;

import androidx.fragment.app.Fragment;

public interface MainNavigator {
    public void navigateTo(Fragment fragment);
    public void subNavigateTo(Fragment fragment);
    public void back();
}