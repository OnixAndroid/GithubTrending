package com.app.githubtrending.data.repository;

import com.app.githubtrending.data.client.GithubApiClient;
import com.app.githubtrending.data.model.DetailsResponse;
import com.app.githubtrending.data.remote.GithubApiService;
import com.app.githubtrending.domain.model.RepoListRequestParams;
import com.app.githubtrending.domain.repository.Repository;
import com.app.githubtrending.ui.model.RepoDetailed;
import com.app.githubtrending.ui.model.RepoItem;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Single;

public class RepositoryImpl implements Repository {

    GithubApiClient gitHubApiClient = new GithubApiClient();
    GithubApiService githubApiService = gitHubApiClient.getGithubApiService();

    @Override
    public List<Boolean> getPopular() {
        return null;
    }


    @Override
    public Single<List<RepoItem>> getTrending(RepoListRequestParams params) {


//        List<RepoItem> list = new ArrayList<RepoItem>();
//        String name = "Username";
//        String newName = String.join(name, String.valueOf(params.getPage()));
//        String repoName = "Repo Name";
//
//        for (long i = 1; i <= 20; i++) {
//            String newRepoName = String.join(repoName, String.valueOf(params.getPage() * 20L + i));
//
//            list.add(new RepoItem(params.getPage() * 20L + i, newName, newRepoName, "https://cdn.icon-icons.com/icons2/640/PNG/512/android-robot-figure-avatar-brand_icon-icons.com_59128.png", "Description", 100));
//        }
//
//        return Single.just(list);


        return githubApiService.searchRepositories(
                        params.getSearchQuery(),
                        params.getSort(),
                        params.getOrder(),
                        params.getPerPage(),
                        params.getPage()
                )
                .map(data -> data.getItems().stream().map(DetailsResponse::toRepo).collect(Collectors.toList()));

    }

    @Override
    public Single<RepoDetailed> getById(long id) {

        return githubApiService.getById(id).map(DetailsResponse::toRepoDetailed);
    }
}
