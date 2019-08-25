package app.kevin.dev.donorverifier.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import app.kevin.dev.donorverifier.R;
import app.kevin.dev.donorverifier.models.Region;

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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_download_region, parent,false);
        }

        TextView regionName = convertView.findViewById(R.id.regionName);
        TextView donorCount = convertView.findViewById(R.id.donorCount);
        TextView photoCount = convertView.findViewById(R.id.photoCount);
        TextView donorsHidden = convertView.findViewById(R.id.donorsHidden);
        TextView barangayCount = convertView.findViewById(R.id.barangayCount);
        TextView barangaysHidden = convertView.findViewById(R.id.barangaysHidden);
        TextView lastUpdate = convertView.findViewById(R.id.lastUpdate);

        ImageButton refresh = convertView.findViewById(R.id.refresh);
        ImageButton openRegionDownload = convertView.findViewById(R.id.openRegionDownload);

        regionName.setText(region.getRegname());
        donorCount.setText("0 Donors");
        donorsHidden.setText("0");
        photoCount.setText("0 Photos");
        barangayCount.setText("0 Barangays");
        barangaysHidden.setText("0");
        lastUpdate.setText("Last Update N/A");

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
