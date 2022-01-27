package rs.co.pamet.android.benefit_lookup.activities;

import rs.co.pamet.android.benefit_lookup.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author MB
 * 
 */
public class ServicesActivity extends Activity {

	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get subcategories for list view
		final String[] subcategories = getIntent().getStringArrayExtra(
				"Subcategories");

		if (subcategories.length > 1) {

			// set up titlebar
			getWindow().requestFeature(Window.FEATURE_CUSTOM_TITLE);
			setContentView(R.layout.activity_services);
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.titlebar);

			// set up title name
			TextView titleText = (TextView) findViewById(R.id.titleText);
			titleText.setText(getIntent().getStringExtra("Category Title"));
			titleText.setSelected(true);

			ImageButton searchButton = (ImageButton) findViewById(R.id.searchButton);
			searchButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					onSearchRequested();
				}
			});

			ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(
					ServicesActivity.this, R.layout.services_list_item,
					R.id.servicesItemText, subcategories);

			list = (ListView) findViewById(R.id.services_list);
			list.setAdapter(listAdapter);

			list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					Intent dIntent = new Intent(ServicesActivity.this,FlipperActivity.class);
					dIntent.putExtra("selectedService", subcategories[position]);
					startActivity(dIntent);
				}
			});

			list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {

					startActivity(new Intent(ServicesActivity.this,
							BenefitInfo.class));
					return true;
				}

			});

		} else {
//			Intent dIntent = new Intent(ServicesActivity.this,TabbedListMapActivity.class);
			Intent dIntent = new Intent(ServicesActivity.this,FlipperActivity.class);
			dIntent.putExtra("selectedService", subcategories[0]);
			startActivity(dIntent);
			
			this.finish();
		}
	}
}
