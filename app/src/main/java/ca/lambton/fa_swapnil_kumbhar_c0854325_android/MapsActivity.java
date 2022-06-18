package ca.lambton.fa_swapnil_kumbhar_c0854325_android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import ca.lambton.fa_swapnil_kumbhar_c0854325_android.adaptor.AddressSuggestionAdaptor;
import ca.lambton.fa_swapnil_kumbhar_c0854325_android.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {

    private static final int REQUEST_CODE = 200;
    private final String apiKey = "AIzaSyBSo8zZUD5p2lrXVV_ejXXG7VtcExLkBWM";
    LocationListener locationListener;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LocationManager manager;
    private Marker newMarker;
    private PlacesClient placesClient;
    List<Address> addresses =new ArrayList<>();

    public void getLocationFromAddress(LatLng latLng) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                latLng.latitude + "," +
                latLng.longitude +
                "&key=" + apiKey;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject result = response.getJSONArray("results").getJSONObject(0);
                    String placeId = result.getString("place_id");
                    fetchPhoto(placeId);
                    String address = result.getString("formatted_address");
                    binding.txtAddress.setText(address);
                    @SuppressLint("UseCompatLoadingForDrawables") Drawable res = getResources().getDrawable(R.drawable.no_image);
                    binding.placeImage.setImageDrawable(res);
                    binding.addressView.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, listener, null);
        queue.add(request);
    }

    private void fetchPhoto(String placeId ) {

        final List<Place.Field> fields = Collections.singletonList(Place.Field.PHOTO_METADATAS);
        final FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeId, fields);

        placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {
            final Place place = response.getPlace();

            // Get the photo metadata.
            final List<PhotoMetadata> metadata = place.getPhotoMetadatas();
            if (metadata == null || metadata.isEmpty()) {
                Log.i("TAG", "No photo metadata.");
                return;
            }
            final PhotoMetadata photoMetadata = metadata.get(0);

            // Get the attribution text.
            final String attributions = photoMetadata.getAttributions();

            // Create a FetchPhotoRequest.
            final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata).build();
            placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                Bitmap bitmap = fetchPhotoResponse.getBitmap();
                binding.placeImage.setImageBitmap(bitmap);
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    final ApiException apiException = (ApiException) exception;
                    Log.i("TAG", "Place not found: " + exception.getMessage());
                }
            });
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @RequiresApi(api = Build.VERSION_CODES.S)
            @Override
            public void onLocationChanged(@NonNull Location location) {
                getLocation();
            }
        };
        Places.initialize(this, apiKey);
        placesClient = Places.createClient(this);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        binding.suggestionsList.setOnItemClickListener(this);
        binding.searchBarText.setOnQueryTextListener(this);
        binding.searchBarText.setOnCloseListener(() -> {
            addresses = new ArrayList<>();
            binding.suggestionsList.setAdapter(new AddressSuggestionAdaptor(this, addresses));
            return false;
        });
        binding.searchBarText.setOnClickListener(view -> {
            binding.addressView.setVisibility(View.GONE);
        });
        binding.suggestionsList.setAdapter(new AddressSuggestionAdaptor(this, addresses));
        binding.closeBtn.setOnClickListener(view -> {
            binding.addressView.setVisibility(View.GONE);
        });
        binding.saveBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddNewPlace.class);
            intent.putExtra("placeName", binding.txtAddress.getText().toString());
            intent.putExtra("lat", newMarker.getPosition().latitude);
            intent.putExtra("lng", newMarker.getPosition().longitude);
            BitmapDrawable drawable = (BitmapDrawable) binding.placeImage.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            String imagePath = MediaStore.Images.Media.insertImage(
                    getContentResolver(),
                    bitmap,
                    new Date().toString(),
                    new Date().toString()
            );
            intent.putExtra("imagePath", imagePath);
            startActivity(intent);
            finish();
        });
        setContentView(binding.getRoot());
        Places.initialize(getApplicationContext(), apiKey);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
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

    private Marker addMarker(LatLng location, String title) {
        LatLng latLng = new LatLng(location.latitude, location.longitude);
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(title));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        return marker;
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        if (!manager.isProviderEnabled(LocationManager.FUSED_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }
        mMap.setOnMapClickListener(latLng -> {
            onClickMarker(latLng, "New Location");
            if (newMarker != null) {
                fetchPlaceInformation();
            }
        });
        mMap.setOnMarkerClickListener(marker -> {
            getLocationFromAddress(marker.getPosition());
            return false;
        });
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                newMarker = marker;
                fetchPlaceInformation();
            }

            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {

            }
        });
    }

    private void onClickMarker(LatLng location, String title) {
        if (newMarker != null) {
            newMarker.remove();
        }
        newMarker = addMarker(location, title);
        newMarker.setDraggable(true);
        newMarker.showInfoWindow();
    }

    private void fetchPlaceInformation() {
        getLocationFromAddress(newMarker.getPosition());
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            Location locationGPS = manager.getLastKnownLocation(LocationManager.FUSED_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double lng = locationGPS.getLongitude();
                onClickMarker(new LatLng(lat, lng), "Your location");
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = GoogleApiAvailability.getInstance().
                    getErrorDialog
                            (this, REQUEST_CODE, REQUEST_CODE,
                                    new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialogInterface) {

                                            Toast.makeText(MapsActivity.this,
                                                    "The Service is not available",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });

            assert errorDialog != null;
            errorDialog.show();
        }
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", (dialog, which) -> dialog.cancel());
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Address address = addresses.get(i);
        addresses = new ArrayList<>();
        binding.suggestionsList.setAdapter(new AddressSuggestionAdaptor(this, addresses));
        View view1 = this.getCurrentFocus();
        if (view1 != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        onClickMarker(latLng, address.getAddressLine(0));
        getLocationFromAddress(latLng);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (binding.addressView.getVisibility() == View.VISIBLE) {
            binding.addressView.setVisibility(View.GONE);
        }
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(s, 15);
            if(addresses.isEmpty())
            {
                this.addresses.clear();
            }
            else {
                this.addresses = addresses;
            }
            System.out.println(this.addresses.size());
            binding.suggestionsList.setAdapter(new AddressSuggestionAdaptor(this, addresses));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}