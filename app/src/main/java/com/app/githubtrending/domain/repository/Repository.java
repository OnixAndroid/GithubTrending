package com.app.githubtrending.domain.repository;

import com.app.githubtrending.domain.model.RepoListRequestParams;
import com.app.githubtrending.ui.model.RepoDetailed;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public interface Repository {

    Single<List<RepoDetailed>> getTrending(RepoListRequestParams params);

    Maybe<RepoDetailed> getById(long id);

    Flowable<List<RepoDetailed>> getFavourites(RepoListRequestParams params);

    Completable addToFavourites(RepoDetailed repoItem);

    Completable deleteFromFavourite(long id);

    Single<Boolean> isFavourite(long id);

    Single<Long> getCount();
}
