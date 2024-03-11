package com.app.githubtrending.ui.list;

import android.annotation.SuppressLint;

import com.app.githubtrending.domain.common.AppSchedulers;
import com.app.githubtrending.domain.model.RepoListRequestParams;
import com.app.githubtrending.domain.repository.Repository;
import com.app.githubtrending.ui.model.RepoDetailed;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TrendingViewModel extends ListViewModel {

    @Inject
    TrendingViewModel(Repository repository, AppSchedulers schedulers) {
        super(repository, schedulers);
    }

    @Override
    public void loadMore() {
        getTrendingRepos();
    }


    @SuppressLint("CheckResult")
    private void getTrendingRepos() {

        ListScreenState state = _state.getValue();
        assert state != null;
        int perPage = state.getItemsPerPage();
        int page = state.getCurrentPage() + 1;

        RepoListRequestParams params = new RepoListRequestParams(
                state.getSearchQuery(),
                perPage, page,
                state.getSort(),
                state.getOrder(),
                state.getSelectedFilter()
        );

        state.setLoading(true);
        _state.setValue(state);

        repository.getTrending(params)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe(
                        data -> {
                            hideError();
                            hideLoader();

                            if (data == null || data.isEmpty()) {
                                handleInputList(data);
                                setHasNextPage(false);
                                return;
                            }

                            List<RepoDetailed> list = _state.getValue().getRepoList();
                            ArrayList<RepoDetailed> newList = new ArrayList<>(list);
                            newList.addAll(data);
                            ListScreenState currentState = _state.getValue();
                            currentState.setDetailedList(newList);
                            _state.setValue(currentState);

                            state.setLoading(false);
                            state.setCurrentPage(page);
                            state.setHasNextPage(data.size() >= state.getItemsPerPage());

                            _state.setValue(state);
                            addBottomLoader();
                        },
                        this::handleError
                );
    }

    private void handleInputList(List<RepoDetailed> data) {
        if(data == null && Objects.requireNonNull(_state.getValue()).getList().isEmpty()) {
            showNoItemsMessage();
        }

        assert data != null;
        if(data.isEmpty() && Objects.requireNonNull(_state.getValue()).getList().isEmpty()) {
            showNoItemsMessage();
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void refresh() {

        ListScreenState state = _state.getValue();
        assert state != null;
        int perPage = state.getItemsPerPage();
        int page = 1;

        RepoListRequestParams params = new RepoListRequestParams(
                state.getSearchQuery(),
                perPage,
                page,
                state.getSort(),
                state.getOrder(),
                state.getSelectedFilter()
        );

        state.setRefreshing(true);
        _state.setValue(state);
        setHasNextPage(true);

        repository.getTrending(params)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe(
                        data -> {
                            hideError();
                            hideLoader();

                            if (data == null || data.isEmpty()) {
                                handleInputList(data);
                                setHasNextPage(false);
                                return;
                            }

                            ListScreenState currentState = _state.getValue();
                            currentState.setDetailedList(data);
                            currentState.setRefreshing(false);
                            currentState.setCurrentPage(page);
                            currentState.setHasNextPage(data.size() >= currentState.getItemsPerPage());

                            _state.setValue(currentState);
                            addBottomLoader();
                        },
                        this::handleError
                );
    }
}