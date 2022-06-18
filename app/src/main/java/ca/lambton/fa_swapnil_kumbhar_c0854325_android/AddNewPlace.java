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

public class AddNewPlace extends AppCompatActivity implements OnMapReadyCallback {
    private ActivityAddNewPlaceBinding binding;
    GoogleMap mMap;
    Marker marker;
    private PlacesRoomDB placesRoomDB;
    LatLng latLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewPlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        placesRoomDB = PlacesRoomDB.getInstance(this);
        setTitle("Add new place");

        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("imagePath");
        String address = intent.getStringExtra("placeName");
        binding.txtAddress.setText(address);
        final Uri imageUri = Uri.parse(imagePath);
        try {
            final Bitmap selectedImage =  getBitmapFormUri(this, imageUri);
            binding.placeImage.setImageBitmap(selectedImage);
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
            placesRoomDB.placeDAO().addPlace(new Place(
                    binding.txtTitle.getText().toString(),
                    binding.txtAddress.getText().toString(),
                    latLng.latitude,
                    latLng.longitude,
                    new Date(),
                    imagePath
            ));
            finish();
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.mMap = googleMap;
        this.mMap.getUiSettings().setScrollGesturesEnabled(false);
        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("lat", 0);
        double lng = intent.getDoubleExtra("lng", 0);
        latLng = new LatLng(lat, lng);
        marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Title"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        mMap.getUiSettings().setMapToolbarEnabled(false);
    }


}