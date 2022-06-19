package ca.lambton.fa_swapnil_kumbhar_c0854325_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

import ca.lambton.fa_swapnil_kumbhar_c0854325_android.Helper.MyButtonClickListener;
import ca.lambton.fa_swapnil_kumbhar_c0854325_android.Helper.PlaceListItemSwipeHelper;
import ca.lambton.fa_swapnil_kumbhar_c0854325_android.SharedPreferences.UserSettings;
import ca.lambton.fa_swapnil_kumbhar_c0854325_android.adaptor.PlaceListAdaptor;
import ca.lambton.fa_swapnil_kumbhar_c0854325_android.database.Place;
import ca.lambton.fa_swapnil_kumbhar_c0854325_android.database.PlacesRoomDB;
import ca.lambton.fa_swapnil_kumbhar_c0854325_android.databinding.ActivityPlaceListBinding;

public class PlaceList extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private PlacesRoomDB placesRoomDB;
    GoogleMap mMap;

    private List<Place> places;
    LinearLayoutManager layoutManager;
    private ActivityPlaceListBinding binding;
    boolean isMapView = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlaceListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Home");

        binding.placeList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        binding.placeList.setLayoutManager(layoutManager);

        placesRoomDB = PlacesRoomDB.getInstance(this);

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(view -> {
            Intent i = new Intent(PlaceList.this, MapsActivity.class);
            startActivity(i);
        });

        UserSettings userSettings = new UserSettings().getInstance(getApplicationContext());
        boolean firstTimeOpen = new UserSettings().getInstance(getApplicationContext()).isFirstTimeOpen();

        if (firstTimeOpen) {
            insertPlaces();
            userSettings.setIsFirstTimeOpen(false);
        }
        binding.viewToggle.setOnClickListener(view -> {
            if (isMapView) {
                binding.viewToggle.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_view_list_48_black));
                binding.map.setVisibility(View.GONE);
                binding.placeList.setVisibility(View.VISIBLE);
            } else {
                binding.viewToggle.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_map_48_black));
                binding.placeList.setVisibility(View.GONE);
                binding.map.setVisibility(View.VISIBLE);
            }
            isMapView = !isMapView;
        });
        if (isMapView) {
            binding.viewToggle.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_map_48_black));
            binding.placeList.setVisibility(View.GONE);
            binding.map.setVisibility(View.VISIBLE);
        } else {
            binding.map.setVisibility(View.GONE);
            binding.placeList.setVisibility(View.VISIBLE);
            binding.viewToggle.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_view_list_48_black));
        }
        new PlaceListItemSwipeHelper(this, binding.placeList, 300) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<PlaceListItemSwipeHelper.MyButton> buffer) {
                buffer.add(new MyButton(PlaceList.this, "", 30, R.drawable.ic_baseline_delete_48_white, Color.parseColor("#e54304"), new MyButtonClickListener() {
                    @Override
                    public void onClick(int pos) {
                        Place place = places.get(pos);
                        placesRoomDB.placeDAO().deletePlace(place.getId());
                        Toast.makeText(PlaceList.this, place.getName() + " is deleted successfully", Toast.LENGTH_SHORT).show();
                        onResume();
                    }
                }));
                buffer.add(new MyButton(PlaceList.this, "", 30, R.drawable.ic_baseline_create_48_white, Color.parseColor("#09af00"), new MyButtonClickListener() {
                    @Override
                    public void onClick(int pos) {
                        Intent intent = new Intent(getApplicationContext(), AddNewPlace.class);
                        intent.putExtra("placeId", places.get(pos).getId());
                        startActivity(intent);
                    }
                }));
            }
        };
    }

    private void insertPlaces() {
        placesRoomDB.placeDAO().addPlace(new Place("Taj Mahal", "Taj Mahal", 27.1752474,77.8330407, new Date(), null));
        placesRoomDB.placeDAO().addPlace(new Place("Great Wall of China", "Great Wall of China", 40.4319118,116.5681862, new Date(), null));
        placesRoomDB.placeDAO().addPlace(new Place("Chichén Itzá", "Chichén Itzá", 20.6828175,-88.5727964, new Date(), null));
        placesRoomDB.placeDAO().addPlace(new Place("Petra", "Petra", 30.328459,35.4421735, new Date(), null));
        placesRoomDB.placeDAO().addPlace(new Place("Machu Picchu", "Machu Picchu", -13.2086968,-72.5619342, new Date(), null));
        placesRoomDB.placeDAO().addPlace(new Place("Christ the Redeemer", "Christ the Redeemer", -22.951911,-43.2126759, new Date(), null));
        placesRoomDB.placeDAO().addPlace(new Place("Colosseum", "Colosseum", 41.8902102,12.4922309, new Date(), null));
        placesRoomDB.placeDAO().addPlace(new Place("Sukhothai, Thailand","Sukhothai, Thailand", 17.2519855,99.1364003, new Date(), null));
        placesRoomDB.placeDAO().addPlace(new Place("CN Tower", "CN Tower", 43.6425701,-79.3892455, new Date(), null));
        placesRoomDB.placeDAO().addPlace(new Place("Walt Disney World® Resort","Walt Disney World® Resort", 28.3771903,-81.5752247, new Date(), null));
    }

    @Override
    protected void onResume() {
        super.onResume();
        places = placesRoomDB.placeDAO().getAllPlaces();
        binding.placeList.setAdapter(new PlaceListAdaptor(this, places));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        for (Place place: places) {
            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(place.getLat(), place.getLng())).title(place.getName()));
            if (marker != null) {
                marker.setTag(place.getId());
            }
        }
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        int placeId = (int) marker.getTag();
        Intent intent = new Intent(getApplicationContext(), PlaceDetailsActivity.class);
        intent.putExtra("placeId", placeId);
        startActivity(intent);
        return false;
    }
}