package com.app.githubtrending.ui.list;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.githubtrending.domain.common.AppSchedulers;
import com.app.githubtrending.domain.common.Event;
import com.app.githubtrending.domain.model.Filter;
import com.app.githubtrending.domain.repository.Repository;
import com.app.githubtrending.ui.common.ErrorMessage;
import com.app.githubtrending.ui.model.Repo;
import com.app.githubtrending.ui.model.RepoLoader;
import com.app.githubtrending.ui.navigator.Router;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.subjects.PublishSubject;
import retrofit2.HttpException;


abstract class ListViewModel extends ViewModel {

    protected final Repository repository;

    protected final AppSchedulers schedulers;

    ListViewModel(Repository repository, AppSchedulers schedulers) {
        this.repository = repository;
        this.schedulers = schedulers;
    }

    protected final MutableLiveData<ListScreenState> _state = new MutableLiveData<>(new ListScreenState());
    @NotNull public final LiveData<ListScreenState> state = _state;

    protected final MutableLiveData<Event<Router>> _navigationEvents = new MutableLiveData<>();
    public final LiveData<Event<Router>> navigationEvents = _navigationEvents;

    public void openDetails(long id) {
        _navigationEvents.setValue(new Event<>(new Router.Details(id)));
    }

    protected void hideLoader() {
        ListScreenState state = _state.getValue();
        assert state != null;
        List<Repo> list = state.getRepoList().stream()
                .map(item -> (Repo) item).collect(Collectors.toList());
        state.setList(list);
        state.setLoading(false);
        state.setRefreshing(false);
        _state.setValue(state);
    }

    protected void setHasNextPage(boolean value) {
        ListScreenState state = _state.getValue();
        assert state != null;
        state.setHasNextPage(value);
        _state.setValue(state);
    }

    public void addBottomLoader() {
        ListScreenState state = _state.getValue();

        assert state != null;
        ArrayList<Repo> repos = new ArrayList<>(state.getRepoList());

        if (!state.hasNextPage()) {
            state.setList(repos);
        } else {
            if (state.getList().get(state.getList().size() - 1) instanceof RepoLoader) return;
            repos.add(new RepoLoader());
        }

        state.setList(repos);
        _state.setValue(state);
    }

    protected void showNoItemsMessage() {
        ListScreenState currentState = _state.getValue();
        assert currentState != null;
        currentState.setList(List.of());
        _state.setValue(currentState);
    }

    abstract public void loadMore();

    private final PublishSubject<String> searchQuery = PublishSubject.create();

    @SuppressLint("CheckResult")
    public final void setupSearchQueryUpdater() {
        searchQuery.debounce(600, TimeUnit.MILLISECONDS)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe(query -> {
                    ListScreenState state = _state.getValue();
                    assert state != null;
                    state.setSearchQuery(query);
                    state.setCurrentPage(0);
                    state.setList(List.of());
                    _state.setValue(state);

                    refresh();
                });
    }


    public void setSearchQuery(String query) {
        if (Objects.equals(query, Objects.requireNonNull(state.getValue()).getSearchQuery())) {
            return;
        }
        searchQuery.onNext(query);
    }

    public void setFilter(Filter selectedFilter) {
        ListScreenState state = _state.getValue();
        assert state != null;
        if (state.getSelectedFilter() == selectedFilter) {
            return;
        }
        state.setList(List.of());
        state.setCurrentPage(0);
        state.setSelectedFilter(selectedFilter);
        _state.setValue(state);

        refresh();
    }

    void handleError(Throwable throwable) {
        if (throwable instanceof HttpException e) {
            if (e.code() == 403) {
                showError(new ErrorMessage.ApiLimit());
            } else {
                showError(new ErrorMessage.HttpError(e.code()));
            }
        } else {
            showError(new ErrorMessage.UnknownError());
        }
    }

    protected void showError(ErrorMessage errorMessage) {
        ListScreenState state = _state.getValue();
        assert state != null;
        state.setErrorMessage(errorMessage);
        _state.setValue(state);
    }

    protected void hideError() {
        ListScreenState state = _state.getValue();
        assert state != null;
        state.setErrorMessage(new ErrorMessage.NoError());
        _state.setValue(state);
    }

    public abstract void refresh();
}