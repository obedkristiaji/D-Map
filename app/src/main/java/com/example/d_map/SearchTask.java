package com.example.d_map;

import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.location.Location;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchTask {
    private final Context context;
    private final MainActivity activity;
    private final String baseUrl = "https://www.mapquestapi.com/search/v2/radius?key=GHMLYHtZdprKGLFSXGBaGHAsb9pF4iAZ&radius=100&maxMatches=5&origin=";

    public SearchTask(Context context, MainActivity main) {
        this.context = context;
        this.activity = main;
    }

    public void execute(LatLng location, String query) {
        JSONObject jsonInput = new JSONObject();
        this.callVolley(jsonInput, query, location);
    }

    private void callVolley(JSONObject json, String query, LatLng location) {
        RequestQueue queue = Volley.newRequestQueue(this.context);
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                this.baseUrl + location.getLatitude() + "," + location.getLongitude() + "&outFormat=json&hostedData=mqap.internationalpois%7Cnavsics%20=%20?%7C554101%7Cname,address,lat,lng",
                json,
                new ResponseListener(),
                new ErrorListener()
        );

        queue.add(request);
    }

    private void processResult(JSONArray json) throws JSONException {
        for(int i = 0; i <= json.length(); i++) {
            JSONObject fields = (JSONObject) json.getJSONObject(i).get("fields");
            String name = fields.get("name").toString();
            String address = fields.get("address").toString();
            LatLng location = new LatLng((double) fields.get("lat"), (double) fields.get("lng"));
            this.activity.addOtherMarker(this.activity.getMapboxMap(), location, name, address);
        }
    }

    private class ResponseListener implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            if (response != null) {
                Log.d("response", response.toString());
                try {
                    processResult(response.getJSONArray("searchResults"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
        }
    }
}
