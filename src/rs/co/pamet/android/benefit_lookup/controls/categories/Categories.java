package rs.co.pamet.android.benefit_lookup.controls.categories;

import java.util.ArrayList;
import java.util.List;
import rs.co.pamet.android.benefit_lookup.R;

/**
 * 
 * @author MB
 * 
 */

public class Categories {

	/** List of all categories */
	private static List<Category> CATEGORY_LIST = null;

	private static final int[] CATEGORY_IMAGES_IDS = {
			R.drawable.back_spine_neck, R.drawable.brain_head,
			R.drawable.children_health,
			R.drawable.digestive_system_liver_gallbladder,
			R.drawable.ears_nose_throat, R.drawable.eyes,
			R.drawable.heart_circulation, R.drawable.hips_legs_knees_feet,
			R.drawable.kidneys_bladder, R.drawable.lungs,
			R.drawable.men_health, R.drawable.office_visits,
			R.drawable.shoulders_arms_hands, R.drawable.weight_loss,
			R.drawable.women_health };

	/* This static field acts as the app's "fake" database of location data */
	private static final String[] CATEGORY_BACK_SPINE_NECK = {
			"Back, Back Surgery - Laminectomy",
			"Back, Laminectomy - Inpatient", "Back, Spinal Fusion (Anterior)",
			"Back, Spinal Fusion (Posterior)", "Chiropractic Treatment",
			"Spine, Bone Density Study of Spine or Pelvis", "Spine, MRI Spine" };

	private static final String[] CATEGORY_BRAIN_HEAD = { "CT Scan Head",
			"MRI Brain" };

	private static final String[] CATEGORY_CHILDREN_HEALTH = {
			"Tonsillectomy and Adenoidectomy, Under Age 12", "Well Child Visit" };

	private static final String[] CATEGORY_DIGESTIVE_SYSTEM = {
			"Colonoscopy Screening", "Colonoscopy with Biopsy",
			"CT Scan Abdomen", "CT Scan Pelvis",
			"Gallbladder Removal, Laparoscopic",
			"Hernia Inguinal Repair (Age 5+)", "Upper GI Endoscopy",
			"Upper GI Endoscopy with Biopsy",
			"Weight Loss, Bariatric Surgery (Lap Band)",
			"Weight Loss, Laparoscopic Gastric Bypass" };

	private static final String[] CATEGORY_EARS_NOSE_THROAT = {
			"Ears, Tympanostomy",
			"Nasal/Sinus - Corrective Surgery - Septoplasty",
			"Nasal/Sinus - Endoscopy - Sinus Surgery",
			"Tonsillectomy and Adenoidectomy, Under Age 12" };

	private static final String[] CATEGORY_EYES = { "Cataract Removal" };

	private static final String[] CATEGORY_HEART_CIRCULATION = {
			"Cardiac Angioplasty with Drug Eluting Stent",
			"Cardiac Defibrillator Implant without Cardiac Cathet...",
			"Coronary Bypass (CABG) without Cardiac Catheterizati...",
			"Left Heart Catheterization" };

	private static final String[] CATEGORY_HIPS_LEGS_KNEES_FEET = {
			"CT Scan Pelvis", "Foot, Bunionectomy",
			"Foot, Hammertoe Correction", "Hip Replacement",
			"Knee Arthroscopy with Cartilage Repair", "Knee Replacement",
			"Knee, ACL Repair by Arthroscopy", "MRI Lower Limb",
			"MRI Lower Limb with Joint" };

	private static final String[] CATEGORY_KIDNEYS_BLADDER = {
			"Bladder Repair For Incontinence (Sling)",
			"Bladder, Urethra and Bladder Scope", "CT Scan Abdomen",
			"Kidney, Lithotripsy - Fragmenting of Kidney Stones" };

	private static final String[] CATEGORY_LUNGS = { "Bronchoscopy" };

	private static final String[] CATEGORY_MEN_HEALTH = { "Hernia Inguinal Repair (Age 5+)" };

	private static final String[] CATEGORY_OFFICE_VISITS = {
			"Chiropractic Treatment", "Office Visit with Current Doctor",
			"Office Visit with New Doctor", "Well Child Visit",
			"Women's Health, OB/GYN Exam" };

