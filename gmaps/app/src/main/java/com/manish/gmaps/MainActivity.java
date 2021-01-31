package com.manish.gmaps;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.dynamic.SupportFragmentWrapper;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.PrimitiveIterator;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    private final double GYAN_VIHAR_LATITUDE = 26.809260798405976;
    private final double GYAN_VIHAR_LONGITUDE = 75.86128786776558;
    public static final int PERMISSION_REQUEST_CODE = 9001;
    public static final int PLAY_SERVICES_ERROR_CODE = 9002;
    private boolean mRequestPermissionGranted;
    private MapView mapView;
    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGoogleMap();

//        //for runtime map fragement
//        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.fragment_map_container,mapFragment)
//                .commit();
//        mapFragment.getMapAsync(this);

        //for compile time map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_id);
        supportMapFragment.getMapAsync(this);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap=googleMap;
//        MarkerOptions markerOptions = new MarkerOptions()
//                .title("Title")
//                .position(new LatLng(26.809260798405976,75.86128786776558));
//
//        mGoogleMap.addMarker(markerOptions);

        goToLocations(GYAN_VIHAR_LATITUDE,GYAN_VIHAR_LONGITUDE);
    }

    private void goToLocations(double lat , double lng) {
        LatLng latLng = new LatLng(lat,lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,DEFAULT_ZOOM);
        mGoogleMap.moveCamera(cameraUpdate);
    }

    private void initGoogleMap() {
        if (isServicesOk()) {
            if (mRequestPermissionGranted) {
                Toast.makeText(this , "Permission Granted" , Toast.LENGTH_SHORT).show();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestLocationPermission();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestLocationPermission() {

        if (ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION} , PERMISSION_REQUEST_CODE);
        }
    }

        private boolean isServicesOk(){
            GoogleApiAvailability googleApi = GoogleApiAvailability.getInstance();
            int result = googleApi.isGooglePlayServicesAvailable(this);
            if (result == ConnectionResult.SUCCESS) {
                return true;
            } else if (googleApi.isUserResolvableError(result)) {
                Dialog dialog = googleApi.getErrorDialog(this , result , PLAY_SERVICES_ERROR_CODE , task ->
                        Toast.makeText(this , "Dialog cancelled by the user" , Toast.LENGTH_SHORT).show());
                dialog.show();
            } else {
                Toast.makeText(this , "Play services required" , Toast.LENGTH_SHORT).show();
            }

            return false;
        }

        @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
            super.onRequestPermissionsResult(requestCode , permissions , grantResults);
            if (requestCode == PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this , "Permission Granted" , Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this , "Permission Not Granted" , Toast.LENGTH_SHORT).show();
            }
        }


}

