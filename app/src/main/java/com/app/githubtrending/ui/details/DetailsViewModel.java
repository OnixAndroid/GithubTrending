package com.app.githubtrending.ui.details;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.githubtrending.R;
import com.app.githubtrending.domain.common.AppSchedulers;
import com.app.githubtrending.domain.common.Event;
import com.app.githubtrending.domain.repository.Repository;
import com.app.githubtrending.ui.model.RepoDetailed;
import com.app.githubtrending.ui.navigator.Router;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import retrofit2.HttpException;

@HiltViewModel
public class DetailsViewModel extends ViewModel {

    private final Repository repository;

    private final AppSchedulers schedulers;

    @Inject
    DetailsViewModel(Repository repository, AppSchedulers schedulers) {
        this.repository = repository;
        this.schedulers = schedulers;
    }

    private final MutableLiveData<DetailsScreenState> _state = new MutableLiveData<>(new DetailsScreenState());
    public final LiveData<DetailsScreenState> state = _state;

    private final MutableLiveData<Event<Router>> _navigationEvents = new MutableLiveData<>();
    public final LiveData<Event<Router>> navigationEvents = _navigationEvents;

    private final MutableLiveData<Event<Integer>> _errorEvents = new MutableLiveData<>();
    public final LiveData<Event<Integer>> errorEvents = _errorEvents;

    @SuppressLint("CheckResult")
    public void loadRepoDetails(long id) {
        repository.getById(id)
                .subscribeOn(schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        data -> {
                            hideError();

                            DetailsScreenState state = _state.getValue();
                            state.setRepo(data);
                            _state.setValue(state);
                            setIsFavourite();
                        },
                        this::handleError
                );
    }

    @SuppressLint("CheckResult")
    public void addToFavourites() {
        RepoDetailed repo = state.getValue().getRepo();
        if (repo != null) {
            repository.addToFavourites(repo)
                    .subscribeOn(schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::setIsFavourite);
        }
    }

    @SuppressLint("CheckResult")
    private void setIsFavourite() {
        repository.isFavourite(state.getValue().getRepo().getId())
                .subscribeOn(schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    DetailsScreenState screenState = state.getValue();
                    screenState.setFavourite(data);
                    _state.setValue(screenState);
                });
    }

    @SuppressLint("CheckResult")
    public void removeFromFavourites() {
        RepoDetailed repo = state.getValue().getRepo();
        if (repo != null) {
            repository.deleteFromFavourite(repo.getId())
                    .subscribeOn(schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                                repository.isFavourite(1)
                                        .subscribeOn(schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(data -> {
                                            DetailsScreenState screenState = state.getValue();
                                            screenState.setFavourite(data);
                                            _state.setValue(screenState);
                                        });
                            },
                            this::handleError);
        }
    }

    private void handleError(Throwable throwable) {
        if (throwable instanceof HttpException) {
            HttpException e = (HttpException) throwable;
            if (e.code() == 403) {
                showError(R.string.api_request_limit_error);
            } else {
                showError(R.string.http_error);
            }
        } else {
            showError(R.string.http_error);
        }
    }

    protected void showError(int stringRes) {
        DetailsScreenState state = _state.getValue();
        state.setErrorMessageRes(stringRes);
        _state.setValue(state);
    }

    protected void hideError() {
        DetailsScreenState state = _state.getValue();
        state.setErrorMessageRes(0);
        _state.setValue(state);
    }


    public void moveBack() {
        _navigationEvents.setValue(new Event<>(new Router.PopularRepos()));
    }
}