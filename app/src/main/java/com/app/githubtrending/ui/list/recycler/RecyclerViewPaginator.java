package com.app.githubtrending.ui.list.recycler;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class RecyclerViewPaginator extends RecyclerView.OnScrollListener {

    private Long currentPage = 0L;

    private final RecyclerView.LayoutManager layoutManager;

    public RecyclerViewPaginator(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(this);
        this.layoutManager = recyclerView.getLayoutManager();
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if(newState == SCROLL_STATE_IDLE) {
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();

            int lastVisibleItemPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();

            if(isLoading() || isLastPage()) return;

            if ((visibleItemCount + lastVisibleItemPosition + THRESHOLD) >= totalItemCount) {
                loadMore(getStartSize(), getMaxSize());
            }
        }
    }

    public Long getStartSize() {
        return ++currentPage;
    }

    public Long getMaxSize() {
        return  currentPage + BATCH_SIZE;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    public abstract boolean isLastPage();
    public abstract boolean isLoading();
    public abstract void loadMore(Long start , Long count);

    private static final long BATCH_SIZE = 19L;
    private static final int THRESHOLD = 2;
}