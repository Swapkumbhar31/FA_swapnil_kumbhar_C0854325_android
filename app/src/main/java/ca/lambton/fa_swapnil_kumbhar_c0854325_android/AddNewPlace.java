package ca.lambton.fa_swapnil_kumbhar_c0854325_android;

import static ca.lambton.fa_swapnil_kumbhar_c0854325_android.Helper.ImageHelper.getBitmapFormUri;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Date;

import ca.lambton.fa_swapnil_kumbhar_c0854325_android.database.Place;
import ca.lambton.fa_swapnil_kumbhar_c0854325_android.database.PlacesRoomDB;
import ca.lambton.fa_swapnil_kumbhar_c0854325_android.databinding.ActivityAddNewPlaceBinding;

public class AddNewPlace extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {
    GoogleMap mMap;
    Marker marker;
    LatLng latLng;
    Place place = null;
    private ActivityAddNewPlaceBinding binding;
    private PlacesRoomDB placesRoomDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewPlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        placesRoomDB = PlacesRoomDB.getInstance(this);
        setTitle("Add new place");
        Uri imageUri = null;
        Intent intent = getIntent();
        int placeId = intent.getIntExtra("placeId", 0);
        String imagePath;
        if (placeId == 0) {
            imagePath = intent.getStringExtra("imagePath");
            String address = intent.getStringExtra("placeName");
            double lat = intent.getDoubleExtra("lat", 0);
            double lng = intent.getDoubleExtra("lng", 0);
            latLng = new LatLng(lat, lng);
            binding.txtAddress.setText(address);
            imageUri = Uri.parse(imagePath);
        } else {
            place = placesRoomDB.placeDAO().getPlaceByID(placeId);
            binding.txtAddress.setText(place.getAddress());
            binding.txtTitle.setText(place.getName());
            latLng = new LatLng(place.getLat(), place.getLng());
            imagePath = place.getImagePath();
            if (place.getImagePath() != null) {
                imageUri = Uri.parse(place.getImagePath());
            }
            binding.addToWishlist.setText("Update place");
            binding.addToWishlist.setIcon(null);
        }
        try {
            if (imageUri != null) {
                final Bitmap selectedImage = getBitmapFormUri(this, imageUri);
                binding.placeImage.setImageBitmap(selectedImage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        binding.addToWishlist.setOnClickListener(view -> {
            if (binding.txtTitle.getText().toString().isEmpty()) {
                binding.txtTitle.setError("Title is required");
                return;
            }
            if (binding.txtAddress.getText().toString().isEmpty()) {
                binding.txtAddress.setError("Title is required");
                return;
            }
            if (place == null) {
                placesRoomDB.placeDAO().addPlace(new Place(
                        binding.txtTitle.getText().toString(),
                        binding.txtAddress.getText().toString(),
                        latLng.latitude,
                        latLng.longitude,
                        new Date(),
                        imagePath
                ));
            } else {
                place.setName(binding.txtTitle.getText().toString());
                place.setAddress(binding.txtAddress.getText().toString());
                place.setLat(latLng.latitude);
                place.setLng(latLng.longitude);
                placesRoomDB.placeDAO().updatePlace(place);
            }
            finish();
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.mMap = googleMap;
        if (place == null) {
            this.mMap.getUiSettings().setScrollGesturesEnabled(false);
        }
        mMap.getUiSettings().setMapToolbarEnabled(false);
        marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Title"));
        if (marker != null) {
            marker.setDraggable(true);
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        mMap.setOnMarkerDragListener(this);
    }


    @Override
    public void onMarkerDrag(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        this.marker = marker;
        this.latLng = marker.getPosition();
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {

    }
}