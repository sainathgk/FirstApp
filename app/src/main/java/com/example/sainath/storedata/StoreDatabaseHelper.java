package com.example.sainath.storedata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sa on 24-03-2015.
 */
public class StoreDatabaseHelper extends SQLiteOpenHelper {

    public StoreDatabaseHelper(Context context) {
        super(context, StoreConstants.dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(StoreConstants.CREATE_PRODUCTS_TABLE);
        db.execSQL(StoreConstants.CREATE_VALUES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public SQLiteDatabase getStoreDatabase() {
        return this.getWritableDatabase();
    }
}