	private static final String[] CATEGORY_SHOULDERS_ARMS_HANDS = {
			"MRI Upper Limb", "Shoulder Arthroscopy",
			"Shoulder Arthroscopy with Rotator Cuff Repair",
			"Wrist, Carpal Tunnel" };

	private static final String[] CATEGORY_WEIGHT_LOSS = {
			"Weight Loss, Bariatric Surgery (Lap Band)",
			"Weight Loss, Laparoscopic Gastric Bypass" };

	private static final String[] CATEGORY_WOMEN_HEALTH = {
			"Bladder Repair For Incontinence (Sling)",
			"Breast Biopsy Percutaneous w/ Imaging",
			"Breast Biopsy with Device", "Breast Lumpectomy",
			"Breast, Analog Mammography", "Breast, Digital Mammography",
			"Childbirth, C-Section Delivery", "Childbirth, Vaginal Delivery",
			"Dilation & Curettage - D&C", "Hysterectomy",
			"Laparoscopic Removal of Ovaries and/or Fallopian Tub...",
			"Laparoscopic Tubal Block or Tubal Ligation",
			"Spine, Bone Density Study of Spine or Pelvis",
			"Women's Health, OB/GYN Exam" };

	/* end of fake declaration */

	public Categories() {
		CATEGORY_LIST = new ArrayList<Category>();

		CATEGORY_LIST.add(new Category(CATEGORY_IMAGES_IDS[0],
				"Back, spine, neck", CATEGORY_BACK_SPINE_NECK));
		CATEGORY_LIST.add(new Category(CATEGORY_IMAGES_IDS[1], "Brain, head",
				CATEGORY_BRAIN_HEAD));
		CATEGORY_LIST.add(new Category(CATEGORY_IMAGES_IDS[2],
				"Children's health", CATEGORY_CHILDREN_HEALTH));
		CATEGORY_LIST.add(new Category(CATEGORY_IMAGES_IDS[3],
				"Digestive system, liver, gallbladder",
				CATEGORY_DIGESTIVE_SYSTEM));
		CATEGORY_LIST.add(new Category(CATEGORY_IMAGES_IDS[4],
				"Ears, nose, throat", CATEGORY_EARS_NOSE_THROAT));
		CATEGORY_LIST.add(new Category(CATEGORY_IMAGES_IDS[5], "Eyes",
				CATEGORY_EYES));
		CATEGORY_LIST.add(new Category(CATEGORY_IMAGES_IDS[6],
				"Heart and circulation", CATEGORY_HEART_CIRCULATION));
		CATEGORY_LIST.add(new Category(CATEGORY_IMAGES_IDS[7],
				"Hips, legs, knees, feet", CATEGORY_HIPS_LEGS_KNEES_FEET));
		CATEGORY_LIST.add(new Category(CATEGORY_IMAGES_IDS[8],
				"Kidneys and bladder", CATEGORY_KIDNEYS_BLADDER));
		CATEGORY_LIST.add(new Category(CATEGORY_IMAGES_IDS[9], "Lungs",
				CATEGORY_LUNGS));
		CATEGORY_LIST.add(new Category(CATEGORY_IMAGES_IDS[10], "Men's health",
				CATEGORY_MEN_HEALTH));
		CATEGORY_LIST.add(new Category(CATEGORY_IMAGES_IDS[11],
				"Office visits", CATEGORY_OFFICE_VISITS));
		CATEGORY_LIST.add(new Category(CATEGORY_IMAGES_IDS[12],
				"Shoulders, arms, hands", CATEGORY_SHOULDERS_ARMS_HANDS));
		CATEGORY_LIST.add(new Category(CATEGORY_IMAGES_IDS[13], "Weight loss",
				CATEGORY_WEIGHT_LOSS));
		CATEGORY_LIST.add(new Category(CATEGORY_IMAGES_IDS[14],
				"Women's health", CATEGORY_WOMEN_HEALTH));
	}

	public Category getCategory(int index) {
		return CATEGORY_LIST.get(index);
	}

	public int size() {
		return CATEGORY_LIST.size();
	}

}
