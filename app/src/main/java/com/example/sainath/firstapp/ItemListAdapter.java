package com.example.sainath.firstapp;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.sainath.storedata.StoreConstants;
import com.example.sainath.storedata.StoreDataProvider;

/**
 * Created by Sainath on 02-05-2015.
 */
public class ItemListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    Cursor mItemsCursor = null;

    private String mType = null;

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

    public void setListType(String listType)
    {
        mType = listType;
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
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
            convertView = mInflater.inflate(R.layout.product_list_item, null);

        if(mItemsCursor != null && mItemsCursor.getCount() != 0)
        {
            mItemsCursor.moveToPosition(position);

            ((TextView) convertView.findViewById(R.id.item_product_name)).setText(mItemsCursor.getString(mItemsCursor.getColumnIndex(StoreConstants.colProductName)));
            ((TextView) convertView.findViewById(R.id.item_brand_name)).setText(mItemsCursor.getString(mItemsCursor.getColumnIndex(StoreConstants.colBrandName)));

            ((TextView) convertView.findViewById(R.id.pack_size_number)).setText(mItemsCursor.getInt(mItemsCursor.getColumnIndex(StoreConstants.colPackSize))+"");
            ((TextView) convertView.findViewById(R.id.mrp_price_view)).setText(mItemsCursor.getString(mItemsCursor.getColumnIndex(StoreConstants.colMRP)));
            ((TextView) convertView.findViewById(R.id.sell_price_view)).setText(mItemsCursor.getString(mItemsCursor.getColumnIndex(StoreConstants.colSellingPrice)));

            Bitmap mBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Documents/items/" + mItemsCursor.getString(mItemsCursor.getColumnIndex(StoreConstants.colProductImage)));

            ((ImageView) convertView.findViewById(R.id.item_preview)).setImageBitmap(mBitmap.createScaledBitmap(mBitmap,100,100,false));

            mBitmap.recycle();

            convertView.setTag(mItemsCursor.getInt(mItemsCursor.getColumnIndex(StoreConstants.colSerialNumber)));
        }
        else {
            ((TextView) convertView.findViewById(R.id.item_product_name)).setText("Nidhish");
            ((TextView) convertView.findViewById(R.id.item_brand_name)).setText("Sainath");
        }

        /*if(mType.equalsIgnoreCase("selectable")) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToCheckout(v);
                }
            });


        }*/

        return convertView;
    }

    private void addToCheckout(View v)
    {
        int[] items = new int[3];

        items[0] = (int) v.getTag();
        items[1] = Integer.parseInt(((TextView) v.findViewById(R.id.qty_number)).getText().toString());
        items[2] = Integer.parseInt(((TextView) v.findViewById(R.id.pack_size_number)).getText().toString());

        StoreDataProvider.getInstance(mContext).insertItemToCheckout(items);

        v.setVisibility(View.GONE);

        v.invalidate();
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