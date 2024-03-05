package com.app.githubtrending.ui.list;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.githubtrending.R;
import com.app.githubtrending.databinding.FragmentListBinding;
import com.app.githubtrending.ui.details.DetailsFragment;
import com.app.githubtrending.ui.list.recycler.RepoListAdapter;
import com.app.githubtrending.ui.list.recycler.RepoListViewHolder;
import com.app.githubtrending.ui.list.recycler.SpaceBetweenDecoration;
import com.app.githubtrending.ui.navigator.MainNavigator;
import com.app.githubtrending.ui.navigator.Router;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RepoListFragment extends Fragment {

    private ListViewModel vm;
    private FragmentListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);

        setBindingData();
        setupRepoListObserver();
        setupNavigationObserver();
        vm.getTrendingRepos();

        return binding.getRoot();
    }

    private final RepoListViewHolder.OnItemClickListener itemClickListener = new RepoListViewHolder.OnItemClickListener() {
        @Override
        public void onItemClick(long id) {
            vm.openDetails(id);
        }
    };

    private void setBindingData() {
        vm = new ViewModelProvider(this).get(ListViewModel.class);

        binding.reposList.setAdapter(new RepoListAdapter(itemClickListener));
        binding.reposList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.reposList.addItemDecoration(new SpaceBetweenDecoration(R.dimen.padding_medium));
        binding.searchBar.setEndIconOnClickListener(view -> {
            if (binding.searchBar.getEditText() != null) {
                binding.searchBar.getEditText().getText().clear();
            }
        });
        if (binding.searchBar.getEditText() != null) {
            setupSearchTextListener();
        }
    }

    private void setupSearchTextListener() {
        binding.searchBar.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                vm.setSearchQuery(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed
            }
        });
    }

    private void setupRepoListObserver() {
        vm.repo.observe(getViewLifecycleOwner(), repoList -> {
            if (binding.reposList.getAdapter() instanceof RepoListAdapter) {
                ((RepoListAdapter) binding.reposList.getAdapter()).submitList(repoList);
            }
        });
    }

    private void setupNavigationObserver() {
        vm.navigationEvents.observe(requireActivity(), event -> {
            Router screen = event.getContentIfNotHandled();
            if (screen == null) {
                return;
            }

            if (screen instanceof Router.PopularRepos) {
                ((MainNavigator) requireActivity()).back();
            } else if(screen instanceof Router.Details) {
                long id = ((Router.Details) screen).getItemId();

                ((MainNavigator) requireActivity()).subNavigateTo(DetailsFragment.newInstance(id));
            }
        });
    }
}