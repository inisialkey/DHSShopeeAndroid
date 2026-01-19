package com.fungsitama.dhsshopee.activity.dashboard.MENU;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fungsitama.dhsshopee.util.SessionManager;
import com.fungsitama.dhsshopee.R;
import com.fungsitama.dhsshopee.activity.driver.KENDARAAN.DaftarPengirimanActivity;
import com.fungsitama.dhsshopee.activity.driver.KENDARAAN.DaftarKendaraanActivity;
import com.fungsitama.dhsshopee.activity.driver.KENDARAAN.DaftarReturActivity;
import com.fungsitama.dhsshopee.activity.driver.ORDER.OrderBaruActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MenuSupirFragment extends Fragment {

    SessionManager manager;
    private static String vtoken,vusername,vtypeLogin,vtypeUser;

    public MenuSupirFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_supir, container, false);

        manager = new SessionManager();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        vtoken = defaultSharedPreferences.getString("token", null);
        vusername = defaultSharedPreferences.getString("username", null);
        vtypeLogin = defaultSharedPreferences.getString("typeLogin", null);
        vtypeUser = defaultSharedPreferences.getString("typeUser", null);

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        TextView l_user = (TextView) view.findViewById(R.id.user);
        TextView l_date = (TextView) view.findViewById(R.id.date);
        l_user.setText(vusername);
        l_date.setText(currentDate);

        CardView cvOrderBaru = (CardView) view.findViewById(R.id.cv_orderBaru);
        CardView cvTambahKendaraan = (CardView) view.findViewById(R.id.cv_TambahKendaraan);
        CardView cvDaftarTruck = (CardView) view.findViewById(R.id.cv_DaftarTruck);
        CardView cvDaftarRetur = (CardView) view.findViewById(R.id.cv_DaftarRetur);

        cvOrderBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                edit.putString("vtoken", vtoken);
                edit.putString("vusername", vusername);
                edit.putString("vtypeLogin", vtypeLogin);
                edit.putString("vtypeUser", vtypeUser);
                edit.commit();
                startActivity(new Intent(getActivity(), OrderBaruActivity.class));
            }
        });

        cvTambahKendaraan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                edit.putString("vtoken", vtoken);
                edit.putString("vusername", vusername);
                edit.putString("vtypeLogin", vtypeLogin);
                edit.putString("vtypeUser", vtypeUser);
                edit.commit();
                startActivity(new Intent(getActivity(), DaftarKendaraanActivity.class));
            }
        });

        cvDaftarTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                edit.putString("vtoken", vtoken);
                edit.putString("vusername", vusername);
                edit.putString("vtypeLogin", vtypeLogin);
                edit.putString("vtypeUser", vtypeUser);
                edit.commit();
                startActivity(new Intent(getActivity(), DaftarPengirimanActivity.class));
            }
        });

        cvDaftarRetur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                edit.putString("vtoken", vtoken);
                edit.putString("vusername", vusername);
                edit.putString("vtypeLogin", vtypeLogin);
                edit.putString("vtypeUser", vtypeUser);
                edit.commit();
                startActivity(new Intent(getActivity(), DaftarReturActivity.class));
            }
        });

        return view;
    }
}