package app.kevin.dev.donorverifier.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

import app.kevin.dev.donorverifier.R;
import app.kevin.dev.donorverifier.models.News;

public class NewsAdapter extends ArrayAdapter<News> {

    private Context context;
    private NewsAdapterButtonClickListener onClickListener;

    public NewsAdapter(@NonNull Context context, ArrayList<News> announcements, NewsAdapterButtonClickListener onClickListener) {
        super(context, 0, announcements);
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final News news = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_news, parent,false);
        }

        ImageView thumbnail = convertView.findViewById(R.id.thumbnail);
        TextView title = convertView.findViewById(R.id.title);
        TextView subTitle = convertView.findViewById(R.id.subTitle);
        ImageView btn = convertView.findViewById(R.id.overflow);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onClickListener != null)
                    onClickListener.onClick(news);
            }
        });

        title.setText(news.getTitle());
        if(news.getContent().length() > 50) {
            subTitle.setText(news.getContent().substring(0,50) + "..");
        }else{
            subTitle.setText(news.getContent());
        }

        byte[] decodedString = Base64.decode(news.getThumbnail(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        thumbnail.setImageBitmap(decodedByte);


//        URL url = null;
//        try {
//            url = new URL(news.getThumbnail());
//            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            thumbnail.setImageBitmap(bmp);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        DownloadImageWithURLTask downloadImageWithURLTask = new DownloadImageWithURLTask(thumbnail);
//        downloadImageWithURLTask.execute(news.getThumbnail());

        return convertView;
    }

    public static class DownloadImageWithURLTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageWithURLTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String pathToFile = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(pathToFile).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
