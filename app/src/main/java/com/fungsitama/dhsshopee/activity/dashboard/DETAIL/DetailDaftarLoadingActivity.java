package com.fungsitama.dhsshopee.activity.dashboard.DETAIL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fungsitama.dhsshopee.R;
import com.fungsitama.dhsshopee.activity.dhs.Loading.DaftarLoadingActivity;
import com.fungsitama.dhsshopee.activity.dhs.Loading.DaftarLoadingDetailActivity;
import com.fungsitama.dhsshopee.activity.dhs.Loading.ScanBarcodeLoadingActivity;
import com.fungsitama.dhsshopee.activity.dhs.Unloading.DaftarUnloadingActivity;
import com.fungsitama.dhsshopee.activity.main.NavMenuActivity;
import com.fungsitama.dhsshopee.adapter.ListDaftarLoadingAdapter;
import com.fungsitama.dhsshopee.model.ListDaftarLoadingModel;
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

public class DetailDaftarLoadingActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    SessionManager manager;
    private static String vtoken,vusername,vtypeLogin,vtypeUser,startDate,endDate;

    public SwipeRefreshLayout swipeRefreshLayout;

    public List<ListDaftarLoadingModel> listPengirimanModels;
    public ListDaftarLoadingAdapter mAdapter;
    private RecyclerView recyclerView;
    private TextView tvKosong;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_daftar_loading);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Daftar Muat Barang");
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager = new SessionManager();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        vtoken = defaultSharedPreferences.getString("token", null);
        vusername = defaultSharedPreferences.getString("username", null);
        vtypeLogin = defaultSharedPreferences.getString("typeLogin", null);
        vtypeUser = defaultSharedPreferences.getString("typeUser", null);
        startDate = defaultSharedPreferences.getString("vstratDate", null);
        endDate = defaultSharedPreferences.getString("vendDate", null);

        tvKosong = findViewById(R.id.tv_Kosong);

        recyclerView = (RecyclerView) findViewById(R.id.rv_daftar_list);
        this.listPengirimanModels = new ArrayList();
        this.mAdapter = new ListDaftarLoadingAdapter(listPengirimanModels, this, new ListDaftarLoadingAdapter.Onclick() {
            @Override
            public void onEvent(ListDaftarLoadingModel modelItem, int pos) {
                Intent intent = new Intent(DetailDaftarLoadingActivity.this, ScanBarcodeLoadingActivity.class);
                intent.putExtra("id", listPengirimanModels.get(pos).getId());
                intent.putExtra("noTrans", listPengirimanModels.get(pos).getTransNumber());
                startActivity(intent);
            }
        }, new ListDaftarLoadingAdapter.Onclick() {
            @Override
            public void onEvent(ListDaftarLoadingModel modelItem, int pos) {
                new SweetAlertDialog(DetailDaftarLoadingActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                        .setContentText("Apakah anda yakin akan menghapus nomor transkasi ini ?")
                        .setConfirmText("Ya")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                String id = listPengirimanModels.get(pos).getId();

                                getDeleteNoTranskasi(id);

                                sweetAlertDialog.cancel();
                            }
                        })
                        .setCancelText("Tidak")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        })
                        .show();
            }
        }, new ListDaftarLoadingAdapter.Onclick() {
            @Override
            public void onEvent(ListDaftarLoadingModel modelItem, int pos) {
                Intent intent = new Intent(DetailDaftarLoadingActivity.this, DaftarLoadingDetailActivity.class);
                intent.putExtra("id", listPengirimanModels.get(pos).getId());
                intent.putExtra("noTrans", listPengirimanModels.get(pos).getTransNumber());
                intent.putExtra("status", listPengirimanModels.get(pos).getStatus());
                startActivity(intent);
                //Toast.makeText(DetailDaftarLoadingActivity.this, "Klik Detail", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_go_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_go_home) {

            Intent intent = new Intent(
                    DetailDaftarLoadingActivity.this,
                    NavMenuActivity.class
            );

            startActivity(intent);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    swipeRefreshLayout.setRefreshing(true);
                    onRefresh();
                    swipeRefreshLayout.setRefreshing(false);
                } catch (Exception unused) {
                }
            }
        }, 1000);

    }

    public void onRefresh() {
        try {
            listPengirimanModels.clear();
            tvKosong.setVisibility(View.GONE);
            getDaftarLoading();
            mAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getDaftarLoading() {
        swipeRefreshLayout.setRefreshing(true);
        RequestQueue newRequestQueue = Volley.newRequestQueue(this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("page", 1);
            jSONObject.put("pageSize", 10000);
            JSONObject filters = new JSONObject();
            filters.put("startDate", startDate);
            filters.put("endDate", endDate);
            filters.put("status", "Loading");
            jSONObject.put("filters", filters);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.DaftarLoading);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), jSONObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                Date date1,date2,date3;
                try {
                    listPengirimanModels.clear();
                    JSONArray jSONArray = jSONObject.getJSONArray("data");
                    Log.d("TAG", "aa: " + jSONArray);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jsobj = jSONArray.getJSONObject(i);
                        String xid = jsobj.getString("id");
                        String xtransNumber = jsobj.getString("transNumber");
                        String xnameDriver = jsobj.getString("nameDriver");
                        String xcodeVehicle = jsobj.getString("codeVehicle");
                        String xnameOrigin = jsobj.getString("nameOrigin");
                        String xnameDestination = jsobj.getString("nameDestination");
                        String xnameTransit = jsobj.getString("nameTransit");
                        String xstatus = jsobj.getString("status");
                        String xqrcode = jsobj.getString("qrcode");
                        String xloadType = jsobj.getString("loadType");

                        String xcheckinTransId = jsobj.getString("transactionDate");
                        Date time1 = Calendar.getInstance().getTime();
                        try {
                            date1 = simpleDateFormat.parse(xcheckinTransId);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            date1 = time1;
                        }

                        String xcreatedAt = jsobj.getString("createdAt");
                        Date time2 = Calendar.getInstance().getTime();
                        try {
                            date2 = simpleDateFormat.parse(xcreatedAt);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            date2 = time2;
                        }

                        String xunloadingAt = jsobj.getString("unloadingAt");
                        Date time3 = Calendar.getInstance().getTime();
                        try {
                            date3 = simpleDateFormat.parse(xunloadingAt);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            date3 = time3;
                        }

                        ListDaftarLoadingModel listdata = new ListDaftarLoadingModel(xid,xtransNumber,
                                xnameDriver,xcodeVehicle,xnameOrigin,xnameDestination,xnameTransit,
                                xstatus,xqrcode,xloadType,date1,date2,date3);

                        listPengirimanModels.add(listdata);
                    }

                    if (listPengirimanModels.isEmpty()){
                        Log.d("TAG", "Data : " + " Data Tidak ditemukan");

                        tvKosong.setVisibility(View.VISIBLE);
                        tvKosong.setText(getResources().getString(R.string.kosong));

                    }else {

                        tvKosong.setVisibility(View.GONE);
                        Log.d("TAG", "Data : " + " Data ditemukan");
                    }

                    mAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Context applicationContext = getApplicationContext();
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
                listPengirimanModels.clear();
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


    private void getDeleteNoTranskasi(String id) {
        swipeRefreshLayout.setRefreshing(true);
        RequestQueue newRequestQueue = Volley.newRequestQueue(this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.HapusDaftarLoading);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), jSONObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                Date date1;
                try {

                    String msg = jSONObject.getString("message");
                    new SweetAlertDialog(DetailDaftarLoadingActivity.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Informasi")
                            .setContentText(msg)
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    onRefresh();
                                    sweetAlertDialog.cancel();
                                }
                            }).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                    Context applicationContext = getApplicationContext();
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
                new SweetAlertDialog(DetailDaftarLoadingActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                        .setContentText(msg)
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                                swipeRefreshLayout.setRefreshing(false);
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