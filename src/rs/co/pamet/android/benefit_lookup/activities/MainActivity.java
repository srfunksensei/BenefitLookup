package rs.co.pamet.android.benefit_lookup.activities;

import rs.co.pamet.android.benefit_lookup.controls.CoverFlow;

import rs.co.pamet.android.benefit_lookup.R;
import rs.co.pamet.android.benefit_lookup.controls.adapters.AbstractCoverFlowImageAdapter;
import rs.co.pamet.android.benefit_lookup.controls.adapters.ReflectingImageAdapter;
import rs.co.pamet.android.benefit_lookup.controls.adapters.ResourceImageAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;

import android.widget.TextView;

/**
 * The Class MainActivity.
 */
public class MainActivity extends Activity {

	private TextView textView;
	private TextView titleText;
	private ImageButton searchButton;
	private AbstractCoverFlowImageAdapter coverImageAdapter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set up titlebar
		getWindow().requestFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar);

		// set up title name
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("CATEGORIES");

		searchButton = (ImageButton) findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onSearchRequested();
			}
		});
		// note resources below are taken using getIdentifier to allow importing
		// this library as library.
		// final CoverFlow coverFlow1 = (CoverFlow)
		// findViewById(R.id.coverflow);
		// setupCoverFlow(coverFlow1, false);
		final CoverFlow reflectingCoverFlow = (CoverFlow) findViewById(R.id.coverflowReflect);
		setupCoverFlow(reflectingCoverFlow, true);
	}

	/**
	 * Setup cover flow.
	 * 
	 * @param mCoverFlow
	 *            the m cover flow
	 * @param reflect
	 *            the reflect
	 */
	private void setupCoverFlow(final CoverFlow mCoverFlow,
			final boolean reflect) {
		if (reflect) {
			coverImageAdapter = new ReflectingImageAdapter(
					new ResourceImageAdapter(this));
		} else {
			coverImageAdapter = new ResourceImageAdapter(this);
		}
		mCoverFlow.setAdapter(coverImageAdapter);
		mCoverFlow.setSelection(2, true);

		textView = (TextView) findViewById(R.id.statusText);
		textView.setText(coverImageAdapter.getCategoryName(2));

		setupListeners(mCoverFlow);
	}

	/**
	 * Sets the up listeners.
	 * 
	 * @param mCoverFlow
	 *            the new up listeners
	 */
	private void setupListeners(final CoverFlow mCoverFlow) {
		mCoverFlow.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent,
					final View view, final int position, final long id) {
				Intent i = new Intent(MainActivity.this, ServicesActivity.class);

				i.putExtra("Category Title", textView.getText());
				i.putExtra("Subcategories",
						coverImageAdapter.getSubcategoryList(position));

				startActivity(i);

			}

		});
		mCoverFlow.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(final AdapterView<?> parent,
					final View view, final int position, final long id) {
				textView.setText(coverImageAdapter.getCategoryName(position));
			}

			@Override
			public void onNothingSelected(final AdapterView<?> parent) {
				textView.setText("Nothing clicked!");
			}
		});
	}

}
