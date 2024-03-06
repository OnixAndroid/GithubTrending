package com.app.githubtrending.ui.list;

public class ListScreenState {
    private int currentPage = 0;
    private final int itemsPerPage = 20;
    private boolean hasNextPage = true;
    private boolean isLoading = false;

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

//    public class ListScreenState(int currentPage, boolean isLoading) {
//
//    }
}
