package com.fungsitama.dhsshopee.activity.main;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.cardview.widget.CardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fungsitama.dhsshopee.util.DataHolder;
import com.fungsitama.dhsshopee.util.SessionManager;
import com.fungsitama.dhsshopee.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SessionManager manager;
    private static String vtoken, vusername, vtypeLogin, vtypeUser,codeMaster;
    CarouselView carouselView;
    int[] sampleImages = {};
    private LinearLayout lvAgent, lvRA, lvWH, lvGH, lvAP;
    private TextView txt_ra_smu_in, txt_ra_koli_in, txt_ra_berat_in,
            txt_ra_smu_out, txt_ra_koli_out, txt_ra_berat_out,
            txt_ra_smu_outstanding, txt_ra_koli_outstanding, txt_ra_berat_outstanding,
            txt_wh_smu_in, txt_wh_koli_in, txt_wh_berat_in,
            txt_wh_smu_out, txt_wh_koli_out, txt_wh_berat_out,
            txt_wh_smu_outstanding, txt_wh_koli_outstanding, txt_wh_berat_outstanding,
            txt_agent_smu_in, txt_agent_koli_in, txt_agent_berat_in,
            txt_agent_smu_out, txt_agent_koli_out, txt_agent_berat_out,
            txt_agent_smu_outstanding, txt_agent_koli_outstanding, txt_agent_berat_outstanding,
            txt_gh_smu_in, txt_gh_koli_in, txt_gh_berat_in,
            txt_gh_smu_out, txt_gh_koli_out, txt_gh_berat_out,
            txt_gh_smu_outstanding, txt_gh_koli_outstanding, txt_gh_berat_outstanding,
            txt_ap_cw, txt_ap_value, txt_ap_income;

    private String str_ra_smu_in, str_ra_koli_in, str_ra_berat_in,
            str_ra_smu_out, str_ra_koli_out, str_ra_berat_out,
            str_ra_smu_outstanding, str_ra_koli_outstanding, str_ra_berat_outstanding,
            str_wh_smu_in, str_wh_koli_in, str_wh_berat_in,
            str_wh_smu_out, str_wh_koli_out, str_wh_berat_out,
            str_wh_smu_outstanding, str_wh_koli_outstanding, str_wh_berat_outstanding,
            str_agent_smu_in, str_agent_koli_in, str_agent_berat_in,
            str_agent_smu_out, str_agent_koli_out, str_agent_berat_out,
            str_agent_smu_outstanding, str_agent_koli_outstanding, str_agent_berat_outstanding,
            str_gh_smu_in, str_gh_koli_in, str_gh_berat_in,
            str_gh_smu_out, str_gh_koli_out, str_gh_berat_out,
            str_gh_smu_outstanding, str_gh_koli_outstanding, str_gh_berat_outstanding,
            str_ap_cw, str_ap_value, str_ap_income;
    private Calendar calendar;
    private Calendar calendar2;
    private TextView dateView;
    private TextView dateView2;
    private TextView dateView3;
    private int day;
    private int day2;
    private int month;
    private int month2;
    private int year;
    private int year2;
    private Switch switchButton;
    private TextView textView;
    private String switchOff = "Domestik";
    private String switchOn = "Internasional";
    private TextView tvDt1, tvDt2;
    //public SwipeRefreshLayout swipeRefreshLayout;
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
            showDate(i, i2 + 1, i3);
        }
    };
    private DatePickerDialog.OnDateSetListener myDateListener2 = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
            showDate2(i, i2 + 1, i3);
        }
    };

    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    private ImageButton btndT1;
    private ImageButton btndT2;
    private ImageButton tbrefresh;
    private Handler mHandler = new Handler();
    private CheckBox cb_income,cb_outgoing, cb_incoming, cb_domestik, cb_internasional;
    private EditText etAsal;
    private Boolean typeKirim;

    private Spinner sp_outgoing,sp_incoming;
    private String[] val_outgoing = {
            "Shipper",
            "RA",
            "WH",
            "Manifest"
    };
    private String[] val_incoming = {
            "Manifest",
            "Transit",
            "Release"
    };
    private String s_sp_outgoing,s_sp_incoming;
    private SearchableSpinner spinAP;
    private ArrayList<String> alspinAP;
    public JSONArray rOriginAP;
    private LinearLayout ll_value,ll_income;

    private LinearLayout lyMenungguVefrifikasi, lyTotalChekout, lyTotalOutstanding;
    private CardView cv_G,cv_H,cv_I;
    private CardView cv_A,cv_B,cv_C;
    private CardView cv_D,cv_E,cv_F;
    private CardView cv_J,cv_K,cv_L;
    private Button filter;
    String txt_Total_Outstanding,txt_TotalChekout,txt_MenungguVrifikasi;
    String txt_TotalCheckIn,txt_TotalCheckOut,txt_TotalOutstanding;
    private Boolean val_str_outgoing,val_str_incoming, val_str_domestik, val_str_internasioanl;
    private TextView txt_outgoing, txt_incoming,txt_sisasaldo;
    private int sisaSaldo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        getActivity().getWindow().setStatusBarColor(Color.BLUE);
        carouselView = (CarouselView) view.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

        manager = new SessionManager();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        vtoken = defaultSharedPreferences.getString("token", null);
        vusername = defaultSharedPreferences.getString("username", null);
        vtypeLogin = defaultSharedPreferences.getString("typeLogin", null);
        vtypeUser = defaultSharedPreferences.getString("typeUser", null);
        codeMaster = defaultSharedPreferences.getString("codeMaster", null);
        txt_sisasaldo = (TextView) view.findViewById(R.id.sisasaldo);
        //Toast.makeText(getActivity(), "Code Master: "+ codeMaster +" "+ vtypeUser, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append(getString(R.string.API));
                    sb.append("modules/private/m_agent/my");
                    StringRequest r1 = new StringRequest(1, sb.toString(), new Response.Listener<String>() {
                        public void onResponse(String str) {
                            try {
                                JSONObject jSONObject = new JSONObject(str).getJSONObject("data");
                                sisaSaldo = jSONObject.getInt("saldo");
                                Locale localeID = new Locale("in", "ID");
                                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                                String Saldo = formatRupiah.format(sisaSaldo);
                                txt_sisasaldo.setText(Saldo.substring(0, Saldo.length()).replace(",00", ""));

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Context applicationContext = getContext();
                                StringBuilder sb = new StringBuilder();
                                sb.append("Error: ");
                                sb.append(e.getMessage());
                                Toast.makeText(applicationContext, sb.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError volleyError) {
                            volleyError.printStackTrace();
                        }
                    }) {
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap hashMap = new HashMap();
                            StringBuilder sb = new StringBuilder();
                            sb.append("Bearer ");
                            sb.append(vtoken);
                            hashMap.put("Authorization", sb.toString());
                            return hashMap;
                        }
                    };
                    Volley.newRequestQueue(getContext()).add(r1);
                } catch (Exception unused) {
                }
            }
        }, 1000);
        //Agen-Chekin
        cv_G = (CardView) view.findViewById(R.id.g);
        cv_H = (CardView) view.findViewById(R.id.h);
        cv_I = (CardView) view.findViewById(R.id.i);
        cv_G.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_MenungguVrifikasi = "Menunggu Verifikasi";
            }
        });
        cv_H.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_Total_Outstanding = "Total Chek Out";
            }
        });
        cv_I.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_TotalChekout = "Total Outstanding";
            }
        });

        //RA
        cv_A = (CardView) view.findViewById(R.id.a);
        cv_B = (CardView) view.findViewById(R.id.b);
        cv_C = (CardView) view.findViewById(R.id.c);
        cv_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_TotalCheckIn = "Total Check In";
            }
        });
        cv_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_TotalCheckOut = "Total Check Out";
            }
        });
        cv_C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_TotalOutstanding = "Total Outstanding";
            }
        });

        //WH
        cv_D = (CardView) view.findViewById(R.id.d);
        cv_E = (CardView) view.findViewById(R.id.e);
        cv_F = (CardView) view.findViewById(R.id.f);
        cv_D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_TotalCheckIn = "Total Check In";
            }
        });
        cv_E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_TotalCheckOut = "Total Check Out";
            }
        });
        cv_F.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_TotalOutstanding = "Total Outstanding";
            }
        });

        //GH
        cv_J = (CardView) view.findViewById(R.id.j);
        cv_K = (CardView) view.findViewById(R.id.k);
        cv_L = (CardView) view.findViewById(R.id.l);
        cv_J.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_TotalCheckIn = "Total Loading";
                //getInentListGh(txt_TotalCheckIn);
            }
        });
        cv_K.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_TotalCheckOut = "Total Unloading";
                //getInentListGh(txt_TotalCheckOut);
            }
        });
        cv_L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_TotalOutstanding = "Total Outstanding";
               //getInentListGh(txt_TotalOutstanding);
            }
        });


        lvAgent = (LinearLayout) view.findViewById(R.id.lvAgent);
        lvRA = (LinearLayout) view.findViewById(R.id.lvRA);
        lvWH = (LinearLayout) view.findViewById(R.id.lvWH);
        lvGH = (LinearLayout) view.findViewById(R.id.lvGH);
        lvAP = (LinearLayout) view.findViewById(R.id.lvAP);
        tvDt1 = (TextView) getActivity().findViewById(R.id.dt1awal);
        tvDt2 = (TextView) getActivity().findViewById(R.id.dt2akhir);
        txt_ra_smu_in = (TextView) view.findViewById(R.id.ra_smu_in);
        txt_ra_koli_in = (TextView) view.findViewById(R.id.ra_koli_in);
        txt_ra_berat_in = (TextView) view.findViewById(R.id.ra_berat_in);
        txt_ra_smu_out = (TextView) view.findViewById(R.id.ra_smu_out);
        txt_ra_koli_out = (TextView) view.findViewById(R.id.ra_koli_out);
        txt_ra_berat_out = (TextView) view.findViewById(R.id.ra_berat_out);
        txt_ra_smu_outstanding = (TextView) view.findViewById(R.id.ra_smu_outstanding);
        txt_ra_koli_outstanding = (TextView) view.findViewById(R.id.ra_koli_outstanding);
        txt_ra_berat_outstanding = (TextView) view.findViewById(R.id.ra_berat_outstanding);

        txt_wh_smu_in = (TextView) view.findViewById(R.id.wh_smu_in);
        txt_wh_koli_in = (TextView) view.findViewById(R.id.wh_koli_in);
        txt_wh_berat_in = (TextView) view.findViewById(R.id.wh_berat_in);
        txt_wh_smu_out = (TextView) view.findViewById(R.id.wh_smu_out);
        txt_wh_koli_out = (TextView) view.findViewById(R.id.wh_koli_out);
        txt_wh_berat_out = (TextView) view.findViewById(R.id.wh_berat_out);
        txt_wh_smu_outstanding = (TextView) view.findViewById(R.id.wh_smu_outstanding);
        txt_wh_koli_outstanding = (TextView) view.findViewById(R.id.wh_koli_outstanding);
        txt_wh_berat_outstanding = (TextView) view.findViewById(R.id.wh_berat_outstanding);

        txt_agent_smu_in = (TextView) view.findViewById(R.id.agent_smu_in);
        txt_agent_koli_in = (TextView) view.findViewById(R.id.agent_koli_in);
        txt_agent_berat_in = (TextView) view.findViewById(R.id.agent_berat_in);
        txt_agent_smu_out = (TextView) view.findViewById(R.id.agent_smu_out);
        txt_agent_koli_out = (TextView) view.findViewById(R.id.agent_koli_out);
        txt_agent_berat_out = (TextView) view.findViewById(R.id.agent_berat_out);
        txt_agent_smu_outstanding = (TextView) view.findViewById(R.id.agent_smu_outstanding);
        txt_agent_koli_outstanding = (TextView) view.findViewById(R.id.agent_koli_outstanding);
        txt_agent_berat_outstanding = (TextView) view.findViewById(R.id.agent_berat_outstanding);

        txt_gh_smu_in = (TextView) view.findViewById(R.id.gh_smu_in);
        txt_gh_koli_in = (TextView) view.findViewById(R.id.gh_koli_in);
        txt_gh_berat_in = (TextView) view.findViewById(R.id.gh_berat_in);
        txt_gh_smu_out = (TextView) view.findViewById(R.id.gh_smu_out);
        txt_gh_koli_out = (TextView) view.findViewById(R.id.gh_koli_out);
        txt_gh_berat_out = (TextView) view.findViewById(R.id.gh_berat_out);
        txt_gh_smu_outstanding = (TextView) view.findViewById(R.id.gh_smu_outstanding);
        txt_gh_koli_outstanding = (TextView) view.findViewById(R.id.gh_koli_outstanding);
        txt_gh_berat_outstanding = (TextView) view.findViewById(R.id.gh_berat_outstanding);

        txt_ap_cw = (TextView) view.findViewById(R.id.tv_ap_cw);
        txt_ap_value = (TextView) view.findViewById(R.id.tv_ap_value);
        txt_ap_income = (TextView) view.findViewById(R.id.tv_ap_income);

        tbrefresh = (ImageButton) view.findViewById(R.id.refresh);
        dateView = (TextView) view.findViewById(R.id.dt1awal);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dateView2 = (TextView) view.findViewById(R.id.dt2akhir);
        dateView3 = (TextView) view.findViewById(R.id.dt3send);
        calendar2 = Calendar.getInstance();
        year2 = calendar.get(Calendar.YEAR);
        month2 = calendar.get(Calendar.MONTH);
        day2 = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day - 1);
        showDate2(year2, month2 + 1, day2);

        spinAP=(SearchableSpinner)view.findViewById(R.id.spinAP);
        etAsal=(EditText) view.findViewById(R.id.et_Asal);
        cb_domestik = (CheckBox) view.findViewById(R.id.cb_domestik);
        cb_internasional = (CheckBox) view.findViewById(R.id.cb_internasional);
        cb_outgoing = (CheckBox) view.findViewById(R.id.cb_outgoing);
        cb_incoming = (CheckBox) view.findViewById(R.id.cb_incoming);
        cb_income =(CheckBox) view.findViewById(R.id.cb_income);
        TextView l_user = (TextView) view.findViewById(R.id.user);
        TextView l_tools = (TextView) view.findViewById(R.id.tools);
        l_user.setText(vusername);
        filter= (Button)view.findViewById(R.id.filter);
        txt_outgoing = (TextView) view.findViewById(R.id.txt_outgoing);
        txt_incoming = (TextView) view.findViewById(R.id.txt_incoming);
        CardView entridata = (CardView) view.findViewById(R.id.entridata);
        CardView belum_bayar = (CardView) view.findViewById(R.id.belum_bayar);
        CardView histori = (CardView) view.findViewById(R.id.histori);
        CardView inra = (CardView) view.findViewById(R.id.inra);
        CardView loadingra = (CardView) view.findViewById(R.id.loadingra);
        CardView truckout = (CardView) view.findViewById(R.id.truckout);
        CardView loadingwh = (CardView) view.findViewById(R.id.loadingwh);
        CardView prepareoutwh = (CardView) view.findViewById(R.id.prepareoutwh);
        CardView outwh = (CardView) view.findViewById(R.id.outwh);
        CardView appron = (CardView) view.findViewById(R.id.appron);
        CardView loading = (CardView) view.findViewById(R.id.loading);
        CardView unloading = (CardView) view.findViewById(R.id.unloading);
        ll_value = (LinearLayout) view.findViewById(R.id.ll_value);
        ll_income = (LinearLayout) view.findViewById(R.id.ll_income);
        setCheckBoxListener();

        switchButton = (Switch) view.findViewById(R.id.switchButton);
        textView = (TextView) view.findViewById(R.id.textView);

        switchButton.setChecked(false);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textView.setText(switchOn);
                    typeKirim = Boolean.valueOf("false");
                } else {
                    textView.setText(switchOff);
                    typeKirim = Boolean.valueOf("true");
                }
            }
        });

        if (switchButton.isChecked()) {
            textView.setText(switchOn);
            typeKirim = Boolean.valueOf("false");
        } else {
            textView.setText(switchOff);
            typeKirim = Boolean.valueOf("true");
        }

        tbrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
                //Toast.makeText(getActivity(), "Refrseh Halaman", Toast.LENGTH_SHORT).show();
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAP();
                DataHolder.getInstance().setStatus(txt_ap_cw.getText().toString());
            }
        });
        btndT1 = (ImageButton) view.findViewById(R.id.dt1);

        btndT1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TanggalAwal();
            }
        });

        btndT2 = (ImageButton) view.findViewById(R.id.dt2);
        btndT2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TanggalAkhir();
            }
        });

        if (vtypeUser.equals("AGENT") || vtypeUser.equals("ADMIN_AGENT")) {
            l_tools.setText("Agent Tools");
            lvAgent.setVisibility(View.VISIBLE);
            lvRA.setVisibility(View.GONE);
            lvWH.setVisibility(View.GONE);
            lvGH.setVisibility(View.GONE);
            lvAP.setVisibility(View.GONE);
            getAgentIn();
            getAgentOut();
            getAgentOutStanding();

        } else if (vtypeUser.equals("HELPDESK") || vtypeUser.equals("HELP_DESK")) {
                l_tools.setText("Help Desk Tools");
                lvAgent.setVisibility(View.VISIBLE);
                lvRA.setVisibility(View.GONE);
                lvWH.setVisibility(View.GONE);
                lvGH.setVisibility(View.GONE);
                lvAP.setVisibility(View.GONE);
                getAgentIn();
                getAgentOut();
                getAgentOutStanding();

        }else if (vtypeUser.equals("RA") || vtypeUser.equals("ADMIN_RA")) {
            l_tools.setText("RA Tools");
            lvAgent.setVisibility(View.GONE);
            lvRA.setVisibility(View.VISIBLE);
            lvWH.setVisibility(View.GONE);
            lvGH.setVisibility(View.GONE);
            lvAP.setVisibility(View.GONE);
            getRAIn();
            getRAOut();
            getRAOutStanding();
        } else if (vtypeUser.equals("WAREHOUSE") || vtypeUser.equals("ADMIN_WAREHOUSE")) {
            l_tools.setText("WAREHOUSE Tools");
            lvAgent.setVisibility(View.GONE);
            lvRA.setVisibility(View.GONE);
            lvWH.setVisibility(View.VISIBLE);
            lvGH.setVisibility(View.GONE);
            lvAP.setVisibility(View.GONE);
            getWHIn();
            getWHOut();
            getWHOutStanding();
        } else if (vtypeUser.equals("GROUND HANDLING") || vtypeUser.equals("GROUND_HANDLING")) {
            l_tools.setText("GROUND HANDLING Tools");
            lvAgent.setVisibility(View.GONE);
            lvRA.setVisibility(View.GONE);
            lvWH.setVisibility(View.GONE);
            lvGH.setVisibility(View.VISIBLE);
            lvAP.setVisibility(View.GONE);
            getGHIn();
            getGHOut();
            getGHOutStanding();
        } else if (vtypeUser.equals("AP") || vtypeUser.equals("ADMIN_AP")) {
            l_tools.setText("SIGAP Tools");
            lvAgent.setVisibility(View.GONE);
            lvRA.setVisibility(View.GONE);
            lvWH.setVisibility(View.GONE);
            lvGH.setVisibility(View.GONE);
            lvAP.setVisibility(View.VISIBLE);
            alspinAP = new ArrayList<>();
            getOrigin();
            getAP();
        } else {
            l_tools.setText("SIGAP Tools");
            lvAgent.setVisibility(View.VISIBLE);
            lvRA.setVisibility(View.VISIBLE);
            lvWH.setVisibility(View.VISIBLE);
        }

        sp_outgoing = (Spinner) view.findViewById(R.id.sp_outgoing);
        sp_incoming = (Spinner) view.findViewById(R.id.sp_incoming);
        // inisialiasi Array Adapter dengan memasukkan string array di atas
        final ArrayAdapter<String> adapterO = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, val_outgoing);
        final ArrayAdapter<String> adapterI = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, val_incoming);
        // mengeset Array Adapter tersebut ke Spinner
        sp_outgoing.setAdapter(adapterO);
        sp_incoming.setAdapter(adapterI);
        // mengeset listener untuk mengetahui saat item dipilih
        sp_outgoing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
                s_sp_outgoing = adapterO.getItem(i).toString();
                txt_outgoing.setText(adapterO.getItem(i).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                s_sp_outgoing="";
                txt_outgoing.setText("");

            }
        });
        sp_incoming.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
                s_sp_incoming = adapterI.getItem(i).toString();
                txt_incoming.setText(adapterI.getItem(i).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                txt_incoming.setText("");
                s_sp_incoming = "";

            }
        });

        spinAP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                etAsal.setText(getVOrigin(position));


            }
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });


        findViews();
      //  mHandler = new Handler();
      //  mHandler.postDelayed(m_Runnable, 2000);

        return view;
    }


    private void findViews()
    {
        spinAP.setTitle("Pilih Airport");
    }

    private void getOrigin() {

        RequestQueue newRequestQueue = Volley.newRequestQueue(getContext());
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("page", 1);
            jSONObject.put("pageSize", 10000);
            JSONObject filters = new JSONObject();
            jSONObject.put("filters", filters);
            filters.put("apcode", vusername);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.API));
        sb.append("modules/private/m_airport/list");
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), jSONObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    rOriginAP = jSONObject.getJSONArray("data");

                    //getStudents(result);
                    for (int i = 0; i < rOriginAP.length(); i++) {
                        try {
                            jSONObject = rOriginAP.getJSONObject(i);
                            /*if(jSONObject.getString("apcode").equals(vusername)) {*/
                                ArrayList<String> arrayList = alspinAP;

                                StringBuilder sb = new StringBuilder();
                                sb.append(jSONObject.getString("code"));
                                sb.append(" - ");
                                sb.append(jSONObject.getString("name"));
                                sb.append(" - ");
                                sb.append(jSONObject.getString("nameCity"));
                                arrayList.add(sb.toString());
                            //}
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    spinAP.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, alspinAP));

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("Error: ", volleyError.getMessage());
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                StringBuilder sb = new StringBuilder();
                sb.append("Bearer ");
                sb.append(vtoken);
                hashMap.put("Authorization", sb.toString());
                return hashMap;
            }

        };
        newRequestQueue.add(r3);
    }

    private String getVOrigin(int i) {
        String str = "";
        try {
            str = rOriginAP.getJSONObject(i).getString("code");
            spinAP.setSelection(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    }
    private void setCheckBoxListener() {
        cb_domestik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_domestik.isChecked()) {
                   val_str_domestik = true;
                   // getAP();
                }else{
                   val_str_domestik = false;
                   // getAP();
                }
            }
        });
        cb_internasional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_internasional.isChecked()) {
                 val_str_internasioanl = true;
                   // getAP();
                }else{
                 val_str_internasioanl = false;
                   // getAP();
                }
            }
        });
        cb_outgoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    val_str_outgoing=true;
                    txt_outgoing.setText("Shipper");
                    sp_outgoing.setVisibility(View.VISIBLE);
                }else{
                    val_str_outgoing = false;
                    txt_outgoing.setText("");
                    sp_outgoing.setVisibility(View.INVISIBLE);
                    s_sp_outgoing ="";
                }
            }
        });
        cb_incoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    val_str_incoming = true;
                    txt_incoming.setText("Manifest");
                    sp_incoming.setVisibility(View.VISIBLE);

                }else{
                   val_str_incoming=false;
                    txt_incoming.setText("");
                    sp_incoming.setVisibility(View.GONE);
                    s_sp_incoming ="";

                }

            }
        });
        cb_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {

                    ll_income.setVisibility(View.VISIBLE);
                    ll_value.setVisibility(View.GONE);
                }else{

                    ll_income.setVisibility(View.GONE);
                    ll_value.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private final Runnable m_Runnable = new Runnable() {
        public void run() {
            if (vtypeUser.equals("AGENT") || vtypeUser.equals("ADMIN_AGENT")) {
                getAgentIn();
                getAgentOut();
                getAgentOutStanding();

            } else if (vtypeUser.equals("RA") || vtypeUser.equals("ADMIN_RA")) {
                getRAIn();
                getRAOut();
                getRAOutStanding();
            } else if (vtypeUser.equals("WAREHOUSE") || vtypeUser.equals("ADMIN_WAREHOUSE")) {
                getWHIn();
                getWHOut();
                getWHOutStanding();
            } else if (vtypeUser.equals("GROUND HANDLING") || vtypeUser.equals("GROUND_HANDLING")) {
                getGHIn();
                getGHOut();
                getGHOutStanding();
            } else if (vtypeUser.equals("AP") || vtypeUser.equals("ADMIN_AP")) {
                getAP();

            } else {
            }
            mHandler.postDelayed(m_Runnable, 2000);
        }

    };

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(m_Runnable);
    }

    public void getAP() {
      /*  if(cb_outgoing.isChecked()==false){
            s_sp_outgoing = "";
        }
        if(cb_incoming.isChecked()==false){
            s_sp_incoming = "";
        }//
        /* val_outgoing =  txt_outgoing.getText().toString();
         val_incoming =  txt_incoming.getText().toString();*/
       // Toast.makeText(getContext() , val_outgoing + " - " + val_incoming, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getContext(), etAsal.getText().toString(), Toast.LENGTH_SHORT).show();
        RequestQueue newRequestQueue = Volley.newRequestQueue(getActivity());
        JSONObject jSONObject = new JSONObject();
        try {
          //  Toast.makeText(getContext(),s_sp_outgoing +" - "+ s_sp_incoming, Toast.LENGTH_SHORT).show();

            /*jSONObject.put("startDate", dateView.getText().toString());
            jSONObject.put("endDate", dateView2.getText().toString());*/
            jSONObject.put("outgoing", cb_outgoing.isChecked());
            jSONObject.put("income", cb_incoming.isChecked());
            jSONObject.put("posisiout", txt_outgoing.getText().toString());
            jSONObject.put("posisiin", txt_incoming.getText().toString());
            jSONObject.put("domestic", cb_domestik.isChecked());
            jSONObject.put("inter", cb_internasional.isChecked());
            jSONObject.put("airport", etAsal.getText().toString());
            /*jSONObject.put("outgoing", true);
            jSONObject.put("income", true);
            jSONObject.put("posisiout", "Shipper");
            jSONObject.put("posisiin", "Manifest");
            jSONObject.put("domestic", true);
            jSONObject.put("inter", false);*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.API));
        sb.append("modules/private/dashboard/ap_android/dap");
        //sb.append("modules/private/dashboard/tonaseAgentIn");
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    JSONArray jSONArray = jSONObject.getJSONArray("data");

                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jsobj = jSONArray.getJSONObject(i);
                        String xkg = jsobj.getString("kg");
                        String xval = jsobj.getString("val");
                        String xincome = jsobj.getString("income");
                        str_ap_cw = "" + xkg;
                        str_ap_value = "" + xval;
                        str_ap_income = "" + xincome;

                        Locale localeID = new Locale("in", "ID");
                        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

                        String totalSMU = formatRupiah.format((double) Double.parseDouble(str_ap_cw));
                        txt_ap_cw.setText(totalSMU.substring(2, totalSMU.length()).replace(",00", "") + " KG");

                        String totalKoli = formatRupiah.format((double) Double.parseDouble(str_ap_value));
                        txt_ap_value.setText(totalKoli.substring(0, totalKoli.length()).replace(",00", ""));

                        String totalBerat = formatRupiah.format((double) Double.parseDouble(str_ap_income));
                        txt_ap_income.setText(totalBerat.substring(0, totalBerat.length()).replace(",00", ""));

                    }
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                //NewBagtagActivity.this.swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("Error: ", volleyError.getMessage());
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                StringBuilder sb = new StringBuilder();
                sb.append("Bearer ");
                sb.append(vtoken);
                hashMap.put("Authorization", sb.toString());
                return hashMap;
            }

            public byte[] getBody() {
                byte[] bArr = null;
                try {
                    if (jSONObject2 != null) {
                        bArr = jSONObject2.getBytes("utf-8");
                    }
                    return bArr;
                } catch (UnsupportedEncodingException unused) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jSONObject2, "utf-8");
                    return null;
                }
            }
        };
        newRequestQueue.add(r3);
    }

    public void getAgentIn() {
        RequestQueue newRequestQueue = Volley.newRequestQueue(getActivity());
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("startDate", dateView.getText().toString());
            jSONObject.put("endDate", dateView2.getText().toString());
            jSONObject.put("domestic", typeKirim);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.API));
        sb.append("modules/private/dashboard/DailyAgentIn");
        //sb.append("modules/private/dashboard/tonaseAgentIn");
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    JSONArray jSONArray = jSONObject.getJSONArray("data");

                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jsobj = jSONArray.getJSONObject(i);
                        String xsmu = jsobj.getString("smuNumber");
                        String xkoli = jsobj.getString("jumlahKoli");
                        String xberat = jsobj.getString("estChargeKg");

                        str_agent_smu_in = "" + xsmu;
                        str_agent_koli_in = "" + xkoli;
                        str_agent_berat_in = "" + xberat;

                        Locale localeID = new Locale("in", "ID");
                        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                        if (str_agent_smu_in == null || str_agent_smu_in == "0") {
                            String totalSMU = "0";
                            txt_agent_smu_in.setText(totalSMU);
                        } else {
                            String totalSMU = formatRupiah.format((double) Double.parseDouble(str_agent_smu_in));
                            txt_agent_smu_in.setText(totalSMU.substring(2, totalSMU.length()).replace(",00", ""));
                        }
                        if (str_agent_koli_in == null || str_agent_koli_in == "0") {
                            String totalKoli = "0";
                            txt_agent_koli_in.setText(totalKoli);
                        } else {
                            String totalKoli = formatRupiah.format((double) Double.parseDouble(str_agent_koli_in));
                            txt_agent_koli_in.setText(totalKoli.substring(2, totalKoli.length()).replace(",00", ""));
                        }
                        if (str_agent_berat_in == null || str_agent_berat_in == "0") {
                            String totalBerat = "0";
                            txt_agent_berat_in.setText(totalBerat);
                        } else {
                            String totalBerat = formatRupiah.format((double) Double.parseDouble(str_agent_berat_in));
                            txt_agent_berat_in.setText(totalBerat.substring(2, totalBerat.length()).replace(",00", ""));
                        }

                    }
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                //NewBagtagActivity.this.swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("Error: ", volleyError.getMessage());
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                StringBuilder sb = new StringBuilder();
                sb.append("Bearer ");
                sb.append(vtoken);
                hashMap.put("Authorization", sb.toString());
                return hashMap;
            }

            public byte[] getBody() {
                byte[] bArr = null;
                try {
                    if (jSONObject2 != null) {
                        bArr = jSONObject2.getBytes("utf-8");
                    }
                    return bArr;
                } catch (UnsupportedEncodingException unused) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jSONObject2, "utf-8");
                    return null;
                }
            }
        };
        newRequestQueue.add(r3);
    }

    public void getAgentOut() {
        RequestQueue newRequestQueue = Volley.newRequestQueue(getActivity());
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("startDate", dateView.getText().toString());
            jSONObject.put("endDate", dateView2.getText().toString());
            jSONObject.put("domestic", typeKirim);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.API));
        sb.append("modules/private/dashboard/DailyAgentOut");
        //sb.append("modules/private/dashboard/tonaseAgentOut");
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    JSONArray jSONArray = jSONObject.getJSONArray("data");

                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jsobj = jSONArray.getJSONObject(i);
                        String xsmu = jsobj.getString("smuNumber");
                        String xkoli = jsobj.getString("jumlahKoli");
                        String xberat = jsobj.getString("estChargeKg");

                        str_agent_smu_out = "" + xsmu;
                        str_agent_koli_out = "" + xkoli;
                        str_agent_berat_out = "" + xberat;

                        Locale localeID = new Locale("in", "ID");
                        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                        if (str_agent_smu_out == null || str_agent_smu_out == "0") {
                            String totalSMU = "0";
                            txt_agent_smu_out.setText(totalSMU);
                        } else {
                            String totalSMU = formatRupiah.format((double) Double.parseDouble(str_agent_smu_out));
                            txt_agent_smu_out.setText(totalSMU.substring(2, totalSMU.length()).replace(",00", ""));
                        }
                        if (str_agent_koli_out == null || str_agent_koli_out == "0") {
                            String totalKoli = "0";
                            txt_agent_koli_out.setText(totalKoli);
                        } else {
                            String totalKoli = formatRupiah.format((double) Double.parseDouble(str_agent_koli_out));
                            txt_agent_koli_out.setText(totalKoli.substring(2, totalKoli.length()).replace(",00", ""));
                        }
                        if (str_agent_berat_out == null || str_agent_berat_out == "0") {
                            String totalBerat = "0";
                            txt_agent_berat_out.setText(totalBerat);
                        } else {
                            String totalBerat = formatRupiah.format((double) Double.parseDouble(str_agent_berat_out));
                            txt_agent_berat_out.setText(totalBerat.substring(2, totalBerat.length()).replace(",00", ""));
                        }

                    }
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                //NewBagtagActivity.this.swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("Error: ", volleyError.getMessage());
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                StringBuilder sb = new StringBuilder();
                sb.append("Bearer ");
                sb.append(vtoken);
                hashMap.put("Authorization", sb.toString());
                return hashMap;
            }

            public byte[] getBody() {
                byte[] bArr = null;
                try {
                    if (jSONObject2 != null) {
                        bArr = jSONObject2.getBytes("utf-8");
                    }
                    return bArr;
                } catch (UnsupportedEncodingException unused) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jSONObject2, "utf-8");
                    return null;
                }
            }
        };
        newRequestQueue.add(r3);
    }

    public void getAgentOutStanding() {
        RequestQueue newRequestQueue = Volley.newRequestQueue(getActivity());
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("domestic", typeKirim);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.API));
        sb.append("modules/private/dashboard/TotalAgenteOutstanding");
        //sb.append("modules/private/dashboard/tonaseAgentOutstanding");
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    JSONArray jSONArray = jSONObject.getJSONArray("data");

                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jsobj = jSONArray.getJSONObject(i);
                        String xsmu = jsobj.getString("smuNumber");
                        String xbiaya = jsobj.getString("finalCost");
                        String xberat = jsobj.getString("ChargeKg");

                        str_agent_smu_outstanding = "" + xsmu;
                        str_agent_koli_outstanding = "" + xbiaya;
                        str_agent_berat_outstanding = "" + xberat;

                        Locale localeID = new Locale("in", "ID");
                        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

                        if (str_agent_smu_outstanding == null || str_agent_smu_outstanding == "0") {
                            String totalSMU = "0";
                            txt_agent_smu_outstanding.setText(totalSMU);
                        } else {
                            String totalSMU = formatRupiah.format((double) Double.parseDouble(str_agent_smu_outstanding));
                            txt_agent_smu_outstanding.setText(totalSMU.substring(2, totalSMU.length()).replace(",00", ""));
                        }
                        if (str_agent_koli_outstanding == null) {
                            String totalKoli = "0";
                            txt_agent_koli_outstanding.setText(totalKoli);
                        } else {
                            String totalBiaya = formatRupiah.format((double) Double.parseDouble(str_agent_koli_outstanding));
                            txt_agent_koli_outstanding.setText(totalBiaya.substring(0, totalBiaya.length()).replace(",00", ""));
                        }
                        if (str_agent_berat_outstanding == null || str_agent_berat_outstanding == "0") {
                            String totalBerat = "0";
                            txt_agent_berat_outstanding.setText(totalBerat);
                        } else {
                            String totalBerat = formatRupiah.format((double) Double.parseDouble(str_agent_berat_outstanding));
                            txt_agent_berat_outstanding.setText(totalBerat.substring(2, totalBerat.length()).replace(",00", ""));
                        }


                    }
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                //NewBagtagActivity.this.swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("Error: ", volleyError.getMessage());
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                StringBuilder sb = new StringBuilder();
                sb.append("Bearer ");
                sb.append(vtoken);
                hashMap.put("Authorization", sb.toString());
                return hashMap;
            }

            public byte[] getBody() {
                byte[] bArr = null;
                try {
                    if (jSONObject2 != null) {
                        bArr = jSONObject2.getBytes("utf-8");
                    }
                    return bArr;
                } catch (UnsupportedEncodingException unused) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jSONObject2, "utf-8");
                    return null;
                }
            }
        };
        newRequestQueue.add(r3);
    }

    public void getRAIn() {
        RequestQueue newRequestQueue = Volley.newRequestQueue(getActivity());
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("startDate", dateView.getText().toString());
            jSONObject.put("endDate", dateView2.getText().toString());
            jSONObject.put("domestic", typeKirim);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.API));
        sb.append("modules/private/dashboard/tonaseRAIn");
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    JSONArray jSONArray = jSONObject.getJSONArray("data");

                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jsobj = jSONArray.getJSONObject(i);
                        String xsmu = jsobj.getString("smuNumber");
                        String xkoli = jsobj.getString("jumlahKoli");
                        String xberat = jsobj.getString("estChargeKg");

                        str_ra_smu_in = "" + xsmu;
                        str_ra_koli_in = "" + xkoli;
                        str_ra_berat_in = "" + xberat;

                        Locale localeID = new Locale("in", "ID");
                        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

                        if (str_ra_smu_in == null) {
                            String totalSMU = "0";
                            txt_ra_smu_in.setText(totalSMU);
                        } else {
                            String totalSMU = formatRupiah.format((double) Double.parseDouble(str_ra_smu_in));
                            txt_ra_smu_in.setText(totalSMU.substring(2, totalSMU.length()).replace(",00", ""));
                        }
                        if (str_ra_koli_in == null) {
                            String totalKoli = "0";
                            txt_ra_koli_in.setText(totalKoli);
                        } else {
                            String totalKoli = formatRupiah.format((double) Double.parseDouble(str_ra_koli_in));
                            txt_ra_koli_in.setText(totalKoli.substring(2, totalKoli.length()).replace(",00", ""));
                        }
                        if (str_ra_berat_in == null) {
                            String totalBerat = "0";
                            txt_ra_berat_in.setText(totalBerat);
                        } else {
                            String totalBerat = formatRupiah.format((double) Double.parseDouble(str_ra_berat_in));
                            txt_ra_berat_in.setText(totalBerat.substring(2, totalBerat.length()).replace(",00", ""));
                        }

                    }
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                //NewBagtagActivity.this.swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("Error: ", volleyError.getMessage());
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                StringBuilder sb = new StringBuilder();
                sb.append("Bearer ");
                sb.append(vtoken);
                hashMap.put("Authorization", sb.toString());
                return hashMap;
            }

            public byte[] getBody() {
                byte[] bArr = null;
                try {
                    if (jSONObject2 != null) {
                        bArr = jSONObject2.getBytes("utf-8");
                    }
                    return bArr;
                } catch (UnsupportedEncodingException unused) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jSONObject2, "utf-8");
                    return null;
                }
            }
        };
        newRequestQueue.add(r3);
    }

    public void getRAOut() {
        RequestQueue newRequestQueue = Volley.newRequestQueue(getActivity());
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("startDate", dateView.getText().toString());
            jSONObject.put("endDate", dateView2.getText().toString());
            jSONObject.put("domestic", typeKirim);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.API));
        sb.append("modules/private/dashboard/tonaseRAOut");
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    JSONArray jSONArray = jSONObject.getJSONArray("data");

                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jsobj = jSONArray.getJSONObject(i);
                        String xsmu = jsobj.getString("smuNumber");
                        String xkoli = jsobj.getString("jumlahKoli");
                        String xberat = jsobj.getString("estChargeKg");

                        str_ra_smu_out = "" + xsmu;
                        str_ra_koli_out = "" + xkoli;
                        str_ra_berat_out = "" + xberat;

                        Locale localeID = new Locale("in", "ID");
                        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                        if (str_ra_smu_out == null) {
                            String totalSMU = "0";
                            txt_ra_smu_out.setText(totalSMU);
                        } else {
                            String totalSMU = formatRupiah.format((double) Double.parseDouble(str_ra_smu_out));
                            txt_ra_smu_out.setText(totalSMU.substring(2, totalSMU.length()).replace(",00", ""));
                        }
                        if (str_ra_koli_out == null) {
                            String totalKoli = "0";
                            txt_ra_koli_out.setText(totalKoli);
                        } else {
                            String totalKoli = formatRupiah.format((double) Double.parseDouble(str_ra_koli_out));
                            txt_ra_koli_out.setText(totalKoli.substring(2, totalKoli.length()).replace(",00", ""));
                        }
                        if (str_ra_berat_out == null) {
                            String totalBerat = "0";
                            txt_ra_berat_out.setText(totalBerat);
                        } else {
                            String totalBerat = formatRupiah.format((double) Double.parseDouble(str_ra_berat_out));
                            txt_ra_berat_out.setText(totalBerat.substring(2, totalBerat.length()).replace(",00", ""));
                        }

                    }
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                //NewBagtagActivity.this.swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("Error: ", volleyError.getMessage());
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                StringBuilder sb = new StringBuilder();
                sb.append("Bearer ");
                sb.append(vtoken);
                hashMap.put("Authorization", sb.toString());
                return hashMap;
            }

            public byte[] getBody() {
                byte[] bArr = null;
                try {
                    if (jSONObject2 != null) {
                        bArr = jSONObject2.getBytes("utf-8");
                    }
                    return bArr;
                } catch (UnsupportedEncodingException unused) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jSONObject2, "utf-8");
                    return null;
                }
            }
        };
        newRequestQueue.add(r3);
    }

    public void getRAOutStanding() {
        RequestQueue newRequestQueue = Volley.newRequestQueue(getActivity());
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("startDate", dateView.getText().toString());
            jSONObject.put("endDate", dateView2.getText().toString());
            jSONObject.put("domestic", typeKirim);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.API));
        sb.append("modules/private/dashboard/tonaseRAOutstanding");
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    JSONArray jSONArray = jSONObject.getJSONArray("data");

                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jsobj = jSONArray.getJSONObject(i);
                        String xsmu = jsobj.getString("smuNumber");
                        String xkoli = jsobj.getString("jumlahKoli");
                        String xberat = jsobj.getString("estChargeKg");


                        str_ra_smu_outstanding = "" + xsmu;
                        str_ra_koli_outstanding = "" + xkoli;
                        str_ra_berat_outstanding = "" + xberat;

                        Locale localeID = new Locale("in", "ID");
                        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                        if (str_ra_smu_outstanding == null) {
                            String totalSMU = "0";
                            txt_ra_smu_outstanding.setText(totalSMU);
                        } else {
                            String totalSMU = formatRupiah.format((double) Double.parseDouble(str_ra_smu_outstanding));
                            txt_ra_smu_outstanding.setText(totalSMU.substring(2, totalSMU.length()).replace(",00", ""));
                        }
                        if (str_ra_koli_outstanding == null) {
                            String totalKoli = "0";
                            txt_ra_koli_outstanding.setText(totalKoli);
                        } else {
                            String totalKoli = formatRupiah.format((double) Double.parseDouble(str_ra_koli_outstanding));
                            txt_ra_koli_outstanding.setText(totalKoli.substring(2, totalKoli.length()).replace(",00", ""));
                        }
                        if (str_ra_berat_outstanding == null) {
                            String totalBerat = "0";
                            txt_ra_berat_outstanding.setText(totalBerat);
                        } else {
                            String totalBerat = formatRupiah.format((double) Double.parseDouble(str_ra_berat_outstanding));
                            txt_ra_berat_outstanding.setText(totalBerat.substring(2, totalBerat.length()).replace(",00", ""));
                        }

                    }
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                //NewBagtagActivity.this.swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("Error: ", volleyError.getMessage());
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                StringBuilder sb = new StringBuilder();
                sb.append("Bearer ");
                sb.append(vtoken);
                hashMap.put("Authorization", sb.toString());
                return hashMap;
            }

            public byte[] getBody() {
                byte[] bArr = null;
                try {
                    if (jSONObject2 != null) {
                        bArr = jSONObject2.getBytes("utf-8");
                    }
                    return bArr;
                } catch (UnsupportedEncodingException unused) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jSONObject2, "utf-8");
                    return null;
                }
            }
        };
        newRequestQueue.add(r3);
    }

    public void getWHIn() {
        RequestQueue newRequestQueue = Volley.newRequestQueue(getActivity());
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("startDate", dateView.getText().toString());
            jSONObject.put("endDate", dateView2.getText().toString());
            jSONObject.put("domestic", typeKirim);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.API));
        sb.append("modules/private/dashboard/tonaseWarehouseIn");
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    JSONArray jSONArray = jSONObject.getJSONArray("data");

                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jsobj = jSONArray.getJSONObject(i);
                        String xsmu = jsobj.getString("smuNumber");
                        String xkoli = jsobj.getString("jumlahKoli");
                        String xberat = jsobj.getString("estChargeKg");

                        str_wh_smu_outstanding = "" + xsmu;
                        str_wh_koli_outstanding = "" + xkoli;
                        str_wh_berat_outstanding = "" + xberat;
                    }
                    Locale localeID = new Locale("in", "ID");
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                    if (str_wh_smu_outstanding == null) {
                        String totalSMU = "0";
                        txt_wh_smu_outstanding.setText(totalSMU);
                    } else {
                        String totalSMU = formatRupiah.format((double) Double.parseDouble(str_wh_smu_outstanding));
                        txt_wh_smu_outstanding.setText(totalSMU.substring(2, totalSMU.length()).replace(",00", ""));
                    }

                    if (str_wh_koli_outstanding == null) {
                        String totalKoli = "0";
                        txt_wh_koli_outstanding.setText(totalKoli);
                    } else {
                        String totalKoli = formatRupiah.format((double) Double.parseDouble(str_wh_koli_outstanding));
                        txt_wh_koli_outstanding.setText(totalKoli.substring(2, totalKoli.length()).replace(",00", ""));
                    }

                    if (str_wh_berat_outstanding == null) {
                        String totalBerat = "0";
                        txt_wh_berat_outstanding.setText(totalBerat);
                    } else {
                        String totalBerat = formatRupiah.format((double) Double.parseDouble(str_wh_berat_outstanding));
                        txt_wh_berat_outstanding.setText(totalBerat.substring(2, totalBerat.length()).replace(",00", ""));
                    }

                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                //NewBagtagActivity.this.swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("Error: ", volleyError.getMessage());
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                StringBuilder sb = new StringBuilder();
                sb.append("Bearer ");
                sb.append(vtoken);
                hashMap.put("Authorization", sb.toString());
                return hashMap;
            }

            public byte[] getBody() {
                byte[] bArr = null;
                try {
                    if (jSONObject2 != null) {
                        bArr = jSONObject2.getBytes("utf-8");
                    }
                    return bArr;
                } catch (UnsupportedEncodingException unused) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jSONObject2, "utf-8");
                    return null;
                }
            }
        };
        newRequestQueue.add(r3);
    }

    public void getWHOut() {
        RequestQueue newRequestQueue = Volley.newRequestQueue(getActivity());
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("startDate", dateView.getText().toString());
            jSONObject.put("endDate", dateView2.getText().toString());
            jSONObject.put("domestic", typeKirim);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.API));
        sb.append("modules/private/dashboard/tonaseWarehouseOut");
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    JSONArray jSONArray = jSONObject.getJSONArray("data");

                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jsobj = jSONArray.getJSONObject(i);
                        String xsmu = jsobj.getString("smuNumber");
                        String xkoli = jsobj.getString("jumlahKoli");
                        String xberat = jsobj.getString("estChargeKg");
                        str_wh_smu_out = "" + xsmu;
                        str_wh_koli_out = "" + xkoli;
                        str_wh_berat_out = "" + xberat;
                    }
                    Locale localeID = new Locale("in", "ID");
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                    if (str_wh_smu_out == null) {
                        String totalSMU = "0";
                        txt_wh_smu_out.setText(totalSMU);
                    } else {
                        String totalSMU = formatRupiah.format((double) Double.parseDouble(str_wh_smu_out));
                        txt_wh_smu_out.setText(totalSMU.substring(2, totalSMU.length()).replace(",00", ""));
                    }

                    if (str_wh_koli_out == null) {
                        String totalKoli = "0";
                        txt_wh_koli_out.setText(totalKoli);
                    } else {
                        String totalKoli = formatRupiah.format((double) Double.parseDouble(str_wh_koli_out));
                        txt_wh_koli_out.setText(totalKoli.substring(2, totalKoli.length()).replace(",00", ""));
                    }

                    if (str_wh_berat_out == null) {
                        String totalBerat = "0";
                        txt_wh_berat_out.setText(totalBerat);
                    } else {
                        String totalBerat = formatRupiah.format((double) Double.parseDouble(str_wh_berat_out));
                        txt_wh_berat_out.setText(totalBerat.substring(2, totalBerat.length()).replace(",00", ""));
                    }

                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                //NewBagtagActivity.this.swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("Error: ", volleyError.getMessage());
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                StringBuilder sb = new StringBuilder();
                sb.append("Bearer ");
                sb.append(vtoken);
                hashMap.put("Authorization", sb.toString());
                return hashMap;
            }

            public byte[] getBody() {
                byte[] bArr = null;
                try {
                    if (jSONObject2 != null) {
                        bArr = jSONObject2.getBytes("utf-8");
                    }
                    return bArr;
                } catch (UnsupportedEncodingException unused) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jSONObject2, "utf-8");
                    return null;
                }
            }
        };
        newRequestQueue.add(r3);
    }

    public void getWHOutStanding() {
        RequestQueue newRequestQueue = Volley.newRequestQueue(getActivity());
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("startDate", dateView.getText().toString());
            jSONObject.put("endDate", dateView2.getText().toString());
            jSONObject.put("domestic", typeKirim);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.API));
        sb.append("modules/private/dashboard/tonaseWarehouseOutstanding");
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    JSONArray jSONArray = jSONObject.getJSONArray("data");

                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jsobj = jSONArray.getJSONObject(i);
                        String xsmu = jsobj.getString("smuNumber");
                        String xkoli = jsobj.getString("jumlahKoli");
                        String xberat = jsobj.getString("estChargeKg");

                        str_wh_smu_outstanding = "" + xsmu;
                        str_wh_koli_outstanding = "" + xkoli;
                        str_wh_berat_outstanding = "" + xberat;

                    }
                    Locale localeID = new Locale("in", "ID");
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                    if (str_wh_smu_outstanding == null) {
                        String totalSMU = "0";
                        txt_wh_smu_outstanding.setText(totalSMU);
                    } else {
                        String totalSMU = formatRupiah.format((double) Double.parseDouble(str_wh_smu_outstanding));
                        txt_wh_smu_outstanding.setText(totalSMU.substring(2, totalSMU.length()).replace(",00", ""));
                    }

                    if (str_wh_koli_outstanding == null) {
                        String totalKoli = "0";
                        txt_wh_koli_outstanding.setText(totalKoli);
                    } else {
                        String totalKoli = formatRupiah.format((double) Double.parseDouble(str_wh_koli_outstanding));
                        txt_wh_koli_outstanding.setText(totalKoli.substring(2, totalKoli.length()).replace(",00", ""));
                    }

                    if (str_wh_berat_outstanding == null) {
                        String totalBerat = "0";
                        txt_wh_berat_outstanding.setText(totalBerat);
                    } else {
                        String totalBerat = formatRupiah.format((double) Double.parseDouble(str_wh_berat_outstanding));
                        txt_wh_berat_outstanding.setText(totalBerat.substring(2, totalBerat.length()).replace(",00", ""));
                    }


                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                //NewBagtagActivity.this.swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("Error: ", volleyError.getMessage());
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                StringBuilder sb = new StringBuilder();
                sb.append("Bearer ");
                sb.append(vtoken);
                hashMap.put("Authorization", sb.toString());
                return hashMap;
            }

            public byte[] getBody() {
                byte[] bArr = null;
                try {
                    if (jSONObject2 != null) {
                        bArr = jSONObject2.getBytes("utf-8");
                    }
                    return bArr;
                } catch (UnsupportedEncodingException unused) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jSONObject2, "utf-8");
                    return null;
                }
            }
        };
        newRequestQueue.add(r3);
    }

    public void getGHIn() {
        RequestQueue newRequestQueue = Volley.newRequestQueue(getActivity());
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("startDate", dateView.getText().toString());
            jSONObject.put("endDate", dateView2.getText().toString());
            jSONObject.put("domestic", typeKirim);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.API));
        sb.append("modules/private/dashboard/tonaseWarehouseIn");
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    JSONArray jSONArray = jSONObject.getJSONArray("data");

                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jsobj = jSONArray.getJSONObject(i);
                        String xsmu = jsobj.getString("smuNumber");
                        String xkoli = jsobj.getString("jumlahKoli");
                        String xberat = jsobj.getString("estChargeKg");

                        str_wh_smu_outstanding = "" + xsmu;
                        str_wh_koli_outstanding = "" + xkoli;
                        str_wh_berat_outstanding = "" + xberat;
                    }
                    Locale localeID = new Locale("in", "ID");
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                    if (str_wh_smu_outstanding == null) {
                        String totalSMU = "0";
                        txt_wh_smu_outstanding.setText(totalSMU);
                    } else {
                        String totalSMU = formatRupiah.format((double) Double.parseDouble(str_wh_smu_outstanding));
                        txt_wh_smu_outstanding.setText(totalSMU.substring(2, totalSMU.length()).replace(",00", ""));
                    }

                    if (str_wh_koli_outstanding == null) {
                        String totalKoli = "0";
                        txt_wh_koli_outstanding.setText(totalKoli);
                    } else {
                        String totalKoli = formatRupiah.format((double) Double.parseDouble(str_wh_koli_outstanding));
                        txt_wh_koli_outstanding.setText(totalKoli.substring(2, totalKoli.length()).replace(",00", ""));
                    }

                    if (str_wh_berat_outstanding == null) {
                        String totalBerat = "0";
                        txt_wh_berat_outstanding.setText(totalBerat);
                    } else {
                        String totalBerat = formatRupiah.format((double) Double.parseDouble(str_wh_berat_outstanding));
                        txt_wh_berat_outstanding.setText(totalBerat.substring(2, totalBerat.length()).replace(",00", ""));
                    }

                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                //NewBagtagActivity.this.swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("Error: ", volleyError.getMessage());
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                StringBuilder sb = new StringBuilder();
                sb.append("Bearer ");
                sb.append(vtoken);
                hashMap.put("Authorization", sb.toString());
                return hashMap;
            }

            public byte[] getBody() {
                byte[] bArr = null;
                try {
                    if (jSONObject2 != null) {
                        bArr = jSONObject2.getBytes("utf-8");
                    }
                    return bArr;
                } catch (UnsupportedEncodingException unused) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jSONObject2, "utf-8");
                    return null;
                }
            }
        };
        newRequestQueue.add(r3);
    }

    public void getGHOut() {
        RequestQueue newRequestQueue = Volley.newRequestQueue(getActivity());
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("startDate", dateView.getText().toString());
            jSONObject.put("endDate", dateView2.getText().toString());
            jSONObject.put("domestic", typeKirim);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.API));
        sb.append("modules/private/dashboard/tonaseWarehouseOut");
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    JSONArray jSONArray = jSONObject.getJSONArray("data");

                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jsobj = jSONArray.getJSONObject(i);
                        String xsmu = jsobj.getString("smuNumber");
                        String xkoli = jsobj.getString("jumlahKoli");
                        String xberat = jsobj.getString("estChargeKg");
                        str_wh_smu_out = "" + xsmu;
                        str_wh_koli_out = "" + xkoli;
                        str_wh_berat_out = "" + xberat;
                    }
                    Locale localeID = new Locale("in", "ID");
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                    if (str_wh_smu_out == null) {
                        String totalSMU = "0";
                        txt_wh_smu_out.setText(totalSMU);
                    } else {
                        String totalSMU = formatRupiah.format((double) Double.parseDouble(str_wh_smu_out));
                        txt_wh_smu_out.setText(totalSMU.substring(2, totalSMU.length()).replace(",00", ""));
                    }

                    if (str_wh_koli_out == null) {
                        String totalKoli = "0";
                        txt_wh_koli_out.setText(totalKoli);
                    } else {
                        String totalKoli = formatRupiah.format((double) Double.parseDouble(str_wh_koli_out));
                        txt_wh_koli_out.setText(totalKoli.substring(2, totalKoli.length()).replace(",00", ""));
                    }

                    if (str_wh_berat_out == null) {
                        String totalBerat = "0";
                        txt_wh_berat_out.setText(totalBerat);
                    } else {
                        String totalBerat = formatRupiah.format((double) Double.parseDouble(str_wh_berat_out));
                        txt_wh_berat_out.setText(totalBerat.substring(2, totalBerat.length()).replace(",00", ""));
                    }

                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                //NewBagtagActivity.this.swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("Error: ", volleyError.getMessage());
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                StringBuilder sb = new StringBuilder();
                sb.append("Bearer ");
                sb.append(vtoken);
                hashMap.put("Authorization", sb.toString());
                return hashMap;
            }

            public byte[] getBody() {
                byte[] bArr = null;
                try {
                    if (jSONObject2 != null) {
                        bArr = jSONObject2.getBytes("utf-8");
                    }
                    return bArr;
                } catch (UnsupportedEncodingException unused) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jSONObject2, "utf-8");
                    return null;
                }
            }
        };
        newRequestQueue.add(r3);
    }

    public void getGHOutStanding() {
        RequestQueue newRequestQueue = Volley.newRequestQueue(getActivity());
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("startDate", dateView.getText().toString());
            jSONObject.put("endDate", dateView2.getText().toString());
            jSONObject.put("domestic", typeKirim);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.API));
        sb.append("modules/private/dashboard/tonaseWarehouseOutstanding");
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    JSONArray jSONArray = jSONObject.getJSONArray("data");

                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jsobj = jSONArray.getJSONObject(i);
                        String xsmu = jsobj.getString("smuNumber");
                        String xkoli = jsobj.getString("jumlahKoli");
                        String xberat = jsobj.getString("estChargeKg");

                        str_wh_smu_outstanding = "" + xsmu;
                        str_wh_koli_outstanding = "" + xkoli;
                        str_wh_berat_outstanding = "" + xberat;

                    }
                    Locale localeID = new Locale("in", "ID");
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                    if (str_wh_smu_outstanding == null) {
                        String totalSMU = "0";
                        txt_wh_smu_outstanding.setText(totalSMU);
                    } else {
                        String totalSMU = formatRupiah.format((double) Double.parseDouble(str_wh_smu_outstanding));
                        txt_wh_smu_outstanding.setText(totalSMU.substring(2, totalSMU.length()).replace(",00", ""));
                    }

                    if (str_wh_koli_outstanding == null) {
                        String totalKoli = "0";
                        txt_wh_koli_outstanding.setText(totalKoli);
                    } else {
                        String totalKoli = formatRupiah.format((double) Double.parseDouble(str_wh_koli_outstanding));
                        txt_wh_koli_outstanding.setText(totalKoli.substring(2, totalKoli.length()).replace(",00", ""));
                    }

                    if (str_wh_berat_outstanding == null) {
                        String totalBerat = "0";
                        txt_wh_berat_outstanding.setText(totalBerat);
                    } else {
                        String totalBerat = formatRupiah.format((double) Double.parseDouble(str_wh_berat_outstanding));
                        txt_wh_berat_outstanding.setText(totalBerat.substring(2, totalBerat.length()).replace(",00", ""));
                    }


                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                //NewBagtagActivity.this.swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("Error: ", volleyError.getMessage());
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                StringBuilder sb = new StringBuilder();
                sb.append("Bearer ");
                sb.append(vtoken);
                hashMap.put("Authorization", sb.toString());
                return hashMap;
            }

            public byte[] getBody() {
                byte[] bArr = null;
                try {
                    if (jSONObject2 != null) {
                        bArr = jSONObject2.getBytes("utf-8");
                    }
                    return bArr;
                } catch (UnsupportedEncodingException unused) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jSONObject2, "utf-8");
                    return null;
                }
            }
        };
        newRequestQueue.add(r3);
    }

    public void TanggalAwal() {
        // Create the DatePickerDialog instance
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(),
                datePickerTanggalAwal,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        datePicker.setCancelable(false);
        datePicker.setTitle("Select the date");
        datePicker.show();

    }

    private DatePickerDialog.OnDateSetListener datePickerTanggalAwal = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month1 = String.valueOf(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);
            tvDt1.setText(year1 + "-" + month1 + "-" + day1);
        }
    };

    public void TanggalAkhir() {
        // Create the DatePickerDialog instance
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(),
                datePickerTanggalAkhir,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        datePicker.setCancelable(false);
        datePicker.setTitle("Select the date");
        datePicker.show();
    }

    private DatePickerDialog.OnDateSetListener datePickerTanggalAkhir = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month1 = String.valueOf(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);
            tvDt2.setText(year1 + "-" + month1 + "-" + day1);

        }
    };

    public void setRefresh(View view) {
        onRefresh();
    }

    /* access modifiers changed from: protected */
    public Dialog onCreateDialog(int i) {
        if (i == 999) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), myDateListener, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            return datePickerDialog;
        } else if (i != 998) {
            return null;
        } else {
            DatePickerDialog datePickerDialog2 = new DatePickerDialog(getActivity(), myDateListener2, year2, month2, day2);
            datePickerDialog2.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            return datePickerDialog2;
        }
    }

    /* access modifiers changed from: private */
    public void showDate(int i, int i2, int i3) {
        TextView textView = dateView;
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("-");
        sb.append(i2);
        sb.append("-");
        sb.append(i3);
        textView.setText(sb);
    }

    /* access modifiers changed from: private */
    public void showDate2(int i, int i2, int i3) {
        TextView textView = dateView3;
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("-");
        sb.append(i2);
        sb.append("-");
        sb.append(i3);
        textView.setText(sb);
        TextView textView2 = dateView2;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(i);
        sb2.append("-");
        sb2.append(i2);
        sb2.append("-");
        sb2.append(i3);
        textView2.setText(sb2);
    }

    public void onRefresh() {
        if (vtypeUser.equals("AGENT") || vtypeUser.equals("ADMIN_AGENT")) {
            //swipeRefreshLayout.setRefreshing(true);
            getAgentIn();
            getAgentOut();
            getAgentOutStanding();
            //swipeRefreshLayout.setRefreshing(false);

        } else if (vtypeUser.equals("RA") || vtypeUser.equals("ADMIN_RA")) {
            //swipeRefreshLayout.setRefreshing(true);
            getRAIn();
            getRAOut();
            getRAOutStanding();
            //swipeRefreshLayout.setRefreshing(false);
        } else if (vtypeUser.equals("WAREHOUSE") || vtypeUser.equals("ADMIN_WAREHOUSE")) {
            //swipeRefreshLayout.setRefreshing(true);
            getWHIn();
            getWHOut();
            getWHOutStanding();
            //swipeRefreshLayout.setRefreshing(false);
        } else {
            //swipeRefreshLayout.setRefreshing(false);
        }
        //swipeRefreshLayout.setRefreshing(false);
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };



}
