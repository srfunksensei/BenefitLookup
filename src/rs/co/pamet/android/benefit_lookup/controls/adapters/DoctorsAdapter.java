package rs.co.pamet.android.benefit_lookup.controls.adapters;

import java.util.List;

import rs.co.pamet.android.benefit_lookup.R;
import rs.co.pamet.android.benefit_lookup.location.DoctorPlacemark;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author MB
 * 
 */

public class DoctorsAdapter extends ArrayAdapter<DoctorPlacemark> {

	private final Context context;
	private final List<DoctorPlacemark> data;

	public DoctorsAdapter(Context context, int layoutResourceId,
			List<DoctorPlacemark> objects) {
		super(context, layoutResourceId, objects);
		this.context = context;
		this.data = objects;
	}

	@Override
	public void add(DoctorPlacemark object) {
		data.add(object);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public DoctorPlacemark getItem(int position) {
		return data.get(position);
	}

	@Override
	public int getPosition(DoctorPlacemark item) {
		return data.indexOf(item);
	}

	@Override
	public void remove(DoctorPlacemark object) {
		data.remove(object);
	}

	public void remove(int postion) {
		data.remove(postion);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.doctor_info_item, parent,
				false);

		TextView doc_name = (TextView) rowView.findViewById(R.id.doc_name), doc_speciality = (TextView) rowView
				.findViewById(R.id.doc_speciality), doc_cost = (TextView) rowView
				.findViewById(R.id.doc_cost_val), distance = (TextView) rowView
				.findViewById(R.id.doc_distance_val);
		ImageView thumb_image = (ImageView) rowView
				.findViewById(R.id.doc_image);

		doc_name.setText(data.get(position).getDoctorsInfo().getName());
		doc_speciality.setText(data.get(position).getDoctorsInfo().getSpeciality().get(0)); 

		doc_cost.setText("$" + data.get(position).getDoctorsInfo().getCost());
		distance.setText("" + data.get(position).getDistance() + "m");

		thumb_image.setImageResource(data.get(position).getDoctorsInfo().getImageID());

		return rowView;
	}

}
