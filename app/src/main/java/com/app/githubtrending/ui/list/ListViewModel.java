package com.app.githubtrending.ui.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.app.githubtrending.ui.common.Event;
import com.app.githubtrending.ui.model.Repo;
import com.app.githubtrending.ui.navigator.Router;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ListViewModel extends ViewModel {

    private SavedStateHandle savedStateHandle;

    @Inject
    ListViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
    }

    private final MutableLiveData<List<Repo>> _repo = new MutableLiveData<>();
    public LiveData<List<Repo>> repo = _repo;

    private final MutableLiveData<Event<Router>> _navigationEvents = new MutableLiveData<>();
    public LiveData<Event<Router>> navigationEvents = _navigationEvents;

    private final MutableLiveData<Event<Integer>> _errorEvents = new MutableLiveData<>();
    public LiveData<Event<Integer>> errorEvents = _errorEvents;

    public void openDetails(long id) {
        _navigationEvents.setValue(new Event<>(new Router.Details(id)));
    }

    public void getTrendingRepos() {
        List<Repo> list = new ArrayList<Repo>();

        for (long i = 1; i <= 20; i++) {
            list.add(new Repo(i, "Username", "Repo Name", "https://cdn.icon-icons.com/icons2/640/PNG/512/android-robot-figure-avatar-brand_icon-icons.com_59128.png", "Description", 100));
        }

        _repo.setValue(list);
    }

    public void setSearchQuery(String query) {
        // TODO
    }
}