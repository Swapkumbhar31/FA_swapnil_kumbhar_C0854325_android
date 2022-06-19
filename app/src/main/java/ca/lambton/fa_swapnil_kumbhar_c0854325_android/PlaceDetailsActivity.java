package ca.lambton.fa_swapnil_kumbhar_c0854325_android;

import static ca.lambton.fa_swapnil_kumbhar_c0854325_android.Helper.ImageHelper.getBitmapFormUri;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
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
import java.text.DecimalFormat;

import ca.lambton.fa_swapnil_kumbhar_c0854325_android.database.Place;
import ca.lambton.fa_swapnil_kumbhar_c0854325_android.database.PlacesRoomDB;
import ca.lambton.fa_swapnil_kumbhar_c0854325_android.databinding.ActivityPlaceDetailsBinding;

public class PlaceDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityPlaceDetailsBinding binding;
    private PlacesRoomDB placesRoomDB;
    GoogleMap mMap;
    Place place;
    LocationListener locationListener;
    private static final int REQUEST_CODE = 200;
    private LocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlaceDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        placesRoomDB = PlacesRoomDB.getInstance(this);
        manager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @RequiresApi(api = Build.VERSION_CODES.S)
            @Override
            public void onLocationChanged(@NonNull Location location) {
                getLocation();
            }
        };
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

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.S)
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            Location locationGPS = manager.getLastKnownLocation(LocationManager.FUSED_PROVIDER);
            System.out.println("Distance : requested");
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double lng = locationGPS.getLongitude();
                float[] results = new float[1];
                Location.distanceBetween(lat, lng, place.getLat(), place.getLng(), results);
                float distance = results[0];
                DecimalFormat df = new DecimalFormat("0.00");
                binding.txtDistance.setText("Distance : " + df.format(distance / 1000) + " KM");
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_LONG).show();
            }
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

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CODE == requestCode) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this).setMessage("Accessing th location is mandatory ").
                        setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                            }
                        }).setNegativeButton("Cancel", null).create().show();
            } else {
                manager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        0,
                        locationListener
                );
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        LatLng latLng = new LatLng(place.getLat(), place.getLng());
        mMap.addMarker(new MarkerOptions().position(latLng).title(place.getName()));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        getLocation();
    }
}