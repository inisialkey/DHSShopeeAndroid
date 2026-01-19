package com.fungsitama.dhsshopee.activity.driver.KENDARAAN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.fungsitama.dhsshopee.util.ApiConfig;
import com.fungsitama.dhsshopee.util.SessionManager;
import com.fungsitama.dhsshopee.R;
import com.fungsitama.dhsshopee.adapter.ListTambahKendaraanAdapter;
import com.fungsitama.dhsshopee.model.ListKendaraanModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DaftarKendaraanActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    SessionManager manager;
    private static String vtoken,vusername,vtypeLogin,vtypeUser;

    public SwipeRefreshLayout swipeRefreshLayout;

    private EditText etNoKendaraan,etStnkExpired,etKirExpired;
    private Button btnTambah,btnKembali;
    private String noKendaraan,stnkExpired,kirExpired;

    FloatingActionButton btnTambahKendaraan;

    public List<ListKendaraanModel> listPengirimanModels;
    public ListTambahKendaraanAdapter mAdapter;
    private RecyclerView recyclerView;
    private TextView tvKosong;
    private String title;

    ///////////////////////////////
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    private ImageButton btndT1;
    private ImageButton btndT2;

    private String startDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    private String endDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_daftar_kendaraan);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        title = getResources().getString(R.string.daftar_kendaraan);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
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

        recyclerView = (RecyclerView) findViewById(R.id.rv_daftar_list);
        this.listPengirimanModels = new ArrayList();
        this.mAdapter = new ListTambahKendaraanAdapter(listPengirimanModels, this, new ListTambahKendaraanAdapter.Onclick() {
            @Override
            public void onEvent(ListKendaraanModel modelItem, int pos) {
                String id = listPengirimanModels.get(pos).getId();


                //Log.d("TAG", "id Kendaraan: " + id + " " + noKendaraan + " " + kodeKendaraan + " " + namaSupir);
/*
                Intent intent = new Intent(TambahDaftarKendaraanActivity.this, DaftarNomorDOActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("noKendaraan", noKendaraan);
                intent.putExtra("kodeKendaraan", kodeKendaraan);
                intent.putExtra("namaSupir", namaSupir);
                intent.putExtra("status", status);

                startActivity(intent);*/
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

        btnTambahKendaraan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DialogBoxTambahKendaraan();

                /*noKendaraan = etNoKendaraan.getText().toString();

                Log.d("TAG", "Nomor Kendaraan: " + noKendaraan);

                if (noKendaraan.isEmpty() || noKendaraan.equals(" ")){
                    new SweetAlertDialog(TambahDaftarKendaraanActivity.this, 3).setTitleText("Informasi")
                        .setContentText("No Kendaraan Tidak Boleh Kosong")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        })
                        .show();
                }else {

                    new SweetAlertDialog(TambahDaftarKendaraanActivity.this, 3).setTitleText("Informasi")
                        .setContentText("Yakin akan tambah data kendaraan ?")
                        .setConfirmText("Ya")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                getTambahKendaraan(noKendaraan);

                                Log.d("TAG", "DialogBoxTambahKendaraan: " + noKendaraan);

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
                }*/
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
            getListDataKendaraan();
            mAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public void DialogBoxTambahKendaraan(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_tambah_kendaraan,
                (LinearLayout) findViewById(R.id.dialog_tambah_kendaraan));
        AlertDialog alert = builder.create();
        alert.setView(view);

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

        etNoKendaraan = (EditText) view.findViewById(R.id.et_NoKendaraan);
        etStnkExpired = (EditText) view.findViewById(R.id.et_StnkExpired);
        etKirExpired = (EditText) view.findViewById(R.id.et_KirExpired);


        btnTambah = (Button) view.findViewById(R.id.btn_Tambah);
        btnKembali = (Button) view.findViewById(R.id.btn_Kembali);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                noKendaraan = etNoKendaraan.getText().toString();
                stnkExpired = etStnkExpired.getText().toString();
                kirExpired = etKirExpired.getText().toString();


                getTambahKendaraan(noKendaraan,stnkExpired,kirExpired);


                //Log.d("TAG", "DialogBoxTambahKendaraan: " + noKendaraan + "" + stnkExpired + "" + kirExpired);
                alert.dismiss();
            }
        });

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        alert.setCancelable(false);
        alert.show();
    }


    public void TanggalAwal(){
        // Create the DatePickerDialog instance
        DatePickerDialog datePicker = new DatePickerDialog(this,
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

            etStnkExpired.setText(year1 + "-" + month1 + "-" + day1);

            startDate  = year1 + "-" + month1 + "-" + day1;

            Log.d("TAG", "onDateSet1: " + startDate);

        }
    };

    public void TanggalAkhir(){
        // Create the DatePickerDialog instance
        DatePickerDialog datePicker = new DatePickerDialog(this,
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
            etKirExpired.setText(year1 + "-" + month1 + "-" + day1);

            endDate  = year1 + "-" + month1 + "-" + day1;

            Log.d("TAG", "onDateSet2: " + endDate);

        }
    };


    public void getTambahKendaraan(String nomorKendaraan, String stnkExpired, String kirExpired){
        RequestQueue newRequestQueue = Volley.newRequestQueue(DaftarKendaraanActivity.this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("code", nomorKendaraan);
            jSONObject.put("stnkExpired", stnkExpired);
            jSONObject.put("kirExpired", kirExpired);
            String a = jSONObject.toString().replaceAll("\\\\", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString().replaceAll("\\\\", "");
        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.TambahKendaraan);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    String msg = jSONObject.getString("message");
                    new SweetAlertDialog(DaftarKendaraanActivity.this, 2).setTitleText("Informasi")
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
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
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
                new SweetAlertDialog(DaftarKendaraanActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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

            public byte[] getBody() {
                byte[] bArr = null;
                try {
                    if (jSONObject2 != null) {
                        bArr = jSONObject2.getBytes("utf-8");
                        Log.d("TAG", "onKirim: " + jSONObject2);
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

    private void getListDataKendaraan() {
        swipeRefreshLayout.setRefreshing(true);
        RequestQueue newRequestQueue = Volley.newRequestQueue(this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("page", 1);
            jSONObject.put("pageSize", 10000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.DaftarKendaraan);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), jSONObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                Date date1,date2;
                try {
                    listPengirimanModels.clear();
                    JSONArray jSONArray = jSONObject.getJSONArray("data");
                    Log.d("TAG", "aa: " + jSONArray);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jsobj = jSONArray.getJSONObject(i);
                        String xid = jsobj.getString("id");
                        String xcode = jsobj.getString("code");

                        String xstatusUsage = jsobj.getString("statusUsage");

                        String xstnkExpired = jsobj.getString("stnkExpired");
                        Date time1 = Calendar.getInstance().getTime();
                        try {
                            date1 = simpleDateFormat.parse(xstnkExpired);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            date1 = time1;
                        }

                        String xkirExpired = jsobj.getString("kirExpired");
                        Date time2 = Calendar.getInstance().getTime();
                        try {
                            date2 = simpleDateFormat.parse(xkirExpired);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            date2 = time2;
                        }


                        ListKendaraanModel listdata = new ListKendaraanModel(xid,xcode, xstatusUsage,date1,date2);

                        listPengirimanModels.add(listdata);
                    }

                    if (listPengirimanModels.isEmpty()){
                        Log.d("TAG", "Data : " + " Data Tidak ditemukan");

                        tvKosong.setVisibility(View.VISIBLE);
                        tvKosong.setText(title + " " + getResources().getString(R.string.kosong));

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

}