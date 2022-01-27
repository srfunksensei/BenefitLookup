package rs.co.pamet.android.benefit_lookup.location;

import rs.co.pamet.android.benefit_lookup.activities.FlipperActivity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

/**
 * 
 * @author MB
 * 
 */
public class GPSTracker extends Service implements LocationListener {

	/**
	 * Minimum time between updates in milliseconds
	 */
	private static final long MIN_TIME_BW_UPDATES = 1000 * 30 * 1; // 1 minute
	/**
	 * Minimum distance to change Updates in meters
	 */
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 50; // 10 meters

	private Context mContext;

	// flag for GPS status
	private boolean isGPSEnabled = false;

	// flag for network status
	private boolean isNetworkEnabled = false;

	// flag for GPS status
	private boolean canGetLocation = false;

	private Location location; // location
	private double latitude; // latitude
	private double longitude; // longitude

	/**
	 * To check providers' status
	 */
	private LocationManager locationManager;

	// Declaring a Location Task
	private LocationTask locationTask;

	public GPSTracker(LocationTask locationTask) {
		this.mContext = locationTask.getContext();
		this.locationTask = locationTask;

//		getLocation();
	}
	
	public Location getLocation() {
		try {
			locationManager = (LocationManager) mContext
					.getSystemService(Context.LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
				this.canGetLocation = false;
				showSettingsAlert();
			} else {
				this.canGetLocation = true;
				// First get location from Network Provider
				String bestProvider = getBestProvider();
				locationManager.requestLocationUpdates(bestProvider,
						MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES,
						this);

				location = locationManager.getLastKnownLocation(bestProvider);
				if (location != null) {
					latitude = location.getLatitude();
					longitude = location.getLongitude();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

	/**
	 * Returns the name of the provider
	 * 
	 * @return best provider
	 */
	private String getBestProvider() {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);

		criteria.setAltitudeRequired(true);
		criteria.setBearingRequired(false);

		return locationManager.getBestProvider(criteria, true);
	}
	
	/**
	 * Start using GPS listener Calling this function will stop using GPS in your
	 * app
	 * */
	public void startUsingGPS() {
		if (locationManager != null) {
			String bestProvider = getBestProvider();
			locationManager.requestLocationUpdates(bestProvider,
					MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES,
					this);

			location = locationManager.getLastKnownLocation(bestProvider);

		}
	}
	

	/**
	 * Stop using GPS listener Calling this function will stop using GPS in your
	 * app
	 * */
	public void stopUsingGPS() {
		if (locationManager != null) {
			locationManager.removeUpdates(GPSTracker.this);
		}
	}

	/**
	 * Function to get latitude
	 * */
	public double getLatitude() {
		if (location != null) {
			latitude = location.getLatitude();
		}

		return latitude;
	}

	/**
	 * Function to get longitude
	 * */
	public double getLongitude() {
		if (location != null) {
			longitude = location.getLongitude();
		}

		return longitude;
	}

	/**
	 * Function to check GPS/wifi enabled
	 * 
	 * @return boolean
	 * */
	public boolean canGetLocation() {
		return this.canGetLocation;
	}

	/**
	 * Function to show settings alert dialog On pressing Settings button will
	 * lauch Settings Options
	 * */
	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		// Setting Dialog Title
		alertDialog.setTitle("Location disabled");

		// Setting Dialog Message
		alertDialog
				.setMessage("GPS and Network not enabled. Do you want to go to enable it?");

		// On pressing Settings button
		alertDialog.setPositiveButton("Enable",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						startActivity(new Intent(
								Settings.ACTION_SECURITY_SETTINGS));
					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();

	}

	@Override
	public void onLocationChanged(Location location) {
		if(locationTask.getStatus()==AsyncTask.Status.FINISHED){
//			FlipperActivity.setCurrentLocation(location);
			Log.d("placemarks", "gps2  "+location.getLatitude()+"  "+location.getLongitude());
			((FlipperActivity)mContext).newMyLocation(location);
//			LinkedList<DoctorPlacemark> plm =FlipperActivity.placemarks;
//			MapKmlHelper kml = new MapKmlHelper();
//			
//			FlipperActivity.placemarks = kml.recalculateDistances(plm, location);
		}else if (locationTask.getStatus()==AsyncTask.Status.PENDING){
			FlipperActivity.setCurrentLocation(location);
			locationTask.execute(location);
			Log.d("placemarks", "gps1  "+location.getLatitude()+"  "+location.getLongitude());
		}
//		locationTask = new LocationTask(mContext);
		
		
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}
}