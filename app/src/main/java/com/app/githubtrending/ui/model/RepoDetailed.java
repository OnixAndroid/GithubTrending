package com.app.githubtrending.ui.model;

import java.time.LocalDateTime;

public class RepoDetailed {

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

    private String repoName;

    private String username;
    private String repoImageUrl;
    private String description;
    private String repoUrl;
    private LocalDateTime createdAt;
    private long forksCount;
    private String language;

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

    public long getid() {
        return id;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime value) {
        this.createdAt = value;
    }

    public long getForksCount() {
        return forksCount;
    }

    public void setForksCount(long value) {
        this.forksCount = value;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String value) {
        this.language = value;
    }
}
