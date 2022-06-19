package ca.lambton.fa_swapnil_kumbhar_c0854325_android;

import static ca.lambton.fa_swapnil_kumbhar_c0854325_android.Helper.ImageHelper.getBitmapFormUri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import ca.lambton.fa_swapnil_kumbhar_c0854325_android.database.Place;
import ca.lambton.fa_swapnil_kumbhar_c0854325_android.database.PlacesRoomDB;
import ca.lambton.fa_swapnil_kumbhar_c0854325_android.databinding.ActivityPlaceDetailsBinding;

public class PlaceDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityPlaceDetailsBinding binding;
    private PlacesRoomDB placesRoomDB;
    GoogleMap mMap;
    Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlaceDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        placesRoomDB = PlacesRoomDB.getInstance(this);

        Intent intent = getIntent();
        long placeId = intent.getIntExtra("placeId", 0);

        if (placeId != 0) {
            place = placesRoomDB.placeDAO().getPlaceByID(placeId);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }

            binding.markAsVisited.setOnClickListener(view -> {
                place.setVisited(true);
                placesRoomDB.placeDAO().updatePlace(place);
                place = placesRoomDB.placeDAO().getPlaceByID(placeId);
                initView();
            });
            initView();
        } else {
            Toast.makeText(this, "Invalid place id", Toast.LENGTH_LONG).show();
        }
    }

    private void initView() {
        binding.txtTitle.setText(place.getName());
        binding.txtAddress.setText(place.getAddress());
        if (place.getImagePath() != null && !place.getImagePath().isEmpty()) {
            final Uri imageUri = Uri.parse(place.getImagePath());
            try {
                final Bitmap selectedImage =  getBitmapFormUri(this, imageUri);
                binding.placeImage.setImageBitmap(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (place.isVisited()) {
            binding.markAsVisited.setVisibility(View.GONE);
        } else {
            binding.markAsVisited.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        LatLng latLng = new LatLng(place.getLat(), place.getLng());
        mMap.addMarker(new MarkerOptions().position(latLng).title(place.getName()));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }
}