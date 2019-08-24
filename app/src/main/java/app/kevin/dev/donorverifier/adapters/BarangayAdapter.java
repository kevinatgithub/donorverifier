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
import app.kevin.dev.donorverifier.models.Province;

public class BarangayAdapter extends ArrayAdapter<Barangay> {

    private Context context;

    public BarangayAdapter(@NonNull Context context, ArrayList<Barangay> barangays) {
        super(context, 0,barangays);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Barangay barangay = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_key_value, parent,false);
        }

        TextView txtValue = convertView.findViewById(R.id.txtValue);
        TextView txtKey = convertView.findViewById(R.id.txtKey);

        txtValue.setText(barangay.getBgyname());
        txtKey.setText(barangay.getBgycode());

        return convertView;
    }

}
