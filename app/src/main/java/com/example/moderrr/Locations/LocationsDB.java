package com.example.moderrr.Locations;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {LocationsManager.class}, version = 1)
public abstract class LocationsDB extends RoomDatabase{



        public abstract LocationsDao locationsDao();

        private static volatile LocationsDB INSTANCE;

        public static LocationsDB getINSTANCE(final Context context) {
            if (INSTANCE == null){
                synchronized (LocationsDB.class){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LocationsDB.class, "location_room_database").build();
                }
            }
            return INSTANCE;
        }
    }


