package ca.lambton.fa_swapnil_kumbhar_c0854325_android.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import ca.lambton.fa_swapnil_kumbhar_c0854325_android.Helper.DateConverter;

@Entity(tableName = "places")
public class Place {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String name;

    @NonNull
    private String address;

    @Nullable
    private String imagePath;

    @NonNull
    private Double lat;

    @NonNull
    private Double lng;

    @ColumnInfo(defaultValue = "false")
    private boolean isVisited;

    @Nullable
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@Nullable Date createdAt) {
        this.createdAt = createdAt;
    }

    @Nullable
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(@Nullable String imagePath) {
        this.imagePath = imagePath;
    }

    @Nullable
    @TypeConverters(DateConverter.class)
    private Date createdAt;

    public Place(@NonNull String name, @NonNull String address, @NonNull Double lat, @NonNull Double lng, @NonNull Date createdAt, @Nullable String imagePath, @Nullable boolean isVisited) {
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
        this.isVisited = isVisited;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
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
