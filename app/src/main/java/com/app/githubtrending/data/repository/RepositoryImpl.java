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
    public List<Boolean> getPopular() {
        return null;
    }


    @Override
    public Single<List<RepoDetailed>> getTrending(RepoListRequestParams params) {


//        List<RepoDetailed> list = new ArrayList<RepoDetailed>();
//        String name = "Username";
//        String newName = String.join(name, String.valueOf(params.getPage()));
//        String repoName = "Repo Name";
//
//        for (long i = 1; i <= 20; i++) {
//            String newRepoName = String.join(repoName, String.valueOf(params.getPage() * 20L + i));
//
//            list.add(new RepoDetailed(
//                            params.getPage() * 20L + i,
//                            newName,
//                            newRepoName,
//                            "https://cdn.icon-icons.com/icons2/640/PNG/512/android-robot-figure-avatar-brand_icon-icons.com_59128.png",
//                            "Description",
//                            100,
//                            "https://test.com",
//                            LocalDateTime.now(),
//                            200,
//                            "Kotlin"
//                    )
//            );
//        }
//
//        return Single.just(list);


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
                .map(item -> item.toRepoDetailed())
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
