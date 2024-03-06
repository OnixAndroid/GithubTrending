package com.app.githubtrending.domain.repository;

import com.app.githubtrending.domain.model.RepoListRequestParams;
import com.app.githubtrending.ui.model.RepoDetailed;
import com.app.githubtrending.ui.model.RepoItem;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public interface Repository {

    public List<Boolean> getPopular();

    Single<List<RepoItem>> getTrending(RepoListRequestParams params);

    Single<RepoDetailed> getById(long id);
}
