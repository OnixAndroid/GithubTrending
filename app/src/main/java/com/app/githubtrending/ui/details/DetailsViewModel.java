package com.app.githubtrending.ui.details;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.githubtrending.domain.common.AppSchedulers;
import com.app.githubtrending.domain.common.Event;
import com.app.githubtrending.domain.repository.Repository;
import com.app.githubtrending.ui.model.RepoDetailed;
import com.app.githubtrending.ui.navigator.Router;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class DetailsViewModel extends ViewModel {

    private final Repository repository;

    private final AppSchedulers schedulers;

    @Inject
    DetailsViewModel(Repository repository, AppSchedulers schedulers) {
        this.repository = repository;
        this.schedulers = schedulers;
    }

    private final MutableLiveData<RepoDetailed> _repo = new MutableLiveData<>();
    public final LiveData<RepoDetailed> repo = _repo;

    private final MutableLiveData<Event<Router>> _navigationEvents = new MutableLiveData<>();
    public final LiveData<Event<Router>> navigationEvents = _navigationEvents;

    private final MutableLiveData<Event<Integer>> _errorEvents = new MutableLiveData<>();
    public final LiveData<Event<Integer>> errorEvents = _errorEvents;

    public void loadRepoDetails(long id) {
        repository.getById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        data -> _repo.setValue(data),
                        throwable -> Log.d("TEST", "ERROR")
                );
    }

    public void addToFavourites() {
        RepoDetailed repo = this.repo.getValue();
        if (repo != null) {
            // TODO
        }
    }

    public void moveBack() {
        _navigationEvents.setValue(new Event<>(new Router.PopularRepos()));
    }
}