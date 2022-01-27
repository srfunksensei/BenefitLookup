package rs.co.pamet.android.benefit_lookup.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

import rs.co.pamet.android.benefit_lookup.R;
import rs.co.pamet.android.benefit_lookup.controls.adapters.DoctorsAdapter;
import rs.co.pamet.android.benefit_lookup.location.DoctorPlacemark;
import rs.co.pamet.android.benefit_lookup.location.DoctorsItemizedOverlay;
import rs.co.pamet.android.benefit_lookup.location.GPSTracker;
import rs.co.pamet.android.benefit_lookup.location.LocationTask;
import rs.co.pamet.android.benefit_lookup.location.MapKmlHelper;
import rs.co.pamet.android.benefit_lookup.location.directions.DirectionsTask;
import rs.co.pamet.android.benefit_lookup.location.directions.Route;
import rs.co.pamet.android.benefit_lookup.location.directions.Segment;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

@SuppressWarnings("unused")
public class FlipperActivity extends MapActivity {

	/**
	 * Identifies "Redo" menu item
	 */
	private static final int MENU_ID_REFRESH = 0;
	/**
	 * Identifies "Satelite" menu item
	 */
	private static final int MENU_ID_SATELLITE = 1;
	/**
	 * Identifies "Map" menu item
	 */
	private static final int MENU_ID_MAP = 2;

	private GPSTracker gps;

	private ListView doctorsListView;
	private MapView mapView;

	/**
	 * To adjust the map display (zoom, etc)
	 */
	private MapController mapController;

//	/**
//	 * Mark of current location
//	 */
//	private MyLocationOverlay myLocationOverlay;

	/**
	 * Trenutna lokacija
	 */
	private GeoPoint myLocation;

	private static Location currentLocation;

	/**
	 * Lista stvari koje se prikazuju preko mape
	 */
	private List<Overlay> mapOverlays;

	/**
	 * Overlay for current location
	 */
	private DoctorsItemizedOverlay myLocOverlay;

	// /** nikola */

	public static LinkedList<DoctorPlacemark> placemarks = new LinkedList<DoctorPlacemark>();

	public ProgressDialog progressDialog;

	private int cost = 0;
	private int distance = 0;
	private String zipOrAddressStr;
	private EditText zipOrAddress;
	private ArrayAdapter<String> addressAdapter;
	private ArrayList<String> adapterList;
	
	private LinkedList<DoctorPlacemark> filteredPlacemarks;
	private Location tmpLocation;
	boolean locationChanged;
	private LinkedList<DoctorPlacemark> tmpPlacemarks;
	DoctorsAdapter doctorsAdapter;
	private ViewFlipper vf;
	private TextView flipperText;
	private float lastX;
	ImageButton swapView;
	ImageButton filter;
	String serviceText;
	ImageButton costButton;
	ImageButton distanceButton;
	ImageButton deletText;
	Dialog filterDialog;
	Dialog doctorDialog;
	Dialog directionsDialog;
	
	DoctorPlacemark tappedPlacemark;
	
	private boolean isFilterDialogVisible;
	
	private LocationTask lTask;
	private DirectionsTask dTask;
	
