package com.example.sainath.storedata;

/**
 * Created by Sa on 24-03-2015.
 */
public class StoreConstants {
    static final String dbName = "StoreData";

    static final String tableProducts = "PRODUCTS";
    static final String colSerialNumber = "ID";
    static final String colProductName = "product_name";
    static final String colBrandName = "brand_name";
    static final String colCategory = "category";

    static final String tableValues = "STORE_VALUES";
    static final String colValueID = "value_id";
    static final String colProductID = "product_id";
    static final String colPackSize = "pack_size";
    static final String colUnits = "units";
    static final String colMRP = "MRP";
    static final String colSellingPrice = "selling_price";
    static final String colStockAvailable = "stock_available";

    static final String CREATE_PRODUCTS_TABLE = "CREATE TABLE IF NOT EXISTS " + tableProducts + " ( " + colSerialNumber + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            colProductName + " TEXT , " + colBrandName + " TEXT, " + colCategory + " TEXT )";

    static final String CREATE_VALUES_TABLE = "CREATE TABLE IF NOT EXISTS " + tableValues + " ( " + colValueID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            colProductID + " INTEGER NOT NULL , " +
            colPackSize + " INTEGER, " + colUnits + " INTEGER NOT NULL, " + colMRP + " INTEGER NOT NULL, " + colSellingPrice + " INTEGER, "
            + colStockAvailable + " INTEGER, FOREIGN KEY(" + colProductID + ") REFERENCES " + tableProducts + "(" + colSerialNumber + ")  );";

}
