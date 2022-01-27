package rs.co.pamet.android.benefit_lookup.database;

import rs.co.pamet.android.benefit_lookup.controls.categories.Categories;
import rs.co.pamet.android.benefit_lookup.controls.categories.Category;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class BenefitDatabaseHelper extends SQLiteOpenHelper {

	protected static final String DATABASE_NAME = "benefit_lookup";
	private static final int DATABASE_VERSION = 2;

	protected static final String BENEFITS_TABLE_NAME = "benefits";
	protected static final String BENEFITS_TABLE_ID_COLUMN = BaseColumns._ID;
	protected static final String BENEFITS_TABLE_CATEGORY_COLUMN = "CATEGORY";
	protected static final String BENEFITS_TABLE_SUBCATEGORY_COLUMN = "SUBCATEGORY";

	private static final String BENEFITS_CREATE_TABLE = "CREATE TABLE "
			+ BENEFITS_TABLE_NAME + " ( " + BENEFITS_TABLE_ID_COLUMN
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ BENEFITS_TABLE_CATEGORY_COLUMN + " TEXT NOT NULL,"
			+ BENEFITS_TABLE_SUBCATEGORY_COLUMN + " TEXT );";

	@SuppressWarnings("unused")
	private final Context helperContext;
	private SQLiteDatabase benefitDB;

	public BenefitDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null /* factory */, DATABASE_VERSION);
		helperContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		benefitDB = db;
		benefitDB.execSQL(BENEFITS_CREATE_TABLE);
		loadBenefits();
	}

	private void loadBenefits() {
		new Thread(new Runnable() {
			public void run() {
				readBenefits();
			}
		}).start();
	}

	private void readBenefits() {
		Categories ctgs = new Categories();

		for (int i = 0; i < ctgs.size(); i++) {
			Category tmpCategory = ctgs.getCategory(i);

			if (tmpCategory.getSubcategories() != null
					&& tmpCategory.getSubcategories().length > 0) {
				for (String subc : tmpCategory.getSubcategories()) {
					addBenefit(tmpCategory.getCategoryName(), subc);
				}
			} else {
				addBenefit(tmpCategory.getCategoryName(), null);
			}
		}
	}

	public long addBenefit(String category, String subcategory) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(BENEFITS_TABLE_CATEGORY_COLUMN, category);
		initialValues.put(BENEFITS_TABLE_SUBCATEGORY_COLUMN, subcategory);

		return benefitDB.insert(BENEFITS_TABLE_NAME, null, initialValues);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + BENEFITS_TABLE_NAME);
		onCreate(db);
	}

}
