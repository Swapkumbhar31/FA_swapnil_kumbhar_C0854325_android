package ca.lambton.fa_swapnil_kumbhar_c0854325_android.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlaceDAO {

    @Query("select * from places order by createdAt desc")
    List<Place> getAllPlaces();

    @Insert
    void addPlace(Place place);

    @Query("delete from places where id = :id")
    void deletePlace(int id);

    @Update
    void updatePlace(Place place);

    @Query("select * from places where name like '%' || :name || '%'")
    List<Place> searchPlaceByName(String name);
}
