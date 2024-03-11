package com.app.githubtrending.ui.navigator;

import androidx.fragment.app.Fragment;

public interface MainNavigator {
    void navigateTo(Fragment fragment);
    void subNavigateTo(Fragment fragment);
    void back();
}