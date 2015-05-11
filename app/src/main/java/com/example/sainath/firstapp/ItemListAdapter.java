package com.example.sainath.firstapp;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.sainath.storedata.StoreConstants;
import com.example.sainath.storedata.StoreDataProvider;

/**
 * Created by Sainath on 02-05-2015.
 */
public class ItemListAdapter implements ListAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    Cursor mItemsCursor = null;

    public ItemListAdapter(Context ctx, LayoutInflater inflater) {
        mContext = ctx;
        mInflater = inflater;

        mItemsCursor = StoreDataProvider.getInstance(mContext).getAllItems(null);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    public void updateSelectionArgs(String[] selectionArgs) {
        if (mItemsCursor != null)
        {
            if(!mItemsCursor.isClosed())
                mItemsCursor.close();

            mItemsCursor = null;
        }
        mItemsCursor = StoreDataProvider.getInstance(mContext).getAllItems(selectionArgs);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public int getCount() {
        return mItemsCursor.getCount();
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
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = mInflater.inflate(R.layout.product_list_item, null);

        if(mItemsCursor != null && mItemsCursor.getCount() != 0)
        {
            mItemsCursor.moveToPosition(position);

            ((TextView) convertView.findViewById(R.id.item_product_name)).setText(mItemsCursor.getString(mItemsCursor.getColumnIndex(StoreConstants.colProductName)));
            ((TextView) convertView.findViewById(R.id.item_brand_name)).setText(mItemsCursor.getString(mItemsCursor.getColumnIndex(StoreConstants.colBrandName)));

            ((EditText) convertView.findViewById(R.id.pack_size_number)).setText(mItemsCursor.getInt(mItemsCursor.getColumnIndex(StoreConstants.colPackSize))+"");
            ((TextView) convertView.findViewById(R.id.mrp_price_view)).setText(mItemsCursor.getString(mItemsCursor.getColumnIndex(StoreConstants.colMRP)));
            ((TextView) convertView.findViewById(R.id.sell_price_view)).setText(mItemsCursor.getString(mItemsCursor.getColumnIndex(StoreConstants.colSellingPrice)));
        }
        else {
            ((TextView) convertView.findViewById(R.id.item_product_name)).setText("Nidhish");
            ((TextView) convertView.findViewById(R.id.item_brand_name)).setText("Sainath");
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public void closeCursor()
    {
        mItemsCursor.close();
        mItemsCursor = null;
    }

}