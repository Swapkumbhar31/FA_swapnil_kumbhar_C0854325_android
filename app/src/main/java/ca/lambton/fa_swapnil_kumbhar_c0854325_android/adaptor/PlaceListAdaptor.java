package ca.lambton.fa_swapnil_kumbhar_c0854325_android.adaptor;

import static ca.lambton.fa_swapnil_kumbhar_c0854325_android.Helper.ImageHelper.getBitmapFormUri;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PlaceListItemViewHolder holder, int position) {
        Place place = places.get(position);
        holder.txtTitle.setText(place.getName());
        holder.txtSubtitle.setText(place.getAddress());
        if (place.getImagePath() != null && !place.getImagePath().isEmpty()) {
            final Uri imageUri = Uri.parse(place.getImagePath());
            try {
                final Bitmap selectedImage =  getBitmapFormUri(context, imageUri);
                holder.placeImage.setImageBitmap(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            @SuppressLint("UseCompatLoadingForDrawables") Drawable res = context.getResources().getDrawable(R.drawable.no_image);
            holder.placeImage.setImageDrawable(res);
        }

    }

    @Override
    public int getItemCount() {
        return places.size();
    }
}
