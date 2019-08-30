package doh.nvbsp.nbbnets.donorverifier.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import doh.nvbsp.nbbnets.donorverifier.R;
import doh.nvbsp.nbbnets.donorverifier.libs.UserFn;
import doh.nvbsp.nbbnets.donorverifier.models.Donor;

public class DonorRecyclerViewAdapter extends RecyclerView.Adapter<DonorRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<Donor> donors;
    private Context context;
    private DonorRecyclerViewClickListener listener;

    public DonorRecyclerViewAdapter(Context context,ArrayList<Donor> donors,DonorRecyclerViewClickListener listener) {
        this.donors = donors;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View container = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_donor, parent,false);
        return new MyViewHolder(container);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final Donor donor = donors.get(i);
        View convertView = myViewHolder.container;

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(donor);
            }
        });

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
            try{
                byte[] decodedString = Base64.decode(donor.getDonor_photo(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                thumbnail.setImageBitmap(decodedByte);
            }catch (Exception e){

            }
        }
    }

    @Override
    public int getItemCount() {
        return donors.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View container;

        public MyViewHolder(View container) {
            super(container);
            this.container = container;
        }
    }

}
