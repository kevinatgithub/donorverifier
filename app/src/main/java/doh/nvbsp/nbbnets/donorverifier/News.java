package doh.nvbsp.nbbnets.donorverifier;

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

import doh.nvbsp.nbbnets.donorverifier.adapters.NewsAdapter;
import doh.nvbsp.nbbnets.donorverifier.adapters.NewsAdapterButtonClickListener;
import doh.nvbsp.nbbnets.donorverifier.libs.Api;
import doh.nvbsp.nbbnets.donorverifier.libs.UserFn;
import doh.nvbsp.nbbnets.donorverifier.models.CallbackWithResponse;
import doh.nvbsp.nbbnets.donorverifier.models.api_response.NewsResponse;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class News extends AppCompatActivity {

    SwipeRefreshLayout pullToRefresh;
    ListView lvNewsList;
    Realm realm;
    ArrayList<doh.nvbsp.nbbnets.donorverifier.models.News> newsList;
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

    private void saveToDB(ArrayList<doh.nvbsp.nbbnets.donorverifier.models.News> data) {
        realm.beginTransaction();
        for(doh.nvbsp.nbbnets.donorverifier.models.News news : data){
            realm.copyToRealmOrUpdate(news);
        }
        realm.commitTransaction();
    }

    private ArrayList<doh.nvbsp.nbbnets.donorverifier.models.News> retriveFromDB(){


        RealmResults<doh.nvbsp.nbbnets.donorverifier.models.News> result = realm.where(doh.nvbsp.nbbnets.donorverifier.models.News.class).findAll();
        result.sort("id",Sort.DESCENDING);
        ArrayList<doh.nvbsp.nbbnets.donorverifier.models.News> list = new ArrayList<>();
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

    private void loadNews(ArrayList<doh.nvbsp.nbbnets.donorverifier.models.News> announcements) {
        NewsAdapter newsAdapter = new NewsAdapter(this, announcements, new NewsAdapterButtonClickListener() {
            @Override
            public void onClick(doh.nvbsp.nbbnets.donorverifier.models.News news) {
                openPreview(news);
            }
        });
        lvNewsList.setAdapter(newsAdapter);
    }

    private void openPreview(doh.nvbsp.nbbnets.donorverifier.models.News news) {
        Intent intent = new Intent(this,NewsPreview.class);
        intent.putExtra("id",String.valueOf(news.getId()));
        startActivity(intent);
    }
}
