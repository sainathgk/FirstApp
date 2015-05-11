package com.example.sainath.storedata;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sainath.firstapp.R;


public class StoreFormActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_data);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private SQLiteDatabase db = null;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_store_data, container, false);

            db = new StoreDatabaseHelper(getActivity().getApplicationContext()).getStoreDatabase();

            GetAndStoreValues(rootView);

            return rootView;
        }

        private void GetAndStoreValues(View rootView) {
            Button saveBtn = (Button) rootView.findViewById(R.id.done_btn);

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View rootView = v.getRootView();

                    EditText productName = (EditText) rootView.findViewById(R.id.product_text);
                    EditText brandName = (EditText) rootView.findViewById(R.id.brand_text);
                    EditText category = (EditText) rootView.findViewById(R.id.category_type);
                    EditText packSize = (EditText) rootView.findViewById(R.id.pack_value);
                    EditText units = (EditText) rootView.findViewById(R.id.unit_value);
                    EditText mrp = (EditText) rootView.findViewById(R.id.mrp_value);
                    EditText sellingPrice = (EditText) rootView.findViewById(R.id.selling_value);
                    EditText stockAvailable = (EditText) rootView.findViewById(R.id.stock_value);

                    ContentValues products = new ContentValues();

                    products.put(StoreConstants.colProductName, productName.getText().toString());
                    products.put(StoreConstants.colBrandName, brandName.getText().toString());
                    products.put(StoreConstants.colCategory, category.getText().toString());

                    if(db != null) {
                        Long productId = db.insert(StoreConstants.tableProducts, null, products);

                        ContentValues values = new ContentValues();

                        values.put(StoreConstants.colProductID, productId);
                        values.put(StoreConstants.colPackSize, Integer.parseInt(packSize.getText().toString()));
                        values.put(StoreConstants.colUnits, units.getText().toString());
                        values.put(StoreConstants.colMRP, Integer.parseInt(mrp.getText().toString()));
                        values.put(StoreConstants.colSellingPrice, Integer.parseInt(sellingPrice.getText().toString()));
                        values.put(StoreConstants.colStockAvailable, Integer.parseInt(stockAvailable.getText().toString()));

                        db.insert(StoreConstants.tableValues, null, values);
                    }

                    productName.getText().clear();
                    brandName.getText().clear();
                    category.getText().clear();
                    packSize.getText().clear();
                    units.getText().clear();
                    mrp.getText().clear();
                    sellingPrice.getText().clear();
                    stockAvailable.getText().clear();
                }
            });

            Button clearBtn = (Button) rootView.findViewById(R.id.clear_btn);

            clearBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    View rootView = v.getRootView();

                    EditText productName = (EditText) rootView.findViewById(R.id.product_text);
                    EditText brandName = (EditText) rootView.findViewById(R.id.brand_text);
                    EditText category = (EditText) rootView.findViewById(R.id.category_type);
                    EditText packSize = (EditText) rootView.findViewById(R.id.pack_value);
                    EditText units = (EditText) rootView.findViewById(R.id.unit_value);
                    EditText mrp = (EditText) rootView.findViewById(R.id.mrp_value);
                    EditText sellingPrice = (EditText) rootView.findViewById(R.id.selling_value);
                    EditText stockAvailable = (EditText) rootView.findViewById(R.id.stock_value);

                    productName.getText().clear();
                    brandName.getText().clear();
                    category.getText().clear();
                    packSize.getText().clear();
                    units.getText().clear();
                    mrp.getText().clear();
                    sellingPrice.getText().clear();
                    stockAvailable.getText().clear();
                }
            });

            Button checkBtn = (Button) rootView.findViewById(R.id.check_btn);

            checkBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    String name = null;
                    String mrp = null;
                    String projection[] = {StoreConstants.colProductName};
                    Cursor cur = db.query(StoreConstants.tableProducts, projection, null, null, null, null, StoreConstants.colSerialNumber + " DESC ", "1");

                    if(cur != null && cur.moveToFirst())
                    {
                        name = cur.getString(cur.getColumnIndex(StoreConstants.colProductName));
                        cur.close();
                        cur = null;
                    }

                    String value_projection[] = {StoreConstants.colMRP};
                    cur = db.query(StoreConstants.tableValues, value_projection, null, null, null, null, StoreConstants.colProductID + " DESC ", "1");

                    if(cur != null && cur.moveToFirst())
                    {
                        mrp = cur.getString(cur.getColumnIndex(StoreConstants.colMRP));
                        cur.close();
                        cur = null;
                    }

                    Toast.makeText(getActivity().getApplicationContext(), "Latest Product in DB "+name+" of MRP is "+mrp, Toast.LENGTH_LONG).show();
                }
            });

            //new stock_data(productName.getText().toString(), brandName.getText().toString(), category.getText().toString());
            //new stock_extra_data(Integer.parseInt(packSize.getText().toString()), units.getText().toString(), Integer.parseInt(mrp.getText().toString()), Integer.parseInt(sellingPrice.getText().toString()),Integer.parseInt(stockAvailable.getText().toString()));
        }
    }

    private class stock_data {
        String mProductName = null;
        String mBrandName = null;
        String mCategory = null;

        private stock_data(String mProductName, String mBrandName, String mCategory) {
            this.mProductName = mProductName;
            this.mBrandName = mBrandName;
            this.mCategory = mCategory;
        }
    }

    private class stock_extra_data {
        int mPackSize = 0;
        String mUnits = null;
        double mMRP = 0.0;
        double mSellingPrice = 0.0;
        int mStockAvailable = 0;

        private stock_extra_data(int mPackSize, String mUnits, double mMRP, double mSellingPrice, int mStockAvailable) {
            this.mPackSize = mPackSize;
            this.mUnits = mUnits;
            this.mMRP = mMRP;
            this.mSellingPrice = mSellingPrice;
            this.mStockAvailable = mStockAvailable;
        }
    }
}
