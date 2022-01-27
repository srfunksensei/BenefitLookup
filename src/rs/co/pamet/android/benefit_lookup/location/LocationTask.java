package rs.co.pamet.android.benefit_lookup.location;

import java.util.LinkedList;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import rs.co.pamet.android.benefit_lookup.activities.FlipperActivity;
import rs.co.pamet.android.benefit_lookup.location.directions.Route;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

public class LocationTask extends
		AsyncTask<Location, Void, LinkedList<DoctorPlacemark>> {

	private Context context;

	private Location loc;
	
	private LinkedList<DoctorPlacemark> doctors;
	private LinkedList<DoctorPlacemark> filteredPlacemarks;
	private MapView mapView;
	private int displayedChild;
	

	private List<Overlay> mapOverlays;
	
	private GeoPoint myLocation;
	
	private Location tmpLocation;
	
	private boolean locChanged;

	private int cost;
	private int distance;
	private String zipOrAddressStr;

	private boolean isFilterDialogVisible;
	private boolean doctorDialogVisible;
	private boolean directionsDialogVisible;
	private Route route;
	
	private DoctorPlacemark tappedPlacemark;
	

	public LocationTask(Context context) {
		this.context = context;
	}

	@Override
	protected void onPostExecute(LinkedList<DoctorPlacemark> result) {
		((FlipperActivity) context).progressDialog.dismiss();
		doctors = result;
		FlipperActivity.setCurrentLocation(loc);
		FlipperActivity.placemarks = result;

		((FlipperActivity) context).onLocationTaskCompleted();
	}

	@Override
	protected void onPreExecute() {
		((FlipperActivity) context).progressDialog.show();
	}

	@Override
	protected LinkedList<DoctorPlacemark> doInBackground(Location... params) {
		loc = params[0];
		LinkedList<DoctorPlacemark> plm = new LinkedList<DoctorPlacemark>();

		MapKmlHelper kml = new MapKmlHelper(params[0].getLatitude(),params[0].getLongitude());

		plm = kml.getDoctorPlacemarks();

		return plm;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public LinkedList<DoctorPlacemark> getDoctors() {
		return doctors;
	}

	public void setDoctors(LinkedList<DoctorPlacemark> doctors) {
		this.doctors = doctors;
	}

	public MapView getMapView() {
		return mapView;
	}

	public void setMapView(MapView mapView) {
		this.mapView = mapView;
	}

	public List<Overlay> getMapOverlays() {
		return mapOverlays;
	}

	public void setMapOverlays(List<Overlay> mapOverlays) {
		this.mapOverlays = mapOverlays;
	}

	public Location getTmpLocation() {
		return tmpLocation;
	}

	public void setTmpLocation(Location tmpLocation) {
		this.tmpLocation = tmpLocation;
	}

	public boolean isLocChanged() {
		return locChanged;
	}

	public void setLocChanged(boolean locChanged) {
		this.locChanged = locChanged;
	}
	
	

	public GeoPoint getMyLocation() {
		return myLocation;
	}

	public void setMyLocation(GeoPoint myLocation) {
		this.myLocation = myLocation;
	}

	public LinkedList<DoctorPlacemark> getFilteredPlacemarks() {
		return filteredPlacemarks;
	}

	public void setFilteredPlacemarks(LinkedList<DoctorPlacemark> filteredPlacemarks) {
		this.filteredPlacemarks = filteredPlacemarks;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getZipOrAddressStr() {
		return zipOrAddressStr;
	}

	public void setZipOrAddressStr(String zipOrAddressStr) {
		this.zipOrAddressStr = zipOrAddressStr;
	}
	public int getDisplayedChild() {
		return displayedChild;
	}

	public boolean isDoctorDialogVisible() {
		return doctorDialogVisible;
	}

	public void setDoctorDialogVisible(boolean doctorDialogVisible) {
		this.doctorDialogVisible = doctorDialogVisible;
	}

	public void setDisplayedChild(int displayedChild) {
		this.displayedChild = displayedChild;
	}

	public boolean isFilterDialogVisible() {
		return isFilterDialogVisible;
	}

	public void setFilterDialogVisible(boolean isFilterDialogVisible) {
		this.isFilterDialogVisible = isFilterDialogVisible;
	}
	public DoctorPlacemark getTappedPlacemark() {
		return tappedPlacemark;
	}

	public void setTappedPlacemark(DoctorPlacemark tappedPlacemark) {
		this.tappedPlacemark = tappedPlacemark;
	}

	public boolean isDirectionsDialogVisible() {
		return directionsDialogVisible;
	}

	public void setDirectionsDialogVisible(boolean directionsDialogVisible) {
		this.directionsDialogVisible = directionsDialogVisible;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

}
