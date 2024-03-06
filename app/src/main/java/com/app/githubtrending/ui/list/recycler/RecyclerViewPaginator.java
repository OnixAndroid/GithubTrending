package com.app.githubtrending.ui.list.recycler;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_SETTLING;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.githubtrending.ui.model.Repo;
import com.app.githubtrending.ui.model.RepoLoader;

import java.util.ArrayList;

public abstract class RecyclerViewPaginator extends RecyclerView.OnScrollListener {

    private Long batchSize = 19l;

    private Long currentPage = 0l;

    private Integer threshold = 2;

    private RecyclerView.LayoutManager layoutManager;
    public RecyclerViewPaginator(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(this);
        this.layoutManager = recyclerView.getLayoutManager();
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (newState == SCROLL_STATE_SETTLING) {

            if(isLoading()) return;

            if(isLastPage()) return;

            if (recyclerView.getAdapter() instanceof RepoListAdapter adapter) {
                ArrayList<Repo> list = new ArrayList<>(adapter.getCurrentList());

                if (list.stream().anyMatch( item -> item instanceof RepoLoader)) return;

                list.add(new RepoLoader());
                adapter.submitList(list);
            }
        }

        if(newState == SCROLL_STATE_IDLE) {
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();

            int lastVisibleItemPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();;

            if(isLoading()) return;

            if(isLastPage()) return;

            if ((visibleItemCount + lastVisibleItemPosition + threshold) >= totalItemCount) {
                loadMore(getStartSize(), getMaxSize());
            }
        }
    }

    public Long getStartSize() {
        return ++currentPage;
    }

    public Long getMaxSize() {
        return  currentPage + batchSize;
    }

    public void reset() {
        currentPage = 0l;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    public abstract boolean isLastPage();
    public abstract boolean isLoading();
    public abstract void loadMore(Long start , Long count);
}