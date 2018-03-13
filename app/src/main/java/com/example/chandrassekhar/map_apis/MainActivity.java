package com.example.chandrassekhar.map_apis;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationSource.OnLocationChangedListener {

    GoogleMap mgoogleMap;
    Button button;
    GoogleApiClient mGoogleApiClient;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleServicesAvailability()) {  //calling the method to check the availability of google play services.
            Toast.makeText(this, "Perfect!!!", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_main);//!---- place the setcontentview here as it might display a view even though the google play services are not available on the users phone.
            button = findViewById(R.id.button);
            button.setOnClickListener(this);
            initMap();
        } else {
            Toast.makeText(this, " No Google Maps", Toast.LENGTH_LONG).show();
        }
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
        mapFragment.getMapAsync(this);  //OnMapReady is the call back method for this function.
    }

    public boolean googleServicesAvailability() {  //user created function in order to check whether the google play store services are available on the users phone or not and returns a boolean value.
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();  //GoogleApiAvailability is a built in class. We use it and get the instance.
        int isAvailable = api.isGooglePlayServicesAvailable(this); //it returns three values. ( Checking for google play services ).
        if (isAvailable == ConnectionResult.SUCCESS) {   // in case of Availability of google play services.
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {   //if the error is fixable then it returns a dialogue box , maybe updating  a version. etc
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);  // (context,errorcode(int),requestcode (int)). Dialogue is provided by the api.
            dialog.show();  //displays the dialogue box.
        } else {   //if the google play store services are not available on the users phone.
            Toast.makeText(this, "Cannot Connect to play Sevices", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mgoogleMap = googleMap;
          goToLocationZoom(28.4985897,77.3759081, 15);
        //     if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        //        return;
        //  }
        //   mgoogleMap.setMyLocationEnabled(true); //adds the pointer on the top right corner of the google map.
      //  mGoogleApiClient = new GoogleApiClient.Builder(this)
       //         .addApi(LocationServices.API)
        //        .addConnectionCallbacks(this)
         //       .addOnConnectionFailedListener(this)
          //      .build();
        // mGoogleApiClient.connect();
    }

    private void goToLocation(double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        mgoogleMap.moveCamera(update);  //takes this to a particular location.

    }

    private void goToLocationZoom(double lat, double lng, float z) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, z);
        mgoogleMap.moveCamera(update);  //takes this to a particular location.
    }

    @Override
    public void onClick(View view) {
        EditText edit1 = (EditText) findViewById(R.id.edit_text);
        String location = edit1.getText().toString();
        Geocoder gc = new Geocoder(this);   //Converts a string into Latitude and Longitudes.
        try {
            List<Address> list = gc.getFromLocationName(location, 1);
            Address address = list.get(0); //for getting the first location from the list and Address is an built in object.
            String locality = address.getLocality();
            Toast.makeText(this, locality, Toast.LENGTH_LONG).show();

            double lat = address.getLatitude();
            double lng = address.getLongitude();
            goToLocationZoom(lat, lng, 15);

            if(marker!=null)
            {
                marker.remove();
            }

            //Below (131-132) is the code to add a new marker to the map.
            MarkerOptions options = new MarkerOptions().title(location).position(new LatLng(lat,lng)).snippet("I am Here!");
            marker=mgoogleMap.addMarker(options);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);  //Provides a menu on top right corner which shows a list of map types.
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.maptypeNone:
                mgoogleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.mapTypeNormal:
                mgoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeHybrid:
                mgoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.mapTypeSatellite:
                mgoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                mgoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    LocationRequest mLocationRequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(this);
     // LocationServices..requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location==null)
        {
            Toast.makeText(this, "Cant get current location", Toast.LENGTH_LONG).show();

        }
        else
        {
            LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,15);
            mgoogleMap.animateCamera(update);
        }

    }
}
