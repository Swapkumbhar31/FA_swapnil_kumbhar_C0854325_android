package ca.lambton.fa_swapnil_kumbhar_c0854325_android.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.lambton.fa_swapnil_kumbhar_c0854325_android.R;
import ca.lambton.fa_swapnil_kumbhar_c0854325_android.database.Place;

public class PlaceListAdaptor extends RecyclerView.Adapter<PlaceListItemViewHolder> {
    Context context;
    List<Place> places;

    public PlaceListAdaptor(Context context, List<Place> places) {
        this.context = context;
        this.places = places;
    }

    @NonNull
    @Override
    public PlaceListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.place_row_layout, parent, false);
        return new PlaceListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceListItemViewHolder holder, int position) {
        holder.txtTitle.setText(places.get(position).getName());
        holder.txtSubtitle.setText(places.get(position).getLat() + "," + places.get(position).getLng());
    }

    @Override
    public int getItemCount() {
        return places.size();
    }
}
