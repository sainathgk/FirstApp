package com.example.sainath.firstapp;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.sainath.firstapp.dummy.DummyContent;


public class ItemListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_checkout) {
            Toast.makeText(this.getApplicationContext(), "List Added", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        /**
         * The fragment's ListView/GridView.
         */
        private AbsListView mListView;

        /**
         * The Adapter which will be used to populate the ListView/GridView with
         * Views.
         */
        private ItemListAdapter mAdapter;

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // TODO: Change Adapter to display your content

            mAdapter = new ItemListAdapter(this.getActivity().getApplicationContext(), (LayoutInflater)this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE));

            /*mAdapter = new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS);*/
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            //View rootView = inflater.inflate(R.layout.fragment_list, container, false);

            View view = inflater.inflate(R.layout.fragment_item, container, false);

            // Set the adapter
            mListView = (AbsListView) view.findViewById(android.R.id.list);
            mListView.setAdapter(mAdapter);

            //mListView.setAdapter(mListAdapter);

            // Set OnItemClickListener so we can be notified on item clicks
            //mListView.setOnItemClickListener(this);

            return view;
        }

        @Override
        public void onDetach() {
            super.onDetach();

            mAdapter.closeCursor();
        }
    }
}
