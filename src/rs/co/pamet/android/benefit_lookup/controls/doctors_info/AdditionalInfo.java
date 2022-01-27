package rs.co.pamet.android.benefit_lookup.controls.doctors_info;

public class AdditionalInfo {
	private String category;
	private String description;

	public AdditionalInfo(String category, String description) {
		super();
		this.category = category;
		this.description = description;
	}

	/**
	 * @return category name
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @return category description
	 */
	public String getDescription() {
		return description;
	}

}
