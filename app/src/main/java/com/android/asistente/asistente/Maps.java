package com.android.asistente.asistente;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.android.asistente.asistente.business.GPS;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng latLong = new LatLng(GPS._latOrigen, GPS._longOrigen);
        mMap.addMarker(new MarkerOptions().position(latLong).title(GPS.origen));
        addMarkers();
        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, zoomLevel));

    }
    public void addMarkers(){
        LatLng latLong = new LatLng(GPS._latOrigen, GPS._longOrigen);
        LatLng latLong2 = new LatLng(GPS._latDestino, GPS._longDestino);
        mMap.addMarker(new MarkerOptions().position(new LatLng(GPS._latDestino,GPS._longDestino)).title(GPS.destino));
        mMap.addPolyline(new PolylineOptions().add(latLong,latLong2).width(5).color(Color.RED));
    }
}
