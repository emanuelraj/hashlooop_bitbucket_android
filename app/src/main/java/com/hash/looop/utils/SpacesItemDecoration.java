package com.hash.looop.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by mathan on 25/10/15.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int mHorizontalSpacing;
    private int mVerticalSpacing;

    public SpacesItemDecoration(int horizontalSpacing, int verticalSpacing) {
        this.mHorizontalSpacing = horizontalSpacing;
        mVerticalSpacing = verticalSpacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mHorizontalSpacing;
        outRect.right = mHorizontalSpacing;
        outRect.bottom = mVerticalSpacing;

        // Add top margin only for the first item to avoid double mHorizontalSpacing between items
        if(parent.getChildAdapterPosition(view) == 0) {
            outRect.top = mVerticalSpacing;
        }
    }
}