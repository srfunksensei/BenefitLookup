package rs.co.pamet.android.benefit_lookup.controls.doctors_info;

import java.util.LinkedList;

/**
 * 
 * @author MB
 * 
 */

public class DoctorsInfo {

	private String name;
	private LinkedList<String> specialties;
	private LinkedList<AdditionalInfo> otherInfo;
	private String address;
	private String phone;
	private int cost;
	private int imageID;

	public DoctorsInfo(String name, LinkedList<String> specialties,
			String address, String phone, int cost, int imageID,
			LinkedList<AdditionalInfo> otherInfo) {
		super();
		this.name = name;

		this.specialties = specialties;
		this.address = address;
		this.phone = phone;
		this.cost = cost;
		this.imageID = imageID;
		this.otherInfo = otherInfo;
	}
	
	public DoctorsInfo(String name, String specialties,
			String address, String phone, int cost, int imageID,
			LinkedList<AdditionalInfo> otherInfo) {
		super();
		this.name = name;

		this.specialties = new LinkedList<String>();
		this.specialties.add(specialties);
		
		this.address = address;
		this.phone = phone;
		this.cost = cost;
		this.imageID = imageID;
		this.otherInfo = otherInfo;
	}

	public DoctorsInfo() {
		
	}

	/**
	 * @return doctors name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return list of doctors specialties
	 */
	public LinkedList<String> getSpeciality() {
		return specialties;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	/**
	 * @return doctors address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @return doctors phone
	 */
	public String getPhone() {
		return phone;
	}


	/**
	 * @return imageID
	 */
	public int getImageID() {
		return imageID;
	}

	/**
	 * @return list of doctors other info
	 */
	public LinkedList<AdditionalInfo> getOtherInfo() {
		return otherInfo;
	}

	/**
	 * @param location
	 *            the index of the element to return
	 * 
	 * @return AdditionalInfo at the specified index
	 */
	public AdditionalInfo getOtherInfor(int location) {
		return otherInfo.get(location);
	}
}
