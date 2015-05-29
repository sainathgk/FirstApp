package com.example.sainath.firstapp;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.ActionMode;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sainath.firstapp.dummy.DummyContent;
import com.example.sainath.storedata.StoreDataProvider;


public class ItemListActivity extends ActionBarActivity implements ActionMode.Callback{

    protected Object mActionMode = null;
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

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements ActionMode.Callback{

        /**
         * The fragment's ListView/GridView.
         */
        private ListView mListView;

        /**
         * The Adapter which will be used to populate the ListView/GridView with
         * Views.
         */
        private ItemListAdapter mAdapter;

        protected Object mActionMode = null;

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // TODO: Change Adapter to display your content

            mAdapter = new ItemListAdapter(this.getActivity().getApplicationContext(), (LayoutInflater)this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            mAdapter.setListType("normal");
            /*mAdapter = new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS);*/
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            //View rootView = inflater.inflate(R.layout.fragment_list, container, false);

            View view = inflater.inflate(R.layout.fragment_item, container, false);

            // Set the adapter
            mListView = (ListView) view.findViewById(android.R.id.list);
            mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            mListView.setAdapter(mAdapter);

            /*mAdapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    mListView.invalidate();
                }
            });*/

            //mListView.setAdapter(mListAdapter);

            /*mListView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity().getApplicationContext(),"List onClick", Toast.LENGTH_LONG).show();
                }
            });

            mListView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(getActivity().getApplicationContext(), "List On Long Click", Toast.LENGTH_LONG).show();
                    return false;
                }
            });*/

            // Set OnItemClickListener so we can be notified on item clicks
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Toast.makeText(getActivity().getApplicationContext(), "List Item OnClick", Toast.LENGTH_LONG).show();
                    /*int[] items = new int[3];

                    items[0] = (int) view.getTag();
                    items[1] = Integer.parseInt(((TextView) view.findViewById(R.id.qty_number)).getText().toString());
                    items[2] = Integer.parseInt(((TextView) view.findViewById(R.id.pack_size_number)).getText().toString());

                    StoreDataProvider.getInstance(getActivity().getApplicationContext()).insertItemToCheckout(items);

                    mListView.removeView(view);
                    mListView.invalidate();*/
                }
            });

            mListView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    mActionMode = mListView.startActionMode(PlaceholderFragment.this);
                    view.setSelected(true);
                    return true;
                }
            });

            mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    Toast.makeText(getActivity().getApplicationContext(), "Create Action Mode", Toast.LENGTH_LONG).show();
                    return false;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    Toast.makeText(getActivity().getApplicationContext(), "Create Action Mode", Toast.LENGTH_LONG).show();
                }
            });

            return view;
        }

        @Override
        public void onDetach() {
            super.onDetach();

            //mAdapter.closeCursor();
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    }
}
