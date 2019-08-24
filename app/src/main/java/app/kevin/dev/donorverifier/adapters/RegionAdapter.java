package app.kevin.dev.donorverifier.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.kevin.dev.donorverifier.R;
import app.kevin.dev.donorverifier.models.Barangay;
import app.kevin.dev.donorverifier.models.Region;

public class RegionAdapter extends ArrayAdapter<Region> {

    private Context context;

    public RegionAdapter(@NonNull Context context, ArrayList<Region> regions) {
        super(context, 0, regions);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Region region = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_key_value, parent,false);
        }

        TextView txtValue = convertView.findViewById(R.id.txtValue);
        TextView txtKey = convertView.findViewById(R.id.txtKey);

        txtValue.setText(region.getRegname());
        txtKey.setText(region.getRegcode());

        return convertView;
    }
}
