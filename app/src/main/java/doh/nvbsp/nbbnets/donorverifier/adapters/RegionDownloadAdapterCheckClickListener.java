package doh.nvbsp.nbbnets.donorverifier.adapters;

import android.view.View;

import doh.nvbsp.nbbnets.donorverifier.models.Region;

public interface RegionDownloadAdapterCheckClickListener {
    void onClick(Region region, View convertView, boolean isCheckButton);
}
