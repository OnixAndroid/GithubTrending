package com.app.githubtrending.ui.model;

import java.time.LocalDateTime;

public class RepoDetailed implements Repo {

    public RepoDetailed(
            long id,
            String username,
            String repoName,
            String repoImageUrl,
            String description,
            int starsCount,
            String repoUrl,
            LocalDateTime createdAt,
            long forksCount,
            String language
    ) {
        this.id = id;
        this.username = username;
        this.repoName = repoName;
        this.repoImageUrl = repoImageUrl;
        this.description = description;
        this.starsCount = starsCount;
        this.repoUrl = repoUrl;
        this.createdAt = createdAt;
        this.forksCount = forksCount;
        this.language = language;
    }

    private final long id;

    private final int starsCount;

    private final String repoName;

    private final String username;
    private final String repoImageUrl;
    private final String description;
    private final String repoUrl;
    private final LocalDateTime createdAt;
    private final long forksCount;
    private final String language;

    public long getId() {
        return id;
    }

    public String getRepoName() {
        return repoName;
    }

    public String getRepoImageUrl() {
        return repoImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }

    public int getStarsCount() {
        return starsCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public long getForksCount() {
        return forksCount;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public String getLanguage() {
        return language;
    }
}
