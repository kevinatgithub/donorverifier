package app.kevin.dev.donorverifier.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.kevin.dev.donorverifier.R;
import app.kevin.dev.donorverifier.libs.UserFn;
import app.kevin.dev.donorverifier.models.Donor;

public class DonorAdapter extends ArrayAdapter<Donor> {

    private Context context;

    public DonorAdapter(@NonNull Context context, ArrayList<Donor> donors) {
        super(context, 0, donors);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Donor donor = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_donor, parent,false);
        }

        TextView fullName = convertView.findViewById(R.id.txtValue);
        TextView bdate = convertView.findViewById(R.id.txtKey);
        TextView gender = convertView.findViewById(R.id.gender);
        ImageView thumbnail = convertView.findViewById(R.id.thumbnail);
        ImageView mayDonate = convertView.findViewById(R.id.mayDonate);

        mayDonate.setImageDrawable(context.getResources().getDrawable(android.R.drawable.screen_background_light_transparent));
        mayDonate.setColorFilter(context.getResources().getColor(android.R.color.holo_green_light));

        fullName.setText(UserFn.convertToTitleCaseIteratingChars(donor.getFname() + " " +donor.getMname() + " " + donor.getLname()));

        bdate.setText(donor.getBdate());

        if(donor.getGender().toUpperCase().equals("M"))
            gender.setText("Male");
        else if(donor.getGender().toUpperCase().equals("F"))
            gender.setText("Female");
        else
            gender.setText("");

        mayDonate.setVisibility(View.INVISIBLE);
        if(donor.getDonation_stat() != null){
            if(donor.getDonation_stat().toUpperCase().equals("Y")){
                mayDonate.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_check));
                mayDonate.setColorFilter(context.getResources().getColor(android.R.color.holo_green_light));
                mayDonate.setVisibility(View.VISIBLE);
            }else if(donor.getDonation_stat().toUpperCase().equals("N")){
                mayDonate.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_mistake));
                mayDonate.setColorFilter(context.getResources().getColor(android.R.color.holo_red_light));
                mayDonate.setVisibility(View.VISIBLE);
            }
        }

        return convertView;
    }
}
