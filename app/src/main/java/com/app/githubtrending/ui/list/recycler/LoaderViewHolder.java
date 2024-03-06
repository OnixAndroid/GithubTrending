package com.app.githubtrending.ui.list.recycler;

import androidx.recyclerview.widget.RecyclerView;

import com.app.githubtrending.databinding.LoaderItemBinding;

public class LoaderViewHolder extends RecyclerView.ViewHolder {

    private final LoaderItemBinding binding;

    public LoaderViewHolder(LoaderItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
