package app.kevin.dev.donorverifier.adapters;

import android.view.View;

import app.kevin.dev.donorverifier.models.Region;

public interface RegionDownloadAdapterCheckClickListener {
    void onClick(Region region, View convertView, boolean isCheckButton);
}
