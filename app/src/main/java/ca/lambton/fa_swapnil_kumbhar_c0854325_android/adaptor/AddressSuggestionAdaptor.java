package ca.lambton.fa_swapnil_kumbhar_c0854325_android.adaptor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ca.lambton.fa_swapnil_kumbhar_c0854325_android.R;

public class AddressSuggestionAdaptor extends BaseAdapter {

    List<Address> addresses;
    Context context;
    LayoutInflater inflater;

    public AddressSuggestionAdaptor(Context context, List<Address> addresses) {
        this.addresses = addresses;
        this.context = context;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return addresses.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.address_suggestion_row_layout, null);
        TextView txtAddress = view.findViewById(R.id.txtAddress);
        txtAddress.setText(addresses.get(i).getAddressLine(0));
        return view;
    }
}
