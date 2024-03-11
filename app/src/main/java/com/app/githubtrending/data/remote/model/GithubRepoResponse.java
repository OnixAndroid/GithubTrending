package com.app.githubtrending.data.remote.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GithubRepoResponse {
    @SerializedName("items")
    private List<DetailsResponse> details;

    public List<DetailsResponse> getItems() { return details; }
}

class OwnerResponse {
    @SerializedName("login")
    private String login;
    @SerializedName("avatar_url")
    private String avatarUrl;

    public String getLogin() { return login; }

    public String getAvatarUrl() { return avatarUrl; }
}
