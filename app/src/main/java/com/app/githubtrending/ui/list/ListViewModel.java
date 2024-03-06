package com.app.githubtrending.ui.list;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.app.githubtrending.domain.common.AppSchedulers;
import com.app.githubtrending.domain.common.Event;
import com.app.githubtrending.domain.model.RepoListRequestParams;
import com.app.githubtrending.domain.repository.Repository;
import com.app.githubtrending.ui.model.RepoItem;
import com.app.githubtrending.ui.navigator.Router;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;

@HiltViewModel
public class ListViewModel extends ViewModel {

    private SavedStateHandle savedStateHandle;

    private final Repository repository;

    private final AppSchedulers schedulers;

    @Inject
    ListViewModel(SavedStateHandle savedStateHandle, Repository repository, AppSchedulers schedulers) {
        this.savedStateHandle = savedStateHandle;
        this.repository = repository;
        this.schedulers = schedulers;
    }

    private final MutableLiveData<List<RepoItem>> _repo = new MutableLiveData<>(new ArrayList<>());

    @NotNull public final LiveData<List<RepoItem>> repo = _repo;

    private final MutableLiveData<ListScreenState> _state = new MutableLiveData<>(new ListScreenState());
    @NotNull public final LiveData<ListScreenState> state = _state;

    private final MutableLiveData<Event<Router>> _navigationEvents = new MutableLiveData<>();
    public final LiveData<Event<Router>> navigationEvents = _navigationEvents;

    private final MutableLiveData<Event<Integer>> _errorEvents = new MutableLiveData<>();
    public final LiveData<Event<Integer>> errorEvents = _errorEvents;

    public void openDetails(long id) {
        _navigationEvents.setValue(new Event<>(new Router.Details(id)));
    }

    @SuppressLint("CheckResult")
    public void getTrendingRepos() {
        String query = "created:>2024-02-01";
        String sort = "stars";
        String order = "desc";
        int perPage = state.getValue().getItemsPerPage();
        int page = state.getValue().getCurrentPage() + 1;

        RepoListRequestParams params = new RepoListRequestParams(query, perPage, page, sort, order);

        RxJavaPlugins.setErrorHandler(e -> { });

        ListScreenState state = _state.getValue();
        state.setLoading(true);
        _state.setValue(state);

        repository.getTrending(params)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe(
                        data -> {

                            if(data == null) return;
                            List<RepoItem> list = _repo.getValue();
                            ArrayList<RepoItem> newList = new ArrayList<>(list);
                            newList.addAll(data);
                            _repo.setValue(newList);

                            state.setLoading(false);
                            state.setCurrentPage(page);
                            state.setHasNextPage(data.size() >= state.getItemsPerPage());

                            _state.setValue(state);
                        },
                        throwable -> {
                            Log.d("TEST", "ERROR");
                        }
                );
    }

    public void setSearchQuery(String query) {
        // TODO
    }
}