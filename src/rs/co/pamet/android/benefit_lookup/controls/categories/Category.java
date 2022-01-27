package rs.co.pamet.android.benefit_lookup.controls.categories;

/**
 * 
 * @author MB
 * 
 */
public class Category {
	private int resourceId;

	private String name;

	private String[] subcategories = null;

	public Category(int resourceId, String categoryName, String[] subcategories) {
		super();
		this.resourceId = resourceId;
		this.name = categoryName;
		this.subcategories = subcategories;
	}

	public int getResourceId() {
		return resourceId;
	}

	public String getCategoryName() {
		return name;
	}

	public String[] getSubcategories() {
		return subcategories;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public void setCategoryName(String categoryName) {
		this.name = categoryName;
	}

	public void setSubcategories(String[] subcategories) {
		this.subcategories = subcategories;
	}

}
