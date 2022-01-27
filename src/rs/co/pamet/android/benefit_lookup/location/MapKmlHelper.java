package rs.co.pamet.android.benefit_lookup.location;

import java.io.BufferedInputStream;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import rs.co.pamet.android.benefit_lookup.R;
import rs.co.pamet.android.benefit_lookup.controls.doctors_info.DoctorsInfo;

import android.location.Location;

import com.google.android.maps.GeoPoint;

public class MapKmlHelper {

	/**
	 * url mape
	 */
	private static final String mapUrl = "http://maps.google.com/maps/ms?ie=UTF8&authuser=0&msa=0&output=kml&msid=208071513065681353864.0004d0a521ff65cbb5df9";
	/**
	 * Lista lokacija koje se dobijaju parsiranjem
	 */
	private LinkedList<DoctorPlacemark> placemarks;
	/**
	 * Geografska duzina
	 */
	private double longitude;
	/**
	 * Geografska sirina
	 */
	private double latitude;
	/**
	 * Parser
	 */
	private XmlPullParser xpp;

	public LinkedList<DoctorPlacemark> getDoctorPlacemarks() {
		return placemarks;
	}

	public MapKmlHelper(double latitude, double longitude) {

		this.longitude = longitude;
		this.latitude = latitude;
		this.placemarks = parseDoctorPlacemarks();
	}

	public MapKmlHelper() {

	}

