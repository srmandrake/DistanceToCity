package com.devbyrod.distancetocity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends Activity implements ActionBar.TabListener,
                                                        LocationListener,
                                                        FragmentMain.OnFragmentInteractionListener,
                                                        FragmentDisplayInfo.OnFragmentInteractionListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private DatabaseController mDatabaseController;
    private LocationManager mLocationManager;
    private Location mDeviceLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseController = new DatabaseController( this );

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.v( Constants.TAG, "onLocationChanged(): lat = " + location.getLatitude() + ", lon = " + location.getLongitude() );
        this.mDeviceLocation = new Location( location );

        //save this location in preferences
        this.setLocationInSharedPreferences( location );
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

        Log.v(Constants.TAG, "onStatusChanged: Provider(" + provider + "), status = " + status);
    }

    @Override
    public void onProviderEnabled(String provider) {

        Log.v( Constants.TAG, "onProviderEnabled: Provider(" + provider + ")" );
    }

    @Override
    public void onProviderDisabled(String provider) {

        Log.v(Constants.TAG, "onProviderDisabled: Provider(" + provider + ")");
    }

    @Override
    public void onBtnSearchClick(String s) {

        this.processFourSquareResponse(s);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private Location getLocationFromSharedPreferences(){

        SharedPreferences sharedPreferences = getPreferences( MODE_PRIVATE );
        Double lat = new Double( sharedPreferences.getString( Constants.SHARED_PREFERENCES_LAT, "0" ) );
        Double lon = new Double( sharedPreferences.getString( Constants.SHARED_PREFERENCES_LON, "0" ) );

        Location location = new Location( "" );
        location.setLatitude(lat);
        location.setLongitude(lon);;

        return location;
    }

    private void setLocationInSharedPreferences( Location location ){

        SharedPreferences sharedPreferences = getPreferences( MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString( Constants.SHARED_PREFERENCES_LAT, new Double( location.getLatitude() ).toString() );
        editor.putString( Constants.SHARED_PREFERENCES_LON, new Double( location.getLongitude() ).toString() );
        editor.commit();
    }

    private boolean processFourSquareResponse( String s ){

        boolean response = false;

        String data = "";

        ExecutorService executor = null;

        try{

            executor = Executors.newSingleThreadExecutor();
            Callable< String > httpCallable = new HttpCallable( s );
            Future< String > future = executor.submit( httpCallable );

            data = future.get();

            if(data == null){

                throw( new Exception( "Null Data Received from Future!") );
            }

            response = this.processFourSquareResponseInternal( s, data );
        }
        catch ( Exception e ){

            Toast.makeText( this, "Getting Information from Local DataBase", Toast.LENGTH_LONG ).show();

            data = this.mDatabaseController.getSavedQueryResult( s );
            response = this.processFourSquareResponseInternal( s, data );
        }
        finally {

            if(executor != null){

                executor.shutdown();
            }
        }

        return response;
    }

    private boolean processFourSquareResponseInternal( String query, String data ){

        boolean response = false;

        try {

            JSONObject info = new JSONObject( data );
            JSONObject meta = info.getJSONObject( "meta" );

            int code = meta.getInt("code");

            if( code == Constants.HTTP_RESPONSE_CODE_OK ){

                JSONArray venues = info.getJSONObject( "response" ).getJSONArray("venues");

                List<Venue> venuesList = new ArrayList<Venue>();

                for( int i = 0; i < venues.length(); i++ ){

                    JSONObject venue = venues.getJSONObject( i );
                    JSONObject venueLocationInfo = venue.getJSONObject("location");
                    JSONArray venueCategories = venue.getJSONArray("categories");

                    String venueCategory = "Not Set";

                    if( venueCategories.length() > 0 ) {

                        JSONObject category = venueCategories.getJSONObject(0);

                        venueCategory = (category == null) ? "Not Set" : category.getString("name");
                    }

                    double lon = venueLocationInfo.getDouble("lng");
                    double lat = venueLocationInfo.getDouble("lat");

                    String venueAddress = "";
                    JSONArray formattedAddress = venueLocationInfo.getJSONArray("formattedAddress");

                    for( int j = 0; j < formattedAddress.length(); j++ ){

                        venueAddress += formattedAddress.get( j ) + ", ";
                    }

                    //replace last comma and blank with a period
                    venueAddress = venueAddress.substring( 0, venueAddress.length() - 2 ) + ".";

                    venuesList.add( new Venue( venue.getString("name"), venueCategory, venueAddress, lat, lon, this.mDeviceLocation ) );
                }

                ListView listView = (ListView) findViewById(R.id.list_venues);

                ListAdapter listAdapter = new ListAdapter( venuesList, getApplicationContext() );

                listView.setAdapter( listAdapter );

                response = true;
            }

            //The result was processed correctly, so insert it in the Database
            if( !this.mDatabaseController.insertQueryResult( query, data ) ){

                Toast.makeText( this, "Unable to insert record in Database", Toast.LENGTH_LONG ).show();
            }
        }
        catch ( Exception e ){

            e.printStackTrace();
        }

        return response;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return FragmentMain.newInstance(position + 1);

            if( position == 0 ){

                return new FragmentDisplayInfo();
            }

            return FragmentMain.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }

}
