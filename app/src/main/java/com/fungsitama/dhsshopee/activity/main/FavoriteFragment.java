package com.fungsitama.dhsshopee.activity.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fungsitama.dhsshopee.util.SessionManager;
import com.fungsitama.dhsshopee.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FavoriteFragment extends Fragment {
    SessionManager manager;
    private static String vtoken,vusername,vtypeLogin,vtypeUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        CardView menu_all = (CardView) view.findViewById(R.id.menu_all);
        CardView menu_agent = (CardView) view.findViewById(R.id.menu_agent);
        CardView menu_ra = (CardView) view.findViewById(R.id.menu_ra);
        CardView menu_wh = (CardView) view.findViewById(R.id.menu_wh);
        CardView menu_wh_in = (CardView) view.findViewById(R.id.menu_wh_in);
        CardView menu_flight = (CardView) view.findViewById(R.id.menu_flight);
        TextView tittle = (TextView) view.findViewById(R.id.tittle);

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

        return view;
    }
}