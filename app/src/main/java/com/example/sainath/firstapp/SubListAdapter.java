package com.example.sainath.firstapp;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sainath.storedata.StoreConstants;
import com.example.sainath.storedata.StoreDataProvider;

/**
 * Created by Sainath on 29-05-2015.
 */
public class SubListAdapter extends BaseAdapter {

    private Context mContext = null;
    private Cursor mItemsCursor = null;

    public SubListAdapter(Context mContext) {
        this.mContext = mContext;

        mItemsCursor = StoreDataProvider.getInstance(mContext).getAllItems(UIConstants.SearchType.CATEGORY_ALL_PRODUCTS, null);
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = new TextView(mContext);

        view.setTextSize(24.0f);

        if(mItemsCursor != null && mItemsCursor.getCount() != 0) {
            mItemsCursor.moveToPosition(position);
            view.setText(mItemsCursor.getString(mItemsCursor.getColumnIndex(StoreConstants.colProductName)));
        }

        return view;
    }
}
