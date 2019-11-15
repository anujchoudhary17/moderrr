package com.example.moderrr.Locations;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LocationsDao {

        @Insert
        void insert(LocationsManager locationsManager);

        @Update
        void update(LocationsManager locationsManager);

        @Delete
        void delete(LocationsManager locationsManager);

        @Query("SELECT * FROM location_table")
        LiveData<List<LocationsManager>> getAllLocations();


        @Query("SELECT latitude,longitude FROM location_table")
        LiveData<List<LocationsManager>> getLatLong();
    }
















