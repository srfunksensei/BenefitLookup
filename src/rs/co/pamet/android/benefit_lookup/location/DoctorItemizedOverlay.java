package rs.co.pamet.android.benefit_lookup.location;

import java.util.ArrayList;

import rs.co.pamet.android.benefit_lookup.R;
import rs.co.pamet.android.benefit_lookup.activities.DirectionsMap;
import rs.co.pamet.android.benefit_lookup.location.directions.Route;
import rs.co.pamet.android.benefit_lookup.location.directions.Segment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.ItemizedOverlay;

public class DoctorItemizedOverlay extends ItemizedOverlay<DoctorPlacemark> {

	private Context context;
	private ArrayList<DoctorPlacemark> mOverlays = new ArrayList<DoctorPlacemark>();
	private Route route;
	Dialog dialog;
	
	public DoctorItemizedOverlay(Drawable defaultMarker, Route route, Context context) {
		super(boundCenterBottom(defaultMarker));
		this.context = context;
		this.route = route;
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

	@Override
	protected boolean onTap(int index) {
		((DirectionsMap)context).showDirections(mOverlays.get(index));
		
		return super.onTap(index);
	}

	public Dialog getDialog() {
		return dialog;
	}

	public void setDialog(Dialog dialog) {
		this.dialog = dialog;
	}

}