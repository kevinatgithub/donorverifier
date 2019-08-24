package app.kevin.dev.donorverifier;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.kevin.dev.donorverifier.adapters.NewsAdapter;
import app.kevin.dev.donorverifier.libs.UserFn;
import app.kevin.dev.donorverifier.models.News;

public class NewsPreview extends AppCompatActivity implements View.OnClickListener {

    private News news;
    private DrawerLayout drawerLayout;

    ImageView thumbnail;
    TextView title;
    TextView subTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_preview);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(this);

        Intent intent = getIntent();
        String str = intent.getStringExtra("news");
        news = UserFn.gson.fromJson(str, app.kevin.dev.donorverifier.models.News.class);

        thumbnail = findViewById(R.id.thumbnail);
        title = findViewById(R.id.title);
        subTitle = findViewById(R.id.subTitle);

        title.setText(news.getTitle());
        subTitle.setText(news.getContent());

        NewsAdapter.DownloadImageWithURLTask downloadImageWithURLTask = new NewsAdapter.DownloadImageWithURLTask(thumbnail);
        downloadImageWithURLTask.execute(news.getThumbnail());
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
