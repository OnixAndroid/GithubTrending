package com.app.githubtrending.ui.details;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.githubtrending.domain.common.AppSchedulers;
import com.app.githubtrending.domain.repository.Repository;
import com.app.githubtrending.ui.common.ErrorMessage;
import com.app.githubtrending.ui.model.RepoDetailed;

import java.util.Objects;

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

    @SuppressLint("CheckResult")
    public void loadRepoDetails(long id) {
        repository.getById(id)
                .subscribeOn(schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        data -> {
                            hideError();

                            DetailsScreenState state = _state.getValue();
                            assert state != null;
                            state.setRepo(data);
                            _state.setValue(state);
                            setIsFavourite();
                        },
                        this::handleError
                );
    }

    @SuppressLint("CheckResult")
    public void addToFavourites() {
        RepoDetailed repo = Objects.requireNonNull(state.getValue()).getRepo();
        if (repo != null) {
            repository.addToFavourites(repo)
                    .subscribeOn(schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::setIsFavourite);
        }
    }

    @SuppressLint("CheckResult")
    private void setIsFavourite() {
        repository.isFavourite(Objects.requireNonNull(state.getValue()).getRepo().getId())
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
        RepoDetailed repo = Objects.requireNonNull(state.getValue()).getRepo();
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
        DetailsScreenState state = _state.getValue();
        assert state != null;
        state.setErrorMessage(errorMessage);
        _state.setValue(state);
    }

    protected void hideError() {
        DetailsScreenState state = _state.getValue();
        assert state != null;
        state.setErrorMessage(new ErrorMessage.NoError());
        _state.setValue(state);
    }
}