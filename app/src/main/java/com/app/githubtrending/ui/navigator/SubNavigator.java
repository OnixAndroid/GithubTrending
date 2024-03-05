package com.app.githubtrending.ui.navigator;

import androidx.fragment.app.Fragment;

public interface SubNavigator {
    public void navigateTo(Fragment fragment);
    public void startScreen(Fragment fragment);
    public void back();
    public boolean canMoveBack();
}