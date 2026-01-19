package com.fungsitama.dhsshopee.activity.dhs.Loading;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fungsitama.dhsshopee.R;
import com.fungsitama.dhsshopee.activity.dhs.Unloading.ScanBarcodeUnloadingActivity;
import com.fungsitama.dhsshopee.activity.main.NavMenuActivity;
import com.fungsitama.dhsshopee.util.ApiConfig;
import com.fungsitama.dhsshopee.util.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class DaftarLoadingDetailActivity extends AppCompatActivity {

    SessionManager manager;
    private static String vtoken,vusername,vtypeLogin,vtypeUser;

    private TextView txt_noTrans,txt_transactionDate,txt_TanggalStatusLoading,txt_TanggalStatusUnloading,
            txt_noTruck,txt_NamaSupir,txt_asalTujuan,txt_transit,txt_TotalBarcdoe,txt_Status,txt_LoadType;

    private String id,noTrans,status;

    private Button btn_Detail_Barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_loading_detail);

        Intent intent = getIntent();

        id = intent.getStringExtra("id");
        noTrans = intent.getStringExtra("noTrans");
        status = intent.getStringExtra("status");

        Log.d("TAG", "idTranskasi: " + id);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Detail Transkasi");
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager = new SessionManager();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        vtoken = defaultSharedPreferences.getString("token", null);
        vusername = defaultSharedPreferences.getString("username", null);
        vtypeLogin = defaultSharedPreferences.getString("typeLogin", null);
        vtypeUser = defaultSharedPreferences.getString("typeUser", null);

        txt_noTrans = findViewById(R.id.txt_noTrans);
        txt_transactionDate = findViewById(R.id.txt_transactionDate);
        txt_TanggalStatusLoading = findViewById(R.id.txt_TanggalStatusLoading);
        txt_TanggalStatusUnloading = findViewById(R.id.txt_TanggalStatusUnloading);
        txt_noTruck = findViewById(R.id.txt_noTruck);
        txt_NamaSupir = findViewById(R.id.txt_NamaSupir);
        txt_asalTujuan = findViewById(R.id.txt_asalTujuan);
        txt_transit = findViewById(R.id.txt_transit);
        txt_TotalBarcdoe = findViewById(R.id.txt_TotalBarcdoe);
        txt_Status = findViewById(R.id.txt_Status);
        txt_LoadType = findViewById(R.id.txt_LoadType);

        btn_Detail_Barcode = findViewById(R.id.btn_Detail_Barcode);
        btn_Detail_Barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (status.equals("Loading")){
                    Intent intent = new Intent(DaftarLoadingDetailActivity.this, ScanBarcodeLoadingActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("noTrans", noTrans);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(DaftarLoadingDetailActivity.this, ScanBarcodeUnloadingActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("noTrans", noTrans);
                    startActivity(intent);
                }


            }
        });

        getDetailTranskasi();



    }

    @Override
    public boolean onSupportNavigateUp(){
        //code it to launch an intent to the activity you want
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        } else {
            onBackPressed(); //replaced
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_go_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_go_home: {

                Intent intent = new Intent(DaftarLoadingDetailActivity.this, NavMenuActivity.class);
                startActivity(intent);
                finish();

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public void getDetailTranskasi() {
        RequestQueue newRequestQueue = Volley.newRequestQueue(DaftarLoadingDetailActivity.this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.DetailDaftarLoading);

        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            public void onResponse(JSONObject jSONObject) {
                Date date1,date2,date3;
                try {

                    JSONObject jSONObject3 = new JSONObject(jSONObject.getString("data"));

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

                    String id = jSONObject3.getString("id");
                    String transNumber = jSONObject3.getString("transNumber");
                    String nameDriver = jSONObject3.getString("nameDriver");
                    String codeVehicle = jSONObject3.getString("codeVehicle");
                    String status = jSONObject3.getString("status");
                    String nameOrigin = jSONObject3.getString("nameOrigin");
                    String nameDestination = jSONObject3.getString("nameDestination");
                    String nameTransit = jSONObject3.getString("nameTransit");
                    String qrcode = jSONObject3.getString("qrcode");
                    String loadType = jSONObject3.getString("loadType");

                    String transactionDate = jSONObject3.getString("transactionDate");
                    Date time1 = Calendar.getInstance().getTime();
                    try {
                        date1 = simpleDateFormat.parse(transactionDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        date1 = time1;
                    }
                    String tanggal1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date1);

                    String createdAt = jSONObject3.getString("createdAt");
                    Date time2 = Calendar.getInstance().getTime();
                    try {
                        date2 = simpleDateFormat.parse(createdAt);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        date2 = time2;
                    }
                    String tanggal2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date2);

                    String unloadingAt = jSONObject3.getString("unloadingAt");
                    if (unloadingAt.equals("null")){
                        txt_TanggalStatusUnloading.setText("-");
                    }else{
                        Date time3 = Calendar.getInstance().getTime();
                        try {
                            date3 = simpleDateFormat.parse(unloadingAt);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            date3 = time3;
                        }
                        String tanggal3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date3);
                        txt_TanggalStatusUnloading.setText(tanggal3);
                    }


                    txt_noTrans.setText(transNumber);
                    txt_transactionDate.setText(tanggal1);
                    txt_TanggalStatusLoading.setText(tanggal2);

                    txt_noTruck.setText(codeVehicle);
                    txt_NamaSupir.setText(nameDriver);
                    txt_asalTujuan.setText(nameOrigin + " - " + nameDestination);

                    if (nameTransit.equals("null")){
                        txt_transit.setText("-");
                    }else{
                        txt_transit.setText(nameTransit);
                    }

                    txt_TotalBarcdoe.setText(qrcode);
                    txt_Status.setText(status);

                    if (loadType.equals("null")){
                        txt_LoadType.setText("FCL");
                    }else{
                        txt_LoadType.setText(loadType);
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

}