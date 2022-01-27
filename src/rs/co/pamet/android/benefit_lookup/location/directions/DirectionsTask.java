package rs.co.pamet.android.benefit_lookup.location.directions;


import rs.co.pamet.android.benefit_lookup.activities.FlipperActivity;

import com.google.android.maps.GeoPoint;

import android.content.Context;
import android.os.AsyncTask;

public class DirectionsTask extends AsyncTask<GeoPoint, Void, Route> {

	private Context context;

	public DirectionsTask(Context context) {
		this.context = context;
	}

	@Override
	protected void onPostExecute(Route result) {
		((FlipperActivity)context).showDirections(result, context);
	}

	@Override
	protected Route doInBackground(GeoPoint... params) {
		GeoPoint start = params[0], dest = params[1];

		return Route.directions(start, dest);
	}
}