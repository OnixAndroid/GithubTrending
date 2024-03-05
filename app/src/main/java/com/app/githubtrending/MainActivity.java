package com.app.githubtrending;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.app.githubtrending.databinding.ActivityMainBinding;
import com.app.githubtrending.ui.navigator.MainNavigator;
import com.app.githubtrending.ui.navigator.SubNavigator;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements MainNavigator {

    private SubNavigator navigator;

    @Override
    public void navigateTo(Fragment fragment) {
        navigator.startScreen(fragment);
    }

    @Override
    public void subNavigateTo(Fragment fragment) {
        navigator.navigateTo(fragment);
    }

    @Override
    public void back() {
        getOnBackPressedDispatcher().onBackPressed();
    }


    private void setupOnBackPressBehavior() {
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (navigator.canMoveBack()) {
                    return;
                }
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    finish();
                }
            }
        };

        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    private SubNavigator getNavigator() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof SubNavigator) {
                return (SubNavigator) fragment;
            }
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navigator = getNavigator();

        setupOnBackPressBehavior();
    }
}