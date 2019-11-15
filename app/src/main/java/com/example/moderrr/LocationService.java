package com.example.moderrr;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.moderrr.Locations.LocationModel;
import com.example.moderrr.Locations.LocationsManager;
import com.google.android.gms.location.LocationResult;

import java.util.ArrayList;
import java.util.List;

public class LocationService extends BroadcastReceiver {

    public static double distanceBetweenLocations=0;
    public int posOfLoation=0;
    public static final String SHARED_PREFS="sharedPrefs";
    public static final String LocationName="LOCATION_NAME";
    public static final String RingVolume="RING_VOLUME",MediaVolume="MEDIA_VOLUME",SystemVolume="SYSTEM_VOLUME";
    public static final String VibrationSwitch="VIBRATION_MODE";
    public static final String LAT="LAT_VALUE";
    public static final String LONG="LONG_VALUE";
    public LiveData<List<LocationsManager>> allLocationList;
    public LiveData<List<LocationsManager>> allLatLong;



    public static ArrayList<LocationsManager> locationArrayList = new ArrayList<>();


    //---------------------------FINAL LOCATION DETAILS---------------------------
    public static  String readLocationName="LOCATION_NAME";
    public static int readRingVolume=0;
    public static int readMediaVolume=0;
    public static int readSystemVolume=0;
    public static  boolean readVibrationSwitch=false;
    public  static double readLAT=0;
    public  static double readLONG=0;

    public static final String ACTION_PROCESS_UPDATE="";
    LocationModel locationModel;


    @Override
    public void onReceive(Context context, Intent intent) {




        //zhere do you sqve the data , can you show me
        if(intent!=null)
        {
            final String action=intent.getAction();
            if(ACTION_PROCESS_UPDATE.equals(action))
            {
                LocationResult result= LocationResult.extractResult(intent);
                if(result!=null)
                {


                    //---------------------------------------USE getApplication -------------------
                    locationModel=new LocationModel(((Activity) context).getApplication());

                  double LATITUDE= getCurrentLatitude(intent);
                   double LONGITUDE=getCurrentLongitude(intent);

                    //----------CALLING GET DATA------------------------------
                    getData(context,intent);

                    Log.i("------LAT VALUE------","==============="+readLAT);

                    try{


                        if(isInRange(distanceBetweenLocations))
                        {
//                            Toast.makeText(context, "You are at  : "+readLocationName, Toast.LENGTH_SHORT).show();
                            updateAudio(context);


                        }
                        else
                        {
                             Toast.makeText(context,"You are not in saved Locations",Toast.LENGTH_LONG).show();
                        }

                    }
                    catch(Exception ex)
                    {
                        Toast.makeText(context,"I am in catch !!!",Toast.LENGTH_LONG).show();
                    }

                }

            }


        }
    }



    public void getData(final Context context, final Intent intent) {

        allLocationList= locationModel.getAllLocations();
            allLatLong=locationModel.getLatLong();

            String val= String.valueOf(allLatLong.getValue());

        locationModel.getLatLong().observe((LifecycleOwner) context, new Observer<List<LocationsManager>>() {
            @Override
            public void onChanged(List<LocationsManager> locationsManagers) {
                for (LocationsManager latLongOnly : locationsManagers) {
                if(latLongOnly.getLongitude()==getCurrentLongitude(intent))
                {
                    Toast.makeText(context,"Hey I found Longitude using loop",Toast.LENGTH_LONG).show();
                }
                if(latLongOnly.getLatitude()==getCurrentLatitude(intent))
                {
                    Toast.makeText(context,"Hey I found Latitude using loop",Toast.LENGTH_LONG).show();
                }
                }
            }});




//
//
//
//                readLocationName=locationArrayList.get(i).getLocationName();
//
//                readLAT=locationArrayList.get(i).getLatitude();
//                readLONG=locationArrayList.get(i).getLongitude();
//
//                readRingVolume=locationArrayList.get(i).getRingVolume();
//                readSystemVolume=locationArrayList.get(i).getSystemVolume();
//                readMediaVolume=locationArrayList.get(i).getMediaVolume();
//
//                readVibrationSwitch=locationArrayList.get(i).getVibrationEnabled();
//                distanceBetweenLocations= distanceGeoPoints(context,readLAT,getCurrentLatitude(intent),readLONG,getCurrentLongitude(intent));

       Toast.makeText(context,"THIS IS THE LAT LONG "+val,Toast.LENGTH_LONG).show();
        }




    //---------------------------------CHECKING DISTANCE BETWEEN TWO LOCATION AND RETURNING THE DOUBLE VALUE IN METERS ------------------
    public double distanceGeoPoints (Context context,double ulat1,double clat2,double ulong1,double clong2) {
        double lat1 = ulat1;
        double lng1 = ulong1;
        double lat2 = clat2;
        double lng2 = clong2;

        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        int meterConversion = 1609;
        double geopointDistance = dist * meterConversion;

        return geopointDistance;
    }



    public boolean isInRange(double distanceBetweenLocations)
    {
        if(distanceBetweenLocations<500)
            return true;
        else
        return false;
    }



    public void updateAudio(Context context)
    {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,volumeValueGetter(readSystemVolume), 0);
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,volumeValueGetter(readSystemVolume), 0);
        Toast.makeText(context,"MEDIA VOLUME "+readSystemVolume,Toast.LENGTH_LONG).show();
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,volumeValueGetter(readMediaVolume),0);
        audioManager.setStreamVolume(AudioManager.STREAM_RING,volumeValueGetter(readRingVolume),0);
    }

    public int volumeValueGetter(int userSelection)
    {
        int result=0;
        result=(userSelection*15)/100;
        return result;
    }


    public double getCurrentLatitude(Intent intent)
    {
        LocationResult result= LocationResult.extractResult(intent);
        Location location=result.getLastLocation();
        if(result!=null)
        {

            return location.getLatitude();
        }

        return 0.0;
    }

    public double getCurrentLongitude(Intent intent)
    {
        LocationResult result= LocationResult.extractResult(intent);
        Location location=result.getLastLocation();
        if(result!=null) {

        return location.getLongitude();

        }
        return 0.0;
    }

}
