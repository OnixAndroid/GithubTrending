package com.app.githubtrending.data.client;

import com.app.githubtrending.data.remote.GithubApiService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GithubApiClient {

    private static final String BASE_URL = "https://api.github.com/";

    public GithubApiService getGithubApiService() {
        return githubApiService;
    }

    private GithubApiService githubApiService;

    public GithubApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        githubApiService = retrofit.create(GithubApiService.class);
    }
}