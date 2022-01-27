package rs.co.pamet.android.benefit_lookup.controls.adapters;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import rs.co.pamet.android.benefit_lookup.controls.categories.Categories;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

/**
 * This class is an adapter that provides images from a fixed set of resource
 * ids. Bitmaps and ImageViews are kept as weak references so that they can be
 * cleared by garbage collection when not needed.
 * 
 */
public class ResourceImageAdapter extends AbstractCoverFlowImageAdapter {

	/** The Constant TAG. */
	private static final String TAG = ResourceImageAdapter.class
			.getSimpleName();

	private static final Categories CATEGORIES = new Categories();

	/** The bitmap map. */
	private final Map<Integer, WeakReference<Bitmap>> bitmapMap = new HashMap<Integer, WeakReference<Bitmap>>();

	private final Context context;

	/**
	 * Creates the adapter with default set of resource images.
	 * 
	 * @param context
	 *            context
	 */
	public ResourceImageAdapter(final Context context) {
		super();
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public synchronized int getCount() {
		return CATEGORIES.size();
	}

	@Override
	public String getCategoryName(int position) {
		return CATEGORIES.getCategory(position).getCategoryName();
	}

	@Override
	public String[] getSubcategoryList(int position) {
		return CATEGORIES.getCategory(position).getSubcategories();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.polidea.coverflow.AbstractCoverFlowImageAdapter#createBitmap(int)
	 */
	@Override
	protected Bitmap createBitmap(final int position) {
		Log.v(TAG, "creating item " + position);
		final Bitmap bitmap = ((BitmapDrawable) context.getResources()
				.getDrawable(CATEGORIES.getCategory(position).getResourceId()))
				.getBitmap();
		bitmapMap.put(position, new WeakReference<Bitmap>(bitmap));
		return bitmap;
	}
}