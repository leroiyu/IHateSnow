package com.lucky.watisrain;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import android.annotation.TargetApi;
import com.lucky.ihatesnow.R;
import com.lucky.watisrain.map.*;

import android.os.Bundle;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.util.Log;

import com.lucky.watisrain.map.DirectionsView;
import com.lucky.watisrain.map.MapView;

public class MainActivity extends Activity {

	PhotoViewAttacher attacher;
	MapView mapView;
	
	AutoCompleteTextView textStart;
	AutoCompleteTextView textEnd;
	
	public static String startAbbrev = "";
	public static String endAbbrev = "";

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		textStart = (AutoCompleteTextView) findViewById(R.id.autocomplete_loc1);
		textEnd = (AutoCompleteTextView) findViewById(R.id.autocomplete_loc2);
		
		// Get string array from /res/values/strings.xml
		String[] locations = getResources().getStringArray(R.array.locations_array);
		
		// Create adapter, set to both AutoCompleteTextViews
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, locations);
		textStart.setAdapter(adapter);
		textEnd.setAdapter(adapter);
		textEnd.setActivated(false);
		
		mapView = (MapView) findViewById(R.id.mapImageView);
		attacher = new PhotoViewAttacher(mapView);
		mapView.attacher = attacher;
		mapView.directionsView = (DirectionsView) findViewById(R.id.directions_view);
		attacher.setMaximumScale(6);
		
		// Listener called when it's tapped
		attacher.setOnPhotoTapListener(new OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				// Disable keyboard input
				InputMethodManager mgr = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
				mgr.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
				
				textStart.setCursorVisible(false);
				textEnd.setCursorVisible(false);
				
				// X and Y positions relative to image. For example, middle of image
				// is 0.5, 0.5
				
				if (textStart.getText().toString().isEmpty())
					startAbbrev = "";
				if (textEnd.getText().toString().isEmpty())
					endAbbrev = "";
				
				if (startAbbrev.isEmpty() && endAbbrev.isEmpty()) {
					mapView.handleUserTap(arg1, arg2);
					mapView.invalidate();
				}
			}
		});
		
		// Listener called when textStart option clicked
		textStart.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Fetch abbrevation for textStart selection
				startAbbrev = mapView.getAbbrev(textStart.getText().toString());
				mapView.setBuilding1(startAbbrev);
				
				// If endAbbrev not yet set, set first marker.
				if (endAbbrev.isEmpty()){
					mapView.getDirectionsView().selectDestination(startAbbrev);
					textEnd.setActivated(true);
				} else {
					mapView.updateRoute();
				}
			}
		});
		
		// Listener called when textEnd option clicked
		textEnd.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Fetch abbrevation for textEnd selection
				endAbbrev = mapView.getAbbrev(textEnd.getText().toString());
				mapView.setBuilding2(endAbbrev);
				
				if (startAbbrev.isEmpty()){	
				} else {
					mapView.updateRoute();
				}
			}
			
		});
		
		
	}


	@Override
	protected void onStart(){
		super.onStart();

		// Make the zoom reasonable
		attacher.setScale(1.6f, 2312f, 400f, true);

	}
	
	
	// Handle action bar
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			case R.id.action_clear:
				mapView.clearRoute();
				break;
			case R.id.action_settings:
				Global.showSettings(this, mapView);
				break;
		}
		
		return true;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		mapView.clearBtn = menu.findItem(R.id.action_clear);
		if(mapView.clearBtn != null)
			mapView.clearBtn.setVisible(false);
		
		return true;
	}

}
