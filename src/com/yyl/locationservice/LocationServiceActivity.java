package com.yyl.locationservice;
import com.yyl.locationservice.database.*;

import java.util.Calendar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;

public class LocationServiceActivity extends FragmentActivity
	implements LoaderManager.LoaderCallbacks<Cursor>{
	
	private TextView tv;
	private PendingIntent tracking;
	private AlarmManager alarms;
	SimpleCursorAdapter adapter;
	
	private long UPDATE_INTERVAL = 30000;
	private int START_DELAY = 5;
	private String DEBUG_TAG = "LocationServiceActivity";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        drawTable();
        alarms = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
    }
    
    public void start(View view){
    	tv = (TextView) findViewById(R.id.tv1);
		tv.setText("Start tracking");
        setRecurringAlarm(getBaseContext());
    }
    
    public void stop(View view){
    	tv = (TextView) findViewById(R.id.tv1);
		tv.setText("Stop tracking");
		Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
    	tracking = PendingIntent.getBroadcast(getBaseContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    	alarms = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		alarms.cancel(tracking);
		Log.d(DEBUG_TAG, ">>>Stop tracking()");
    }
    
    private void setRecurringAlarm(Context context) {
    	 
        // get a Calendar object with current time
        Calendar cal = Calendar.getInstance();
        // add 5 minutes to the calendar object
        cal.add(Calendar.SECOND, START_DELAY);
     
        Intent intent = new Intent(context, AlarmReceiver.class);
        
        tracking = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        
        alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), UPDATE_INTERVAL, tracking);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	drawTable();
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    }
    
    private void drawTable() {
    	// Fields from the database (projection)
     	// Must include the _id column for the adapter to work
     	String[] from = new String[] { LocTable.COLUMN_ID, LocTable.COLUMN_TIME, 
     			LocTable.COLUMN_LONGITUDE, LocTable.COLUMN_LATITUDE };
     	// Fields on the UI to which we map
     	int[] to = new int[] { R.id.rowid, R.id.time, R.id.longitude, R.id.latitude };
     	getSupportLoaderManager().initLoader(0, null, this);
     	adapter = new SimpleCursorAdapter(this, R.layout.row, null, from,
     			to, 0);
     	ListView listview = (ListView) findViewById(R.id.list);
        listview.setAdapter(adapter);
    }
    
    // Creates a new loader after the initLoader () call
 	@Override
 	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
 		String[] projection = { LocTable.COLUMN_ID, LocTable.COLUMN_TIME, 
 				LocTable.COLUMN_LONGITUDE, LocTable.COLUMN_LATITUDE };
 		CursorLoader cursorLoader = new CursorLoader(this,
 				LocContentProvider.CONTENT_URI, projection, null, null, null);
 		return cursorLoader;
 	}

 	@Override
 	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
 		adapter.swapCursor(data);
 	}

 	@Override
 	public void onLoaderReset(Loader<Cursor> loader) {
 		// data is not available anymore, delete reference
 		adapter.swapCursor(null);
 	}
}