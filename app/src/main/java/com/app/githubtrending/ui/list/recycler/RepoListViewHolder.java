package com.app.githubtrending.ui.list.recycler;

import androidx.recyclerview.widget.RecyclerView;

import com.app.githubtrending.databinding.ListItemBinding;
import com.app.githubtrending.ui.model.Repo;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class RepoListViewHolder extends RecyclerView.ViewHolder {

    public interface OnItemClickListener {
        void onItemClick(long id);
    }

    private final ListItemBinding binding;
    private final OnItemClickListener onItemClickListener;

    public RepoListViewHolder(ListItemBinding binding, OnItemClickListener onItemClickListener) {
        super(binding.getRoot());
        this.binding = binding;
        this.onItemClickListener = onItemClickListener;
    }

    public void bind(Repo repo) {
        binding.repoName.setText(repo.getRepoName());
        binding.username.setText(repo.getUsername());
        binding.description.setText(repo.getDescription());
        binding.starsCount.setText(String.valueOf(repo.getStarsCount()));
        bindImage(repo.getRepoImageUrl());
        binding.listItem.setOnClickListener(view -> onItemClickListener.onItemClick(repo.getId()));
    }

    private void bindImage(String imageUrl) {
        ImageLoader imageLoader = Coil.imageLoader(itemView.getContext());

        ImageRequest request = new ImageRequest.Builder(itemView.getContext())
                .data(imageUrl)
                .crossfade(true)
                .target(binding.repoImage)
                .build();
        imageLoader.enqueue(request);
    }
}
