package ca.lambton.fa_swapnil_kumbhar_c0854325_android.adaptor;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ca.lambton.fa_swapnil_kumbhar_c0854325_android.R;

public class PlaceListItemViewHolder extends RecyclerView.ViewHolder {
    TextView txtTitle;
    TextView txtSubtitle;
    ImageView placeImage;

    public PlaceListItemViewHolder(@NonNull View itemView) {
        super(itemView);
        txtTitle = itemView.findViewById(R.id.title);
        txtSubtitle = itemView.findViewById(R.id.subtitle);
        placeImage = itemView.findViewById(R.id.placeImage);
    }
}
