package com.example.sainath.storedata;

/**
 * Created by Sa on 24-03-2015.
 */
public class StoreConstants {
    static final String dbName = "StoreData";

    static final String tableProducts = "PRODUCTS";
    static final String colSerialNumber = "ID";
    static final public String colProductName = "product_name";
    static final public String colBrandName = "brand_name";
    static final public String colCategory = "category";

    static final String tableValues = "STORE_VALUES";
    static final String colValueID = "value_id";
    static final String colProductID = "product_id";
    static final public String colPackSize = "pack_size";
    static final public String colUnits = "units";
    static final public String colMRP = "MRP";
    static final public String colSellingPrice = "selling_price";
    static final String colStockAvailable = "stock_available";

    static final String CREATE_PRODUCTS_TABLE = "CREATE TABLE IF NOT EXISTS " + tableProducts + " ( " + colSerialNumber + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            colProductName + " TEXT , " + colBrandName + " TEXT, " + colCategory + " TEXT )";

    static final String CREATE_VALUES_TABLE = "CREATE TABLE IF NOT EXISTS " + tableValues + " ( " + colValueID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            colProductID + " INTEGER NOT NULL , " +
            colPackSize + " INTEGER, " + colUnits + " INTEGER DEFAULT 1, " + colMRP + " INTEGER NOT NULL, " + colSellingPrice + " INTEGER, "
            + colStockAvailable + " INTEGER, FOREIGN KEY(" + colProductID + ") REFERENCES " + tableProducts + "(" + colSerialNumber + ")  );";

    static final String ALL_PRODUCTS = "select * from "+tableProducts+ " p inner join "+tableValues+" v on p."+colSerialNumber+
            " = v."+colProductID+";";

    static final String ALL_CATEGORIES = "select distinct "+colCategory+" from "+tableProducts+";";

    static final String PRODUCTS_SEARCH = "select * from "+tableProducts+ " p inner join "+tableValues+" v on p."+colSerialNumber+
            " = v."+colProductID+" where "+colProductName+" like ?";
}
