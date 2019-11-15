package com.example.moderrr.Locations;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class LocationModel extends AndroidViewModel {

        private LiveData<List<LocationsManager>> allLocations;
    private LiveData<List<LocationsManager>> latLongValues;
    private LocationsRepo userRepository;

        public LocationModel(@NonNull Application application) {
            super(application);
            userRepository = new LocationsRepo(application);
            allLocations = userRepository.getAllLocations();
            latLongValues=userRepository.getLatLong();

        }

        public void insert(LocationsManager locationsManager){
            userRepository.insert(locationsManager);
        }

        public LiveData<List<LocationsManager>> getAllLocations(){
            return allLocations;
        }


    public LiveData<List<LocationsManager>> getLatLong(){
        return latLongValues;
    }
    }

