package com.starrynight.tourapiproject.starPage;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class StarRecyclerViewWidth extends RecyclerView.ItemDecoration {

    private final int spacingCount;
    private  final int spacing;

    public StarRecyclerViewWidth(int spacingCount, int spacing) {
        this.spacingCount = spacingCount;
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        int column = position % spacingCount;
        if(position>=0){
            outRect.left = spacing-column*spacing/spacingCount;
            outRect.right = (column+1)*spacing/spacingCount;
            if (position < spacingCount) outRect.top = spacing;
            outRect.bottom = spacing;
        }else{
            outRect.bottom=0;
            outRect.top=0;
            outRect.right=0;
            outRect.left=0;
        }
    }
}
