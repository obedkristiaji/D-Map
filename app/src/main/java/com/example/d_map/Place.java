package com.example.d_map;

import com.mapbox.mapboxsdk.geometry.LatLng;

public class Place {
    String name;
    String address;
    LatLng location;

    public Place(String name, String address, LatLng location) {
        this.name = name;
        this.address = address;
        this.location = location;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public LatLng getLocation() {
        return this.location;
    }
}
