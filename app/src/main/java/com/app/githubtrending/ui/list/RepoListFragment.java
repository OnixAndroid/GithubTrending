package com.app.githubtrending.ui.list;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.githubtrending.R;
import com.app.githubtrending.databinding.FragmentListBinding;
import com.app.githubtrending.domain.model.Filter;
import com.app.githubtrending.ui.details.DetailsFragment;
import com.app.githubtrending.ui.list.recycler.RecyclerViewPaginator;
import com.app.githubtrending.ui.list.recycler.RepoListAdapter;
import com.app.githubtrending.ui.list.recycler.RepoListViewHolder;
import com.app.githubtrending.ui.list.recycler.SpaceBetweenDecoration;
import com.app.githubtrending.ui.navigator.MainNavigator;
import com.app.githubtrending.ui.navigator.Router;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;

@AndroidEntryPoint
public class RepoListFragment extends Fragment {

    private ListViewModel vm;
    private FragmentListBinding binding;

    public static RepoListFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE_KEY, type);
        RepoListFragment fragment = new RepoListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private static final String TYPE_KEY = "type_key";
    public static final int TYPE_ALL = 0;
    public static final int TYPE_FAVOURITES = 1;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);

        int listType = getArguments().getInt(TYPE_KEY);
        if (listType == TYPE_ALL) {
            vm = new ViewModelProvider(this).get(TrendingViewModel.class);
        } else if (listType == TYPE_FAVOURITES) {
            vm = new ViewModelProvider(this).get(FavouritesViewModel.class);
            binding.searchBar.setVisibility(View.GONE);
            binding.filter.setVisibility(View.GONE);
        }

        binding.searchBarText.setText(vm.state.getValue().getSearchQuery());

        setupLogger();
        setBindingData();
        setupScreenStateObserver();
        setupNavigationObserver();
        vm.setupSearchQueryUpdater();

        if (vm.state.getValue().getList().isEmpty()) {
            binding.refresh.setRefreshing(true);
            vm.refresh();
        }

        setupPagination();
        setupFilter();
        setupPullRefresh();

        return binding.getRoot();
    }

    private void setupFilter() {
        List<Filter> filters = Arrays.stream(Filter.values()).collect(Collectors.toList());
        List<String> items = filters.stream()
                .map(filter -> (String) requireContext().getText(filter.filterNameRes))
                .collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.filter.setAdapter(adapter);

        binding.filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Filter selectedItem = filters.get(position);
                vm.setFilter(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }
        });
    }

    private final RepoListViewHolder.OnItemClickListener itemClickListener = new RepoListViewHolder.OnItemClickListener() {
        @Override
        public void onItemClick(long id) {
            vm.openDetails(id);
        }
    };

    private void setBindingData() {
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

    private void setupScreenStateObserver() {
        vm.state.observe(getViewLifecycleOwner(), state -> {
            if (binding.reposList.getAdapter() instanceof RepoListAdapter) {
                ((RepoListAdapter) binding.reposList.getAdapter()).submitList(state.getList());

                if (vm instanceof FavouritesViewModel) {
                    FavouritesViewModel favouritesViewModel = (FavouritesViewModel) vm;

                    favouritesViewModel.handleNextPage();
                }
            }

            if (state.getList().isEmpty() && !state.isRefreshing()) {
                binding.noItemsText.setVisibility(View.VISIBLE);
            } else {
                binding.noItemsText.setVisibility(View.GONE);
            }

            if (state.getList().isEmpty() && state.isRefreshing()) {
                binding.refresh.setRefreshing(true);
                binding.reposList.scrollToPosition(0);
            }

            if (state.getErrorMessageRes() <= 0) {
                binding.errorSurface.setVisibility(View.GONE);
                binding.errorText.setVisibility(View.GONE);
                binding.errorText.setText("");
            } else {
                binding.errorSurface.setVisibility(View.VISIBLE);
                binding.errorText.setVisibility(View.VISIBLE);
                binding.errorText.setText(state.getErrorMessageRes());
            }

            if (!state.isRefreshing()) {
                binding.refresh.setRefreshing(false);
            }

        });
    }

    private void setupPullRefresh() {
        binding.refresh.setOnRefreshListener(() -> vm.refresh());
    }

    private void setupNavigationObserver() {
        vm.navigationEvents.observe(requireActivity(), event -> {
            Router screen = event.getContentIfNotHandled();
            if (screen == null) {
                return;
            }

            if (screen instanceof Router.PopularRepos) {
                ((MainNavigator) requireActivity()).back();
            } else if (screen instanceof Router.Details) {
                long id = ((Router.Details) screen).getItemId();

                ((MainNavigator) requireActivity()).subNavigateTo(DetailsFragment.newInstance(id));
            }
        });
    }


    private void setupPagination() {

        RecyclerViewPaginator recyclerViewPaginator = new RecyclerViewPaginator(binding.reposList) {
            @Override
            public boolean isLastPage() {
                return !vm.state.getValue().hasNextPage();
            }

            @Override
            public void loadMore(Long start, Long count) {
                vm.loadMore();
            }

            @Override
            public boolean isLoading() {
                return vm.state.getValue().isLoading();
            }

            @Override
            public boolean isRefreshing() {
                return vm.state.getValue().isRefreshing() || binding.refresh.isRefreshing();
            }
        };

        binding.reposList.addOnScrollListener(recyclerViewPaginator);

    }

    private void setupLogger() {
        RxJavaPlugins.setErrorHandler(e -> {
            if (e.getLocalizedMessage() == null) {
                Log.d("TRENDING_EXCEPTION", "Unknown exception");
                return;
            }

            Log.d("TRENDING_EXCEPTION", e.getLocalizedMessage());
        });
    }
}