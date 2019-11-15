package com.example.moderrr.Locations;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "location_table")
public class LocationsManager {



    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "lid")
    private int lid;


    @ColumnInfo(name = "locationName")
    private String locationName="Location Name";
    @ColumnInfo(name = "ringVol")
    private int ringVolume=0;
    @ColumnInfo(name="systemVol")
    private int systemVolume=0;
    @ColumnInfo(name = "mediaVol")
    private int mediaVolume=0;
    @ColumnInfo(name="latitude")
    private double latitude=0.0;
    @ColumnInfo(name = "longitude")
    private double longitude=0.0;
    @ColumnInfo(name = "vibrationState")
    private boolean vibrationEnabled=false;



    ///yes
    //done i saved the location
    public LocationsManager(String locationName,int ringVolume,int systemVolume,int mediaVolume,double latitude,double longitude,boolean vibrationEnabled)
    {
        setLocationName(locationName);
        setRingVolume(ringVolume);
        setSystemVolume(systemVolume);
        setMediaVolume(mediaVolume);
        setLatitude(latitude);
        setLongitude(longitude);
        setVibrationEnabled(vibrationEnabled);
    }

    //-------------------------------------SETTERS------------------------------------------



    public void setLid(int lid) {
        this.lid = lid;
    }

    public void setLocationName(String locationName)
    {
        this.locationName=locationName;
    }

    public void setRingVolume(int ringVolume)
    {
        this.ringVolume=ringVolume;
    }

    public void setSystemVolume(int systemVolume)
    {
        this.systemVolume=systemVolume;
    }

    public void setMediaVolume(int ringVolume)
    {
        this.ringVolume=ringVolume;
    }

    public void setLatitude(double latitude)
    {
        this.latitude=latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setVibrationEnabled(boolean vibrationEnabled) {
        this.vibrationEnabled = vibrationEnabled;
    }

    //-----------------------------------------GETTERS------------------------------------

    public int getLid() {
        return lid;
    }

    public String getLocationName() {
        return locationName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getRingVolume() {
        return ringVolume;
    }

    public int getSystemVolume() {
        return systemVolume;
    }

    public int getMediaVolume() {
        return mediaVolume;
    }

    public boolean getVibrationEnabled() {
        return vibrationEnabled;
    }

    @Override
    public String toString() {
        return "LocationsManager{" +
                "lid=" + lid +
                ", locationName='" + locationName + '\'' +
                ", ringVolume=" + ringVolume +
                ", systemVolume=" + systemVolume +
                ", mediaVolume=" + mediaVolume +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", vibrationEnabled=" + vibrationEnabled +
                '}';
    }
}
