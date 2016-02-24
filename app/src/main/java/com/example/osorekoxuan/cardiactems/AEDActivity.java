package com.example.osorekoxuan.cardiactems;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class AEDActivity extends Activity {
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private TextView aedAddress, aedLocation, aedNumber, aedZip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aed);

        aedAddress = (TextView) findViewById(R.id.aed_address);
        aedLocation = (TextView) findViewById(R.id.aed_location);
        aedNumber = (TextView) findViewById(R.id.aed_nubmer);
        aedZip = (TextView) findViewById(R.id.aed_zip);

        Bundle b = getIntent().getExtras();
        double latitude = b.getDouble("latitude");
        double longitude = b.getDouble("longitude");
        Log.d("AED Lat", Double.toString(latitude));
        Log.d("AED Long", Double.toString(longitude));
        ParseGeoPoint geoPoint = new ParseGeoPoint(latitude, longitude);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("AED_DATA");
        query.whereEqualTo("LOCATION", geoPoint);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> pointList, ParseException e) {
                if (e == null) {
                    for(ParseObject point : pointList){
                        aedAddress.setText(point.getString("ADD_FULL"));
                        aedLocation.setText(point.getString("DEV_LOC"));
                        aedNumber.setText(point.getString("DEV_COUNT"));
                        aedZip.setText(point.getString("POSTAL_CD"));
                        Log.d("AED Detailed", point.getString("ADD_FULL"));
                    }
                    Log.d("AED Detailed", " get detailed information successfully");
                } else {
                    Log.d("AED Detailed"," get detailed information error");
                }
            }
        });
    }
}
