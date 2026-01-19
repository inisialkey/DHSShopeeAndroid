package com.fungsitama.dhsshopee.activity.driver.KENDARAAN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
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
import com.fungsitama.dhsshopee.activity.main.NavMenuActivity;
import com.fungsitama.dhsshopee.adapter.ListKendaraanAdapter;
import com.fungsitama.dhsshopee.model.ListPengirimanModel;
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

public class DaftarPengirimanActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

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

    public List<ListPengirimanModel> listPengirimanModels;
    public ListKendaraanAdapter mAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_kendaraan);

        title = getResources().getString(R.string.daftar_pengiriman);
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
        this.mAdapter = new ListKendaraanAdapter(listPengirimanModels, this, new ListKendaraanAdapter.Onclick() {
            @Override
            public void onEvent(ListPengirimanModel modelItem, int pos) {

                String id = listPengirimanModels.get(pos).getId();
                String noKendaraan = listPengirimanModels.get(pos).getCodeVehicle();
                String kodeKendaraan = listPengirimanModels.get(pos).getCodeDriver();
                String namaSupir = listPengirimanModels.get(pos).getNamaSupir();
                String status = listPengirimanModels.get(pos).getStatus();

                Log.d("TAG", "id Kendaraan: " + id + " " + noKendaraan + " " + kodeKendaraan + " " + namaSupir);

                Intent intent = new Intent(DaftarPengirimanActivity.this, DaftarNomorDOActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("noKendaraan", noKendaraan);
                intent.putExtra("kodeKendaraan", kodeKendaraan);
                intent.putExtra("namaSupir", namaSupir);
                intent.putExtra("status", status);

                intent.putExtra("disableButtonTambah", "1");
                intent.putExtra("disableScanBarcode", "1");

                startActivity(intent);

            }
        }, new ListKendaraanAdapter.Onclick() {
            @Override
            public void onEvent(ListPengirimanModel modelItem, int pos) {
                updateStatusOtw(listPengirimanModels.get(pos).getId(), listPengirimanModels.get(pos).getCodeVehicle());
            }
        },new ListKendaraanAdapter.Onclick() {
            @Override
            public void onEvent(ListPengirimanModel modelItem, int pos) {

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

                Intent intent = new Intent(DaftarPengirimanActivity.this, NavMenuActivity.class);
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
            getListDataKendaraan();
            mAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getDaftarKendaraan() {

        RequestQueue newRequestQueue = Volley.newRequestQueue(this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("page", 1);
            jSONObject.put("pageSize", 10000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString();

        Log.d("TAG", "getDaftarKendaraan: " + jSONObject2);

        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.DaftarKendaraan);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), jSONObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    rKendaraan = jSONObject.getJSONArray("data");

                    for (int i = 0; i < rKendaraan.length(); i++) {
                        try {
                            jSONObject = rKendaraan.getJSONObject(i);
                            ArrayList<String> arrayList = alKendaraan;
                            StringBuilder sb = new StringBuilder();
                            sb.append(jSONObject.getString("code"));
                            /*sb.append(" - ");
                            sb.append(jSONObject.getString("name"));*/
                            arrayList.add(sb.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    spinKendaraan.setAdapter(new ArrayAdapter(DaftarPengirimanActivity.this, android.R.layout.simple_list_item_1, alKendaraan));

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

    private String getKodeTujuan(int i) {
        String str = "";
        try {
            str = rKendaraan.getJSONObject(i).getString("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    }

    public void DialogBoxTambahKendaraan(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_pilih_kendaraan,
                (LinearLayout) findViewById(R.id.dialog_pilih_kendaraan));
        AlertDialog alert = builder.create();
        alert.setView(view);


        tvKodeKendaraan = view.findViewById(R.id.tv_KodeKendaraan);
        etKodeTujuan = view.findViewById(R.id.et_KodeKendaraan);
        spinKendaraan = view.findViewById(R.id.spin_Kendaraan);

        spinKendaraan.setTitle("Pilih Kendaraan");


        spinKendaraan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //String item = parentView.getItemAtPosition(position).toString();
                etKodeTujuan.setText(getKodeTujuan(position));
            }

            public void onNothingSelected(AdapterView<?> parentView) {
                //etKodeTujuan.setText(getKodeTujuan(0));
            }
        });
        alKendaraan = new ArrayList<>();

        getDaftarKendaraan();

        btnTambah = (Button) view.findViewById(R.id.btn_Tambah);
        btnKembali = (Button) view.findViewById(R.id.btn_Kembali);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                kodeKendaraan = etKodeTujuan.getText().toString();


                getTambahKendaraan(kodeKendaraan);


                Log.d("TAG", "DialogBoxTambahKendaraan: " + kodeKendaraan);
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

    /*public void DialogBoxTambahKendaraan(){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

        tvKodeTujuan = findViewById(R.id.tv_KodeTujuan);

        etKodeTujuan = new EditText(this);
        builder.setView(etKodeTujuan);

        spinTujuan = new SearchableSpinner(this);
        builder.setView(spinTujuan);



        spinTujuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //String item = parentView.getItemAtPosition(position).toString();
                etKodeTujuan.setText(getKodeTujuan(position));
            }

            public void onNothingSelected(AdapterView<?> parentView) {
                //etKodeTujuan.setText(getKodeTujuan(0));
            }
        });
        alTujuan = new ArrayList<>();

        getTujuan();


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //m_Text = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }*/


    public void getTambahKendaraan(String nomorKendaraan){
        RequestQueue newRequestQueue = Volley.newRequestQueue(DaftarPengirimanActivity.this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("codeVehicle", nomorKendaraan);
            String a = jSONObject.toString().replaceAll("\\\\", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString().replaceAll("\\\\", "");
        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.TambahPengiriman);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    String msg = jSONObject.getString("message");
                    new SweetAlertDialog(DaftarPengirimanActivity.this, 2).setTitleText("Informasi")
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
                new SweetAlertDialog(DaftarPengirimanActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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
            JSONObject filters = new JSONObject();
            filters.put("startDate", dateView.getText().toString());
            filters.put("endDate", dateView2.getText().toString());
            jSONObject.put("filters", filters);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.DaftarPengiriman);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), jSONObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                Date date1;
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

                        String xcheckinTransId = jsobj.getString("transactionDate");
                        Date time1 = Calendar.getInstance().getTime();
                        try {
                            date1 = simpleDateFormat.parse(xcheckinTransId);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            date1 = time1;
                        }

                        String xcodeDriver = jsobj.getString("codeDriver");
                        String xcodeVehicle = jsobj.getString("codeVehicle");
                        String xnamaPenerima = vusername;
                        String xstatus = jsobj.getString("status");
                        String xqtyDO = jsobj.getString("qtyDO");
                        String xqtyBarcode = jsobj.getString("qtyBarcode");

                        ListPengirimanModel listdata = new ListPengirimanModel(xid,xtransNumber, date1,xcodeDriver,xcodeVehicle, xnamaPenerima,xstatus,xqtyDO,xqtyBarcode);

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

    private void updateStatusOtw(String id, String sNoKendaraan){
        new SweetAlertDialog(DaftarPengirimanActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                .setContentText("Yakin akan update Status ke OTW ?")
                .setConfirmText("Ya")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        RequestQueue newRequestQueue = Volley.newRequestQueue(DaftarPengirimanActivity.this);
                        JSONObject jSONObject = new JSONObject();
                        try {
                            jSONObject.put("id", id);
                            String a = jSONObject.toString().replaceAll("\\\\", "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        final String jSONObject2 = jSONObject.toString().replaceAll("\\\\", "");

                        Log.d("TAG", "getId: " + jSONObject2);

                        StringBuilder sb = new StringBuilder();
                        sb.append(ApiConfig.UpdateStatusOTW);
                        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
                            public void onResponse(JSONObject jSONObject) {
                                try {
                                    String msg = jSONObject.getString("message");
                                    new SweetAlertDialog(DaftarPengirimanActivity.this, 2).setTitleText("Informasi")
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
                                new SweetAlertDialog(DaftarPengirimanActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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


}