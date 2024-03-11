package com.app.githubtrending.ui.details;

import com.app.githubtrending.ui.common.ErrorMessage;
import com.app.githubtrending.ui.model.RepoDetailed;

public class DetailsScreenState {

    private boolean isFavourite = false;

    private RepoDetailed repo = null;

    private ErrorMessage errorMessage = new ErrorMessage.NoError();

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

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }
}
