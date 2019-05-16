package com.jew.coree.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.jew.coree.R;
import com.jew.coree.view.fragment.WebViewFragment;

public class BottomBarActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private BottomNavigationView navView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            resetToDefaultIcon();//重置到默认不选中图片
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    item.setIcon(R.mipmap.ic_launcher_round);
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    item.setIcon(R.mipmap.ic_launcher_round);
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    item.setIcon(R.mipmap.ic_launcher_round);
                    mTextMessage.setText(R.string.title_notifications);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fl_container, new WebViewFragment()).commitAllowingStateLoss();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);
        navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setItemIconTintList(null);
        navView.getMenu().findItem(R.id.navigation_home).setChecked(true);
        navView.setSelectedItemId(R.id.navigation_home);
    }

    private void resetToDefaultIcon() {
        MenuItem home =  navView.getMenu().findItem(R.id.navigation_home);
        MenuItem dashboard =  navView.getMenu().findItem(R.id.navigation_dashboard);
        MenuItem notifications =  navView.getMenu().findItem(R.id.navigation_notifications);
        home.setIcon(R.drawable.ic_home_black_24dp);
        dashboard.setIcon(R.drawable.ic_dashboard_black_24dp);
        notifications.setIcon(R.drawable.ic_notifications_black_24dp);
    }

}
