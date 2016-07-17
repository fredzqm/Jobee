package com.fredzqm.jobee.job_seeker.resume;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class NestedListView extends ListView {

    private int listViewTouchAction;
    private static final int MAXIMUM_LIST_ITEMS_VIEWABLE = 99;

    public NestedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        listViewTouchAction = -1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int newHeight = 0;
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            ListAdapter listAdapter = getAdapter();
            if (listAdapter != null && !listAdapter.isEmpty()) {
                int listPosition = 0;
                int count = listAdapter.getCount();
                for (listPosition = 0; listPosition < count
                        && listPosition < MAXIMUM_LIST_ITEMS_VIEWABLE; listPosition++) {
                    View listItem = listAdapter.getView(listPosition, null, this);
                    //now it will not throw a NPE if listItem is a ViewGroup instance
//                    if (listItem instanceof ViewGroup) {
//                        listItem.setLayoutParams(new LayoutParams(
//                                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//                    }
                    listItem.measure(widthMeasureSpec, heightMeasureSpec);
                    int height = listItem.getMeasuredHeight();
                    newHeight += height;
                }
                int divHeight = getDividerHeight();
                newHeight += divHeight * listPosition;
            }
            if ((heightMode == MeasureSpec.AT_MOST) && (newHeight > heightSize)) {
                if (newHeight > heightSize) {
                    newHeight = heightSize;
                }
            }
        } else {
            newHeight = getMeasuredHeight();
        }
        setMeasuredDimension(getMeasuredWidth(), newHeight);
    }

}