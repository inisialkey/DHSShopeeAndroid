package com.fungsitama.dhsshopee.activity.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatButton;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fungsitama.dhsshopee.activity.login.LoginActivity;
import com.fungsitama.dhsshopee.activity.setting.SettingActivity;
import com.fungsitama.dhsshopee.util.SessionManager;
import com.fungsitama.dhsshopee.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AccountFragment extends Fragment {
    private Handler mHandler;
    SessionManager manager;
    private static String vtoken,vusername,vtypeLogin,vtypeUser;
    private TextView txtName, txtType,textservice,textlanguage;
    private AppCompatButton logout;

    private LinearLayout ly_transaksi,ly_klaimBarang,ly_listKlaimBarang,topup,ly_listSudahAmbil,ly_listAcceptInRa,ly_listRejectInRa,ly_listHoldInRa;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        manager = new SessionManager();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        vtoken = defaultSharedPreferences.getString("token", null);
        vusername = defaultSharedPreferences.getString("username", null);
        vtypeLogin = defaultSharedPreferences.getString("typeLogin", null);
        vtypeUser = defaultSharedPreferences.getString("typeUser", null);
        logout = (AppCompatButton)view.findViewById(R.id.logout);

        txtName = (TextView) view.findViewById(R.id.username);
        txtType = (TextView) view.findViewById(R.id.type);
        textservice = (TextView) view.findViewById(R.id.bluetooth);

        txtName.setText(vusername);
        txtType.setText(vtypeUser);

        Log.d("TAG", "onCreateViewData: " + vusername + " - " + vtypeUser);

        topup = (LinearLayout) view.findViewById(R.id.topup);
        ly_transaksi = (LinearLayout) view.findViewById(R.id.ly_transaksi);
        ly_klaimBarang = (LinearLayout) view.findViewById(R.id.ly_klaimBarang);
        ly_listKlaimBarang = (LinearLayout) view.findViewById(R.id.ly_listKlaimBarang);
        ly_listSudahAmbil = (LinearLayout) view.findViewById(R.id.ly_listSudahAmbil);

        ly_listAcceptInRa = (LinearLayout) view.findViewById(R.id.ly_listAcceptInRa);
        ly_listRejectInRa = (LinearLayout) view.findViewById(R.id.ly_listRejectInRa);
        ly_listHoldInRa = (LinearLayout) view.findViewById(R.id.ly_listHoldInRa);

        /*if (vtypeUser.equals("AGENT")){
            topup.setVisibility(View.VISIBLE);
            ly_klaimBarang.setVisibility(View.VISIBLE);
            ly_listKlaimBarang.setVisibility(View.VISIBLE);
            ly_listSudahAmbil.setVisibility(View.VISIBLE);

        }else{
            topup.setVisibility(View.GONE);
            ly_klaimBarang.setVisibility(View.GONE);
            ly_listKlaimBarang.setVisibility(View.GONE);
            ly_listSudahAmbil.setVisibility(View.GONE);
        }

        if (vtypeUser.equals("RA")){
            topup.setVisibility(View.GONE);
            ly_listAcceptInRa.setVisibility(View.GONE);
            ly_listRejectInRa.setVisibility(View.GONE);
            ly_listHoldInRa.setVisibility(View.GONE);
        }*/
        topup.setVisibility(View.GONE);
        ly_klaimBarang.setVisibility(View.GONE);
        ly_listKlaimBarang.setVisibility(View.GONE);
        ly_listSudahAmbil.setVisibility(View.GONE);

        logout.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public final void onClick(View it) {
                Logout();
            }
        }));

        textservice.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public final void onClick(View it) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        }));

        return view;
    }
    private void Logout(){
        new SweetAlertDialog(getActivity(), 3).setTitleText("Yakin akan keluar? ")
                .setContentText("").setCancelText("Cancel")
                .setConfirmText("Yes")
                .showCancelButton(true).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                //resetInstanceId();
                SharedPreferences.Editor edit = getActivity().getSharedPreferences("mypreferences", 0).edit();
                manager.setPreferences(getActivity(), NotificationCompat.CATEGORY_STATUS, "0");
                edit.clear();
                edit.commit();


                Intent intent = new Intent("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.HOME");
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finishAffinity();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                sweetAlertDialog.cancel();
            }
        }).show();
    }
}