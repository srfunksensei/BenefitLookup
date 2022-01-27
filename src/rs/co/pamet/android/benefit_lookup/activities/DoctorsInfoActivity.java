package rs.co.pamet.android.benefit_lookup.activities;

import rs.co.pamet.android.benefit_lookup.R;
import rs.co.pamet.android.benefit_lookup.location.DoctorPlacemark;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

/**
 * 
 * @author MB
 * 
 */

public class DoctorsInfoActivity extends Activity {

	private static DoctorPlacemark doctor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctors_info);
		int ordinal = getIntent().getIntExtra("doctorOrdinal", 0);

		doctor = FlipperActivity.placemarks.get(ordinal);

		// button call
		Button btn = (Button) findViewById(R.id.call);
		btn.setOnClickListener(callDoctor);

		// button map
		btn = (Button) findViewById(R.id.show_directions);
		btn.setOnClickListener(showDirections);

		// doctors name
		TextView textView = (TextView) findViewById(R.id.doc_name);
		textView.setText(doctor.getDoctorsInfo().getName());

		// doctors specialties
		textView = (TextView) findViewById(R.id.doc_speciality_main);
		textView.setText(doctor.getDoctorsInfo().getSpeciality().get(0));
		textView = (TextView) findViewById(R.id.specialities_desc);
		textView.setText(doctor.getDoctorsInfo().getSpeciality().get(0));
		
		// doctors address
		textView = (TextView) findViewById(R.id.doc_address);
		textView.setText(doctor.getDoctorsInfo().getAddress());

		// doctors phone
		textView = (TextView) findViewById(R.id.doc_phone);
		textView.setText(doctor.getDoctorsInfo().getPhone());

		// doctors cost
		textView = (TextView) findViewById(R.id.doc_cost_val);
		textView.setText(doctor.getDoctorsInfo().getCost() + "$");

		// doctors distance
		textView = (TextView) findViewById(R.id.doc_distance_val);
		textView.setText(doctor.getDistance() + "m");
	}

	private OnClickListener showDirections = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent data = new Intent(DoctorsInfoActivity.this,DirectionsMap.class);
			data.putExtra("address", doctor.routableAddress());
			data.putExtra("currLat", FlipperActivity.getCurrentLocation().getLatitude());
			data.putExtra("currLgt", FlipperActivity.getCurrentLocation().getLongitude());

			startActivity(data);
		}
	};

	private OnClickListener callDoctor = new OnClickListener() {
		@Override
		public void onClick(View v) {
			startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ doctor.getDoctorsInfo().getPhone())));
		}
	};

	public static DoctorPlacemark getDoctor() {
		return doctor;
	}

	// public static void setDoctor(DoctorPlacemark doctor) {
	// DoctorsInfoActivity.doctor = doctor;
	// }

}
