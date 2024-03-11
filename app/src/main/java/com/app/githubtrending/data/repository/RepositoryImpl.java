package com.app.githubtrending.data.repository;

import com.app.githubtrending.data.local.dao.RepoDao;
import com.app.githubtrending.data.local.model.RepoEntity;
import com.app.githubtrending.data.remote.GithubApiService;
import com.app.githubtrending.data.remote.model.DetailsResponse;
import com.app.githubtrending.domain.model.Filter;
import com.app.githubtrending.domain.model.RepoListRequestParams;
import com.app.githubtrending.domain.repository.Repository;
import com.app.githubtrending.ui.model.RepoDetailed;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RepositoryImpl implements Repository {

    private final GithubApiService remoteSource;
    private final RepoDao localSource;

    public RepositoryImpl(GithubApiService remoteSource, RepoDao localSource) {
        this.remoteSource = remoteSource;
        this.localSource = localSource;
    }


    @Override
    public Single<List<RepoDetailed>> getTrending(RepoListRequestParams params) {

        return remoteSource.searchRepositories(
                        buildQuery(params.getSearchQuery(), params.getFilter()),
                        params.getSort(),
                        params.getOrder(),
                        params.getPerPage(),
                        params.getPage()
                )
                .map(data -> data.getItems().stream().map(DetailsResponse::toRepoDetailed).collect(Collectors.toList()));

    }

    @Override
    public Maybe<RepoDetailed> getById(long id) {
        return localSource.getById(id)
                .map(RepoEntity::toRepoDetailed)
                .switchIfEmpty(
                        remoteSource.getById(id)
                                .subscribeOn(Schedulers.io())
                                .map(DetailsResponse::toRepoDetailed)
                                .toMaybe()
                );
    }

    @Override
    public Flowable<List<RepoDetailed>> getFavourites(RepoListRequestParams params) {

        return localSource.getFavourites(params.getPage() * params.getPerPage())
                .map(data ->
                        data.stream().map(RepoEntity::toRepoDetailed).collect(Collectors.toList())
                );
    }

    @Override
    public Completable addToFavourites(RepoDetailed repoItem) {
        RepoEntity entity = RepoEntity.toRepoEntity(repoItem);
        return localSource.insertAll(entity);
    }

    @Override
    public Completable deleteFromFavourite(long id) {
        return localSource.delete(id);
    }

    @Override
    public Single<Boolean> isFavourite(long id) {
        return localSource.isExists(id);
    }

    @Override
    public Single<Long> getCount() {
        return localSource.getCount();
    }

    private String buildQuery(String query, Filter filter) {
        LocalDateTime createdFrom;

        if (filter == Filter.LastMonth) {
            createdFrom = LocalDateTime.now().minusMonths(1);
        } else if (filter == Filter.LastWeek) {
            createdFrom = LocalDateTime.now().minusWeeks(1);
        } else if (filter == Filter.LastDay) {
            createdFrom = LocalDateTime.now().minusDays(1);
        } else {
            return query;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String createdFromFormatted = createdFrom.format(formatter);

        String newQuery;

        if (query.isBlank()) {
            newQuery = "created:>" + createdFromFormatted;
        } else {
            try {
                String textQuery = URLEncoder.encode(query.trim(), StandardCharsets.UTF_8.name());

                newQuery = textQuery + "+created:>" + createdFromFormatted;
            } catch (Exception e) {
                newQuery = "created:>" + createdFromFormatted;
            }
        }

        return newQuery;
    }
}
