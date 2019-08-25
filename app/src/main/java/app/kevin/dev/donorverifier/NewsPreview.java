package app.kevin.dev.donorverifier;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.kevin.dev.donorverifier.adapters.NewsAdapter;
import app.kevin.dev.donorverifier.libs.UserFn;
import app.kevin.dev.donorverifier.models.News;
import io.realm.Realm;

public class NewsPreview extends AppCompatActivity implements View.OnClickListener {

    private News news;
    private DrawerLayout drawerLayout;

    Realm realm;
    ImageView thumbnail;
    TextView title;
    TextView subTitle;
    TextView author;
    TextView createDate;
    TextView link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_preview);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(this);

        realm = UserFn.getRealmInstance(this);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        news = realm.where(News.class).equalTo("id",Integer.parseInt(id)).findFirst();

        thumbnail = findViewById(R.id.thumbnail);
        title = findViewById(R.id.title);
        subTitle = findViewById(R.id.subTitle);
        author = findViewById(R.id.author);
        createDate = findViewById(R.id.createDate);
        link = findViewById(R.id.link);

        title.setText(news.getTitle());
        subTitle.setText(news.getContent());
        author.setText(news.getAuthor());
        createDate.setText(news.getCreated_dt());
        
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewsInBrowser();
            }
        });
        
        byte[] decodedString = Base64.decode(news.getThumbnail(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        thumbnail.setImageBitmap(decodedByte);
    }

    private void openNewsInBrowser() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getLink()));
        startActivity(browserIntent);
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