	private ImageButton myLoc;
	private boolean doctorDialogVisible;
	private boolean directionsDialogVisible;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_map_list_flipper);
		
		vf = (ViewFlipper) findViewById(R.id.view_flipper);

		swapView = (ImageButton) findViewById(R.id.listButton);
		swapView.setOnClickListener(swapListener);

		filter = (ImageButton) findViewById(R.id.filterButton);
		filter.setOnClickListener(filterListener);

		serviceText = getIntent().getStringExtra("selectedService");

		flipperText = (TextView) findViewById(R.id.flipperText);
		flipperText.setText(serviceText);	
		
		costButton = (ImageButton) findViewById(R.id.costSortButton);
		costButton.setOnClickListener(costSort);
		costButton.setVisibility(View.INVISIBLE);
		
		distanceButton = (ImageButton) findViewById(R.id.distanceSortButton);
		distanceButton.setOnClickListener(distanceSort);
		distanceButton.setVisibility(View.INVISIBLE);
		// my location button
		myLoc = (ImageButton) findViewById(R.id.myLocation);
		myLoc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				backToMyLocation();
			}
			
		});
		
		
		Object retained = getLastNonConfigurationInstance(); 
		
		if(retained instanceof LocationTask){
			lTask = (LocationTask)retained;
			lTask.setContext(this);
			gps = new GPSTracker(lTask);
			
			locationChanged = lTask.isLocChanged();
			if(locationChanged) gps.stopUsingGPS();
			else gps.getLocation();
			
			
			if(lTask.getStatus()==AsyncTask.Status.FINISHED){
				
				currentLocation = lTask.getLoc();
				if(currentLocation!=null){
					Log.d("rotation", currentLocation.getLatitude()+"");
				}
				
				placemarks = lTask.getDoctors();
				if(placemarks!=null){
					Log.d("rotation", placemarks.size()+"");
				}
				myLocation = lTask.getMyLocation();
				
				cost = lTask.getCost();
				distance = lTask.getDistance();
				zipOrAddressStr = lTask.getZipOrAddressStr();
				configureMapView();

				Log.d("rotation", tmpLocation==null?"null before":"not null before");
				
				tmpLocation = lTask.getTmpLocation();
				Log.d("rotation", tmpLocation==null?"null after":"not null after");
				
				
				if(locationChanged){
					filteredPlacemarks = lTask.getFilteredPlacemarks();
					showFilteredMap(locationChanged);
					Log.d("rotation", "locationChanged");
				}else{
					setupMapView();
					Log.d("rotation", "location Not Changed");
				}
				setupDoctorsView();
				
				vf.setDisplayedChild(lTask.getDisplayedChild());
				
				if(vf.getDisplayedChild()!=0){
					costButton.setVisibility(View.VISIBLE);
					distanceButton.setVisibility(View.VISIBLE);
					swapView.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_mapmode));
				}else{
					costButton.setVisibility(View.INVISIBLE);
					distanceButton.setVisibility(View.INVISIBLE);
					swapView.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_list));	
					}
				isFilterDialogVisible = lTask.isFilterDialogVisible();
				if(isFilterDialogVisible){
					setUpFilterDialog(FlipperActivity.this);
				}
				doctorDialogVisible = lTask.isDoctorDialogVisible();
				
				tappedPlacemark = lTask.getTappedPlacemark();
				if(doctorDialogVisible){
					setupDoctorDialog(tappedPlacemark);
				}
				
				directionsDialogVisible = lTask.isDirectionsDialogVisible();
				if(directionsDialogVisible){
					showDirections(lTask.getRoute(), FlipperActivity.this);
				}
				
			}else{
				if(mapView==null){
					configureMapView();
				}
				progressDialog = new ProgressDialog(FlipperActivity.this);
				progressDialog.setMessage("Locating...");
				progressDialog.show();

				
			}
		}else{
			configureMapView();
			
			lTask= new LocationTask(FlipperActivity.this);
			
			gps = new GPSTracker(lTask);
			gps.getLocation();
			
			progressDialog = new ProgressDialog(FlipperActivity.this);
			progressDialog.setMessage("Locating...");
			progressDialog.show();
			
		}
		

	}

	public void setupDoctorDialog(DoctorPlacemark tmpPlacemark){
		try {
			tappedPlacemark = tmpPlacemark;
			 doctorDialog = new Dialog(FlipperActivity.this);
			// final int docInd = index;
//			tappedPlacemark = mOverlays.get(index);
			 
			doctorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					doctorDialogVisible = false;
					
				}
			}); 
			doctorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			doctorDialog.setContentView(R.layout.doctor_dialog_full);
			doctorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			// doctors title
			TextView textView = (TextView) doctorDialog.findViewById(R.id.docDialogTitle);
			textView.setText(tappedPlacemark.getDoctorsInfo().getName());

			// doctors info
			textView = (TextView) doctorDialog.findViewById(R.id.docDialogInfo);
			textView.setText(Html.fromHtml(tappedPlacemark.toString()));

			// cancel button
			ImageView imageView = (ImageView) doctorDialog.findViewById(R.id.closeButton);
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					doctorDialog.dismiss();
					doctorDialogVisible = false;
				}
			});
		
			// details button
			Button btn = (Button) doctorDialog.findViewById(R.id.docDialogDetails);
			btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent detIntent = new Intent(FlipperActivity.this, DoctorsInfoActivity.class);
					detIntent.putExtra("doctorOrdinal", FlipperActivity.placemarks.indexOf(tappedPlacemark));
					doctorDialog.dismiss();
					doctorDialogVisible = false;
					startActivity(detIntent);

				}
			});

			// directions button
			btn = (Button) doctorDialog.findViewById(R.id.docDialogDirections);
			btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					GeoPoint dest = tappedPlacemark.getPoint();
					GeoPoint start = new GeoPoint((int) (FlipperActivity.getCurrentLocation().getLatitude() * 1e6), 
													(int)(FlipperActivity.getCurrentLocation().getLongitude() * 1e6));

					dTask = new DirectionsTask(FlipperActivity.this);
					doctorDialog.dismiss();
					doctorDialogVisible = false;
					dTask.execute(start, dest);

				}
			});

			doctorDialog.show();
			doctorDialogVisible = true;

		} catch (NullPointerException e) {

			e.printStackTrace();
		}
	}
	

	public void showDirections(Route route, Context context) {
		
		lTask.setRoute(route);
		
		directionsDialog = new Dialog(context);
		directionsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		directionsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		directionsDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				directionsDialogVisible = false;
				
			}
		});
		
		StringBuffer sb = new StringBuffer();

		sb.append(" <b>Distance:</b> " + route.getLength() + "m<br/><br/>");

		for (Segment seg : route.getSegments()) {
			String inst = "<b>" + seg.getLength() + "m:</b>  "
					+ seg.getInstruction() + "<br/>";
			sb.append(inst);
			Log.d("instruction", inst);
		}

		directionsDialog.setContentView(R.layout.instructions);

		TextView text = (TextView) directionsDialog.findViewById(R.id.instrText);
		text.setText(Html.fromHtml(sb.toString()));

		ImageView cancel = (ImageView) directionsDialog.findViewById(R.id.closeButton);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				directionsDialog.dismiss();
			}
		});

		directionsDialog.show();
		directionsDialogVisible = true;
	}

	
	@Override
	public Object onRetainNonConfigurationInstance() {
		Log.d("rotation", "onRetain start");
		
		if(lTask!=null){
			Log.d("rotation", "onRetain not null");
			if(currentLocation!=null){
				lTask.setLoc(currentLocation);
			}
			if(placemarks!=null){
				lTask.setDoctors(placemarks);
			}
			if(mapView!=null){
				lTask.setMapView(mapView);
			}
			if(mapOverlays!=null){
				lTask.setMapOverlays(mapOverlays);
			}
			if(myLocation!=null){
				lTask.setMyLocation(myLocation);
			}
			if(tmpLocation!=null){
				Log.d("rotation", "tmpLocation not null");
				lTask.setTmpLocation(tmpLocation);
			}
			lTask.setFilteredPlacemarks(filteredPlacemarks);
			lTask.setLocChanged(locationChanged);
			lTask.setContext(null);
			lTask.setCost(cost);
			lTask.setDistance(distance);
			lTask.setZipOrAddressStr(zipOrAddressStr);
			lTask.setDisplayedChild(vf.getDisplayedChild());

			lTask.setFilterDialogVisible(isFilterDialogVisible);
			lTask.setDoctorDialogVisible(doctorDialogVisible);
			lTask.setTappedPlacemark(tappedPlacemark);
			lTask.setDirectionsDialogVisible(directionsDialogVisible);
		}
		
		if(isFilterDialogVisible){
			filterDialog.dismiss();
		}
		if(doctorDialogVisible){
			doctorDialog.dismiss();
		}
		
		if(directionsDialogVisible){
			directionsDialog.dismiss();
		}
		return lTask;
	}
	
	
	
