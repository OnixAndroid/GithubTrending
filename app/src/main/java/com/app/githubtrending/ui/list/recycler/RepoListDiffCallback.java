package com.app.githubtrending.ui.list.recycler;

import androidx.recyclerview.widget.DiffUtil;

import com.app.githubtrending.ui.model.Repo;

import java.util.Objects;

public class RepoListDiffCallback extends DiffUtil.ItemCallback<Repo> {

    @Override
    public boolean areItemsTheSame(Repo oldItem, Repo newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(Repo oldItem, Repo newItem) {
        return Objects.equals(oldItem, newItem);
    }
}