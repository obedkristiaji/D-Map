package com.example.d_map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.example.d_map.databinding.PlaceListBinding;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapquest.android.commoncore.model.Line;
import com.mapquest.mapping.maps.MapView;

import java.util.ArrayList;
import java.util.List;

class PlaceListAdapter extends BaseAdapter {
    private Context context;
    private MainActivity activity;
    private MapboxMap map;
    private List<Place> placeList;

    public PlaceListAdapter(Context context, MainActivity main, MapboxMap map) {
        this.context = context;
        this.activity = main;
        this.map = map;
    }

    public void update(List<Place> place) {
        Log.d("placeListAdapter", this.activity.getList().toString());
        this.placeList = place;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return placeList.size();
    }

    @Override
    public Place getItem(int position) {
        return placeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View itemView;
        ViewHolder viewHolder;

        if (view == null) {
            itemView = PlaceListBinding.inflate(LayoutInflater.from(this.context)).getRoot();
            viewHolder = new ViewHolder(itemView, activity);
            itemView.setTag(viewHolder);
        } else {
            itemView = view;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.updateView(this.getItem(position), this.context);
        return itemView;
    }

    private class ViewHolder {
        private View view;
        private MainActivity main;
        private PlaceListBinding binding;

        public ViewHolder(View itemView, MainActivity activity) {
            this.view = itemView;
            this.main = activity;
            this.binding = PlaceListBinding.bind(view);
        }

        public void updateView(Place place, Context context) {
            this.binding.tvPlaceName.setText(place.getName());
            this.binding.tvPlaceAddress.setText(place.getAddress());

            this.binding.placeList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLocation(), 15));
                }
            });
        }
    }
}

