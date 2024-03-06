package com.app.githubtrending.ui.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.githubtrending.databinding.FragmentDetailsBinding;
import com.app.githubtrending.ui.model.RepoDetailed;
import com.app.githubtrending.ui.navigator.MainNavigator;
import com.app.githubtrending.ui.navigator.Router;

import java.time.format.DateTimeFormatter;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DetailsFragment extends Fragment {

    private static final String REPO_ID_KEY = "REPO_ID_KEY";

    public static DetailsFragment newInstance(long id) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putLong(REPO_ID_KEY, id);
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentDetailsBinding binding;

    private DetailsViewModel vm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(inflater, container, false);

        long repoId = getArguments() != null ? getArguments().getLong(REPO_ID_KEY, -1) : -1;
        if (repoId == -1) {
            throw new IllegalArgumentException("Repo ID arg is missing.");
        }

        vm = new ViewModelProvider(this).get(DetailsViewModel.class);

        setupNavigationObserver();
        setupErrorObserver();
        setupRepoObserver();
        vm.loadRepoDetails(repoId);

        return binding.getRoot();
    }

    private void setupNavigationObserver() {
        vm.navigationEvents.observe(requireActivity(), event -> {
            Router screen = event.getContentIfNotHandled();
            if (screen == null) {
                return;
            }

            if (screen instanceof Router.PopularRepos) {
                ((MainNavigator) requireActivity()).back();
            }
        });
    }

    private void setupErrorObserver() {
        vm.errorEvents.observe(getViewLifecycleOwner(), event -> {
            Integer errorRes = event.getContentIfNotHandled();

            if (errorRes == null) {
                return;
            }

            Toast.makeText(requireContext(), getString(errorRes), Toast.LENGTH_SHORT).show();
        });
    }

    private void setupRepoObserver() {
        vm.repo.observe(getViewLifecycleOwner(), repo -> {
            if (repo == null) {
                return;
            }
            setBindingData(repo);
        });
    }

    private void setBindingData(RepoDetailed repo) {
        bindImage(repo.getRepoImageUrl());
        binding.repoName.setText(repo.getRepoName());
        binding.username.setText(repo.getUsername());
        binding.starsCount.setText(String.valueOf(repo.getStarsCount()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedString = repo.getCreatedAt().format(formatter);
        binding.createdAtValue.setText(formattedString);

        binding.languageValue.setText(repo.getLanguage());
        binding.forksValue.setText(String.valueOf(repo.getForksCount()));
        binding.repoUrl.setText(repo.getRepoUrl());
        binding.description.setText(repo.getDescription());
    }

    private void bindImage(String imageUrl) {
        ImageLoader imageLoader = Coil.imageLoader(requireContext());

        ImageRequest request = new ImageRequest.Builder(requireContext())
                .data(imageUrl)
                .crossfade(true)
                .target(binding.repoImage)
                .build();
        imageLoader.enqueue(request);
    }
}