package com.example.chandrassekhar.map_apis;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (googleServicesAvailability()){
            Toast.makeText(this,"Perfect!!!",Toast.LENGTH_LONG).show();
        }
    }

    public boolean googleServicesAvailability(){
        GoogleApiAvailability api=GoogleApiAvailability.getInstance();
        int isAvailable=api.isGooglePlayServicesAvailable(this); //it returns three values.
        if (isAvailable == ConnectionResult.SUCCESS){
            return true;
        }

        else if (api.isUserResolvableError(isAvailable)){   //if the error is fixable then it returns a dialogue box.
            Dialog dialog =api.getErrorDialog(this,isAvailable,0);  // (context,errorcode(int),requestcode (int)).
            dialog.show();
        }

        else{
            Toast.makeText(this,"Cannot Connect to play Sevices",Toast.LENGTH_LONG).show();
        }
        return false;
    }
}
