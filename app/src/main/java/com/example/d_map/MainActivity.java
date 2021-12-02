package com.example.d_map;

import android.app.Activity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import com.mapquest.mapping.MapQuest;
import com.mapquest.mapping.maps.MapView;

public class MainActivity extends Activity {
    private final LatLng MAPQUEST_HEADQUARTERS_LOCATION = new LatLng(
            -6.8749534061560915, 107.60492399711352);
    //latitude dan longitude diatur mengikuti lokasi UNPAR.
    private MapView mMapView;
    private MapboxMap mMapboxMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapQuest.start(getApplicationContext());
        setContentView(R.layout.activity_main);

        mMapView = (MapView) findViewById(R.id.mapquestMapView);

        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMapboxMap = mapboxMap;
                mMapView.setStreetMode();
                mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MAPQUEST_HEADQUARTERS_LOCATION, 11));
                addMarker(mapboxMap);
            }
        });
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