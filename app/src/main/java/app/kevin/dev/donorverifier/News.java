package app.kevin.dev.donorverifier;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import app.kevin.dev.donorverifier.libs.Session;
import app.kevin.dev.donorverifier.libs.UserFn;
import app.kevin.dev.donorverifier.models.User;

public class News extends AppCompatActivity {

    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_closed);
        drawerLayout.addDrawerListener(toggle);

        provideDrawerOnClickListeners();
    }

    private void provideDrawerOnClickListeners() {
        NavigationView navView = findViewById(R.id.nav_view);
        View headerView = navView.getHeaderView(0);

        TextView txt_drawer_user_fullname = headerView.findViewById(R.id.txt_drawer_user_fullname);
        TextView txt_drawer_user_designation = headerView.findViewById(R.id.txt_drawer_user_designation);
        String str = Session.get(this,"user",null);
        User user = UserFn.gson.fromJson(str,User.class);
        txt_drawer_user_fullname.setText(user.getUser_fname() + " " + user.getUser_mname() + " " + user.getUser_lname());
        txt_drawer_user_designation.setText(user.getUser_id());

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id){
                    case R.id.settings:

                        break;
                    case R.id.logout:

                        break;
                }
                return false;
            }
        });
    }
}