//	@Override
//	public void finish() {
//		Log.e("finish", "finish");
//		
//		if(!lTask.isCancelled()){
//			lTask.cancel(true);
//		}
//		lTask = null;
//		
//		if(progressDialog != null &&
//				progressDialog.isShowing()){
//			progressDialog.dismiss();
//		}
//		
//		super.finish();
//	}



	private OnClickListener costSort = new OnClickListener() {
		
		@SuppressWarnings("unchecked")
		@Override
		public void onClick(View v) {
			MapKmlHelper kml = new MapKmlHelper();
			
			LinkedList<DoctorPlacemark> plm = new LinkedList<DoctorPlacemark>();
			if(filteredPlacemarks!=null/*&&filteredPlacemarks.size()>0*/){
				plm = filteredPlacemarks;
			}else{
				tmpPlacemarks = (LinkedList<DoctorPlacemark>) placemarks.clone();
				plm = (LinkedList<DoctorPlacemark>) placemarks.clone();
				Log.d("sort", "costClicked "+placemarks.size());
			}
			Log.d("sort", "costClicked"+plm.size());
			kml.sortByCost(plm);
			
			doctorsAdapter.clear();
			for (DoctorPlacemark dp : plm) {
				doctorsAdapter.add(dp);
			}
			doctorsAdapter.notifyDataSetChanged();
			Log.d("sort", "costClicked"+plm.size());
		}
	};


	
	@Override	public boolean onTouchEvent(MotionEvent touchevent) {

		switch (touchevent.getAction()) {

		case MotionEvent.ACTION_DOWN: {

			lastX = touchevent.getX();

			break;

		}

		case MotionEvent.ACTION_UP: {

			float currentX = touchevent.getX();

			if (lastX < currentX) {

				if (vf.getDisplayedChild() == 0)
					break;

				vf.setInAnimation(FlipperActivity.this, R.anim.in_from_left);

				vf.setOutAnimation(FlipperActivity.this, R.anim.out_to_right);

				vf.showNext();
				swapView.setImageDrawable(getResources().getDrawable(
						android.R.drawable.ic_menu_mapmode));
			}

			if (lastX > currentX)

			{

				if (vf.getDisplayedChild() == 1)
					break;

				vf.setInAnimation(FlipperActivity.this, R.anim.in_from_right);

				vf.setOutAnimation(FlipperActivity.this, R.anim.out_to_left);

				vf.showPrevious();
				swapView.setImageDrawable(getResources().getDrawable(
						R.drawable.ic_menu_list));
			}

			break;

		}

		}

		return false;

	}

	/* Request updates at startup */
	@Override
	protected void onResume() {
		super.onResume();

//		if(gps!=null){
//			if (gps.canGetLocation()) {
//				// locationManager.requestLocationUpdates(provider, minTime,
//				// minDistance, listener); TODO
//				gps.getLocation();
//			} else {
//				gps.showSettingsAlert();
//			}
//		}
//		
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (progressDialog != null)
			progressDialog.dismiss();
	}

	/**
	 * @see MapActivity.isRouteDisplayed
	 */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		if (true) {
			menu.clear();
			inflater.inflate(R.menu.activity_tabbed_list_map, menu);

		} else {
			menu.clear();
			inflater.inflate(R.menu.empty, menu);

		}
		return true;
	}

	/**
	 * Creates an option menu by adding the appropriate items
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		if (true) {
			inflater.inflate(R.menu.activity_tabbed_list_map, menu);
		} else {
			inflater.inflate(R.menu.empty, menu);
		}
		return true;

	}

	/**
	 * Reaguje na klik na polje u meniju. U slucaju izbora opcije "Ponovi",
	 * restartuje se Mapa MapActivity
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.menu_refresh:

			startActivity(getIntent());
			finish();
			break;

		case R.id.menu_satellite:

			mapView.setSatellite(true);
			break;

		case R.id.menu_map:

			mapView.setSatellite(false);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * @return the mapOverlays
	 */
	public List<Overlay> getMapOverlays() {
		return mapOverlays;
	}

	/**
	 * @param currentLocation
	 *            the currentLocation to set
	 */
	public static void setCurrentLocation(Location currentLocation) {
		FlipperActivity.currentLocation = currentLocation;
	}

	/**
	 * @return the currentLocation
	 */
	public static Location getCurrentLocation() {
		return currentLocation;
	}


	private void configureMapView() {
		// cofigure my location overlay
		myLocOverlay = new DoctorsItemizedOverlay(getResources().getDrawable(
				R.drawable.my_location), FlipperActivity.this);

		// Configure the Map
		mapView = (MapView) findViewById(R.id.map_view);
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(false);
		mapView.invalidate();

		// Configure the MapContorler
		mapController = mapView.getController();
		mapController.setZoom(14); // Zoom 1 is world view

	}

	public void showFilteredMap(boolean locationChanged) {
		mapView.invalidate();
		mapOverlays = mapView.getOverlays();

		DoctorsItemizedOverlay dOverlay = 
				new DoctorsItemizedOverlay(getResources().getDrawable(R.drawable.location),	FlipperActivity.this);

		for (int i = 0; i < filteredPlacemarks.size(); i++) {

			dOverlay.addOverlay(filteredPlacemarks.get(i));
			

		}
		if(locationChanged){
			myLocation = new GeoPoint((int) (tmpLocation.getLatitude() * 1e6), (int) (tmpLocation.getLongitude() * 1e6));
			Log.d("placemarks", "tmpLocation: "+tmpLocation.getLatitude());
			
		}else{
			Log.d("placemarks", "currentLocation: "+currentLocation.getLatitude());
			myLocation = new GeoPoint((int) (currentLocation.getLatitude() * 1e6), (int) (currentLocation.getLongitude() * 1e6));
		}

		myLocOverlay.clear();
		myLocOverlay.addOverlay(new DoctorPlacemark(myLocation, "me","my location", 0));

		mapOverlays.add(dOverlay);
		mapOverlays.add(myLocOverlay);

		mapController.animateTo(myLocation);
	}

	/**
	 * returns indicator of location back to my current location
	 */
	public void backToMyLocation() {
		
//		tmpLocation = currentLocation;
		tmpLocation = new Location(currentLocation);
		
		// LinkedList<DoctorPlacemark> tmpPlacemarks =
		// (LinkedList<DoctorPlacemark>) placemarks.clone();
		if (tmpPlacemarks != null) {
			placemarks = (LinkedList<DoctorPlacemark>) tmpPlacemarks.clone();
		} else {
			tmpPlacemarks = (LinkedList<DoctorPlacemark>) placemarks.clone();
		}
		
		MapKmlHelper kml = new MapKmlHelper();
		tmpPlacemarks = kml.recalculateDistances(tmpPlacemarks, gps.getLocation());

		filteredPlacemarks = kml.getFilteredPlacemarks(tmpPlacemarks, cost, distance);

		if (filteredPlacemarks != null && filteredPlacemarks.size() > 0) {
			
			tmpPlacemarks = (LinkedList<DoctorPlacemark>) placemarks.clone();
			doctorsAdapter.clear();

			for (DoctorPlacemark dp : filteredPlacemarks) {
				doctorsAdapter.add(dp);
			}
			doctorsAdapter.notifyDataSetChanged();
			
			mapView.getOverlays().clear();
			configureMapView();
			showFilteredMap(true);
//			setFilteredText(locationChanged);
		} else {
			Toast.makeText(FlipperActivity.this, "No doctors found!", Toast.LENGTH_LONG).show();
		}
		
		
//		mapOverlays.clear();
//		tmpLocation =  currentLocation;
//		if(locationChanged){
//			showFilteredMap(locationChanged);
//			Log.d("placemarks", "locationChanged");
//		}else{
//			setupMapView();
//			Log.d("placemarks", "location Not Changed");
//		}
//		MapKmlHelper kml = new MapKmlHelper();
//		if(filteredPlacemarks!=null){
//			filteredPlacemarks = kml.recalculateDistances(filteredPlacemarks, currentLocation);
//			
//		}
//			
//		placemarks = kml.recalculateDistances(placemarks, currentLocation);
//		doctorsAdapter.notifyDataSetChanged();
//		
		
		
//		GeoPoint tmpPoint = new GeoPoint(
//				(int) (currentLocation.getLatitude() * 1e6),
//				(int) (currentLocation.getLongitude() * 1e6));
//		
//		Log.d("placemarks", "back to my loc "+tmpPoint.getLatitudeE6()+"  "+tmpPoint.getLongitudeE6());
//		Log.d("placemarks", "back to my loc myLocation "+myLocation.getLatitudeE6()+"  "+myLocation.getLongitudeE6());
//		if(tmpLocation!=null){
//			tmpLocation = currentLocation;
//		}
//		
////		if (myLocation.getLatitudeE6() != tmpPoint.getLatitudeE6()
////				|| myLocation.getLongitudeE6() != tmpPoint.getLongitudeE6()) {
//			myLocation = tmpPoint;
//
//			// remove previous overlay because of duplication 
//			mapOverlays.remove(myLocOverlay);
//
//			myLocOverlay.clear();
//			myLocOverlay.addOverlay(new DoctorPlacemark(myLocation, "me","my location", 0));
//			
//			
//			MapKmlHelper kml = new MapKmlHelper();
//			if(locationChanged){						
//				filteredPlacemarks = kml.recalculateDistances(filteredPlacemarks, currentLocation);
//				doctorsAdapter.notifyDataSetChanged();
//				Log.d("placemarks", "back to my loc locationChanged");
//			}else{
//				placemarks = kml.recalculateDistances(placemarks, currentLocation);
//				doctorsAdapter.notifyDataSetChanged();
//				Log.d("placemarks", "back to my loc locationNotChanged");
//			}
//			
//			
//			mapOverlays.add(myLocOverlay);
//
////		}
//		mapController.animateTo(myLocation);

	}

	/**
	 * Configures myLocationOverlay and mapOverlays
	 */
	private void setupMapView() {
		myLocation = new GeoPoint((int) (currentLocation.getLatitude() * 1e6),
				(int) (currentLocation.getLongitude() * 1e6));

		if (mapOverlays != null) {
			mapOverlays.clear();
		}
		mapOverlays = mapView.getOverlays();

		DoctorsItemizedOverlay dOverlay = new DoctorsItemizedOverlay(
				getResources().getDrawable(R.drawable.location),
				FlipperActivity.this);
		DoctorsItemizedOverlay bOverlay = new DoctorsItemizedOverlay(
				getResources().getDrawable(R.drawable.my_location),
				FlipperActivity.this);
		bOverlay.addOverlay(new DoctorPlacemark(myLocation, "me",
				"my location", 0));
		for (int i = 0; i < placemarks.size(); i++) {

			dOverlay.addOverlay(placemarks.get(i));

		}
		mapOverlays.add(dOverlay);
		mapOverlays.add(bOverlay);

		mapController.animateTo(myLocation);
	}

	/**
	 * 
	 * Sets up tab doctors with doctors list
	 * 
	 */
	private void setupDoctorsView() {

		doctorsListView = (ListView) findViewById(R.id.doctors_list);
		doctorsAdapter = new DoctorsAdapter(FlipperActivity.this,
				R.layout.doctor_info_item, FlipperActivity.placemarks);

		doctorsListView.setOnCreateContextMenuListener(FlipperActivity.this);
		doctorsListView.setVisibility(View.VISIBLE);
		doctorsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent i = new Intent(FlipperActivity.this,
						DoctorsInfoActivity.class);
				i.putExtra("doctorOrdinal", position);
				startActivity(i);
			}
		});

		doctorsListView.setAdapter(doctorsAdapter);
	}

	public void onLocationTaskCompleted() {
		progressDialog.dismiss();
		
		setupDoctorsView();
		
		
		setupMapView();

	}

	/**
	 * Listener for filter Button
	 */
	private OnClickListener filterListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setUpFilterDialog(FlipperActivity.this);
		}
	};

	private void setUpFilterDialog(Context context){
		filterDialog = new Dialog(context);
		filterDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				isFilterDialogVisible = false;
				
			}
		});
		filterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		filterDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		filterDialog.setContentView(R.layout.locations_filter);
		
		filterDialog.setCancelable(true);

		addressAdapter = new ArrayAdapter<String>(FlipperActivity.this, R.layout.services_list_item, R.id.servicesItemText, adapterList);

		zipOrAddress = (EditText) filterDialog.findViewById(R.id.address);
		if(zipOrAddressStr!=null&&zipOrAddressStr.length()>0){
			zipOrAddress.setText(zipOrAddressStr);
		}
		deletText =  (ImageButton) filterDialog.findViewById(R.id.deleteText);
		deletText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				zipOrAddress.setText("");
				
			}
		});
		Button cancelFilter = (Button) filterDialog.findViewById(R.id.filterCancelButton);


		cancelFilter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				filterDialog.dismiss();
				isFilterDialogVisible= false;
			}
		});

		SeekBar costSlider = (SeekBar) filterDialog.findViewById(R.id.costSeekBar);
		costSlider.setMax(1000);
		costSlider.setProgress(cost);

		final TextView costText = (TextView) filterDialog.findViewById(R.id.costChosen);
		costText.setText(cost + "$");

		costSlider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				cost = progress;
				costText.setText(progress + "$");
			}
		});

		SeekBar distanceSlider = (SeekBar) filterDialog.findViewById(R.id.distanceSeekBar);
		distanceSlider.setMax(10000);
		distanceSlider.setProgress(distance);

		final TextView distanceText = (TextView) filterDialog.findViewById(R.id.distanceChoosen);
		distanceText.setText(distance + "m");
		distanceSlider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				distance = progress;
				distanceText.setText(progress + "m");
			}
		});

		Button aplyFilter = (Button) filterDialog.findViewById(R.id.filterSetButton);
		aplyFilter.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {

				locationChanged = false;
//				tmpLocation = currentLocation;
				tmpLocation = new Location(currentLocation);
				zipOrAddressStr = zipOrAddress.getText().toString();
				// LinkedList<DoctorPlacemark> tmpPlacemarks =
				// (LinkedList<DoctorPlacemark>) placemarks.clone();
				if (tmpPlacemarks != null) {
					placemarks = (LinkedList<DoctorPlacemark>) tmpPlacemarks.clone();
				} else {
					tmpPlacemarks = (LinkedList<DoctorPlacemark>) placemarks.clone();
				}

				MapKmlHelper kml = new MapKmlHelper();
				Log.d("geocode", zipOrAddressStr);

				if (zipOrAddressStr != null && !zipOrAddressStr.equals("")) {
					Geocoder geocoder = new Geocoder(FlipperActivity.this);
					List<Address> addresses = new ArrayList<Address>();
					try {
						addresses = geocoder.getFromLocationName(zipOrAddressStr, 1);
					} catch (IllegalArgumentException ex) {
						Toast.makeText(FlipperActivity.this, "illegal argument", Toast.LENGTH_LONG).show();
					} catch (IOException e) {
						e.printStackTrace();
						Toast.makeText(FlipperActivity.this, "IO error", Toast.LENGTH_LONG).show();

					}
					if (addresses != null && addresses.size() > 0 && addresses.get(0) != null) {
						tmpLocation.setLatitude(addresses.get(0).getLatitude());
						tmpLocation.setLongitude(addresses.get(0).getLongitude());

						gps.stopUsingGPS();
						Log.d("placemarks", currentLocation.getLongitude() + " " + currentLocation.getLatitude());
					} else {
						Toast.makeText(FlipperActivity.this, "Location not found", Toast.LENGTH_LONG).show();
					}
				}

				if(!currentLocation.equals(tmpLocation)){
					locationChanged = true;
				}
				if (locationChanged) {
					tmpPlacemarks = kml.recalculateDistances(tmpPlacemarks, tmpLocation);
				}

				filteredPlacemarks = kml.getFilteredPlacemarks(tmpPlacemarks, cost, distance);

				if (filteredPlacemarks != null && filteredPlacemarks.size() > 0) {
					
					tmpPlacemarks = (LinkedList<DoctorPlacemark>) placemarks.clone();
					doctorsAdapter.clear();

					for (DoctorPlacemark dp : filteredPlacemarks) {
						doctorsAdapter.add(dp);
					}
					doctorsAdapter.notifyDataSetChanged();
					
					filterDialog.dismiss();
					isFilterDialogVisible= false;
					
					mapView.getOverlays().clear();
					configureMapView();
					showFilteredMap(locationChanged);
//					setFilteredText(locationChanged);
				} else {
					Toast.makeText(FlipperActivity.this, "No doctors found!", Toast.LENGTH_LONG).show();
				}

			}
		});
		filterDialog.show();
		isFilterDialogVisible= true;
	}


	private OnClickListener distanceSort = new OnClickListener() {

		@SuppressWarnings("unchecked")
		@Override
		public void onClick(View v) {
			MapKmlHelper kml = new MapKmlHelper();

			LinkedList<DoctorPlacemark> plm = new LinkedList<DoctorPlacemark>();
			if (filteredPlacemarks != null/* &&filteredPlacemarks.size()>0 */) {
				plm = filteredPlacemarks;
			} else {
				tmpPlacemarks = (LinkedList<DoctorPlacemark>) placemarks.clone();
				plm = (LinkedList<DoctorPlacemark>) placemarks.clone();
			}
			Log.d("sort", " distanceClicked " + plm.size());
			kml.sortByDistance(plm);

			doctorsAdapter.clear();
			for (DoctorPlacemark dp : plm) {
				doctorsAdapter.add(dp);
			}
			doctorsAdapter.notifyDataSetChanged();

			Log.d("sort", " distanceClicked " + plm.size());
		}
	};

	/**
	 * Swaps from map to list
	 * 
	 */
	private OnClickListener swapListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (vf.getDisplayedChild() != 0) {

				vf.setInAnimation(FlipperActivity.this, R.anim.in_from_left);

				vf.setOutAnimation(FlipperActivity.this, R.anim.out_to_right);
				vf.showNext();
				costButton.setVisibility(View.INVISIBLE);
				distanceButton.setVisibility(View.INVISIBLE);
				((ImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_list));

			} else {

				vf.setInAnimation(FlipperActivity.this, R.anim.in_from_right);

				vf.setOutAnimation(FlipperActivity.this, R.anim.out_to_left);
				vf.showPrevious();

				costButton.setVisibility(View.VISIBLE);
				distanceButton.setVisibility(View.VISIBLE);
				((ImageButton) v).setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_mapmode));

			}

		}
	};

	// private void setFilteredText(boolean locationChanged){
	// StringBuffer sb = new StringBuffer(serviceText);
	// if(locationChanged&&!zipOrAddressStr.equals("")){
	// sb.append("<br/>Location: "+zipOrAddressStr);
	// }
	// if(cost!=0){
	// sb.append("<br/>"+"Max cost: "+cost+"$   ");
	// }
	// if(distance!=0){
	// if(cost==0){
	// sb.append("<br/>");
	// }else{
	// sb.append(" | ");
	// }
	// sb.append("Max distance: "+distance+"m");
	// }
	// flipperText.setText(Html.fromHtml(sb.toString()));
	// }



	public void newMyLocation(Location location) {
		setCurrentLocation(location);
		tmpLocation =  location;
		mapOverlays.clear();
		

		if(locationChanged){
			showFilteredMap(locationChanged);
			Log.d("placemarks", "locationChanged");
		}else{
			setupMapView();
			Log.d("placemarks", "location Not Changed");
		}
		
		MapKmlHelper kml = new MapKmlHelper();
		if(filteredPlacemarks!=null){
			filteredPlacemarks = kml.recalculateDistances(filteredPlacemarks, location);
			
		}
			
		placemarks = kml.recalculateDistances(placemarks, location);
		doctorsAdapter.notifyDataSetChanged();
	}}
