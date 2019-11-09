package com.adpdigital.chabok.starter.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.adpdigital.chabok.starter.R;
import com.adpdigital.push.location.LocationManager;
import com.adpdigital.push.location.LocationParams;
import com.adpdigital.push.location.OnLocationUpdateListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class GeoActivity extends AppCompatActivity implements OnMapReadyCallback,
        OnLocationUpdateListener {
    private static final String TAG = GeoActivity.class.getSimpleName();

    private LocationManager locationManager;
    private Location currentLocation;
    private GoogleMap map;

    private boolean permissionGranted;
    private boolean mapIsReady;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo);

        initializeLocationManager();
        checkLocationPermission();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStop");

        onPermissionGranted();
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.d(TAG, "onStop");

        if (locationManager != null) {
            locationManager.removeListener();
            locationManager.stop();
        }
    }

    private void initializeLocationManager() {
        Log.d(TAG, "initializeLocationManager");
        locationManager = LocationManager.init(this);
    }

    private void updateUI() {
        if (mapIsReady && currentLocation != null) {
            Log.d(TAG, "CurrentLocation = (" + currentLocation.getLatitude() + ", " + currentLocation.getLongitude() + ")");

            LatLng point = addMarker(currentLocation.getLatitude(), currentLocation.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLng(point));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        mapIsReady = true;
        checkMyLocation();
    }

    private LatLng addMarker(double latitude, double longitude) {
        LatLng point = new LatLng(latitude, longitude);
        map.addMarker(new MarkerOptions().position(point).title("Lat: " + latitude
                + " , Long: " + longitude)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(
                                BitmapDescriptorFactory.HUE_GREEN)));
        return point;
    }

    @Override
    public void onLocationUpdated(Location location) {
        currentLocation = location;
        updateUI();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        currentLocation = locationManager.getLastLocation();
        updateUI();
    }

    @Override
    public void onSuspended() {
        Log.d(TAG, "onSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed");
    }

    @Override
    public void onGeofencesRegisteredSuccessful() {
        Log.d(TAG, "onGeofencesRegisteredSuccessful");
    }

    private void checkMyLocation() {
        if (permissionGranted && mapIsReady) {
            map.setMyLocationEnabled(true);

            currentLocation = locationManager.getLastLocation();
            updateUI();
        }
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission check failed. Please handle it in your app before setting up location");
            String[] permissions = new String[2];
            permissions[0] = Manifest.permission.ACCESS_FINE_LOCATION;
            permissions[1] = Manifest.permission.ACCESS_COARSE_LOCATION;
            ActivityCompat.requestPermissions(this, permissions, 10);
        } else {
            Log.d(TAG, "checkPermissions: permissions already granted");
            permissionGranted = true;
            onPermissionGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult");
        if (grantResults[0] == PERMISSION_GRANTED && grantResults[1] == PERMISSION_GRANTED) {
            Log.d(TAG, "onRequestPermissionsResult: permissions granted");
            permissionGranted = true;
            onPermissionGranted();
        } else {
            permissionGranted = false;
        }
    }

    private void onPermissionGranted() {
        locationManager.startLocationUpdates(LocationParams.BEST_EFFORT);
        locationManager.addListener(this);

        checkMyLocation();
    }
}
