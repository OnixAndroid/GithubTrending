package com.app.githubtrending.ui.navigator;

import androidx.fragment.app.Fragment;

public interface SubNavigator {
    void navigateTo(Fragment fragment);
    void startScreen(Fragment fragment);
    boolean canMoveBack();
}