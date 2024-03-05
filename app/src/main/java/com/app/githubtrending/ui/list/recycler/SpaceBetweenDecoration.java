package com.app.githubtrending.ui.list.recycler;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpaceBetweenDecoration extends RecyclerView.ItemDecoration {

    private final int dimenId;

    public SpaceBetweenDecoration(int dimenId) {
        this.dimenId = dimenId;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        Context context = view.getContext();
        int spacePx = context.getResources().getDimensionPixelSize(dimenId);

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = spacePx;
        }
        outRect.bottom = spacePx;
    }
}