	/**
	 * Download kml fajla i smestanje u folder Beotel
	 */
	public void downloadKML() {
		File f = new File(android.os.Environment.getExternalStorageDirectory()
				+ java.io.File.separator + "BenefitLookup"
				+ java.io.File.separator + "Map.kml");

		if (!f.getParentFile().exists())
			;
		{
			f.getParentFile().mkdir();
		}
		try {
			InputStream is = getConnection(mapUrl);
			if (is != null) {
				BufferedInputStream bis = new BufferedInputStream(is);

				FileOutputStream fs = new FileOutputStream(f);

				int data;

				while ((data = bis.read()) != -1) {
					fs.write(data);
				}

				fs.flush();
				fs.close();
				bis.close();
				is.close();
			}

		} catch (FileNotFoundException e) {

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public LinkedList<DoctorPlacemark> recalculateDistances(
			LinkedList<DoctorPlacemark> plmr, Location newLocation) {

		for (DoctorPlacemark dp : plmr) {
			Location docLocation = new Location("");
			docLocation.setLatitude(dp.getPoint().getLatitudeE6() / 1e6);
			docLocation.setLongitude(dp.getPoint().getLongitudeE6() / 1e6);
			Float newDistance = newLocation.distanceTo(docLocation);
			dp.setDistance(newDistance.intValue());
		}
		sortByDistance(plmr);
		return plmr;
	}

	/**
	 * Parsira se fajl, popunjavaju se podaci u DoctorPlacemark objekat i on se
	 * dodaje u listu.
	 * 
	 * @return lista Palcemark-a
	 */
	private LinkedList<DoctorPlacemark> parseDoctorPlacemarks() {

		LinkedList<DoctorPlacemark> plmr = new LinkedList<DoctorPlacemark>();
		try {
			InputStream istr = getConnection(mapUrl);

			xpp = XmlPullParserFactory.newInstance().newPullParser();
			xpp.setInput(istr, "UTF-8");

			int event = xpp.getEventType();
			String tagP = "Placemark";

			while (event != XmlPullParser.END_DOCUMENT) {

				skipToTag(tagP);

				skipToTag("name");
				String title = xpp.nextText();

				skipToTag("description");

				while (xpp.nextToken() != XmlPullParser.CDSECT)
					;

				String desc = xpp.getText();
				
				String[] infoArray = desc.split("\n");
				String specialties = infoArray[0].substring(
						infoArray[0].indexOf(":") + 1).trim(), address = infoArray[1]
						.substring(infoArray[1].indexOf(":") + 1).trim(), phone = infoArray[2]
						.substring(infoArray[2].indexOf(":") + 1).trim();

				skipToTag("coordinates");
				String[] ns = xpp.nextText().split(",");
				event = xpp.next();

				double lgt = Double.parseDouble(ns[0]);
				double lat = Double.parseDouble(ns[1]);

				GeoPoint gp = new GeoPoint((int) (lat * 1e6), (int) (lgt * 1e6));

				Location location1 = new Location("");
				location1.setLatitude(lat);
				location1.setLongitude(lgt);

				Location location2 = new Location("");
				location2.setLatitude(latitude);
				location2.setLongitude(longitude);

				Float distanceInMeters = location2.distanceTo(location1);

				DoctorPlacemark pl = new DoctorPlacemark(gp, title, desc,
						distanceInMeters.intValue());

				DoctorsInfo info = new DoctorsInfo(title, specialties, address,
						phone, (int) (Math.random() * 1000),
						R.drawable.doctor_image, null); //TODO
				pl.setDoctorsInfo(info);
				plmr.add(pl);

			}

		} catch (XmlPullParserException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		sortByDistance(plmr);
		return plmr;

	}

	public void sortByDistance(LinkedList<DoctorPlacemark> plmr) {
		Collections.sort(plmr, new Comparator<DoctorPlacemark>() {
			public int compare(DoctorPlacemark o1, DoctorPlacemark o2) {
				Integer dist1 = o1.getDistance();
				Integer dist2 = o2.getDistance();
				return dist1.compareTo(dist2);
			}
		});
	}

	public void sortByCost(LinkedList<DoctorPlacemark> plmr) {
		Collections.sort(plmr, new Comparator<DoctorPlacemark>() {
			public int compare(DoctorPlacemark o1, DoctorPlacemark o2) {
				Integer cost1 = o1.getDoctorsInfo().getCost();
				Integer cost2 = o2.getDoctorsInfo().getCost();
				return cost1.compareTo(cost2);
			}
		});

	}

	/**
	 * Dohvatanje podataka preko zadatog url-a
	 * 
	 * @param url
	 * @return InputStream sa podacima
	 */

	private InputStream getConnection(String url) {
		InputStream is = null;
		try {
			URLConnection conn = new URL(url).openConnection();

			is = conn.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}

	/**
	 * Skok na tag sa zadatim imenom
	 * 
	 * @param tagName
	 * @throws Exception
	 */
	private void skipToTag(String tagName) throws Exception {
		int event = xpp.getEventType();
		while (event != XmlPullParser.END_DOCUMENT
				&& !tagName.equals(xpp.getName())) {
			event = xpp.next();
		}
	}

	/**
	 * Pravljenje liste od k najblizih DoctorPlacemark-a
	 * 
	 * @param k
	 * @return lista
	 */
	public LinkedList<DoctorPlacemark> getNearestDoctorPlacemarks(
			LinkedList<DoctorPlacemark> placemarks, int k) {

		LinkedList<DoctorPlacemark> plmList = new LinkedList<DoctorPlacemark>();

		for (int i = 0; i < k && i < placemarks.size(); i++) {
			DoctorPlacemark p = placemarks.get(i);

			plmList.add(p);
		}

		return plmList;
	}

	/**
	 * Lista od k najjeftinijih DoctorPlacemark-a
	 * 
	 * @param k
	 * @return lista
	 */
	public LinkedList<DoctorPlacemark> getCheapestDoctorPlacemarks(
			LinkedList<DoctorPlacemark> placemarks, int k) {

		LinkedList<DoctorPlacemark> plmList = new LinkedList<DoctorPlacemark>();

		sortByCost(placemarks);

		for (int i = 0; i < k && i < placemarks.size(); i++) {
			DoctorPlacemark p = placemarks.get(i);

			plmList.add(p);
		}

		return plmList;
	}

	public LinkedList<DoctorPlacemark> getFilteredPlacemarks(
			LinkedList<DoctorPlacemark> placemarks, int cost, int distance) {

		if (placemarks == null) {
			return null;
		}

		if (cost == 0 && distance == 0) {
			return placemarks;
		}
		LinkedList<DoctorPlacemark> plmList = new LinkedList<DoctorPlacemark>();

		for (DoctorPlacemark dp : placemarks) {

			if (cost > 0) {
				if (distance > 0) {
					if (dp.getDistance() <= distance
							&& dp.getDoctorsInfo().getCost() <= cost) {
						plmList.add(dp);
					}
				} else {
					if (dp.getDoctorsInfo().getCost() <= cost) {
						plmList.add(dp);
					}
				}
			} else if (distance > 0) {
				if (dp.getDistance() <= distance) {
					plmList.add(dp);
				}
			}
		}

		return plmList;
	}
}
