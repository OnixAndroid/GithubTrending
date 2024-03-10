package com.app.githubtrending.ui.list;

import android.annotation.SuppressLint;

import androidx.lifecycle.SavedStateHandle;

import com.app.githubtrending.domain.common.AppSchedulers;
import com.app.githubtrending.domain.model.RepoListRequestParams;
import com.app.githubtrending.domain.repository.Repository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FavouritesViewModel extends ListViewModel {

    @Inject
    FavouritesViewModel(SavedStateHandle savedStateHandle, Repository repository, AppSchedulers schedulers) {
        super(savedStateHandle, repository, schedulers);
    }

    @Override
    public void loadMore() {
        getFavouriteRepos();
    }


    @SuppressLint("CheckResult")
    private void getFavouriteRepos() {

        ListScreenState state = _state.getValue();
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

        repository.getFavourites(params)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe(
                        data -> {
                            hideError();
                            hideLoader();

                            if (data == null || data.isEmpty()) {
                                showNoItemsMessage();
                                return;
                            }

                            ListScreenState currentState = _state.getValue();
                            currentState.setDetailedList(data);
                            currentState.setLoading(false);
                            currentState.setCurrentPage(page);

                            _state.setValue(currentState);
                            handleNextPage();
                        },
                        this::handleError
                );
    }

    @SuppressLint("CheckResult")
    @Override
    public void refresh() {

        ListScreenState state = _state.getValue();
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

        repository.getFavourites(params)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe(
                        data -> {
                            hideError();
                            hideLoader();

                            if (data == null || data.isEmpty()) {
                                showNoItemsMessage();
                                return;
                            }

                            ListScreenState currentState = _state.getValue();
                            currentState.setDetailedList(data);
                            currentState.setRefreshing(false);
                            currentState.setCurrentPage(1);

                            _state.setValue(currentState);
                            handleNextPage();
                        },
                        this::handleError
                );
    }

    @SuppressLint("CheckResult")
    void handleNextPage() {
        repository.getCount()
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe(
                        count -> {
                            ListScreenState currentState = _state.getValue();
                            currentState.setHasNextPage(currentState.getList().size() < count);

                            if (currentState.getList().size() >= count) return;

                            addBottomLoader();
                            _state.setValue(currentState);
                        },
                        this::handleError
                );
    }

}