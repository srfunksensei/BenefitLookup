package rs.co.pamet.android.benefit_lookup.location;

import rs.co.pamet.android.benefit_lookup.controls.doctors_info.DoctorsInfo;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class DoctorPlacemark extends OverlayItem {

	private int distance;

	private int type;

	private DoctorsInfo doctorsInfo;

	public DoctorPlacemark(GeoPoint point, String title, String snippet,int dist) {
		super(point, title, snippet);
		setDistance(dist);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	
	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public DoctorsInfo getDoctorsInfo() {
		return doctorsInfo;
	}

	public void setDoctorsInfo(DoctorsInfo doctorsInfo) {
		this.doctorsInfo = doctorsInfo;
	}

	@Override
	public String toString() {
		String s = "<b>Cost:</b> $" + getDoctorsInfo().getCost()
				+ "<br/><b>Area distance:</b> " + getDistance() + "m"
				+ "<br/><b>Address:</b> " + getDoctorsInfo().getAddress()
				+ "<br/><b>Phone:</b> " + getDoctorsInfo().getPhone();
		return s;
	}
}
