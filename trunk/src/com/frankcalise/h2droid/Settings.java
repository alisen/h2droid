package com.frankcalise.h2droid;

import java.util.Calendar;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class Settings extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	
	private static final String OPT_AMOUNT = "SETTING_GOAL";
	private static final String OPT_UNITS = "SETTING_UNITS";
	private static final String OPT_TOASTS = "SETTING_TOASTS";
	private static final String OPT_LARGE_UNITS = "SETTING_LARGE_UNITS";
	private static final String OPT_ENABLE_VOL_UP = "SETTING_ENABLE_VOL_UP";
	private static final String OPT_ENABLE_VOL_DOWN = "SETTING_ENABLE_VOL_DOWN";
	private static final String OPT_VOL_UP_AMOUNT = "SETTING_VOL_UP_AMOUNT";
	private static final String OPT_ENABLE_REMINDERS = "SETTING_ENABLE_REMINDERS";
	private static final String OPT_REMINDER_INTERVAL = "SETTING_REMINDER_INTERVAL";
	private static final String OPT_REMINDER_LED = "SETTING_REMINDER_LIGHT";
	private static final String OPT_REMINDER_SOUND = "SETTING_REMINDER_SOUND";
	private static final String OPT_REMINDER_VIB = "SETTING_REMINDER_VIB";
	private static final String OPT_AMOUNT_DEF = "64";
	private static final String OPT_UNITS_DEF = "1"; // US system
	private static final String OPT_REMINDER_INT_DEF = "60";
	private static final String[] OPT_FAV_AMOUNT_DEF = {"8", "16", "16.9", "20", "33.8"};
	private static final double DEFAULT_AMOUNT = 64.0;
	private static final double DEFAULT_FAV_AMOUNT = 8.0;
	private static final int DEFAULT_REMINDER_INT = 60;
	public static final int UNITS_METRIC = 0;
	public static final int UNITS_IMPERIAL = 1;
	private static final String[] OPT_FAV_AMOUNT = {"FAV_AMOUNT_ONE", 
													"FAV_AMOUNT_TWO",
													"FAV_AMOUNT_THREE",
													"FAV_AMOUNT_FOUR",
													"FAV_AMOUNT_FIVE"};
	
	private EditTextPreference mEditTextPref1;
	private EditTextPreference mEditTextPref2;
	private EditTextPreference mEditTextPref3;
	private EditTextPreference mEditTextPref4;
	private EditTextPreference mEditTextPref5;
	private ListPreference mUnitsPref;
	
	@Override
	protected void onCreate(Bundle savedInstanceBundle) {
		super.onCreate(savedInstanceBundle);
		
		// Load XML prefs file
		addPreferencesFromResource(R.xml.settings);
		
		// Get reference to preferences to update summaries
		mEditTextPref1 = (EditTextPreference)getPreferenceScreen().findPreference(OPT_FAV_AMOUNT[0]);
		mEditTextPref2 = (EditTextPreference)getPreferenceScreen().findPreference(OPT_FAV_AMOUNT[1]);
		mEditTextPref3 = (EditTextPreference)getPreferenceScreen().findPreference(OPT_FAV_AMOUNT[2]);
		mEditTextPref4 = (EditTextPreference)getPreferenceScreen().findPreference(OPT_FAV_AMOUNT[3]);
		mEditTextPref5 = (EditTextPreference)getPreferenceScreen().findPreference(OPT_FAV_AMOUNT[4]);
		mUnitsPref = (ListPreference)getPreferenceScreen().findPreference(OPT_UNITS);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// Setup initial values
		mEditTextPref1.setSummary("Current amount is " + getPreferenceScreen().getSharedPreferences().getString(OPT_FAV_AMOUNT[0], OPT_FAV_AMOUNT_DEF[0]));
		mEditTextPref2.setSummary("Current amount is " + getPreferenceScreen().getSharedPreferences().getString(OPT_FAV_AMOUNT[1], OPT_FAV_AMOUNT_DEF[1]));
		mEditTextPref3.setSummary("Current amount is " + getPreferenceScreen().getSharedPreferences().getString(OPT_FAV_AMOUNT[2], OPT_FAV_AMOUNT_DEF[2]));
		mEditTextPref4.setSummary("Current amount is " + getPreferenceScreen().getSharedPreferences().getString(OPT_FAV_AMOUNT[3], OPT_FAV_AMOUNT_DEF[3]));
		mEditTextPref5.setSummary("Current amount is " + getPreferenceScreen().getSharedPreferences().getString(OPT_FAV_AMOUNT[4], OPT_FAV_AMOUNT_DEF[4]));
		
		String unitsPref = getPreferenceScreen().getSharedPreferences().getString(OPT_UNITS, OPT_UNITS_DEF);
		Log.d("SETTINGS", "UNITS PREF = " + unitsPref);
		if (unitsPref.equals(String.valueOf(UNITS_IMPERIAL))) {
			mUnitsPref.setSummary("US");
		} else {
			mUnitsPref.setSummary("Metric");
		}
		
		
		// Register listener for when a key changes
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		// Unregister listener for when a key changes
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}
	
	/** Get the current value of daily goal amount */
	public static double getAmount(Context context) {
		try {
			String prefString = PreferenceManager.getDefaultSharedPreferences(context)
				.getString(OPT_AMOUNT, OPT_AMOUNT_DEF);
			
			double prefAmount = Double.valueOf(prefString).doubleValue();
			
			return prefAmount;
		} catch (NumberFormatException nfe) {
			return DEFAULT_AMOUNT;
		} 
	}
	
	public static String getFavoriteAmountString(int favIndex, Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
			.getString(OPT_FAV_AMOUNT[favIndex], OPT_FAV_AMOUNT_DEF[favIndex]);
	}
	
	public static int getUnitSystem(Context context) {
		try {
			String prefString = PreferenceManager.getDefaultSharedPreferences(context)
				.getString(OPT_UNITS, OPT_UNITS_DEF);
			
			int prefAmount = Integer.parseInt(prefString);
			
			return prefAmount;
		} catch (NumberFormatException nfe) {
			return UNITS_IMPERIAL;
		} 
	}
	
	public static double getFavoriteAmountDouble(int favIndex, Context context) {
		try {
			String prefString = PreferenceManager.getDefaultSharedPreferences(context)
				.getString(OPT_FAV_AMOUNT[favIndex], OPT_FAV_AMOUNT_DEF[favIndex]);
			
			double prefAmount = Double.valueOf(prefString).doubleValue();
			
			return prefAmount;
		} catch (NumberFormatException nfe) {
			return DEFAULT_FAV_AMOUNT;
		}
	}
	
	public static boolean getToastsSetting(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
			.getBoolean(OPT_TOASTS, true);
	}
	
	public static boolean getLargeUnitsSetting(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
			.getBoolean(OPT_LARGE_UNITS, false);
	}
	
	public static boolean getOverrideVolumeUp(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
			.getBoolean(OPT_ENABLE_VOL_UP, false);
	}
	
	public static boolean getOverrideVolumeDown(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
			.getBoolean(OPT_ENABLE_VOL_DOWN, false);
	}
	
	public static boolean getReminderEnabled(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
			.getBoolean(OPT_ENABLE_REMINDERS, false);
	}
	
	public static int getReminderInterval(Context context) {
		int reminderInterval = 0;
		try {
			String prefString = PreferenceManager.getDefaultSharedPreferences(context).getString(OPT_REMINDER_INTERVAL, OPT_REMINDER_INT_DEF);
			
			reminderInterval = Integer.parseInt(prefString);
		} catch (NumberFormatException nfe) {
			reminderInterval = DEFAULT_REMINDER_INT;
		}
		
		return reminderInterval;
	}
	
	public static Notification getReminderNotification(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		boolean remindersEnabled = prefs.getBoolean(OPT_ENABLE_REMINDERS, false);
		
		if (remindersEnabled) {
			// Reminders are enabled

			// Get light, sound, and vibrate settings
			boolean showLight = prefs.getBoolean(OPT_REMINDER_LED, false);
			String soundPref = prefs.getString(OPT_REMINDER_SOUND, "silent");
			boolean vibrate = prefs.getBoolean(OPT_REMINDER_VIB, false);
			
			// Get the reminder interval user has chosen
			// Default is 60 minutes after last entry
			int reminderInterval = 0;
			try {
				String prefString = prefs.getString(OPT_REMINDER_INTERVAL, OPT_REMINDER_INT_DEF);
				
				reminderInterval = Integer.parseInt(prefString);
				Log.d("REMINDER_INTERVAL", "Interval = " + reminderInterval);
			} catch (NumberFormatException nfe) {
				reminderInterval = DEFAULT_REMINDER_INT;
			}
			
			// Create the calendar object for the notification
			// adjusted for user's interval preference
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, reminderInterval);
			
			// Setup notification object
			String reminderMsg = String.format("Hydrate - %d minutes since last entry", reminderInterval);
			Notification reminder = new Notification(R.drawable.icon,
													 reminderMsg,
													 cal.getTimeInMillis());
			reminder.flags |= Notification.FLAG_AUTO_CANCEL;
			
			// Setup the blinking LED if necessary
			if (showLight) {
				reminder.ledARGB = 0xff0000ff; // Blue LED for water if possible
				reminder.flags |= Notification.FLAG_SHOW_LIGHTS;
			}
			
			// Setup the sound if necessary
			if (!soundPref.equals("silent")) {
				reminder.sound = Uri.parse(soundPref); 
			}
			
			// Setup vibrate if necessary
			if (vibrate) {
				reminder.defaults |= Notification.DEFAULT_VIBRATE;
			}
			
			return reminder;
		} else {
			return null;	
		}
		
	}
	
	public static double getVolumeUpAmount(Context context) {
		try {
			String prefString = PreferenceManager.getDefaultSharedPreferences(context)
				.getString(OPT_VOL_UP_AMOUNT, OPT_FAV_AMOUNT_DEF[0]);
			
			double prefAmount = Double.valueOf(prefString).doubleValue();
			
			return prefAmount;
		} catch (NumberFormatException nfe) {
			return DEFAULT_FAV_AMOUNT;
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		
		// Update the preference summaries for favorite amounts
		if (key.equals(OPT_FAV_AMOUNT[0])) {
			mEditTextPref1.setSummary("Current amount is " + sharedPreferences.getString(key, OPT_FAV_AMOUNT_DEF[0]));
		} else if (key.equals(OPT_FAV_AMOUNT[1])) {
			mEditTextPref2.setSummary("Current amount is " + sharedPreferences.getString(key, OPT_FAV_AMOUNT_DEF[1]));
		} else if (key.equals(OPT_FAV_AMOUNT[2])) {
			mEditTextPref3.setSummary("Current amount is " + sharedPreferences.getString(key, OPT_FAV_AMOUNT_DEF[2]));
		} else if (key.equals(OPT_FAV_AMOUNT[3])) {
			mEditTextPref4.setSummary("Current amount is " + sharedPreferences.getString(key, OPT_FAV_AMOUNT_DEF[3]));
		} else if (key.equals(OPT_FAV_AMOUNT[4])) {
			mEditTextPref5.setSummary("Current amount is " + sharedPreferences.getString(key, OPT_FAV_AMOUNT_DEF[4]));
		} else if (key.equals(OPT_UNITS)) {
			String unitsPref = sharedPreferences.getString(key, OPT_UNITS_DEF);
			if (unitsPref.equals(String.valueOf(UNITS_IMPERIAL))) {
				mUnitsPref.setSummary("US");
			} else {
				mUnitsPref.setSummary("Metric");
			}
			
			// Update the units on the widget
			SharedPreferences localData = getSharedPreferences("hydrate_data", 0);
			Intent widgetIntent = new Intent(AppWidget.FORCE_WIDGET_UPDATE);
			widgetIntent.putExtra("AMOUNT", Double.valueOf(localData.getString("amount", "0")));
	    	widgetIntent.putExtra("PERCENT", Double.valueOf(localData.getString("percent", "0")));
	    	widgetIntent.putExtra("UNITS", Integer.valueOf(unitsPref));
	    	this.sendBroadcast(widgetIntent);
		}
	}
}