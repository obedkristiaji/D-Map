package com.example.d_map;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.d_map.databinding.ActivityMainBinding;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import com.mapquest.mapping.MapQuest;
import com.mapquest.mapping.maps.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements OnMapReadyCallback {
    private static final String TAG = "";
    private LatLng USER_LOCATION;
    private MapView mMapView;
    private MapboxMap mMapboxMap;
//    private EditText et_search;
    private GpsTracker gpsTracker;
    private SearchTask task;
    private ActivityMainBinding binding;
    private List<Place> placeList = new ArrayList();
    private boolean marker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        MapQuest.start(getApplicationContext());
        this.binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
//        et_search = (EditText) findViewById(R.id.et_search);
        mMapView = (MapView) findViewById(R.id.mapquestMapView);

//        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_search);
//        String[] countries = getResources().getStringArray(R.array.locations_array);
//        ArrayAdapter<String> adapter =
//                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
//        textView.setAdapter(adapter);

//        et_search.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                et_search.setHint("");
//                return false;
//            }
//        });

//        et_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    et_search.setHint("");
//                }
//            }
//        });

        task = new SearchTask(MainActivity.this, this);

        // Mendapatkan lokasi inisial
        gpsTracker = new GpsTracker(MainActivity.this, this, task);

        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMapboxMap = mapboxMap;
                mMapView.setStreetMode();
                addMarker(mapboxMap, USER_LOCATION);
                marker = true;
//                mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
//                    @Override
//                    public boolean onMarkerClick(Marker marker) {
//                        LatLng LATEST_LOCATION = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
//                        marker.setPosition(LATEST_LOCATION);
//                        mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LATEST_LOCATION, 15));
//                        return true;
//                    }
//                });

                binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        String selectedItem = binding.spinnerCategory.getSelectedItem().toString();
                        gpsTracker.changeLocation(selectedItem);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        });

//        try {
//            if (ContextCompat.checkSelfPermission(getApplicationContext(),
//                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        init();
    }

    private void init() {
        Log.d(TAG, "init : initializing");
        USER_LOCATION = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        task.execute(USER_LOCATION, "SPBU");



//        et_search.setOnEditorActionListener((new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int action_id, KeyEvent keyEvent) {
//                if(action_id == EditorInfo.IME_ACTION_SEARCH || action_id == EditorInfo.IME_ACTION_DONE
//                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
//
//                    //Nanti cari di mapquest buat function search locationnya ditaro disini
//
//                    locate();
//                }
//                return false;
//            }
//        }));
    }

//    private void locate() {
//        Log.d(TAG, "locating");
//
//        String searchString = et_search.getText().toString();
//
//        Geocoder geocoder = new Geocoder(MainActivity.this);
//        List<Address> list = new ArrayList<>();
//
//        try {
//            list = geocoder.getFromLocationName(searchString, 1);
//        } catch (IOException e) {
//            Log.e(TAG, "locate: IOException: " + e.getMessage());
//        }
//
//        if(list.size() > 0) {
//            Address address = list.get(0);
//
//            Log.d(TAG, "found location : " + address.toString());
//        }
//    }

    public boolean initMarker() {
        return this.marker;
    }

    public void resetList() {
        this.placeList = new ArrayList();
    }

    public void addMarker(MapboxMap mapboxMap, LatLng location) {
        mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(location);
        markerOptions.title("Lokasi Terkini");
        markerOptions.snippet("Ini lokasi anda saat ini.");
        mapboxMap.addMarker(markerOptions);
    }

    public void addOtherMarker(MapboxMap mapboxMap, LatLng location, String name, String address) {
        IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
        Icon icon = iconFactory.fromResource(R.drawable.mapbox_marker_icon_blue);

        this.placeList.add(new Place(name, address, location));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(location);
        markerOptions.title(name);
        markerOptions.snippet(address);
        markerOptions.icon(icon);
        mapboxMap.addMarker(markerOptions);
        Log.d("placeListMarker", this.getList().toString());
    }

    public void initializeAdapter() {
        ListView lstPlace = (ListView) binding.lstSearch;
        PlaceListAdapter placeAdapter = new PlaceListAdapter(MainActivity.this, this, mMapboxMap);
        Log.d("placeList", this.placeList.toString());
        placeAdapter.update(this.placeList);
        lstPlace.setAdapter(placeAdapter);
        Log.d("placeListPlace", this.placeList.toString());
    }

    public List getList() {
        return this.placeList;
    }

    public MapboxMap getMapboxMap(){
        return this.mMapboxMap;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //If user presses allow
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        } else {
            //If user presses deny
            Toast.makeText(getApplicationContext(), "Permission Denied, please restart the app and allow permission", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {

    }
}