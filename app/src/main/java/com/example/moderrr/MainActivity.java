package com.example.moderrr;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.moderrr.AboutUs.AboutUsActivity;
import com.example.moderrr.ContactUs.ContactUsActivity;
import com.example.moderrr.Locations.LocationModel;
import com.example.moderrr.Locations.LocationsManager;
import com.example.moderrr.PermissionActivity.PermissionActivity;
import com.example.moderrr.Setting.SettingsActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnLocation;
    Button btnSettings;
    Button btnContactUs;
    Button btnAboutUs;
    Button btnEditLocation;


    TextView txtLocation;



static MainActivity instance;
LocationRequest locationRequest;
FusedLocationProviderClient fusedLocationProviderClient;



LocationModel locationModel;
    LiveData<List<LocationsManager>> listOfLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        locationModel.getAllLocations().observe(MainActivity.this, new Observer<List<LocationsManager>>() {
            @Override
            public void onChanged(List<LocationsManager> locationsManagers) {
                //task when the data changes
                for (LocationsManager locationsManager : locationsManagers){
                    Log.e("MainActivity", locationsManager.toString());
                }
            }
        });


        instance=this;
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                    updateLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(MainActivity.this,"You must Accept this Location ",Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();




        this.changeScreen();
    }

    private void updateLocation() {
buildLocationRequest();
fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
{
    return;
}
fusedLocationProviderClient.requestLocationUpdates(locationRequest,getPendingIntent());

}

    private PendingIntent getPendingIntent()
    {

        Intent intent =new Intent(this,LocationService.class);
            intent.setAction(LocationService.ACTION_PROCESS_UPDATE);
            return PendingIntent .getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }




   private void buildLocationRequest() {



    locationRequest =new LocationRequest();
locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
locationRequest.setInterval(3000);
locationRequest.setFastestInterval(1000);
locationRequest.setSmallestDisplacement(10f);
}

    void changeScreen(){
            btnLocation=findViewById(R.id.btnLocation);
            btnLocation.setOnClickListener(this);
            btnSettings=findViewById(R.id.btnSettings);
            btnSettings.setOnClickListener(this);
            btnContactUs=findViewById(R.id.btnContactUs);
            btnContactUs.setOnClickListener(this);
            btnAboutUs=findViewById(R.id.btnAboutUs);
            btnAboutUs.setOnClickListener(this);


        }


    @Override
    public void onClick(View v) {
               switch(v.getId())
        {
            case  R.id.btnLocation:
                this.switchToPermission();
                break;
            case R.id.btnSettings:
                this.swtichToSettings();
                break;
            case R.id.btnContactUs:
                this.switchToContactUs();
                break;
                case R.id.btnAboutUs:
            this.switchToAboutUs();
            break;

        }
    }


    void switchToPermission() {
        Intent permissionact = new Intent(MainActivity.this, PermissionActivity.class);
        startActivity(permissionact);
    }

    void swtichToSettings() {
        Intent settingsact = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(settingsact);
    }

    void switchToContactUs() {
        Intent contactUs = new Intent(MainActivity.this, ContactUsActivity.class);
        startActivity(contactUs);
    }
    void switchToAboutUs() {
        Intent aboutUs = new Intent(MainActivity.this, AboutUsActivity.class);
        startActivity(aboutUs);
    }




}
