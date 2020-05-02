package com.panaceasoft.pscity.ui.item.upload.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.panaceasoft.pscity.R;
import com.panaceasoft.pscity.ui.common.PSFragment;
import com.panaceasoft.pscity.utils.AutoClearedValue;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.Utils;

public class MapFragment extends PSFragment {

    String latValue = "48.856452647178386";
    String lngValue = "2.3523519560694695";

    GoogleMap map;

    MarkerOptions markerOptions = new MarkerOptions();
    AutoClearedValue<SupportMapFragment> mapView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map_layout, container, false);

        setHasOptionsMenu(true);

        mapView = new AutoClearedValue<>(this, (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));

        try {
            if (this.getActivity() != null) {
                MapsInitializer.initialize(this.getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.get().onCreate(savedInstanceState);
        mapView.get().onResume();
        mapView.get().getMapAsync(googleMap -> {
            map = googleMap;

            map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(Double.valueOf(latValue), Double.valueOf(lngValue))).zoom(10).bearing(10).tilt(10).build()));

            map.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.valueOf(latValue), Double.valueOf(lngValue)))
                    .title("Shop Name"));

            map.setOnMapClickListener(latLng -> {
                // Creating a marker
                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                markerOptions.draggable(true);

                latValue = String.valueOf(latLng.latitude);
                lngValue = String.valueOf(latLng.longitude);

                // Clears the previously touched position
                map.clear();

                // Animating to the touched position
                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker
                // the touched position
                map.addMarker(markerOptions);
            });

            map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {

                    latValue = String.valueOf(marker.getPosition().latitude);
                    lngValue = String.valueOf(marker.getPosition().longitude);
                }
            });
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pick_location, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.pickButton) {
            Utils.psLog("I am here for ok Button");

            navigationController.navigateBackFromMapView(getActivity(), latValue, lngValue);

            if (getActivity() != null) {
                getActivity().finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    @Override
    public void onDestroy() {

        if (mapView.get() != null) {

            mapView.get().onDestroy();

            if (map != null) {
                map.clear();
            }

        }
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        mapView.get().onLowMemory();
        super.onLowMemory();

    }

    @Override
    public void onPause() {
        mapView.get().onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mapView.get().onResume();
        super.onResume();
    }

    @Override
    protected void initUIAndActions() {

    }

    @Override
    protected void initViewModels() {

    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {

        if (getActivity() != null) {
            latValue = getActivity().getIntent().getStringExtra(Constants.LAT);
            lngValue = getActivity().getIntent().getStringExtra(Constants.LNG);
        }
    }
}
