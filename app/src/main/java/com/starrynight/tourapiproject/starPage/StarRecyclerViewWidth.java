package com.starrynight.tourapiproject.starPage;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class StarRecyclerViewWidth extends RecyclerView.ItemDecoration {

    private final int divWidth;
    private  final int divHeight;

    public StarRecyclerViewWidth(int divWidth, int divHeight) {
        this.divWidth = divWidth;
        this.divHeight = divHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.right = divWidth;
        outRect.top= divHeight;
    }
}
