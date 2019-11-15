package com.example.moderrr.PermissionActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.moderrr.Location.LocationActivity;
import com.example.moderrr.AboutUs.AboutUsActivity;
import com.example.moderrr.Location.LocationActivity;
import com.example.moderrr.MainActivity;
import com.example.moderrr.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class PermissionActivity extends AppCompatActivity {


    private Button btnGrantPermission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



if(ContextCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
{
    startActivity(new Intent(PermissionActivity.this,LocationActivity.class));
    finish();
    return;

}

    setContentView(R.layout.activity_permission);
        btnGrantPermission=findViewById(R.id.btnGrantPermission);
    btnGrantPermission.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Dexter.withActivity(PermissionActivity.this)
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            setContentView(R.layout.activity_location);
                            Intent locationact = new Intent(PermissionActivity.this, LocationActivity.class);
                            startActivity(locationact);
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                        if(response.isPermanentlyDenied())
                        {
                            AlertDialog.Builder builder=new AlertDialog.Builder(PermissionActivity.this);
                                    builder.setTitle("Permission Denied")
                                    .setMessage("Permission to access device location is permanently denied. You need to go to setting to allow the permission")
                                            .setNegativeButton("Cancel",null)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int which) {
                                                    Intent intent =new Intent();
                                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                    intent.setData(Uri.fromParts("package",getPackageName(),null));
                                                }
                                            }).show();
                        }
                        else
                        {
                            Toast.makeText(PermissionActivity.this,"Permission Denied !!!",Toast.LENGTH_SHORT).show();
                        }

                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                        }
                    })
                    .check();
        }
    });

    }
}
