package com.example.d_map;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import com.mapquest.mapping.MapQuest;
import com.mapquest.mapping.maps.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private static final String TAG = "";
    private final LatLng MAPQUEST_HEADQUARTERS_LOCATION = new LatLng(
            -6.8749534061560915, 107.60492399711352);
    //latitude dan longitude diatur mengikuti lokasi UNPAR.
    private MapView mMapView;
    private MapboxMap mMapboxMap;
    private EditText et_search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapQuest.start(getApplicationContext());
        setContentView(R.layout.activity_main);
        et_search = (EditText) findViewById(R.id.et_search);
        mMapView = (MapView) findViewById(R.id.mapquestMapView);


        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_search);
        String[] countries = getResources().getStringArray(R.array.locations_array);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        textView.setAdapter(adapter);

        et_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_search.setHint("");
                return false;
            }
        });

        et_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    et_search.setHint("");
                }
            }
        });

        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMapboxMap = mapboxMap;
                mMapView.setStreetMode();
                mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MAPQUEST_HEADQUARTERS_LOCATION, 11));
                addMarker(mapboxMap);

                init();
            }
        });

    }

    private void init() {
        Log.d(TAG, "init : initializing");

        et_search.setOnEditorActionListener((new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int action_id, KeyEvent keyEvent) {
                if(action_id == EditorInfo.IME_ACTION_SEARCH || action_id == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                    //Nanti cari di mapquest buat function search locationnya ditaro disini

                    locate();
                }
                return false;
            }
        }));
    }

    private void locate() {
        Log.d(TAG, "locating");

        String searchString = et_search.getText().toString();

        Geocoder geocoder = new Geocoder(MainActivity.this);
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "locate: IOException: " + e.getMessage());
        }

        if(list.size() > 0) {
            Address address = list.get(0);

            Log.d(TAG, "found location : " + address.toString());
        }
    }

    CameraPosition position = new CameraPosition.Builder()
        .target(MAPQUEST_HEADQUARTERS_LOCATION) // Sets the new camera position
        .zoom(10) // Sets the zoom to level 10
        .tilt(20) // Set the camera tilt to 20 degrees
        .build(); // Builds the CameraPosition object from the builder

    private void addMarker(MapboxMap mapboxMap) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(MAPQUEST_HEADQUARTERS_LOCATION);
        markerOptions.title("Lokasi Terkini");
        markerOptions.snippet("Ini lokasi anda saat ini.");
        mapboxMap.addMarker(markerOptions);
    }

    @Override
    public void onResume()
    { super.onResume(); mMapView.onResume(); }

    @Override
    public void onPause()
    { super.onPause(); mMapView.onPause(); }

    @Override
    protected void onDestroy()
    { super.onDestroy(); mMapView.onDestroy(); }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    { super.onSaveInstanceState(outState); mMapView.onSaveInstanceState(outState); }
}