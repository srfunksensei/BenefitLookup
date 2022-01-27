package rs.co.pamet.android.benefit_lookup.location;

import java.util.ArrayList;

import rs.co.pamet.android.benefit_lookup.R;
import rs.co.pamet.android.benefit_lookup.activities.DoctorsInfoActivity;
import rs.co.pamet.android.benefit_lookup.activities.FlipperActivity;
import rs.co.pamet.android.benefit_lookup.location.directions.DirectionsTask;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;

public class DoctorsItemizedOverlay extends ItemizedOverlay<DoctorPlacemark> {

	private Context context;
	private ArrayList<DoctorPlacemark> mOverlays = new ArrayList<DoctorPlacemark>();

	private DoctorPlacemark tappedPlacemark;
	private DirectionsTask dTask;

	public DoctorsItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		this.context = context;
		populate();
	}

	public void addOverlay(DoctorPlacemark overlay) {
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected DoctorPlacemark createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	public void removeOverlay(DoctorPlacemark overlay) {
		mOverlays.remove(overlay);
		populate();
	}

	public void clear() {
		mOverlays.clear();
		populate();
	}

	@Override
	protected boolean onTap(int index) {
		try {
//			final Dialog dialog = new Dialog(context);
//			// final int docInd = index;
			tappedPlacemark = mOverlays.get(index);
			
			((FlipperActivity)context).setupDoctorDialog(tappedPlacemark);
			
			
//			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//			dialog.setContentView(R.layout.doctor_dialog_full);
//
//			// doctors title
//			TextView textView = (TextView) dialog.findViewById(R.id.docDialogTitle);
//			textView.setText(tappedPlacemark.getDoctorsInfo().getName());
//
//			// doctors info
//			textView = (TextView) dialog.findViewById(R.id.docDialogInfo);
//			textView.setText(Html.fromHtml(tappedPlacemark.toString()));
//
//			// cancel button
//			ImageView imageView = (ImageView) dialog.findViewById(R.id.closeButton);
//			imageView.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					dialog.dismiss();
//				}
//			});
//
//			// details button
//			Button btn = (Button) dialog.findViewById(R.id.docDialogDetails);
//			btn.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					Intent detIntent = new Intent(context, DoctorsInfoActivity.class);
//					detIntent.putExtra("doctorOrdinal", FlipperActivity.placemarks.indexOf(tappedPlacemark));
//					dialog.dismiss();
//					context.startActivity(detIntent);
//
//				}
//			});
//
//			// directions button
//			btn = (Button) dialog.findViewById(R.id.docDialogDirections);
//			btn.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//
//					GeoPoint dest = tappedPlacemark.getPoint();
//					GeoPoint start = new GeoPoint((int) (FlipperActivity.getTmpLocation().getLatitude() * 1e6), 
//													(int)(FlipperActivity.getTmpLocation().getLongitude() * 1e6));
//
//					dTask = new DirectionsTask(context);
//					dialog.dismiss();
//					dTask.execute(start, dest);
//
//				}
//			});
//
//			dialog.show();

		} catch (NullPointerException e) {

			e.printStackTrace();
		}
		return super.onTap(index);
	}

}
