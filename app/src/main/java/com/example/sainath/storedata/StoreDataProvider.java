package com.example.sainath.storedata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

public class StoreDataProvider{
    private static StoreDataProvider mStoreDP = null;
    private Context mContext = null;
    private SQLiteDatabase mStoreDB = null;

    private StoreDataProvider(Context mContext) {
        this.mContext = mContext;
        mStoreDB = new StoreDatabaseHelper(mContext).getStoreDatabase();
    }

    public static StoreDataProvider getInstance(Context mCtx)
    {
        if(mStoreDP == null)
        {
            mStoreDP = new StoreDataProvider(mCtx);
        }
        return mStoreDP;
    }

    public ArrayList getAllCategories()
    {
        Cursor cursor = null;
        ArrayList<String> categoryList = null;

        try {
            if(mStoreDB != null)
            {
                cursor = mStoreDB.rawQuery(StoreConstants.ALL_CATEGORIES,null);

                if(cursor != null) {
                    categoryList = new ArrayList<String>(cursor.getCount());

                    while (cursor.moveToNext())
                    {
                        categoryList.add(cursor.getString(cursor.getColumnIndex(StoreConstants.colCategory)));
                    }
                    cursor.close();
                }
            }
        }catch (SQLiteException e)
        {
            e.printStackTrace();
        }
        return categoryList;
    }

    public Cursor getAllItems(String[] args)
    {
        Cursor cursor = null;

        try {
            if(mStoreDB != null)
            {
                if(args != null)
                    cursor = mStoreDB.rawQuery(StoreConstants.PRODUCTS_SEARCH,args);
                else
                    cursor = mStoreDB.rawQuery(StoreConstants.ALL_PRODUCTS, null);
            }
        }catch (SQLiteException e)
        {
            e.printStackTrace();
        }

        return cursor;
    }

    public void insertValuestoStoreDB(String[] data)
    {
        ContentValues products = new ContentValues();

        products.put(StoreConstants.colProductName, data[0]);
        products.put(StoreConstants.colBrandName, data[1]);
        products.put(StoreConstants.colCategory, data[2]);

        if(mStoreDB != null) {
            Long productId = mStoreDB.insert(StoreConstants.tableProducts, null, products);

            ContentValues values = new ContentValues();

            values.put(StoreConstants.colProductID, productId);
            values.put(StoreConstants.colPackSize, Integer.parseInt(data[3]));
            values.put(StoreConstants.colUnits, 1);
            values.put(StoreConstants.colMRP, Integer.parseInt(data[4]));
            values.put(StoreConstants.colSellingPrice, Integer.parseInt(data[5]));
            //values.put(StoreConstants.colStockAvailable, Integer.parseInt());

            mStoreDB.insert(StoreConstants.tableValues, null, values);
        }
    }

}