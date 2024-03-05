package com.app.githubtrending.ui.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.app.githubtrending.databinding.FragmentDetailsBinding;

public class DetailsFragment extends Fragment {

    public static DetailsFragment newInstance(long id) {
        DetailsFragment fragment = new DetailsFragment();
        return fragment;
    }

    private FragmentDetailsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}