package ca.lambton.fa_swapnil_kumbhar_c0854325_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ca.lambton.fa_swapnil_kumbhar_c0854325_android.Helper.MyButtonClickListener;
import ca.lambton.fa_swapnil_kumbhar_c0854325_android.Helper.PlaceListItemSwipeHelper;
import ca.lambton.fa_swapnil_kumbhar_c0854325_android.SharedPreferences.UserSettings;
import ca.lambton.fa_swapnil_kumbhar_c0854325_android.adaptor.PlaceListAdaptor;
import ca.lambton.fa_swapnil_kumbhar_c0854325_android.database.Place;
import ca.lambton.fa_swapnil_kumbhar_c0854325_android.database.PlacesRoomDB;

public class PlaceList extends AppCompatActivity {
    private PlacesRoomDB placesRoomDB;

    private FloatingActionButton floatingActionButton;

    private List<Place> places;
    RecyclerView placesListView;
    LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);
        setTitle("Home");

        placesListView = findViewById(R.id.place_list);
        placesListView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        placesListView.setLayoutManager(layoutManager);

        placesRoomDB = PlacesRoomDB.getInstance(this);

        floatingActionButton = findViewById(R.id.floatingActionButton);

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

        places = placesRoomDB.placeDAO().getAllPlaces();

        placesListView.setAdapter(new PlaceListAdaptor(this, places));

        PlaceListItemSwipeHelper placeListItemSwipeHelper = new PlaceListItemSwipeHelper(this, placesListView, 400) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<PlaceListItemSwipeHelper.MyButton> buffer) {
//                buffer.add(new MyButton(PlaceList.this, "Delete", 60, 0, Color.parseColor("#ff3c30"), new MyButtonClickListener() {
//                    @Override
//                    public void onClick(int pos) {
//                        Toast.makeText(PlaceList.this, "Hello!!!", Toast.LENGTH_SHORT).show();
//                    }
//                }));
//                buffer.add(new MyButton(PlaceList.this, "ADD", 60, 0, Color.parseColor("#ff3c30"), new MyButtonClickListener() {
//                    @Override
//                    public void onClick(int pos) {
//                        Toast.makeText(PlaceList.this, "BYE!!!", Toast.LENGTH_SHORT).show();
//                    }
//                }));
            }
        };
    }

    private void insertPlaces() {
        placesRoomDB.placeDAO().addPlace(new Place("Taj Mahal", 27.1752474,77.8330407));
        placesRoomDB.placeDAO().addPlace(new Place("Great Wall of China", 40.4319118,116.5681862));
        placesRoomDB.placeDAO().addPlace(new Place("Chichén Itzá", 20.6828175,-88.5727964));
        placesRoomDB.placeDAO().addPlace(new Place("Petra", 30.328459,35.4421735));
        placesRoomDB.placeDAO().addPlace(new Place("Machu Picchu", -13.2086968,-72.5619342));
        placesRoomDB.placeDAO().addPlace(new Place("Christ the Redeemer", -22.951911,-43.2126759));
        placesRoomDB.placeDAO().addPlace(new Place("Colosseum", 41.8902102,12.4922309));
        placesRoomDB.placeDAO().addPlace(new Place("Sukhothai, Thailand", 17.2519855,99.1364003));
        placesRoomDB.placeDAO().addPlace(new Place("CN Tower", 43.6425701,-79.3892455));
        placesRoomDB.placeDAO().addPlace(new Place("Walt Disney World® Resort", 28.3771903,-81.5752247));
    }
}