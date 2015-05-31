package com.example.sainath.firstapp;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sainath.storedata.StoreConstants;
import com.example.sainath.storedata.StoreDataProvider;

/**
 * Created by Sainath on 02-05-2015.
 */
public class ItemListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    Cursor mItemsCursor = null;
    private Spinner spinner;
    private static final String[]paths = {"10", "20", "30"};

    private String mType = null;

    private int mItemCount = 0;

    public ItemListAdapter(Context ctx, LayoutInflater inflater) {
        mContext = ctx;
        mInflater = inflater;
    }

    public void setListType(String listType)
    {
        mType = listType;
    }

    public void updateSelectionArgs(final UIConstants.SearchType searchType, final String[] selectionArgs) {
        if (mItemsCursor != null)
        {
            if(!mItemsCursor.isClosed())
                mItemsCursor.close();

            mItemsCursor = null;
        }
        Runnable dbRunnable = new Runnable() {
            public void run() {
                mItemsCursor = StoreDataProvider.getInstance(mContext).getAllItems(searchType, selectionArgs);
                mItemCount = mItemsCursor.getCount();
            }
        };
        dbRunnable.run();
    }

    @Override
    public int getCount() {
        return mItemCount;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null)
            convertView = mInflater.inflate(R.layout.product_list_item, null);

        if(mItemsCursor != null && mItemsCursor.getCount() != 0)
        {
            mItemsCursor.moveToPosition(position);

            ((TextView) convertView.findViewById(R.id.item_product_name)).setText(mItemsCursor.getString(mItemsCursor.getColumnIndex(StoreConstants.colProductName)));
            ((TextView) convertView.findViewById(R.id.item_brand_name)).setText(mItemsCursor.getString(mItemsCursor.getColumnIndex(StoreConstants.colBrandName)));

            //((TextView) convertView.findViewById(R.id.pack_size_number)).setText(mItemsCursor.getInt(mItemsCursor.getColumnIndex(StoreConstants.colPackSize)) + "");
            ((TextView) convertView.findViewById(R.id.mrp_price_view)).setText(mItemsCursor.getString(mItemsCursor.getColumnIndex(StoreConstants.colMRP)));
            ((TextView) convertView.findViewById(R.id.sell_price_view)).setText(mItemsCursor.getString(mItemsCursor.getColumnIndex(StoreConstants.colSellingPrice)));

            ((EditText) convertView.findViewById(R.id.qty_number)).setText(mItemsCursor.getString(mItemsCursor.getColumnIndex(StoreConstants.colUnits)));

            Bitmap mBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Documents/items/" + mItemsCursor.getString(mItemsCursor.getColumnIndex(StoreConstants.colProductImage)));

            if(mBitmap != null) {
                ((ImageView) convertView.findViewById(R.id.item_preview)).setImageBitmap(mBitmap.createScaledBitmap(mBitmap, 80, 80, false));

                mBitmap.recycle();
            }

            final TextView packSizeValue = (TextView) convertView.findViewById(R.id.pack_size_number);
            packSizeValue.setText(mItemsCursor.getInt(mItemsCursor.getColumnIndex(StoreConstants.colPackSize)) + "");

            spinner = (Spinner)convertView.findViewById(R.id.spinner);
            ArrayAdapter<String>adapter = new ArrayAdapter<String>(mContext,
                    android.R.layout.simple_spinner_item,paths);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    packSizeValue.setText((String) parent.getItemAtPosition(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    if (mType.equalsIgnoreCase("selectable"))
                        packSizeValue.setText((String) parent.getItemAtPosition(0));
                    else
                        packSizeValue.setText(mItemsCursor.getInt(mItemsCursor.getColumnIndex(StoreConstants.colPackSize)) + "");
                }
            });

            convertView.setTag(mItemsCursor.getInt(mItemsCursor.getColumnIndex(StoreConstants.colSerialNumber)));
        }
        else {
            ((TextView) convertView.findViewById(R.id.item_product_name)).setText("Nidhish");
            ((TextView) convertView.findViewById(R.id.item_brand_name)).setText("Sainath");
        }

        final EditText qty = (EditText) convertView.findViewById(R.id.qty_number);
        //qty.setTag(0,position);

        //qty.setTag(1,mItemsCursor.getInt(mItemsCursor.getColumnIndex(StoreConstants.colSerialNumber)));
        qty.setTag(position);
        qty.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_NUMPAD_ENTER) {
                    Toast.makeText(mContext, "Key enter is Done", Toast.LENGTH_SHORT).show();
                    View rootView = v.getRootView();

                    //Update the MRP & Selling Price according to the quantity
                    updatePrice(rootView);

                    //Update the Quantity to Checkout Table
                    /*if(mType.equalsIgnoreCase("normal"))
                        updateQtyToCheckout(v);*/

                    return true;
                }
                return false;
            }
        });

        if(mType.equalsIgnoreCase("selectable")) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToCheckout(v);
                }
            });
        }
        else
        {
            updatePrice(convertView);
            ((CheckBox) convertView.findViewById(R.id.item_checkBox)).setVisibility(View.GONE);
        }

        return convertView;
    }

    private void updatePrice(View rootView)
    {
        TextView mrpView = (TextView) rootView.findViewById(R.id.mrp_price_view);
        TextView spView = (TextView) rootView.findViewById(R.id.sell_price_view);

        EditText qtyView = (EditText) rootView.findViewById(R.id.qty_number);

        int qtyValue = Integer.parseInt(qtyView.getText().toString());
        if(mItemsCursor != null && mItemsCursor.moveToPosition((int) qtyView.getTag())) {
            int mrpValue = Integer.parseInt(mItemsCursor.getString(mItemsCursor.getColumnIndex(StoreConstants.colMRP)));
            int spValue = Integer.parseInt(mItemsCursor.getString(mItemsCursor.getColumnIndex(StoreConstants.colSellingPrice)));

            mrpView.setText((mrpValue * qtyValue) + "");
            spView.setText((spValue * qtyValue) + "");
        }

        mrpView.invalidate();
        spView.invalidate();
        rootView.invalidate();
    }

    private void updateQtyToCheckout(View v)
    {
        int[] items = new int[3];

        items[0] = (int) v.getTag();
        items[1] = Integer.parseInt(((EditText) v.findViewById(R.id.qty_number)).getText().toString());

        StoreDataProvider.getInstance(mContext).updateItemToCheckout(items);
    }

    private void addToCheckout(View v)
    {
        int[] items = new int[3];

        items[0] = (int) v.getTag();
        items[1] = Integer.parseInt(((EditText) v.findViewById(R.id.qty_number)).getText().toString());
        items[2] = Integer.parseInt(((TextView) v.findViewById(R.id.pack_size_number)).getText().toString());

        StoreDataProvider.getInstance(mContext).insertItemToCheckout(items);

        v.setVisibility(View.GONE);
        //mItemCount--;
        v.invalidate();

    }

    public void closeCursor()
    {
        mItemsCursor.close();
        mItemsCursor = null;
    }
}