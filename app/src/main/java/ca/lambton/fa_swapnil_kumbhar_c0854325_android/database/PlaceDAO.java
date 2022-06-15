package ca.lambton.fa_swapnil_kumbhar_c0854325_android.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlaceDAO {

    @Query("select * from place")
    List<Place> getAllPlaces();

    @Insert
    Place addPlace(Place place);

    @Query("delete from place where id = :id")
    void deletePlace(int id);

    @Update
    void updatePlace(Place place);

    @Query("select * from place where name like '%' || :name || '%'")
    List<Place> searchPlaceByName(String name);
}
