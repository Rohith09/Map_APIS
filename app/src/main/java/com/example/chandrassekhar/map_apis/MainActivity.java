package com.example.chandrassekhar.map_apis;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mgoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleServicesAvailability()){  //calling the method to check the availability of google play services.
            Toast.makeText(this,"Perfect!!!",Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_main);  //!---- place the setcontentview here as it might display a view even though the google play services are not available on the users phone.
            initMap();

        }
        else{
            Toast.makeText(this," No Google Maps",Toast.LENGTH_LONG).show();
        }


    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
        mapFragment.getMapAsync(this);  //OnMapReady is the call back method for this function.
    }

    public boolean googleServicesAvailability(){  //user created function in order to check whether the google play store services are available on the users phone or not and returns a boolean value.
        GoogleApiAvailability api=GoogleApiAvailability.getInstance();  //GoogleApiAvailability is a built in class. We use it and get the instance.
        int isAvailable=api.isGooglePlayServicesAvailable(this); //it returns three values. ( Checking for google play services ).
        if (isAvailable == ConnectionResult.SUCCESS){   // in case of Availability of google play services.
            return true;
        }

        else if (api.isUserResolvableError(isAvailable)){   //if the error is fixable then it returns a dialogue box , maybe updating  a version. etc
            Dialog dialog =api.getErrorDialog(this,isAvailable,0);  // (context,errorcode(int),requestcode (int)). Dialogue is provided by the api.
            dialog.show();  //displays the dialogue box.
        }

        else{   //if the google play store services are not available on the users phone.
            Toast.makeText(this,"Cannot Connect to play Sevices",Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mgoogleMap = googleMap;
        goToLocationZoom(28.4985897,77.3759081, 15);

    }

    private void goToLocation(double lat, double lng) {
        LatLng ll = new LatLng(lat , lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        mgoogleMap.moveCamera(update);  //takes this to a particular location.

    }

    private void goToLocationZoom(double lat, double lng,float  z) {
        LatLng ll = new LatLng(lat , lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,z);
        mgoogleMap.moveCamera(update);  //takes this to a particular location.

    }
}
