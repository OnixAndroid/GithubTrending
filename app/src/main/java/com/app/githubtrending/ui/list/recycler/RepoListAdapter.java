package com.app.githubtrending.ui.list.recycler;

import static com.app.githubtrending.ui.list.recycler.RepoListViewHolder.TYPE_ITEM;
import static com.app.githubtrending.ui.list.recycler.RepoListViewHolder.TYPE_LOADER;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.app.githubtrending.R;
import com.app.githubtrending.databinding.ListItemBinding;
import com.app.githubtrending.databinding.LoaderItemBinding;
import com.app.githubtrending.ui.model.Repo;
import com.app.githubtrending.ui.model.RepoDetailed;

public class RepoListAdapter extends ListAdapter<Repo, RecyclerView.ViewHolder> {

    private final RepoListViewHolder.OnItemClickListener onItemClickListener;

    public RepoListAdapter(RepoListViewHolder.OnItemClickListener onItemClickListener) {
        super(new RepoListDiffCallback());
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TYPE_ITEM:
                View itemListView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                ListItemBinding itemListBinding = ListItemBinding.bind(itemListView);
                return new RepoListViewHolder(itemListBinding, onItemClickListener);

            case TYPE_LOADER:
                View itemLoader = LayoutInflater.from(parent.getContext()).inflate(R.layout.loader_item, parent, false);
                LoaderItemBinding itemLoaderBinding = LoaderItemBinding.bind(itemLoader);
                return new LoaderViewHolder(itemLoaderBinding);

            default:
                throw new RuntimeException("There is no type that matches the type " + viewType +
                        " + make sure you are using types correctly");
        }

//        View itemListView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
//        ListItemBinding itemListBinding = ListItemBinding.bind(itemListView);
//        return new RepoListViewHolder(itemListBinding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM) {
            RepoDetailed repoItem = ((RepoDetailed) getItem(position));
            ((RepoListViewHolder) holder).bind(repoItem);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getCurrentList().size() - 1 >= position && getCurrentList().get(position).getId() >= 0) {
            return TYPE_ITEM;
        } else {
            return TYPE_LOADER;
        }
    }

    //    @Override
//    public void onBindViewHolder(RepoListViewHolder holder, int position) {
//        Repo repo = getItem(position);
//        holder.bind(repo);
//    }
}