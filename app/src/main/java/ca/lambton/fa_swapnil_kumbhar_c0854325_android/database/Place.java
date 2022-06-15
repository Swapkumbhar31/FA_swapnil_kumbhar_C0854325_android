package ca.lambton.fa_swapnil_kumbhar_c0854325_android.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
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
}
