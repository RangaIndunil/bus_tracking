package com.example.bustrackingapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

public class MapActivityLocationReceive  extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private HashMap<String, FirebaseReceiveData> hashMapMarkers;
    private Marker marker;

    private GetLookingName getLookingName;
    private GetLookingFrom getLookingFrom;
    private GetLookingTo getLookingTo;

    private String looking_from;
    private String looking_to;
    private String looking_name;
    private boolean bus_availability = false;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hashMapMarkers = new HashMap<>();

        getLookingName = new GetLookingName(this);
        getLookingFrom = new GetLookingFrom(this);
        getLookingTo = new GetLookingTo(this);

        final Context cntx = this;

        looking_name = (String) getLookingName.getname();
        getLookingName.setname("");
        looking_from = (String) getLookingFrom.getfrom();
        getLookingFrom.setfrom("");
        looking_to = (String) getLookingTo.getto();
        getLookingTo.setto("");

        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }

        setContentView(R.layout.activity_maps_location_share);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot child : dataSnapshot.child("drivers").getChildren()){
                        String name = (String) child.getKey();
                        double latitude = child.child("latitude").getValue(Double.class);
                        double longitude = child.child("longitude").getValue(Double.class);
                        String from = child.child("from").getValue(String.class);
                        String to = child.child("to").getValue(String.class);
                        String number = child.child("number").getValue(String.class);
                        String type = child.child("type").getValue(String.class);

                        if(looking_name != null || !looking_name.equals("")){
                            if (looking_name.equals(name)) {
                                addMarkerToMap(from, to, latitude, longitude, number, type);
                                bus_availability = true;
                            }
                        }

                        if((looking_from != null && !looking_from.equals("")) || (looking_to != null &&
                                !looking_to.equals(""))){
                            if(looking_from.equals(from) && looking_to.equals(to)){
                                addMarkerToMap(from, to, latitude, longitude, number, type);
                                bus_availability = true;
                            }
                        }

                        counter ++;

                        if (counter == dataSnapshot.getChildrenCount() && !bus_availability) {
                            Toast.makeText(cntx, "No bus available, Please check your inputs"
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                }catch (Exception e){
                    return;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MapActivityLocationReceive.this, FirstLoadActivity.class));
    }

    private void addMarkerToMap(String from, String to, double latitude, double longitude,
                                String number, String type) {
        if(hashMapMarkers != null){
            for (Map.Entry<String, FirebaseReceiveData> me : hashMapMarkers.entrySet()) {
                String current_name = me.getKey();
                if(number == current_name){
                    hashMapMarkers.remove(number);
                }
            }
            mMap.clear();
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            showMap(latLng, "me");
                        }
                    }
                });

        FirebaseReceiveData data = new FirebaseReceiveData(number, from, to, latitude, longitude, type);
        hashMapMarkers.put(number, data);

        for (Map.Entry<String, FirebaseReceiveData> me : hashMapMarkers.entrySet()) {
            String current_name = me.getKey();
            FirebaseReceiveData hashMapMaker = hashMapMarkers.get(current_name);
            MarkerOptions options = new MarkerOptions().position(new LatLng(hashMapMaker.getLatitude(),
                    hashMapMaker.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_marker)).title("Number : " + hashMapMaker.getNumber()
                    + ", Type : " + hashMapMaker.getType());
            marker = mMap.addMarker(options);
        }
    }

    private void showMap(LatLng latLng, String place) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                latLng).zoom(13).tilt(60).bearing(30).build();
        marker = mMap.addMarker(new MarkerOptions().position(latLng).title(place));
        marker.showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.addCircle(new CircleOptions().center(latLng).
                radius(90).
                strokeWidth(3).
                strokeColor(Color.RED).
                fillColor(Color.TRANSPARENT));
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }
}