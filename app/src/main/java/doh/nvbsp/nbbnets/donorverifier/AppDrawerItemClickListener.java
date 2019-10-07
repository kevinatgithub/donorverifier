package doh.nvbsp.nbbnets.donorverifier;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import doh.nvbsp.nbbnets.donorverifier.libs.Session;
import doh.nvbsp.nbbnets.donorverifier.libs.UserFn;
import doh.nvbsp.nbbnets.donorverifier.models.User;

public class AppDrawerItemClickListener implements NavigationView.OnNavigationItemSelectedListener {
    private Activity activity;


    public AppDrawerItemClickListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id){
            case R.id.news:
                Intent newsIntent = new Intent(activity,News.class);
                activity.startActivity(newsIntent);
                activity.finish();
                break;
            case R.id.verifier:
                Intent verifierIntent = new Intent(activity,DonorVerifier.class);
                activity.startActivity(verifierIntent);
                activity.finish();
                break;
//            case R.id.upload:
//                Intent uploadIntent = new Intent(activity,Upload.class);
//                activity.startActivity(uploadIntent);
//                activity.finish();
//                break;
            case R.id.download:
                Intent downloadIntent = new Intent(activity,Download.class);
                activity.startActivity(downloadIntent);
                activity.finish();
                break;
            case R.id.statistics:
                Intent statisticsIntent = new Intent(activity,Statistics.class);
                activity.startActivity(statisticsIntent);
                activity.finish();
                break;
            case R.id.user_manual:
                Intent userManualIntent = new Intent(activity,Manual.class);
                activity.startActivity(userManualIntent);
                activity.finish();
                break;
            case R.id.check_updates:
                Intent checkUpdatesIntent = new Intent(activity,CheckUpdates.class);
                activity.startActivity(checkUpdatesIntent);
                activity.finish();
                break;
            case R.id.logout:
                logout();
                break;
        }
        return false;
    }

    private void logout() {
        Session.delete(activity,"user");
        Intent intent = new Intent(activity, Login.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void prepareAppDrawer(AppCompatActivity activity){
        Toolbar toolbar = activity.findViewById(R.id.app_bar);
        activity.setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = activity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(activity,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_closed);
        drawerLayout.addDrawerListener(toggle);

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {

            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });

        NavigationView navView = activity.findViewById(R.id.nav_view);
        View headerView = navView.getHeaderView(0);

        String str = Session.get(activity,"user",null);

        if(str != null){
            TextView txt_drawer_user_fullname = headerView.findViewById(R.id.txt_drawer_user_fullname);
            TextView txt_drawer_user_designation = headerView.findViewById(R.id.txt_drawer_user_designation);
            User user = UserFn.gson.fromJson(str,User.class);
            txt_drawer_user_fullname.setText(user.getUser_fname() + " " + user.getUser_mname() + " " + user.getUser_lname());
            txt_drawer_user_designation.setText(user.getUser_id());
        }


        navView.setNavigationItemSelectedListener(new AppDrawerItemClickListener(activity));

    }
}
