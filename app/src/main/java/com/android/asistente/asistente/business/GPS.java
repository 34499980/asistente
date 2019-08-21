package com.android.asistente.asistente.business;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.Maps;
import com.android.asistente.asistente.Services.asistenteservice;

import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;
import static androidx.core.content.ContextCompat.checkSelfPermission;
import static androidx.core.content.ContextCompat.startActivities;

public class GPS extends Activity implements LocationListener {
    LocationManager locationManager;
    LocationListener locationListener;
    public static double _latOrigen = 0;
    public static double _longOrigen = 0;
    public static double _latDestino = 0;
    public static double _longDestino = 0;
    public static String destino;
    public static String origen = "origen";

    public  void getActualLatLong() {
        locationManager = (LocationManager) asistenteservice.getContext().getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                _latOrigen = location.getLatitude();
                _longOrigen = location.getLongitude();
                Maps.updateMarker();


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                asistenteservice.getContext().startActivity(intent);
            }
        };
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        } else {
            locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
        }
    }

    public  void getLatLongByAddress(String address) {
        try {
            if(address.indexOf(";") <= -1){
            Location _location;
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            } else {

                Geocoder geoDestino = new Geocoder(asistenteservice.getContext());
                List<Address> locationsDestino = geoDestino.getFromLocationName(address, 1);
                // _location = locationManager.getLastKnownLocation(address);
                _latDestino = locationsDestino.get(0).getLatitude();
                _longDestino = locationsDestino.get(0).getLongitude();
                locationManager = (LocationManager) asistenteservice.getContext().getSystemService(LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, this);
                Location locationGPS = (Location) locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                //Geocoder geoOrigen = new Geocoder(asistenteservice.getContext(),Locale.getDefault());
                // List<Address> locationsOrigen = geoOrigen.get
                // _location = locationManager.getLastKnownLocation(address);
                if (locationGPS != null) {
                    _latOrigen = locationGPS.getLatitude();
                    _longOrigen = locationGPS.getLongitude();
                }


            }
            }else{
                origen = address.substring(0,address.indexOf(";"));
                destino = address.substring(address.indexOf(";")+1);
                Geocoder geo = new Geocoder(asistenteservice.getContext());
                List<Address> locations = geo.getFromLocationName(destino, 1);
                // _location = locationManager.getLastKnownLocation(address);
                _latDestino = locations.get(0).getLatitude();
                _longDestino = locations.get(0).getLongitude();
                List<Address> locationOrigen = geo.getFromLocationName(origen, 1);
                // _location = locationManager.getLastKnownLocation(address);
                _latOrigen = locationOrigen.get(0).getLatitude();
                _longOrigen = locationOrigen.get(0).getLongitude();
            }
        }catch(Exception ex){
            Log.appendLog("GPS: "+ex.getMessage());
        }
    }

    @SuppressLint("WrongConstant")
    public int checkSelfPermission(String accessFineLocation) {
        return 0;
    }

    public void getAddres(){

    }
    public static String ProcesarDatosEntrada(String value){
        String result = "";
        try {

            if (value.toLowerCase().contains("llegar a")) {
                result = value.substring(value.toLowerCase().indexOf("llegar a") + 9);
            } else if((value.toLowerCase().contains("ir hasta")) || (value.toLowerCase().contains("ir a"))){
                result = value.substring(value.toLowerCase().indexOf("ir a") + 4);
            } else if((value.toLowerCase().contains("ir de"))){
                int indexOrigen = value.toLowerCase().indexOf("ir de")+6;
                int indexDestino= value.toLowerCase().indexOf("hasta")+5;
               result = value.substring(indexOrigen,indexDestino-5)+";"+value.substring(indexDestino,value.length());

            }
           destino = result;
            return result;
        }catch(Exception ex){
            Log.appendLog("GPS: "+ex.getMessage());
            return result;
        }
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    public void enabledDesabledGps(boolean status){
        try {
            Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
            intent.putExtra("enabled", status);
            asistenteservice.getContext().sendBroadcast(intent);
        }catch(Exception ex){
            Log.appendLog("Helper.GPS:" +ex.getMessage());
        }
    }
}