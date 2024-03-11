package com.app.githubtrending.ui.list;

import com.app.githubtrending.domain.model.Filter;
import com.app.githubtrending.ui.common.ErrorMessage;
import com.app.githubtrending.ui.model.Repo;
import com.app.githubtrending.ui.model.RepoDetailed;

import java.util.List;
import java.util.stream.Collectors;

public class ListScreenState {
    private List<Repo> list = List.of();
    private int currentPage = 0;
    private boolean hasNextPage = true;
    private boolean isLoading = false;
    private boolean isRefreshing = true;
    private ErrorMessage errorMessage = new ErrorMessage.NoError();
    private String searchQuery = "";
    private Filter selectedFilter = Filter.LastMonth;
    private static final int ITEMS_PER_PAGE = 20;

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
        return ITEMS_PER_PAGE;
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

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
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
        return "stars";
    }

    public String getOrder() {
        return "desc";
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

    public void setRefreshing(boolean refreshing) {
        isRefreshing = refreshing;
    }
}
