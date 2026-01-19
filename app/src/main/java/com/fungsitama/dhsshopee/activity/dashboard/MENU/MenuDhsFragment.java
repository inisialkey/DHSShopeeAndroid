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

import com.fungsitama.dhsshopee.R;
import com.fungsitama.dhsshopee.activity.dhs.DaftarMuatBarangActivity;
import com.fungsitama.dhsshopee.activity.dhs.Loading.DaftarLoadingActivity;
import com.fungsitama.dhsshopee.activity.dhs.Loading.TambahMuatBarangActivity;
import com.fungsitama.dhsshopee.activity.dhs.Unloading.DaftarUnloadingActivity;
import com.fungsitama.dhsshopee.util.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MenuDhsFragment extends Fragment {


    SessionManager manager;
    private static String vtoken,vusername,vtypeLogin,vtypeUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_dhs, container, false);

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

        CardView cvTambahMuatBarang = (CardView) view.findViewById(R.id.cv_TambahMuatBarang);
        CardView cvDaftarMuatBarang = (CardView) view.findViewById(R.id.cv_DaftarMuatBarang);
        CardView cvUnloading = (CardView) view.findViewById(R.id.cv_Unloading);

        cvTambahMuatBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                edit.putString("vtoken", vtoken);
                edit.putString("vusername", vusername);
                edit.putString("vtypeLogin", vtypeLogin);
                edit.putString("vtypeUser", vtypeUser);
                edit.commit();
                startActivity(new Intent(getActivity(), TambahMuatBarangActivity.class));
            }
        });

        cvDaftarMuatBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                edit.putString("vtoken", vtoken);
                edit.putString("vusername", vusername);
                edit.putString("vtypeLogin", vtypeLogin);
                edit.putString("vtypeUser", vtypeUser);
                edit.commit();
                startActivity(new Intent(getActivity(), DaftarLoadingActivity.class));
            }
        });


        cvUnloading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                edit.putString("vtoken", vtoken);
                edit.putString("vusername", vusername);
                edit.putString("vtypeLogin", vtypeLogin);
                edit.putString("vtypeUser", vtypeUser);
                edit.commit();
                startActivity(new Intent(getActivity(), DaftarUnloadingActivity.class));
            }
        });

        return view;

    }
}