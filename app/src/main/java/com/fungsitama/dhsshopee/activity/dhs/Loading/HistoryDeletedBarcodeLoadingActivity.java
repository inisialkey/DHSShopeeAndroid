package com.fungsitama.dhsshopee.activity.dhs.Loading;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fungsitama.dhsshopee.R;
import com.fungsitama.dhsshopee.adapter.ListBarcodeLoadingAdapter;
import com.fungsitama.dhsshopee.adapter.ListRiwayatDeleteBarcodeAdapter;
import com.fungsitama.dhsshopee.model.ListBarcodeLoadingModel;
import com.fungsitama.dhsshopee.model.ListDeleteBarcodeLoadingModel;
import com.fungsitama.dhsshopee.util.ApiConfig;
import com.fungsitama.dhsshopee.util.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HistoryDeletedBarcodeLoadingActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    SessionManager manager;
    private static String vtoken,vusername,vtypeLogin,vtypeUser;

    public SwipeRefreshLayout swipeRefreshLayout;

    public List<ListDeleteBarcodeLoadingModel> listDataModel;
    public ListRiwayatDeleteBarcodeAdapter mAdapter;
    private RecyclerView recyclerView;
    private TextView tvKosong;

    private String id, noTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_deleted_barcode_loading);

        Intent intent = getIntent();

        id = intent.getStringExtra("id");
        noTrans = intent.getStringExtra("noTrans");

        Log.d("TAG", "id trCargoId: " + id);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Riwayat Hapus Barcode "+ noTrans);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager = new SessionManager();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        vtoken = defaultSharedPreferences.getString("token", null);
        vusername = defaultSharedPreferences.getString("username", null);
        vtypeLogin = defaultSharedPreferences.getString("typeLogin", null);
        vtypeUser = defaultSharedPreferences.getString("typeUser", null);

        tvKosong = findViewById(R.id.tv_Kosong);

        recyclerView = (RecyclerView) findViewById(R.id.rv_daftar_list);
        this.listDataModel = new ArrayList();
        this.mAdapter = new ListRiwayatDeleteBarcodeAdapter(listDataModel, this, new ListRiwayatDeleteBarcodeAdapter.Onclick() {
            @Override
            public void onEvent(ListDeleteBarcodeLoadingModel modelItem, int pos) {

            }
        });

        this.recyclerView.setAdapter(this.mAdapter);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });

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

    public void onRefresh() {
        try {
            listDataModel.clear();
            tvKosong.setVisibility(View.GONE);
            getDaftarBarcodeLoading();
            mAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getDaftarBarcodeLoading() {

        swipeRefreshLayout.setRefreshing(true);
        RequestQueue newRequestQueue = Volley.newRequestQueue(HistoryDeletedBarcodeLoadingActivity.this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("trCargoId", id);
            jSONObject.put("page", 1);
            jSONObject.put("pageSize", 10000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.HistoryDeleteBarcodeLoading);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), jSONObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                Date date1;
                try {
                    listDataModel.clear();
                    JSONArray jSONArray = jSONObject.getJSONArray("data");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jsobj = jSONArray.getJSONObject(i);
                        String xid = jsobj.getString("id");
                        String xqrcode = jsobj.getString("qrcode");
                        String xstatus = jsobj.getString("status");
                        String xrowNumber = jsobj.getString("rowNumber");



                        String xcreatedAt = jsobj.getString("createdAt");
                        Date time1 = Calendar.getInstance().getTime();
                        try {
                            date1 = simpleDateFormat.parse(xcreatedAt);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            date1 = time1;
                        }

                        ListDeleteBarcodeLoadingModel listdata = new ListDeleteBarcodeLoadingModel(xid,xqrcode,xstatus,xrowNumber,date1);
                        listDataModel.add(listdata);
                    }

                    if (listDataModel.isEmpty()){
                        Log.d("TAG", "Data : " + " Data Tidak ditemukan");

                        tvKosong.setVisibility(View.VISIBLE);
                        tvKosong.setText("Barcode Masih Kosong");

                    }else {

                        tvKosong.setVisibility(View.GONE);
                        Log.d("TAG", "Data : " + " Data ditemukan");
                    }

                    mAdapter.notifyDataSetChanged();

                    swipeRefreshLayout.setRefreshing(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Context applicationContext = getApplicationContext();
                    StringBuilder sb = new StringBuilder();
                    sb.append("Error: ");
                    sb.append(e.getMessage());
                    //Toast.makeText(applicationContext, sb.toString(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("Error: ", volleyError.getMessage());
                String responseBody = null;
                try {
                    responseBody = new String(volleyError.networkResponse.data, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                JSONObject data = null;
                try {
                    data = new JSONObject(responseBody);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String msg = data.optString("message");
                new SweetAlertDialog(HistoryDeletedBarcodeLoadingActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                        .setContentText(msg)
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        }).show();
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

}