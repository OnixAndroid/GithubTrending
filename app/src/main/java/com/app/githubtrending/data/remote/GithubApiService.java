package com.app.githubtrending.data.remote;

import com.app.githubtrending.data.remote.model.DetailsResponse;
import com.app.githubtrending.data.remote.model.GithubRepoResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GithubApiService {
    @GET("search/repositories")
    Single<GithubRepoResponse> searchRepositories(
            @Query(value = "q", encoded = true) String query,
            @Query("sort") String sort,
            @Query("order") String order,
            @Query("per_page") int perPage,
            @Query("page") int page
    );

    @GET("repositories/{id}")
    Single<DetailsResponse> getById(
            @Path("id") long id
    );
}