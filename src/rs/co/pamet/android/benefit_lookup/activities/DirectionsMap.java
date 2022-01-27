package rs.co.pamet.android.benefit_lookup.activities;

import java.util.List;

import rs.co.pamet.android.benefit_lookup.R;

import rs.co.pamet.android.benefit_lookup.location.DoctorItemizedOverlay;
import rs.co.pamet.android.benefit_lookup.location.DoctorPlacemark;
import rs.co.pamet.android.benefit_lookup.location.directions.Route;
import rs.co.pamet.android.benefit_lookup.location.directions.RouteOverlay;
import rs.co.pamet.android.benefit_lookup.location.directions.RouteTask;
import rs.co.pamet.android.benefit_lookup.location.directions.Segment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class DirectionsMap extends MapActivity {

//	private static final int MENU_ID_DIRECTIONS = 0;
//	private static final int MENU_ID_SATELLITE = 1;

	private MapView mapView;

	private Route route;
	private RouteTask rTask;

	private double lat;
	private double lgt;

	private ProgressDialog dialog;

	private Dialog dirDialog;

	DoctorPlacemark tapped;
	private boolean dirDialogShown;
	
	public void onRouteCalculated() {
		route = rTask.getRoute();
		List<Overlay> mapOverlays = mapView.getOverlays();
		if(dialog!=null){
			dialog.dismiss();
		}
		
		// route
		RouteOverlay routeOverlay = new RouteOverlay(route, getResources().getColor(R.color.base_color_blue));
		mapOverlays.add(routeOverlay);

		// selected doctors' location
		DoctorItemizedOverlay dOverlay = new DoctorItemizedOverlay(getResources().getDrawable(R.drawable.location), route, DirectionsMap.this);
		dOverlay.addOverlay(DoctorsInfoActivity.getDoctor());
		mapOverlays.add(dOverlay);
		DoctorPlacemark me = new DoctorPlacemark(new GeoPoint((int) (lat * 1E6), (int) (lgt * 1E6)), null, "Your location", 0);

		// my location
		DoctorItemizedOverlay mOverlay = new DoctorItemizedOverlay(getResources().getDrawable(R.drawable.my_location),
											null, DirectionsMap.this);
		mOverlay.addOverlay(me);
		mapOverlays.add(mOverlay);

		mapView.getController().setCenter(new GeoPoint((int) (lat * 1E6), (int) (lgt * 1E6)));
		mapView.setBuiltInZoomControls(true);
		mapView.invalidate();
	}

	

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_doctors_map);

		mapView = (MapView) findViewById(R.id.doctorMapView); 
		
		lat = getIntent().getDoubleExtra("currLat", 0.0);
		lgt = getIntent().getDoubleExtra("currLgt", 0.0);
		Object retained = getLastNonConfigurationInstance(); 
		
		if(retained instanceof RouteTask){
			rTask = (RouteTask) retained;
			rTask.setContext(DirectionsMap.this);
			if(rTask.getStatus()==AsyncTask.Status.FINISHED){
				onRouteCalculated();
				tapped =rTask.getTapped();
				dirDialogShown = rTask.isDirDialogShown();
				
				if(dirDialogShown){
					showDirections(tapped);
				}
			}else{
				dialog = new ProgressDialog(DirectionsMap.this);
				dialog.setMessage("Loading...");
				dialog.setCancelable(true);
				dialog.show();
			}
		}else{
			
				dialog = new ProgressDialog(DirectionsMap.this);
				dialog.setMessage("Loading...");
				dialog.setCancelable(true);
				dialog.show();

				rTask = new RouteTask(DirectionsMap.this);		
			rTask.execute(new GeoPoint((int) (lat * 1E6), (int) (lgt * 1E6)),DoctorsInfoActivity.getDoctor().getPoint());
		}
		

		// String routeAddress = getIntent().getStringExtra("address");

		


	}

	@Override
	protected boolean isRouteDisplayed() {
		return true;
	}
	@Override
	public Object onRetainNonConfigurationInstance() {
		if(rTask!=null){
			rTask.setContext(null);
			if(route!=null){
				rTask.setRoute(route);
			}
			rTask.setTapped(tapped);
			rTask.setDirDialogShown(dirDialogShown);
		}
		if(dialog!=null){
			dialog.dismiss();
		}
		if(dirDialog!=null){
			dirDialog.dismiss();
		}
		return rTask;
	}



	public void showDirections(DoctorPlacemark tapped) {
		
		try {
			this.tapped = tapped;
			
			dirDialog = new Dialog(DirectionsMap.this);

			dirDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dirDialog.setContentView(R.layout.doctor_dialog);
			dirDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			// doctors title
			TextView textView = (TextView) dirDialog.findViewById(R.id.docDialogTitle);
			textView.setText(tapped.getDoctorsInfo().getName());

			// doctors info
			textView = (TextView) dirDialog.findViewById(R.id.docDialogInfo);
			textView.setText(Html.fromHtml(tapped.toString()));

			StringBuffer sb = new StringBuffer();
			sb.append(" <b>Distance:</b> " + route.getLength() + "m<br/><br/>");

			for (Segment seg : route.getSegments()) {
				String inst = "<b>" + seg.getLength() + "m:</b>  "
						+ seg.getInstruction() + "<br/>";
				sb.append(inst);
			}

			// directions to doctor
			textView = (TextView) dirDialog.findViewById(R.id.docDirectionsInfo);
			textView.setText(Html.fromHtml(sb.toString()));

			// cancel button
			ImageView imageView = (ImageView) dirDialog	.findViewById(R.id.closeButton);
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dirDialog.dismiss();
					dirDialogShown = false;
				}
			});

			dirDialog.show();
			dirDialogShown = true;
		} catch (NullPointerException e) {

			e.printStackTrace();
		}
	}
	


}
