package ca.lambton.fa_swapnil_kumbhar_c0854325_android.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Place.class}, version = 1)
public abstract class PlacesRoomDB extends RoomDatabase {

    public static final String DATABASE_NAME = "places";

    public static volatile PlacesRoomDB placesRoomDB;

    public static PlacesRoomDB getInstance(Context context) {
        if (placesRoomDB == null) {
            placesRoomDB = Room
                    .databaseBuilder(context, PlacesRoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return placesRoomDB;
    }

    public abstract PlaceDAO placeDAO();
}
