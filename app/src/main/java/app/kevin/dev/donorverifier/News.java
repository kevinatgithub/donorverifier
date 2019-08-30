package app.kevin.dev.donorverifier;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import app.kevin.dev.donorverifier.adapters.NewsAdapter;
import app.kevin.dev.donorverifier.adapters.NewsAdapterButtonClickListener;
import app.kevin.dev.donorverifier.libs.Api;
import app.kevin.dev.donorverifier.libs.UserFn;
import app.kevin.dev.donorverifier.models.Callback;
import app.kevin.dev.donorverifier.models.CallbackWithResponse;
import app.kevin.dev.donorverifier.models.api_response.NewsResponse;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class News extends AppCompatActivity {

    SwipeRefreshLayout pullToRefresh;
    ListView lvNewsList;
    Realm realm;
    ArrayList<app.kevin.dev.donorverifier.models.News> newsList;
    TextView lblLoading;
    ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        realm = UserFn.getRealmInstance(this);

        AppDrawerItemClickListener.prepareAppDrawer(this);

        pullToRefresh = findViewById(R.id.pullToRefresh);
        lvNewsList = findViewById(R.id.announcementsList);

        lblLoading = findViewById(R.id.lblLoading);
        pbLoading = findViewById(R.id.pbLoading);

        newsList = retriveFromDB();
        if(newsList.size() == 0){
            lblLoading.setVisibility(View.VISIBLE);
            pbLoading.setVisibility(View.VISIBLE);
            fetchNews(new CallbackWithResponse() {
                @Override
                public void execute(@Nullable JSONObject response) {
                    lblLoading.setVisibility(View.INVISIBLE);
                    pbLoading.setVisibility(View.INVISIBLE);
                    NewsResponse response1 = UserFn.gson.fromJson(response.toString(),NewsResponse.class);
                    saveToDB(response1.getData());
                    Collections.reverse(response1.getData());
                    loadNews(response1.getData());
                }
            });
        }else{
            loadNews(newsList);
        }

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchNews(new CallbackWithResponse() {
                    @Override
                    public void execute(@Nullable JSONObject response) {
                        NewsResponse response1 = UserFn.gson.fromJson(response.toString(),NewsResponse.class);
                        saveToDB(response1.getData());
                        Collections.reverse(response1.getData());
                        loadNews(response1.getData());
                        pullToRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void saveToDB(ArrayList<app.kevin.dev.donorverifier.models.News> data) {
        realm.beginTransaction();
        for(app.kevin.dev.donorverifier.models.News news : data){
            realm.copyToRealmOrUpdate(news);
        }
        realm.commitTransaction();
    }

    private ArrayList<app.kevin.dev.donorverifier.models.News> retriveFromDB(){


        RealmResults<app.kevin.dev.donorverifier.models.News> result = realm.where(app.kevin.dev.donorverifier.models.News.class).findAll();
        result.sort("id",Sort.DESCENDING);
        ArrayList<app.kevin.dev.donorverifier.models.News> list = new ArrayList<>();
        list.addAll(result);
        Collections.reverse(list);
        return list;
    }

    private void fetchNews(final CallbackWithResponse callback) {
        String url = UserFn.url(UserFn.API_NEWS);
        Api.call(this, url, new CallbackWithResponse() {
            @Override
            public void execute(@Nullable JSONObject response) {
                Toast.makeText(News.this, "News downloaded.", Toast.LENGTH_SHORT).show();
                callback.execute(response);
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
        lvNewsList.setAdapter(newsAdapter);
    }

    private void openPreview(app.kevin.dev.donorverifier.models.News news) {
        Intent intent = new Intent(this,NewsPreview.class);
        intent.putExtra("id",String.valueOf(news.getId()));
        startActivity(intent);
    }
}
