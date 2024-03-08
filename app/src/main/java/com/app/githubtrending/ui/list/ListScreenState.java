package com.app.githubtrending.ui.list;

import androidx.annotation.StringRes;

import com.app.githubtrending.domain.model.Filter;
import com.app.githubtrending.ui.model.Repo;
import com.app.githubtrending.ui.model.RepoDetailed;

import java.util.List;
import java.util.stream.Collectors;

public class ListScreenState {
    private List<Repo> list = List.of();
    private int currentPage = 0;
    private final int itemsPerPage = 20;
    private boolean hasNextPage = true;
    private boolean isLoading = false;
    private boolean isRefreshing = true;
    @StringRes
    private int errorMessageRes = 0;
    private String searchQuery = "";
    private Filter selectedFilter = Filter.LastMonth;
    private String sort = "stars";
    private String order = "desc";

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean hasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public List<Repo> getList() {
        return list;
    }
    public List<RepoDetailed> getRepoList() {
        return list.stream()
                .filter(item -> item instanceof RepoDetailed)
                .map(item -> (RepoDetailed) item)
                .collect(Collectors.toList());
    }

    public void setDetailedList(List<RepoDetailed> list) {
        this.list = list.stream().map(data -> (Repo) data).collect(Collectors.toList());
    }

    public void setList(List<Repo> list) {
        this.list = list;
    }

    public int getErrorMessageRes() {
        return errorMessageRes;
    }

    public void setErrorMessageRes(@StringRes int errorMessageRes) {
        this.errorMessageRes = errorMessageRes;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public Filter getSelectedFilter() {
        return selectedFilter;
    }

    public void setSelectedFilter(Filter selectedFilter) {
        this.selectedFilter = selectedFilter;
    }

    public String getSort() {
        return sort;
    }

    public String getOrder() {
        return order;
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

    public void setRefreshing(boolean refreshing) {
        isRefreshing = refreshing;
    }
}
