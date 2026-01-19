package com.fungsitama.dhsshopee.activity.dhs.Unloading;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fungsitama.dhsshopee.R;
import com.fungsitama.dhsshopee.activity.dhs.Loading.DaftarGambarBarcodeManualActivity;
import com.fungsitama.dhsshopee.activity.dhs.Loading.DetailListBarcodeActivity;
import com.fungsitama.dhsshopee.activity.dhs.Loading.ScanBarcodeLoadingActivity;
import com.fungsitama.dhsshopee.adapter.ListBarcodeLoadingAdapter;
import com.fungsitama.dhsshopee.adapter.ListBarcodeLoadingDetailAdapter;
import com.fungsitama.dhsshopee.adapter.ListBarcodeUnloadingAdapter;
import com.fungsitama.dhsshopee.adapter.ListBarcodeUnloadingDetailAdapter;
import com.fungsitama.dhsshopee.model.ListBarcodeDetailModel;
import com.fungsitama.dhsshopee.model.ListBarcodeLoadingModel;
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

public class DetailListBarcodeUnloadingActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    SessionManager manager;
    private static String vtoken,vusername,vtypeLogin,vtypeUser;

    public SwipeRefreshLayout swipeRefreshLayout;

    public List<ListBarcodeDetailModel> listDataModel;
    public ListBarcodeUnloadingDetailAdapter mAdapter;
    private RecyclerView recyclerView;
    private TextView tvKosong;

    private String id, noTrans;

    private NestedScrollView nestedScroll;
    private int page = 13;
    ProgressBar progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list_barcode);

        Intent intent = getIntent();

        id = intent.getStringExtra("id");
        noTrans = intent.getStringExtra("noTrans");

        Log.d("TAG", "id: " + id);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Detail Bongkar Muat Barang "+ noTrans);
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
        this.mAdapter = new ListBarcodeUnloadingDetailAdapter(listDataModel, this, new ListBarcodeUnloadingDetailAdapter.Onclick() {
            @Override
            public void onEvent(ListBarcodeDetailModel modelItem, int pos) {
                //String barCode = listDataModel.get(pos).getQrcode();
                if (listDataModel.get(pos).getManual().equals("true")){
                    if (listDataModel.get(pos).getStatus().equals("Loading")){
                        Intent intent = new Intent(DetailListBarcodeUnloadingActivity.this, DaftarGambarBarcodeManualActivity.class);

                        intent.putExtra("id", listDataModel.get(pos).getId());
                        intent.putExtra("barcode", listDataModel.get(pos).getQrcode());
                        intent.putExtra("trCargoId", id);
                        intent.putExtra("noTrans", noTrans);
                        intent.putExtra("vstatus", "Unloading");

                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(DetailListBarcodeUnloadingActivity.this, DaftarGambarBarcodeManualActivity.class);

                        intent.putExtra("id", listDataModel.get(pos).getId());
                        intent.putExtra("barcode", listDataModel.get(pos).getQrcode());
                        intent.putExtra("trCargoId", id);
                        intent.putExtra("noTrans", noTrans);
                        intent.putExtra("vstatus", "Unloading");

                        startActivity(intent);
                    }
                }

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


        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        nestedScroll = (NestedScrollView) findViewById(R.id.nestedScroll);

        nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                    int page1 = 10;
                    page = page+page1;
                    progressBar1.setVisibility(View.VISIBLE);
                    getDaftarBarcodeLoading(page);

                    //Toast.makeText(getContext(), "Refresh Aktif 0 " + progressBar1.getVisibility(), Toast.LENGTH_SHORT).show();

                }
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
            getDaftarBarcodeLoading(page);
            mAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getDaftarBarcodeLoading(int page) {

        //swipeRefreshLayout.setRefreshing(true);
        RequestQueue newRequestQueue = Volley.newRequestQueue(DetailListBarcodeUnloadingActivity.this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("trCargoId", id);
            jSONObject.put("page", 1);
            jSONObject.put("pageSize", page);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.DaftarBarcodeLoading);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), jSONObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                Date date1,date2;
                try {
                    listDataModel.clear();

                    progressBar1.setVisibility(View.GONE);

                    if (progressBar1.getVisibility() == View.GONE){
                        progressBar1.setVisibility(View.GONE);
                    }else {
                        swipeRefreshLayout.setRefreshing(true);
                    }

                    JSONArray jSONArray = jSONObject.getJSONArray("data");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jsobj = jSONArray.getJSONObject(i);
                        String xid = jsobj.getString("id");
                        String xqrcode = jsobj.getString("qrcode");
                        String xstatus = jsobj.getString("status");
                        String xrowNumber = jsobj.getString("rowNumber");
                        String xscan = jsobj.getString("scan");
                        String xmanual = jsobj.getString("manual");
                        String ximport = jsobj.getString("import");


                        String xcreatedAt = jsobj.getString("createdAt");
                        Date time1 = Calendar.getInstance().getTime();
                        try {
                            date1 = simpleDateFormat.parse(xcreatedAt);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            date1 = time1;
                        }

                        String xunloadingAt = jsobj.getString("unloadingAt");
                        if (xunloadingAt.equals("null")){
                            String sxUnloading = "-";

                            Date time2 = Calendar.getInstance().getTime();
                            try {
                                date2 = simpleDateFormat.parse(sxUnloading);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                date2 = null;
                            }

                        }else{
                            Date time2 = Calendar.getInstance().getTime();
                            try {
                                date2 = simpleDateFormat.parse(xunloadingAt);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                date2 = time2;
                            }

                        }

                        Log.d("TAG", "onResponseDate: " + date2 + "--" + xunloadingAt);

                        ListBarcodeDetailModel listdata = new ListBarcodeDetailModel(xid,xqrcode,xstatus,xrowNumber,xscan,xmanual, ximport,date1,date2);
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
                new SweetAlertDialog(DetailListBarcodeUnloadingActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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

    private void getHapusBarcode(String id, String barcode) {
        RequestQueue newRequestQueue = Volley.newRequestQueue(DetailListBarcodeUnloadingActivity.this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("trCargoId", id);
            jSONObject.put("qrcode", barcode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.DeleteBarcodeLoading);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), jSONObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {

                try {

                    String jumlahData = jSONObject.getString("data");
                    onRefresh();

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