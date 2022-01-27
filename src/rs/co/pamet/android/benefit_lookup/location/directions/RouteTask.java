package rs.co.pamet.android.benefit_lookup.location.directions;

import rs.co.pamet.android.benefit_lookup.activities.DirectionsMap;
import rs.co.pamet.android.benefit_lookup.location.DoctorPlacemark;

import com.google.android.maps.GeoPoint;

import android.content.Context;
import android.os.AsyncTask;

public class RouteTask extends AsyncTask<GeoPoint, Void, Route> {

	private Context context;
	private Route route;
	DoctorPlacemark tapped;
	private boolean dirDialogShown;
	
	public RouteTask(Context context) {
		this.context = context;
	}

	@Override
	protected void onPostExecute(Route result) {
		((DirectionsMap) context).onRouteCalculated();

	}

	@Override
	protected Route doInBackground(GeoPoint... params) {
		GeoPoint start = params[0];
		GeoPoint dest = params[1];

		route = Route.directions(start, dest);

		return route;
	}

	public Route getRoute() {
		return route;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public DoctorPlacemark getTapped() {
		return tapped;
	}

	public void setTapped(DoctorPlacemark tapped) {
		this.tapped = tapped;
	}

	public boolean isDirDialogShown() {
		return dirDialogShown;
	}

	public void setDirDialogShown(boolean dirDialogShown) {
		this.dirDialogShown = dirDialogShown;
	}

}
