package com.manish.mapss;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static final int PERMISSION_REQUEST_CODE = 9001;
    ArrayList<LatLng> listPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listPoints = new ArrayList<>();
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

        if (ActivityCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                //Reset marker when there is nore than one like 2
                if(listPoints.size() == 2){
                    listPoints.clear();
                    mMap.clear();
                }
                //save first point selected
                listPoints.add(latLng);
                //create marker
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                if(listPoints.size() == 1){
                    //add first marker to map
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }else{
                    //add second marker to the map
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }
                mMap.addMarker(markerOptions);
                if(listPoints.size() == 2){
                    //create the url to get the request from the first marker to second marker
                    String url = getRequestUrl(listPoints.get(0),listPoints.get(1));
                    RequestDirections requestDirections = new RequestDirections();
                    requestDirections.execute(url);
                }


            }
        });
    }


    private String getRequestUrl(LatLng origin , LatLng dest) {
        //value of origin
        String str_org = "origin=" + origin.latitude +","+origin.longitude;
        //value of destionation
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        //set value enable the sensor
        String sensor = "sensor=false";
        //mode for direction
        String mode = "mode=driving";

        // String key = "key="+ getString(R.string.google_maps_api_key);
        //build the full param
        String param = str_org + "&" + str_dest + "&"+sensor +"&" + mode;

        String output = "json";
        //create url request
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?" + param;
        return url;
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString="";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            //Get the response result
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStream.close();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(inputStream != null){
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }




    @Override
    public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
                                             @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode , permissions , grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            Toast.makeText(this , "Permission Granted" , Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this , "Permission Not Granted" , Toast.LENGTH_SHORT).show();
        }
    }

    public class RequestDirections extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";

            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //we will parse json here
            DirectionParser  directionParser = new DirectionParser();
            directionParser.execute(s);
        }
    }

    public class DirectionParser extends AsyncTask<String,Void,List<List<HashMap<String,String>>>>{

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String,String>>> routes = null;

            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionJsonParser directionJsonParser = new DirectionJsonParser();
                routes = directionJsonParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            //display list route here and display it on the map
            ArrayList points = null;
            PolylineOptions polylineOptions = null;
            for(List<HashMap<String,String>> path : lists){

                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for(HashMap<String,String> point: path){
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));

                    points.add(new LatLng(lat,lng));
                }
                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.geodesic(true);
                polylineOptions.color(Color.BLUE);
            }
            if (polylineOptions != null){
                mMap.addPolyline(polylineOptions);
            }else{
                Toast.makeText(getApplicationContext() , "Directions not found" , Toast.LENGTH_SHORT).show();
            }
        }
    }
}