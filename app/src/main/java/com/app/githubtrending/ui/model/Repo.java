package com.app.githubtrending.ui.model;


public class Repo {

    public Repo(long id, String username, String repoName, String repoImageUrl, String description, int starsCount) {
        this.id = id;
        this.username = username;
        this.repoName = repoName;
        this.repoImageUrl = repoImageUrl;
        this.description = description;
        this.starsCount = starsCount;
    }


    private final long id;

    private final int starsCount;

    private String repoName;

    private String username;
    private String repoImageUrl;
    private String description;

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
}
