package com.fungsitama.dhsshopee.activity.dashboard.HOME;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fungsitama.dhsshopee.activity.dashboard.DETAIL.DetailDaftarLoadingActivity;
import com.fungsitama.dhsshopee.activity.dashboard.DETAIL.DetailDaftarUnloadingActivity;
import com.fungsitama.dhsshopee.util.ApiConfig;
import com.fungsitama.dhsshopee.util.SessionManager;
import com.fungsitama.dhsshopee.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import de.mateware.snacky.Snacky;

public class Home_Driver_Fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SessionManager manager;
    private static String vtoken, vusername, vtypeLogin, vtypeUser,codeMaster;
    CarouselView carouselView;
    int[] sampleImages = {R.drawable.kisel_banner,R.drawable.img_dashboard_slide02};
    private LinearLayout lvAgent;
    private TextView txt_agent_smu_in, txt_agent_koli_in, txt_agent_berat_in,
            txt_agent_smu_out, txt_agent_koli_out, txt_agent_berat_out,
            txt_agent_smu_outstanding, txt_agent_koli_outstanding, txt_agent_berat_outstanding;

    private String str_agent_smu_in, str_agent_koli_in, str_agent_berat_in,
            str_agent_smu_out, str_agent_koli_out, str_agent_berat_out,
            str_agent_smu_outstanding, str_agent_koli_outstanding, str_agent_berat_outstanding;
    private Calendar calendar;
    private Calendar calendar2;
    private TextView dateView;
    private TextView dateView2;
    private TextView dateView3;
    private int day;
    private int day2;
    private int month;
    private int month2;
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
    SharedPreferences pref;
    SearchView searchView = null;
    /* access modifiers changed from: private */
    public SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton tbrefresh, dt1, dt2;
    private int year;
    private int year2;
    private TextView tvDt1, tvDt2, total, totalkoli, totalkg,totalsmu;

    Calendar cal = Calendar.getInstance(TimeZone.getDefault());

    private String startDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    private String endDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    private Handler mHandler = new Handler();

    private LinearLayout lyMenungguVefrifikasi, lyTotalChekout, lyTotalOutstanding;
    private CardView cv_G,cv_H,cv_I;
    String txt_Total_Outstanding,txt_TotalChekout,txt_MenungguVrifikasi;

    private BroadcastReceiver broadcastReceiver;
    private static boolean firstConnect = true;

    private TextView tvTotalLoading,tvTotalUnloading,tvTotalBarcodeLoading,tvTotalBarcodeUnloading;
    private String title;

    public Home_Driver_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate (R.layout.fragment_home__driver_, container, false);

        getActivity().getWindow().setStatusBarColor(Color.RED);
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

        TextView l_user = (TextView) view.findViewById(R.id.user);
        TextView l_tools = (TextView) view.findViewById(R.id.tools);
        l_user.setText(vusername);
        l_tools.setText( vtypeUser + " Tools");

        Log.d("TAG", "onCreateViewData: " + vtypeUser + " - " + vusername);


        //Agen-Chekin
        cv_G = (CardView) view.findViewById(R.id.g);
        cv_H = (CardView) view.findViewById(R.id.h);
        cv_I = (CardView) view.findViewById(R.id.i);
        cv_G.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_MenungguVrifikasi = "Verifikasi";
            }
        });
        cv_H.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_Total_Outstanding = "ChekOut";
            }
        });
        cv_I.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_TotalChekout = "Outstanding";
            }
        });

        lvAgent = (LinearLayout) view.findViewById(R.id.lvAgent);
        tvDt1 = (TextView) getActivity().findViewById(R.id.dt1awal);
        tvDt2 = (TextView) getActivity().findViewById(R.id.dt2akhir);

        txt_agent_smu_in = (TextView) view.findViewById(R.id.agent_smu_in);
        txt_agent_koli_in = (TextView) view.findViewById(R.id.agent_koli_in);
        txt_agent_berat_in = (TextView) view.findViewById(R.id.agent_berat_in);
        txt_agent_smu_out = (TextView) view.findViewById(R.id.agent_smu_out);
        txt_agent_koli_out = (TextView) view.findViewById(R.id.agent_koli_out);
        txt_agent_berat_out = (TextView) view.findViewById(R.id.agent_berat_out);
        txt_agent_smu_outstanding = (TextView) view.findViewById(R.id.agent_smu_outstanding);
        txt_agent_koli_outstanding = (TextView) view.findViewById(R.id.agent_koli_outstanding);
        txt_agent_berat_outstanding = (TextView) view.findViewById(R.id.agent_berat_outstanding);

        tbrefresh = (ImageButton) view.findViewById(R.id.refresh);
        dt1 = (ImageButton) view.findViewById(R.id.dt1);
        dt2 = (ImageButton) view.findViewById(R.id.dt2);
        dateView = (TextView) view.findViewById(R.id.dt1awal);
        calendar = Calendar.getInstance();
        year = this.calendar.get(Calendar.YEAR);
        month = this.calendar.get(Calendar.MONTH);
        day = this.calendar.get(Calendar.DAY_OF_MONTH);
        dateView2 = (TextView) view.findViewById(R.id.dt2akhir);
        dateView3 = (TextView) view.findViewById(R.id.dt3send);
        calendar2 = Calendar.getInstance();
        year2 = this.calendar.get(Calendar.YEAR);
        month2 = this.calendar.get(Calendar.MONTH);
        day2 = this.calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);
        showDate2(year2, month2 + 1, day2);

        CardView entridata = (CardView) view.findViewById(R.id.entridata);
        CardView belum_bayar = (CardView) view.findViewById(R.id.belum_bayar);
        CardView histori = (CardView) view.findViewById(R.id.histori);

        Log.d("TAG", "vtypeUser: " + vtypeUser);

        tbrefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onRefresh();
            }
        });
        dt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TanggalAwal();
                /*showDialog(999);
                Toast.makeText(getContext(), "Pilih tanggal awal", Toast.LENGTH_SHORT).show();*/
            }
        });
        dt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TanggalAkhir();
                /*showDialog(998);
                Toast.makeText(getContext(), "Pilih tanggal akhir", Toast.LENGTH_SHORT).show();*/
            }
        });


        lvAgent.setVisibility(View.VISIBLE);


        tvTotalLoading = view.findViewById(R.id.total_Loading);
        tvTotalUnloading = view.findViewById(R.id.total_Unloading);

        tvTotalBarcodeLoading = view.findViewById(R.id.total_Barcode_Loading);
        tvTotalBarcodeUnloading = view.findViewById(R.id.total_Barcode_Unloading);


        CardView cvDetailLoading = (CardView) view.findViewById(R.id.cv_DetailLoading);
        cvDetailLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("TAG", "tanggal 1: "+ startDate + " " + endDate);

                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                edit.putString("vtoken", vtoken);
                edit.putString("vusername", vusername);
                edit.putString("vtypeLogin", vtypeLogin);
                edit.putString("vtypeUser", vtypeUser);
                edit.putString("vstratDate", startDate);
                edit.putString("vendDate", endDate);
                edit.commit();
                startActivity(new Intent(getActivity(), DetailDaftarLoadingActivity.class));
            }
        });

        CardView cvDetailUnloading = (CardView) view.findViewById(R.id.cv_DetailUnloading);
        cvDetailUnloading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("TAG", "tanggal 1: "+ startDate + " " + endDate);

                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                edit.putString("vtoken", vtoken);
                edit.putString("vusername", vusername);
                edit.putString("vtypeLogin", vtypeLogin);
                edit.putString("vtypeUser", vtypeUser);
                edit.putString("vstratDate", startDate);
                edit.putString("vendDate", endDate);
                edit.commit();
                startActivity(new Intent(getActivity(), DetailDaftarUnloadingActivity.class));
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });



        koneksiReceiver();


        return view;
    }


    public void setRefresh(View view) {
        onRefresh();

    }


    public void TanggalAwal(){
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
            TextView tvDt1 = (TextView) getActivity().findViewById(R.id.dt1awal);
            tvDt1.setText(year1 + "-" + month1 + "-" + day1);

            startDate  = year1 + "-" + month1 + "-" + day1;

            Log.d("TAG", "onDateSet1: " + startDate);

        }
    };


    public void TanggalAkhir(){
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
            TextView tvDt2 = (TextView) getActivity().findViewById(R.id.dt2akhir);
            tvDt2.setText(year1 + "-" + month1 + "-" + day1);

            endDate  = year1 + "-" + month1 + "-" + day1;

            Log.d("TAG", "onDateSet2: " + endDate);

        }
    };

    private void showDialog(int i) {
    }

    /* access modifiers changed from: protected */
    public Dialog onCreateDialog(int i) {
        if (i == 999) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), this.myDateListener, this.year, this.month, this.day);
            //datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            return datePickerDialog;
        } else if (i != 998) {
            return null;
        } else {
            DatePickerDialog datePickerDialog2 = new DatePickerDialog(getContext(), this.myDateListener2, this.year2, this.month2, this.day2);
            //datePickerDialog2.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            return datePickerDialog2;
        }
    }

    /* access modifiers changed from: private */
    public void showDate(int i, int i2, int i3) {
        TextView textView = this.dateView;
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
        TextView textView = this.dateView3;
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append("-");
        sb.append(i2);
        sb.append("-");
        sb.append(i3);
        textView.setText(sb);
        TextView textView2 = this.dateView2;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(i);
        sb2.append("-");
        sb2.append(i2);
        sb2.append("-");
        sb2.append(i3);
        textView2.setText(sb2);
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };


    public void onRefresh() {
        try {
            getTotalLoading();
            getTotalUnloading();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getTotalLoading() {
        swipeRefreshLayout.setRefreshing(true);
        RequestQueue newRequestQueue = Volley.newRequestQueue(getContext());
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("startDate", startDate);
            jSONObject.put("endDate", endDate);
            jSONObject.put("status", "Loading");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.dashboardLoadingAndUnloading);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), jSONObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                Date date1;
                try {

                    JSONObject jsobj = jSONObject.getJSONObject("data");

                    String totalLoading = jsobj.getString("totalTrans");
                    String totalBarcodeLoading = jsobj.getString("totalBarcode");


                    tvTotalLoading.setText(totalLoading);
                    tvTotalBarcodeLoading.setText(totalBarcodeLoading);

                    Log.d("TAG", "aaaaaaaa: " + totalLoading);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Context applicationContext = getContext();
                    StringBuilder sb = new StringBuilder();
                    sb.append("Error: ");
                    sb.append(e.getMessage());
                    Toast.makeText(applicationContext, sb.toString(), Toast.LENGTH_LONG).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("Error: ", volleyError.getMessage());
                // Toast.makeText( ListDataEntryActivity.this, "e" + volleyError.toString(), Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
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

    private void getTotalUnloading() {
        swipeRefreshLayout.setRefreshing(true);
        RequestQueue newRequestQueue = Volley.newRequestQueue(getContext());
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("startDate", startDate);
            jSONObject.put("endDate", endDate);
            jSONObject.put("status", "Unloading");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.dashboardLoadingAndUnloading);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), jSONObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                Date date1;
                try {

                    JSONObject jsobj = jSONObject.getJSONObject("data");

                    String totalLoading = jsobj.getString("totalTrans");
                    String totalBarcodeLoading = jsobj.getString("totalBarcode");

                    tvTotalUnloading.setText(totalLoading);
                    tvTotalBarcodeUnloading.setText(totalBarcodeLoading);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Context applicationContext = getContext();
                    StringBuilder sb = new StringBuilder();
                    sb.append("Error: ");
                    sb.append(e.getMessage());
                    Toast.makeText(applicationContext, sb.toString(), Toast.LENGTH_LONG).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("Error: ", volleyError.getMessage());
                // Toast.makeText( ListDataEntryActivity.this, "e" + volleyError.toString(), Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
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

    private void  koneksiReceiver(){
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {

                    ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
                    if (activeNetwork != null){

                        if(firstConnect) {
                            // do subroutines here
                            //Toast.makeText(context, "Konek", Toast.LENGTH_SHORT).show();

                           /* Snacky.builder()
                                    .setActivity((Activity) context)
                                    .setText("Internet Koneksi Tersedia")
                                    .setDuration(Snacky.LENGTH_INDEFINITE)
                                    .setActionText("OK")
                                    .success()
                                    .show();

                            firstConnect = false;*/
                        }

                    }else {

                        Snacky.builder()
                                .setActivity((Activity) context)
                                .setText("Tidak ada koneksi internet")
                                .setDuration(Snacky.LENGTH_INDEFINITE)
                                .setActionText("OK")
                                .error()
                                .show();

                        firstConnect= true;

                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        };
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    protected void unRegisteredNetwork(){
        try {
            getActivity().unregisterReceiver(broadcastReceiver);

        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisteredNetwork();
    }
}