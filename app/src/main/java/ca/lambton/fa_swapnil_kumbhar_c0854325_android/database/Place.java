package ca.lambton.fa_swapnil_kumbhar_c0854325_android.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "places")
public class Place {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String name;

    @NonNull
    private Double lat;

    @NonNull
    private Double lng;

    @ColumnInfo(defaultValue = "false")
    private boolean isVisited;

    public Place(@NonNull String name, @NonNull Double lat, @NonNull Double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public Double getLat() {
        return lat;
    }

    public void setLat(@NonNull Double lat) {
        this.lat = lat;
    }

    @NonNull
    public Double getLng() {
        return lng;
    }

    public void setLng(@NonNull Double lng) {
        this.lng = lng;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }
}
