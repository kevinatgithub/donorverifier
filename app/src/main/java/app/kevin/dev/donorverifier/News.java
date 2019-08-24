package app.kevin.dev.donorverifier;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import app.kevin.dev.donorverifier.adapters.NewsAdapter;
import app.kevin.dev.donorverifier.adapters.NewsAdapterButtonClickListener;
import app.kevin.dev.donorverifier.libs.Api;
import app.kevin.dev.donorverifier.libs.ConnectivityManager;
import app.kevin.dev.donorverifier.libs.Session;
import app.kevin.dev.donorverifier.libs.UserFn;
import app.kevin.dev.donorverifier.models.Callback;
import app.kevin.dev.donorverifier.models.CallbackWithResponse;
import app.kevin.dev.donorverifier.models.User;
import app.kevin.dev.donorverifier.models.api_response.NewsResponse;

public class News extends AppCompatActivity {

    ListView announcementsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);



        AppDrawerItemClickListener.prepareAppDrawer(this);

        announcementsList = findViewById(R.id.announcementsList);
        fetchNews();
    }

    private void fetchNews() {
        String url = UserFn.url(UserFn.API_NEWS);
        url = url.replace("{max}","0");
        Api.call(this, url, new CallbackWithResponse() {
            @Override
            public void execute(@Nullable JSONObject response) {
                NewsResponse newsResponse = UserFn.gson.fromJson(response.toString(),NewsResponse.class);
                if(newsResponse.getStatus().equals("ok")){
                    loadNews(newsResponse.getData());
                }
            }
        });
    }

    private void loadNews(ArrayList<app.kevin.dev.donorverifier.models.News> announcements) {
        NewsAdapter newsAdapter = new NewsAdapter(this, announcements, new NewsAdapterButtonClickListener() {
            @Override
            public void onClick(app.kevin.dev.donorverifier.models.News news) {
                openPreview(news);
            }
        });
        announcementsList.setAdapter(newsAdapter);
    }

    private void openPreview(app.kevin.dev.donorverifier.models.News news) {
        String str = UserFn.gson.toJson(news);
        Intent intent = new Intent(this,NewsPreview.class);
        intent.putExtra("news",str);
        startActivity(intent);
    }
}
