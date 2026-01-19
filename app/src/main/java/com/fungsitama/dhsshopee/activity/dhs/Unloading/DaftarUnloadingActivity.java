package com.fungsitama.dhsshopee.activity.dhs.Unloading;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import com.fungsitama.dhsshopee.activity.dhs.Loading.DaftarLoadingActivity;
import com.fungsitama.dhsshopee.activity.dhs.Loading.DaftarLoadingDetailActivity;
import com.fungsitama.dhsshopee.activity.dhs.Loading.ScanBarcodeLoadingActivity;
import com.fungsitama.dhsshopee.activity.dhs.Loading.TambahMuatBarangActivity;
import com.fungsitama.dhsshopee.activity.main.NavMenuActivity;
import com.fungsitama.dhsshopee.adapter.ListDaftarLoadingAdapter;
import com.fungsitama.dhsshopee.model.ListDaftarLoadingModel;
import com.fungsitama.dhsshopee.util.ApiConfig;
import com.fungsitama.dhsshopee.util.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

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

public class DaftarUnloadingActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    SessionManager manager;
    private static String vtoken,vusername,vtypeLogin,vtypeUser;

    public SwipeRefreshLayout swipeRefreshLayout;

    private EditText etNoKendaraan,etKodeTujuan;
    private Button btnTambah,btnKembali;
    private String noKendaraan;

    FloatingActionButton btnTambahKendaraan;

    public TextView tvKodeKendaraan;
    private EditText etNoDo,etKoli,etBerat,etKuantitas,etDeskripsi;
    private SearchableSpinner spinKendaraan;
    private ArrayList<String> alKendaraan;
    public JSONArray rKendaraan;
    private String id,noDO,koli,asal,tujuan,berat,kuantitas,deskripsi,kodeKendaraan,namaSupir,status;
    private String title;

    public List<ListDaftarLoadingModel> listPengirimanModels;
    public ListDaftarLoadingAdapter mAdapter;
    private RecyclerView recyclerView;
    private TextView tvKosong;

    //-----------------------------------------------
    private LinearLayout ly_filterData;
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
    private ImageButton tbrefresh;
    private int year;
    private int year2;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_loading);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Bongkar Muat Barang");
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager = new SessionManager();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        vtoken = defaultSharedPreferences.getString("token", null);
        vusername = defaultSharedPreferences.getString("username", null);
        vtypeLogin = defaultSharedPreferences.getString("typeLogin", null);
        vtypeUser = defaultSharedPreferences.getString("typeUser", null);

        btnTambahKendaraan = findViewById(R.id.btn_TambahKendaraan);
        tvKosong = findViewById(R.id.tv_Kosong);


        ly_filterData = (LinearLayout) findViewById(R.id.filterData);
        ly_filterData.setVisibility(View.VISIBLE);
        tbrefresh = (ImageButton) findViewById(R.id.refresh);
        dateView = (TextView) findViewById(R.id.dt1awal);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dateView2 = (TextView) findViewById(R.id.dt2akhir);
        dateView3 = (TextView) findViewById(R.id.dt3send);
        calendar2 = Calendar.getInstance();
        year2 = calendar.get(Calendar.YEAR);
        month2 = calendar.get(Calendar.MONTH);
        day2 = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);
        showDate2(year2, month2 + 1, day2);


        recyclerView = (RecyclerView) findViewById(R.id.rv_daftar_list);
        this.listPengirimanModels = new ArrayList();
        this.mAdapter = new ListDaftarLoadingAdapter(listPengirimanModels, this, new ListDaftarLoadingAdapter.Onclick() {
            @Override
            public void onEvent(ListDaftarLoadingModel modelItem, int pos) {
                Intent intent = new Intent(DaftarUnloadingActivity.this, ScanBarcodeUnloadingActivity.class);
                intent.putExtra("id", listPengirimanModels.get(pos).getId());
                intent.putExtra("noTrans", listPengirimanModels.get(pos).getTransNumber());
                startActivity(intent);
            }
        }, new ListDaftarLoadingAdapter.Onclick() {
            @Override
            public void onEvent(ListDaftarLoadingModel modelItem, int pos) {
                new SweetAlertDialog(DaftarUnloadingActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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
                Intent intent = new Intent(DaftarUnloadingActivity.this, DaftarLoadingDetailActivity.class);
                intent.putExtra("id", listPengirimanModels.get(pos).getId());
                intent.putExtra("noTrans", listPengirimanModels.get(pos).getTransNumber());
                intent.putExtra("status", listPengirimanModels.get(pos).getStatus());
                startActivity(intent);
                //Toast.makeText(DaftarUnloadingActivity.this, "Klik Detail", Toast.LENGTH_SHORT).show();
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

        btnTambahKendaraan.setVisibility(View.GONE);
        btnTambahKendaraan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DaftarUnloadingActivity.this, TambahMuatBarangActivity.class);
                startActivity(intent);
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
        switch (item.getItemId()) {
            case R.id.action_go_home: {

                Intent intent = new Intent(DaftarUnloadingActivity.this, NavMenuActivity.class);
                startActivity(intent);
                finish();

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }



    public void setDate(View view) {
        showDialog(999);
        //Toast.makeText(getApplicationContext(), "Pilih tanggal awal", Toast.LENGTH_SHORT).show();
    }

    public void setDate2(View view) {
        showDialog(998);
        //Toast.makeText(getApplicationContext(), "Pilih tanggal akhir", Toast.LENGTH_SHORT).show();
    }

    public void setRefresh(View view) {
        onRefresh();
    }

    /* access modifiers changed from: protected */
    public Dialog onCreateDialog(int i) {
        if (i == 999) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, myDateListener, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            return datePickerDialog;
        } else if (i != 998) {
            return null;
        } else {
            DatePickerDialog datePickerDialog2 = new DatePickerDialog(this, myDateListener2, year2, month2, day2);
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
            filters.put("startDate", dateView.getText().toString());
            filters.put("endDate", dateView2.getText().toString());
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
                    new SweetAlertDialog(DaftarUnloadingActivity.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Informasi")
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
                new SweetAlertDialog(DaftarUnloadingActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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