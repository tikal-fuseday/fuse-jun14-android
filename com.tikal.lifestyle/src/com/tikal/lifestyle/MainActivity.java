package com.tikal.lifestyle;

import java.util.List;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    
    
    private GpsTrackerService gpsTrackerService;
    private static Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        
        StartAndBindService();        
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        StartAndBindService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StartAndBindService();
    }
    
    @Override
	protected void onDestroy() {
		StopAndUnbindService();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		StopAndUnbindService();
		super.onPause();
	}

	@Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
            	mTitle = getString(R.string.title_section4);
        }
        
        if (number == 4)
        {
        	GeoMapper mapper = new GeoMapper();
        	String address = mapper.getAddress(this.getApplicationContext(), 32.106535, 34.834499);
        	Toast.makeText(this.getApplicationContext(), address, Toast.LENGTH_LONG).show();
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private final ServiceConnection gpsServiceConnection = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name) {
            gpsTrackerService = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            gpsTrackerService = ((GpsTrackerService.GpsTrackingBinder) service).getService();
            //GpsTrackerService.SetServiceClient(MainActivity.this);
        }
    };
    
    private void StartAndBindService()
    {
    	serviceIntent = new Intent(this, GpsTrackerService.class);
        startService(serviceIntent);
        bindService(serviceIntent, gpsServiceConnection, Context.BIND_AUTO_CREATE);
    }
    
    private void StopAndUnbindService()
    {
    	unbindService(gpsServiceConnection);
    	stopService(serviceIntent);
    }

    public void CalcRouteAndSend()
    {
    	GpsTracker tracker = gpsTrackerService.getGpsTracker();
    	List<Location> locations = tracker.getLastLocations();
    	
    	if (locations.size() == 0)
    		return;
        Location lastLocation = locations.get(locations.size() - 1);
		float distance = locations.get(0).distanceTo(lastLocation);
        
		GeoMapper mapper = new GeoMapper();
		String location = mapper.getAddress(this.getApplicationContext(), lastLocation.getLatitude(), lastLocation.getLongitude());
        TwitterClient tc = new TwitterClient();
        tc.SendLocation(locations.get(0).getLatitude(), locations.get(0).getLongitude(), 0, "Moved " + distance + " meters to " + location, this);
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

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            

            
            ImageButton imgBtn = (ImageButton)rootView.findViewById(R.id.start_btn);
            imgBtn.setOnClickListener(new OnClickListener() 
            {
    			public void onClick(View v) 
    			{    				   				
    				
    				MainActivity activity = (MainActivity)getActivity();
    				GpsTrackerService trackerService = activity.gpsTrackerService;
    				
    				if(trackerService.isTracking())
    				{
    					trackerService.StopGpsManager();
    					//
    					
        				v.setBackgroundResource(R.drawable.start);
        				Toast.makeText(v.getContext(), "Distance calculation stopped", Toast.LENGTH_SHORT).show();

        				activity.CalcRouteAndSend();
    				} else {
        				
        				trackerService.StartGpsManager();
        				
        				v.setBackgroundResource(R.drawable.stop);
        				Toast.makeText(v.getContext(), "Distance calculation started", Toast.LENGTH_SHORT).show();
    					
    				}
    				
    			}
    		});

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
        
       
    }

}
