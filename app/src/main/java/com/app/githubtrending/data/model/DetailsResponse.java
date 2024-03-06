package com.app.githubtrending.data.model;

import com.app.githubtrending.ui.model.RepoDetailed;
import com.app.githubtrending.ui.model.RepoItem;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DetailsResponse {


    public RepoItem toRepo() {
        return new RepoItem(this.id, this.owner.getLogin(), this.name, this.owner.getAvatarurl(), this.description, this.stargazersCount);
    }

    public RepoDetailed toRepoDetailed() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");

        LocalDateTime dateTime = LocalDateTime.parse(createdAt, formatter);

        return new RepoDetailed(
                this.id,
                this.owner.getLogin(),
                this.name,
                this.owner.getAvatarurl(),
                this.description,
                this.stargazersCount,
                this.url,
                dateTime,
                forksCount,
                language
        );
    }
    @SerializedName("id")
    private long id;

    @SerializedName("stargazers_count")
    private int stargazersCount;
    @SerializedName("forks")
    private long forks;
    @SerializedName("html_url")
    private String url;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("owner")
    private OwnerResponse owner;
    @SerializedName("forks_count")
    private long forksCount;
    @SerializedName("language")
    private String language;

    public long getStargazersCount() {
        return stargazersCount;
    }

    public void setStargazersCount(int value) {
        this.stargazersCount = value;
    }

    public long getid() {
        return id;
    }

    public void setid(long value) {
        this.id = value;
    }

    public long getForks() {
        return forks;
    }

    public void setForks(long value) {
        this.forks = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String value) {
        this.createdAt = value;
    }

    public OwnerResponse getOwner() {
        return owner;
    }

    public void setOwner(OwnerResponse value) {
        this.owner = value;
    }

    public long getForksCount() {
        return forksCount;
    }

    public void setForksCount(long value) {
        this.forksCount = value;
    }

    public String getUrl() {
        return url;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String value) {
        this.language = value;
    }
}
