package com.example.moderrr.AddLocation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moderrr.Locations.LocationModel;
import com.example.moderrr.Locations.LocationsManager;
import com.example.moderrr.MainActivity;
import com.example.moderrr.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddLocationActivity extends AppCompatActivity{


    EditText locationNameEntered;
    LatLng latLng;
    String latLngInString;
    String[] sLatLng;
    String sLat,sLng,locationName;
    double latitude,longitude;
    SeekBar seekBarRing,seekBarMedia,seekBarSystem;
    int seekSystem,seekMedia,seekRing;
    Switch vibrationSwitch;
    Boolean vibrationSwitchState=false;
    Button btnDone;




   public static final ArrayList<LocationsManager> locationsManagers=new ArrayList<LocationsManager>();

    public static final String SHARED_PREFS="sharedPrefs";

    LocationModel locationModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        locationModel=new LocationModel(getApplication());
        locationNameEntered = (EditText) findViewById(R.id.txtLocationName);
        locationNameEntered.setText(" ");
//------------------------SEEK BAR INITIALIZATION--------------------
        seekBarRing = (SeekBar) findViewById(R.id.seekBarRing);
        seekBarMedia = (SeekBar) findViewById(R.id.seekBarMedia);
        seekBarSystem = (SeekBar) findViewById(R.id.seekBarSystem);

//-------------------------------SWITCH BUTTON-----------------------
        vibrationSwitch = (Switch) findViewById(R.id.switchVibration);

        btnDone=(Button) findViewById(R.id.btnDone);

        latLng=getIntent().getExtras().getParcelable("latlngOfPlace");
        latLngInString = latLng.toString();
        sLatLng = latLngInString.substring(10, latLngInString.length() - 1).split(",");
        sLat = sLatLng[0];
        sLng = sLatLng[1];
        latitude = Double.parseDouble(sLat);
        longitude = Double.parseDouble(sLng);

        locationName=getAddress(latitude,longitude);


         Toast.makeText(AddLocationActivity.this,
                " HEY I AM LAT "+locationName, Toast.LENGTH_LONG).show();
        Log.d(
                "------------here",locationName
        );
        locationNameEntered.setText(locationName);



        seekBarRing.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                 seekRing = seekBarRing.getProgress();

                Toast.makeText(AddLocationActivity.this,
                        " Call Volume "+ seekRing, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        //------------------------------------------------------------------------------
        seekBarMedia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                 seekMedia = seekBarMedia.getProgress();

                Toast.makeText(AddLocationActivity.this,
                        " Media Volume "+ seekMedia, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //------------------------------------------------------------------------------
        seekBarSystem.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                 seekSystem = seekBarSystem.getProgress();


                Toast.makeText(AddLocationActivity.this,
                        " System Volume "+ seekSystem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        vibrationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                 vibrationSwitchState = vibrationSwitch.isChecked();
                if(vibrationSwitchState) {
                    Toast.makeText(AddLocationActivity.this,
                            " Vibration On", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(AddLocationActivity.this,
                            " Vibration Off ", Toast.LENGTH_SHORT).show();
                }                }
        });


btnDone.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        saveData();

        Toast.makeText(AddLocationActivity.this,
                " Location Added !!!", Toast.LENGTH_LONG).show();


        ///or wait for testing purposes i ll pass data manually

//-----------------------------CALLING METHOD TO SAVE DATA-----------------------------

// done

        Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivityIntent);
    }
});





    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(AddLocationActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            //------------------------------GET THROUGH FARE GIVES MAIN ADDRESS---------------------------
            String add =  obj.getThoroughfare();
            return add;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

                   }
    return "NULL";
    }

//----------------THIS SAVES THE DATA IN THE DATABASE--------------------------
    public void saveData()
    {

        LocationsManager locationsManager= new LocationsManager(locationName,seekRing,seekSystem,seekMedia,latitude,longitude,false);

        locationsManager.setLocationName(locationName);
        locationsManager.setLatitude(latitude);
        locationsManager.setLongitude(longitude);
        locationsManager.setRingVolume(seekRing);
        locationsManager.setMediaVolume(seekMedia);
        locationsManager.setSystemVolume(seekSystem);
//----------------------------------SAVING DATA USING INSERT----------------------------------------


        locationModel.insert(locationsManager);










        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        Gson gson = new Gson();

        ///here we re saving an object , not list of jobects
        String json=gson.toJson(locationsManager);
        editor.putString("Location", json);
        editor.apply();




    }

}
