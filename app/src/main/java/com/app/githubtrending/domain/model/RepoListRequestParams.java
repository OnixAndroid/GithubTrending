package com.app.githubtrending.domain.model;

public class RepoListRequestParams {

    private final String searchQuery;
    private final int perPage;
    private final int page;
    private final String sort;
    private final String order;

    public RepoListRequestParams(String searchQuery, int perPage, int page, String sort, String order) {
        this.searchQuery = searchQuery;
        this.page = page;
        this.perPage = perPage;
        this.sort = sort;
        this.order = order;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public int getPerPage() {
        return perPage;
    }

    public int getPage() {
        return page;
    }

    public String getSort() {
        return sort;
    }

    public String getOrder() {
        return order;
    }
}
