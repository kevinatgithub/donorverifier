package doh.nvbsp.nbbnets.donorverifier.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import doh.nvbsp.nbbnets.donorverifier.R;
import doh.nvbsp.nbbnets.donorverifier.libs.UserFn;
import doh.nvbsp.nbbnets.donorverifier.models.Donor;

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

        fullName.setText("");
        bdate.setText("");
        gender.setText("");
        thumbnail.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_photo));
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

        if(donor.getDonor_photo() != null){
            byte[] decodedString = Base64.decode(donor.getDonor_photo(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            thumbnail.setImageBitmap(decodedByte);
        }

        return convertView;
    }
}
