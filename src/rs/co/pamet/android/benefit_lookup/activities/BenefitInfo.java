package rs.co.pamet.android.benefit_lookup.activities;

import rs.co.pamet.android.benefit_lookup.R;
import android.os.Bundle;
import android.app.Activity;

import android.text.Html;
import android.widget.TextView;

public class BenefitInfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_benefit_info);

		String prefferedText = "<p>Amount or percentage you pay for this service (\"copay\")</p>"
				+ "<b>" + getResources().getString(R.string.dummy_lorem_ipsum_short) + "</b>";

		String nonPrefferedText = "<p>Does your payment count toward satisfying your deductible?</p>"
				+ "<b>" + getResources().getString(R.string.dummy_lorem_ipsum_short) + "</b>";

		// preferred text
		TextView text = (TextView) findViewById(R.id.prefered_text);
		text.setText(Html.fromHtml(prefferedText));

		// non-preferred text
		text = (TextView) findViewById(R.id.non_prefered_text);
		text.setText(Html.fromHtml(nonPrefferedText));
	}
}