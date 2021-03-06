package com.example.sainath.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sainath.storedata.StoreDataProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

    private ArrayList<String> mCategoryList;

    public static boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mCategoryList = StoreDataProvider.getInstance(this.getApplicationContext()).getAllCategories();

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1, mCategoryList != null ? mCategoryList.get(position) : UIConstants.DEFAULT_CATEGORY))
                .commit();
    }

    //Change the Title of the screen
    public void onSectionAttached(int number) {
        if(mCategoryList != null && mCategoryList.size() > 0)
            mTitle = mCategoryList.get(number - 1);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        switch (id)
        {
            case R.id.action_enterData:
                //startActivity(new Intent(this.getApplicationContext(), StoreFormActivity.class));

                if(isFirst) {
                    // Log.i("Sainath",Environment.getExternalStorageDirectory().getAbsolutePath());
                    new Runnable(){
                        @Override
                        public void run() {
                            File testF = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Documents", "grocery.txt");//new File("groc.txt");

                            try {
                                Scanner sc = new Scanner(testF);

                                while (sc.hasNextLine()) {
                                    String[] lineData = sc.nextLine().split(",");

                                    StoreDataProvider.getInstance(getApplicationContext()).insertValuestoStoreDB(lineData);
                                }
                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Products are added to Store Database", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }.run();

                    isFirst = false;
                }
                break;

            case R.id.action_view_cart:
                startActivity(new Intent(this.getApplicationContext(), ItemListActivity.class));
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private static final String ARG_SECTION_NAME = "section_name";

        /**
         * The pager widget, which handles animation and allows swiping horizontally to access previous
         * and next wizard steps.
         */
        private ViewPager mPager;

        /**
         * The pager adapter, which provides the pages to the view pager widget.
         */
        private PagerAdapter mPagerAdapter;

        private final int interval = 5000; // 1 Second
        private Handler handler = new Handler();
        private Runnable runnable = null;

        private ItemListAdapter mListAdapter;

        private int mDrawerSelection = -1;

        private String mCategorySelected = null;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, String category) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_SECTION_NAME, category);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState) {
            mDrawerSelection = getArguments().getInt(ARG_SECTION_NUMBER);
            mCategorySelected = getArguments().getString(ARG_SECTION_NAME);

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            final EditText searchItem = (EditText) rootView.findViewById(R.id.searchItem);

            final ImageButton cancelBtn = (ImageButton)rootView.findViewById(R.id.cancelButton);

            final ListView itemList = (ListView) rootView.findViewById(R.id.ItemslistView);

            mListAdapter = new ItemListAdapter(this.getActivity().getApplicationContext(), (LayoutInflater)this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            mListAdapter.setListType("selectable");

            if(mCategorySelected.equalsIgnoreCase(UIConstants.DEFAULT_CATEGORY)) {
                // Instantiate a ViewPager and a PagerAdapter.
                mPager = (ViewPager) rootView.findViewById(R.id.home_screen_offer_viewer);
                mPagerAdapter = new ScreenSlidePagerAdapter(this.getFragmentManager());
                mPager.setAdapter(mPagerAdapter);

                runnable = new Runnable() {
                    public void run() {
                        int nextItem = mPager.getCurrentItem() + 1;
                        if (nextItem == NUM_PAGES) {
                            nextItem = 0;
                        }

                        mPager.setCurrentItem(nextItem, true);
                    }
                };

                this.getActivity().runOnUiThread(runnable);

                handler.postDelayed(runnable, interval);

                mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        handler.postDelayed(runnable, interval);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }
            else {
                if(mPager != null)
                    mPager.setVisibility(View.GONE);

                itemList.setVisibility(View.VISIBLE);
                //Toast.makeText(getActivity().getApplicationContext(),"Selected Category : "+mCategorySelected, Toast.LENGTH_SHORT).show();

                String[] args = {mCategorySelected};
                mListAdapter.updateSelectionArgs(UIConstants.SearchType.CATEGORY_ALL_PRODUCTS,args);

                itemList.setAdapter(mListAdapter);
            }

            searchItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    cancelBtn.setVisibility(View.VISIBLE);
                    itemList.setVisibility(View.VISIBLE);
                    if (mPager != null)
                        mPager.setVisibility(View.GONE);

                    return false;
                }
            });

            TextWatcher searchTextWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String searchText = searchItem.getText().toString();

                    if(searchText == null || searchText.isEmpty() || searchText.contains(" "))
                    {
                        //cancelBtn.setVisibility(View.GONE);
                        if(mPager != null)
                            mPager.setVisibility(View.VISIBLE);

                        itemList.setVisibility(View.VISIBLE);

                        String[] args = {mCategorySelected};
                        mListAdapter.updateSelectionArgs(UIConstants.SearchType.CATEGORY_ALL_PRODUCTS, args);

                        itemList.setAdapter(mListAdapter);
                    }
                    else
                    {
                        String[] args = {"%"+searchText+"%"};

                        if(mCategorySelected.equalsIgnoreCase(UIConstants.DEFAULT_CATEGORY))
                        {
                            mListAdapter.updateSelectionArgs(UIConstants.SearchType.ALL_PRODUCTS_SEARCH, args);
                        }
                        else {
                            //TBD add category to arguments
                            String[] argsCategory = {"%"+searchText+"%",mCategorySelected};
                            mListAdapter.updateSelectionArgs(UIConstants.SearchType.CATEGORY_PRODUCTS_SEARCH, argsCategory);
                        }
                        itemList.setAdapter(mListAdapter);

                        if(mPager != null)
                            mPager.setVisibility(View.GONE);
                        itemList.setVisibility(View.VISIBLE);

                        itemList.invalidate();
                        itemList.invalidateViews();
                    }
                }
            };

            searchItem.addTextChangedListener(searchTextWatcher);

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        @Override
        public void onResume() {
            super.onResume();
        }

        private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
            public ScreenSlidePagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int position) {
                return ScreenSlidePageFragment.create(position);
                //return ItemFragment.newInstance(null,null);
            }

            @Override
            public int getCount() {
                return NUM_PAGES;
            }
        }
    }

}
