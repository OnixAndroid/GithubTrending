package com.app.githubtrending.ui.list.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.ListAdapter;

import com.app.githubtrending.R;
import com.app.githubtrending.databinding.ListItemBinding;
import com.app.githubtrending.ui.model.Repo;

public class RepoListAdapter extends ListAdapter<Repo, RepoListViewHolder> {

    private final RepoListViewHolder.OnItemClickListener onItemClickListener;

    public RepoListAdapter(RepoListViewHolder.OnItemClickListener onItemClickListener) {
        super(new RepoListDiffCallback());
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RepoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemListView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ListItemBinding itemListBinding = ListItemBinding.bind(itemListView);
        return new RepoListViewHolder(itemListBinding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(RepoListViewHolder holder, int position) {
        Repo repo = getItem(position);
        holder.bind(repo);
    }
}