package com.example.osorekoxuan.cardiactems;

import android.app.ListActivity;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode.GeoPoint;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class EventListActivity extends ListActivity {

    ArrayList<String> item = new ArrayList<>();
    BootstrapButton testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        ParseQuery<ParseObject> emergencyQuery = ParseQuery.getQuery("Emergency");
        // 4
        emergencyQuery.orderByDescending("createdAt");
        // 6
        emergencyQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                // Handle the results
                for (ParseObject event : objects) {
                    String temp = "Location: " + event.getString("Name");
                    item.add(temp);
                    temp = "Status: " + event.getString("Status");
                    item.add(temp);
                    item.add(" ");
                    Log.d("Event", item.get(0));
                }
                setListAdapter(new ArrayAdapter<String>(EventListActivity.this, android.R.layout.simple_list_item_1, item));
            }
        });

        GeoPoint point = getLocationFromAddress("106 GoodWood Park Court");

        testButton = (BootstrapButton) findViewById(R.id.test_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPush();
            }
        });

    }

    public GeoPoint getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            Log.d("Address Lat", Double.toString(location.getLatitude()));
            Log.d("Address Long", Double.toString(location.getLongitude()));

            p1 = new GeoPoint(2, (location.getLatitude() * 1E6),
                    (location.getLongitude() * 1E6));
            return p1;
        }catch (Exception ex){

        }
        return p1;
    }

    public void sendPush(){
        // Find users near a given location
        /*ParseQuery userQuery = ParseUser.getQuery();
        userQuery.whereWithinMiles("location", stadiumLocation, 1.0);*/

        // Find devices associated with these users
        ParseQuery pushQuery = ParseInstallation.getQuery();
        //pushQuery.whereMatchesQuery("user", userQuery);

        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery); // Set our Installation query
        push.setMessage("This is a test for push notification");
        push.sendInBackground();
    }
}
