package com.starrynight.tourapiproject.observationPage.course;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CourseDividerDecorator extends RecyclerView.ItemDecoration{

    private Drawable mDivider;

    public CourseDividerDecorator(Drawable mDivider) {
        this.mDivider = mDivider;
    }

//    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        int left =  parent.getPaddingStart();
//        int right = parent.getWidth() - mDivider.getIntrinsicWidth();
//
//        int childCount = parent.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View child = parent.getChildAt(i);
//
//            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
//
//            int top = child.getBottom() + params.bottomMargin;
//            int bottom = top+mDivider.getIntrinsicHeight();
//
//            mDivider.setBounds(left, top, right, bottom);
//            mDivider.draw(c);
//
//        }
//    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = 24;

    }
}
