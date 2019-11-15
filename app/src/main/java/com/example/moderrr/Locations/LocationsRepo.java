package com.example.moderrr.Locations;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class LocationsRepo {


        private LocationsDao locationsDao;
        private LiveData<List<LocationsManager>> allLocations;
    private LiveData<List<LocationsManager>> latLongValues;

    public LocationsRepo(Application application) {
            LocationsDB db = LocationsDB.getINSTANCE(application);
            locationsDao = db.locationsDao();
            allLocations = locationsDao.getAllLocations();
        latLongValues=locationsDao.getLatLong();
    }

        public LiveData<List<LocationsManager>> getAllLocations() {
            return allLocations;
        }

        public void insert(LocationsManager locationsManager){
            new insertAsyncTask(locationsDao).execute(locationsManager);
        }

        public LiveData<List<LocationsManager>> getLatLong()
        {
            return latLongValues;
        }


        private static class insertAsyncTask extends AsyncTask<LocationsManager, Void, Void> {

            private LocationsDao asyncTaskDao;

            insertAsyncTask(LocationsDao locationsDao){
                asyncTaskDao = locationsDao;
            }

            @Override
            protected Void doInBackground(LocationsManager... locationsManager) {
                asyncTaskDao.insert(locationsManager[0]);
                return null;
            }
        }
    }

