package rs.co.pamet.android.benefit_lookup.activities;

import rs.co.pamet.android.benefit_lookup.R;
import rs.co.pamet.android.benefit_lookup.content_providers.SearchContentProvider;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;

public class SearchActivity extends Activity {

	private TextView resultTitle;
	private ListView resultList;

	// query intent
	private Intent queryIntent;

	// query action
	private String queryAction;

	// query string
	private String queryString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);

		resultTitle = (TextView) findViewById(R.id.searchResultTitle);
		resultList = (ListView) findViewById(R.id.searchResultTitleList);

		queryIntent = getIntent();
		queryAction = queryIntent.getAction();
		queryString = queryIntent.getStringExtra(SearchManager.QUERY);

		if (Intent.ACTION_SEARCH.equals(queryAction)) {
			this.showResults(queryString);
		} else if (Intent.ACTION_VIEW.equals(queryAction)) {
			this.doView(queryIntent);
		}
	}

	@Override
	protected void onNewIntent(Intent newIntent) {
		super.onNewIntent(newIntent);

		queryIntent = newIntent;
		queryAction = queryIntent.getAction();
		queryString = queryIntent.getStringExtra(SearchManager.QUERY);

		if (Intent.ACTION_SEARCH.equals(queryAction)) {
			this.showResults(queryString);
		} else if (Intent.ACTION_VIEW.equals(queryAction)) {
			this.doView(queryIntent);
		}
	}

	/**
	 * Searches the dictionary and displays results for the given query.
	 * 
	 * @param query
	 *            The search query
	 */
	private void showResults(String query) {
		final Cursor cursor = managedQuery(SearchContentProvider.CONTENT_URI, null, null, new String[] { query }, null);

		if (cursor == null) {
			// There are no results
			resultTitle.setText(getString(R.string.no_search_results));
		} else {

			resultTitle.setText("Results for: \"" + queryString + "\"");
			// Specify the columns we want to display in the result
			String[] from = new String[] { SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_TEXT_2 };

			// Specify the corresponding layout elements where we want the
			// columns to go
			int[] to = new int[] { R.id.searchResultCategory, R.id.searchResuluSubcategory };

			// Create a simple cursor adapter for the definitions and apply them
			// to the ListView
			SimpleCursorAdapter words = new SimpleCursorAdapter(this, R.layout.search_result_item, cursor, from, to);
			resultList.setAdapter(words);

			// Define the on-click listener for the list items
			resultList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

					Intent sIntent = new Intent(SearchActivity.this, FlipperActivity.class);

					int wIndex = cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1);
					int dIndex = cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_2);

					String selectedService = cursor.getString(wIndex);
					if (selectedService == null || selectedService.equals("")) {
						selectedService = cursor.getString(dIndex);
					}

					sIntent.putExtra("selectedService", selectedService);

					startActivity(sIntent);
				}
			});

			// Define the onlongclick listener for the list items
			resultList.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

					Intent benefitIntent = new Intent(getApplicationContext(), BenefitInfo.class);
					Uri data = Uri.withAppendedPath(SearchContentProvider.CONTENT_URI, String.valueOf(id));
					benefitIntent.setData(data);
					startActivity(benefitIntent);

					return true;
				}
			});
		}
	}

	private void doView(final Intent queryIntent) {

		Uri uri = queryIntent.getData();

		Cursor cursor = managedQuery(uri, null, null, null, null);

		if (cursor == null) {
			this.finish();
		} else {
			cursor.moveToFirst();

			int wIndex = cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1);
			int dIndex = cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_2);

			String selectedService = cursor.getString(wIndex);
			if (selectedService == null || selectedService.equals("")) {
				selectedService = cursor.getString(dIndex);
			}

			Intent sIntent = new Intent(SearchActivity.this, FlipperActivity.class);
			sIntent.putExtra("selectedService", selectedService);

			startActivity(sIntent);

			this.finish();

		}
	}
}
