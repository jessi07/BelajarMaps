package com.example.belajarmaps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements LocationListener, OnMapReadyCallback {

    private GoogleMap mMap;
    double latitude;
    double longitude;
    LocationManager locationManager;
    Button koordinat;
    Button posisi_user;

    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        koordinat = findViewById(R.id.btn_koordinat);
        posisi_user = findViewById(R.id.btn_posisi);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("MapsAppliaction", "User belum mengaktifkan lokasi");
            return;
        }
        Criteria kriteria = new Criteria();
        provider = locationManager.getBestProvider(kriteria, false);
        final Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            System.out.println("Provider Anda" + provider + " has been selected");
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            onLocationChanged(location);
        } else {
            Log.e("Error", "Lokasi Tidak Ditemukan");
        }

        posisi_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location!= null){
                    location.getLatitude();
                    location.getLongitude();
                }
            }
        });
    }

    public void tombolbutton(View v){
       if(v.getTag().equals("button_koordinat")){
            Toast.makeText(this, "Koordinat  Latitude: " + latitude + " Longitude: "+longitude ,
                    Toast.LENGTH_SHORT).show();
        }
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
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Pilih Button Posisi"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Toast.makeText(this, "Enabled new provider" + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Toast.makeText(this, "Disabled provider" + provider, Toast.LENGTH_SHORT).show();
    }

    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }
}