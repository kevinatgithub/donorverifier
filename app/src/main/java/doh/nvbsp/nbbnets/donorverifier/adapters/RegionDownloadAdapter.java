package doh.nvbsp.nbbnets.donorverifier.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import doh.nvbsp.nbbnets.donorverifier.R;
import doh.nvbsp.nbbnets.donorverifier.libs.Session;
import doh.nvbsp.nbbnets.donorverifier.models.Region;

public class RegionDownloadAdapter extends ArrayAdapter<Region> {

    private Context context;
    private RegionDownloadAdapterCheckClickListener syncClickListener;
    private RegionDownloadAdapterDownloadClickListener downloadClickListener;

    public RegionDownloadAdapter(@NonNull Context context, ArrayList<Region> regions, RegionDownloadAdapterCheckClickListener syncClickListener, RegionDownloadAdapterDownloadClickListener downloadClickListener) {
        super(context, 0, regions);
        this.context = context;
        this.syncClickListener = syncClickListener;
        this.downloadClickListener = downloadClickListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Region region = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_region, parent,false);
        }

        TextView regionName = convertView.findViewById(R.id.regionName);
//        TextView donorCount = convertView.findViewById(R.id.donorCount);
//        TextView photoCount = convertView.findViewById(R.id.photoCount);
        TextView lastUpdate = convertView.findViewById(R.id.lastUpdate);

        String lastCheck = Session.get(context,"last_update_" + region.getRegcode(), null);
        if(lastCheck == null){
            lastUpdate.setText("Last Update : Never");
        }else{
            lastUpdate.setText("Last Update : " + lastCheck);
        }

        Button refresh = convertView.findViewById(R.id.refresh);
        Button openRegionDownload = convertView.findViewById(R.id.openRegionDownload);

        regionName.setText(region.getRegname());
//        donorCount.setText("0 Donors");
//        photoCount.setText("0 Photos");
//        lastUpdate.setText("Last Update N/A");

        final View cv = convertView;
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(syncClickListener != null)
                    syncClickListener.onClick(region,cv,true);
            }
        });

        openRegionDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(downloadClickListener != null)
                    downloadClickListener.onClick(region,cv);
            }
        });

        return convertView;
    }
}
