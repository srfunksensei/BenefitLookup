package rs.co.pamet.android.benefit_lookup;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.Toast;

public class BLWidgetProvider extends AppWidgetProvider {
	private static final String LOG = "widget";

	public static final String EMERGENCY_ACTION = "rs.co.pamet.android.benefit_lookup.widget.EMERGENCY_ACTION";
	public static final String CALL_DOCTOR_ACTION = "rs.co.pamet.android.benefit_lookup.widget.CALL_DOCTOR_ACTION";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.appwidget.AppWidgetProvider#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	// Called when the BroadcastReceiver receives an Intent broadcast.
	// Checks to see whether the intent's action is TOAST_ACTION. If it is, the
	// app widget
	// displays a Toast message for the current item.
	@Override
	public void onReceive(Context context, Intent intent) {
		AppWidgetManager mgr = AppWidgetManager.getInstance(context);

		if (intent.getAction().equals(EMERGENCY_ACTION)) {
			int appWidgetId = intent.getIntExtra(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			int viewIndex = intent.getIntExtra(CALL_DOCTOR_ACTION, 0);
			Toast.makeText(context, "Touched view " + viewIndex,
					Toast.LENGTH_SHORT).show();
		}
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		// update each of the app widgets with the remote adapter
		for (int i = 0; i < appWidgetIds.length; ++i) {

			// Sets up the intent that points to the StackViewService that will
			// provide the views for this collection.
			Intent intent = new Intent(context, BLWidgetProvider.class);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					appWidgetIds[i]);
			// When intents are compared, the extras are ignored, so we need to
			// embed the extras
			// into the data so that the extras will not be ignored.
			intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
			RemoteViews rv = new RemoteViews(context.getPackageName(),
					R.layout.widget_custom);
//			rv.setRemoteAdapter(appWidgetIds[i], R.id.emergency, intent);

			
			// This section makes it possible for items to have individualized
			// behavior.
			// It does this by setting up a pending intent template. Individuals
			// items of a collection
			// cannot set up their own pending intents. Instead, the collection
			// as a whole sets
			// up a pending intent template, and the individual items set a
			// fillInIntent
			// to create unique behavior on an item-by-item basis.
			Intent toastIntent = new Intent(context, BLWidgetProvider.class);
			// Set the action for the intent.
			// When the user touches a particular view, it will have the effect
			// of
			// broadcasting TOAST_ACTION.
			toastIntent.setAction(BLWidgetProvider.EMERGENCY_ACTION);
			toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					appWidgetIds[i]);
			intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
			PendingIntent toastPendingIntent = PendingIntent.getBroadcast(
					context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//			rv.setPendingIntentTemplate(R.id.emergency, toastPendingIntent);

			appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		/*
		 * Log.e(LOG, "onUpdate method called"); // Get all ids ComponentName
		 * thisWidget = new ComponentName(context, BLWidgetProvider.class);
		 * int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		 * 
		 * // Build the intent to call the service Intent intent = new
		 * Intent(context.getApplicationContext(), UpdateWidgetService.class);
		 * intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
		 * 
		 * // Update the widgets via the service context.startService(intent);
		 */
	}
}
