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

import ca.lambton.fa_swapnil_kumbhar_c0854325_android.databinding.ActivityAddNewPlaceBinding;

public class AddNewPlace extends AppCompatActivity implements OnMapReadyCallback {
    private ActivityAddNewPlaceBinding binding;
    GoogleMap mMap;
    Marker marker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewPlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.mMap = googleMap;
        this.mMap.getUiSettings().setScrollGesturesEnabled(false);
        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("lat", 0);
        double lng = intent.getDoubleExtra("lng", 0);
        LatLng latLng = new LatLng(lat, lng);
        marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Title"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        mMap.getUiSettings().setMapToolbarEnabled(false);
    }
}