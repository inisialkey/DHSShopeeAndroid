package com.fungsitama.dhsshopee.activity.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import com.fungsitama.dhsshopee.activity.dashboard.HOME.Home_Driver_Fragment;
import com.fungsitama.dhsshopee.activity.dashboard.MENU.MenuDhsFragment;
import com.fungsitama.dhsshopee.util.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.fungsitama.dhsshopee.R;

public class NavMenuActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private Handler mHandler;
    SessionManager manager;
    private static String vtoken, vusername, vtypeLogin, vtypeUser, codeMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_menu);

        manager = new SessionManager();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        vtoken = defaultSharedPreferences.getString("token", null);
        vusername = defaultSharedPreferences.getString("username", null);
        vtypeLogin = defaultSharedPreferences.getString("typeLogin", null);
        vtypeUser = defaultSharedPreferences.getString("typeUser", null);
        codeMaster = defaultSharedPreferences.getString("codeMaster", null);


         SharedPreferences.Editor sp3 = PreferenceManager.getDefaultSharedPreferences(NavMenuActivity.this).edit();
         sp3.putString("vtoken", vtoken);
         sp3.putString("vusername", vusername);
         sp3.putString("vtypeLogin", vtypeLogin);
         sp3.putString("typeUser", vtypeUser);
         sp3.putString("codeMaster", codeMaster);
         sp3.commit();
         loadFragment(new Home_Driver_Fragment());


        // inisialisasi BottomNavigaionView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bn_main);
        // beri listener pada saat item/menu bottomnavigation terpilih
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.getMenu().getItem(1).setTitle("Menu");

    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();

            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;

        int itemId = menuItem.getItemId();

        if (itemId == R.id.home_menu) {

            savePreferences();

            fragment = new Home_Driver_Fragment();

        } else if (itemId == R.id.activity_menu) {

            savePreferences();

            fragment = new MenuDhsFragment();

        } else if (itemId == R.id.profile_menu) {

            fragment = new AccountFragment();
        }

        return loadFragment(fragment);
    }

    private void savePreferences() {
        SharedPreferences.Editor sp =
                PreferenceManager.getDefaultSharedPreferences(this).edit();

        sp.putString("vtoken", vtoken);
        sp.putString("vusername", vusername);
        sp.putString("vtypeLogin", vtypeLogin);
        sp.putString("typeUser", vtypeUser);
        sp.putString("codeMaster", codeMaster);

        sp.apply();
    }

    public void appExit() {
        //finish();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onBackPressed() {
        // super.onBackPressed();
        appExit();
    }
}
