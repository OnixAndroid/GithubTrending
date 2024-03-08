package com.app.githubtrending.ui.details;

import androidx.annotation.StringRes;

import com.app.githubtrending.ui.model.RepoDetailed;

public class DetailsScreenState {

    private boolean isFavourite = false;

    private RepoDetailed repo = null;
    @StringRes
    private int errorMessageRes = 0;

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public RepoDetailed getRepo() {
        return repo;
    }

    public void setRepo(RepoDetailed repo) {
        this.repo = repo;
    }

    public int getErrorMessageRes() {
        return errorMessageRes;
    }

    public void setErrorMessageRes(@StringRes int errorMessageRes) {
        this.errorMessageRes = errorMessageRes;
    }
}
