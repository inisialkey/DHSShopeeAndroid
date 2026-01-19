package com.fungsitama.dhsshopee.activity.driver.KENDARAAN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
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
import android.widget.EditText;
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
import com.fungsitama.dhsshopee.activity.main.NavMenuActivity;
import com.fungsitama.dhsshopee.adapter.ListNomorDOAdapter;
import com.fungsitama.dhsshopee.model.ListNomorDOModel;
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

public class DaftarReturDOActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    SessionManager manager;
    private static String vtoken,vusername,vtypeLogin,vtypeUser;

    public SwipeRefreshLayout swipeRefreshLayout;

    FloatingActionButton btnTambahNoDO;
    public List<ListNomorDOModel> listNomorDOModels;
    public ListNomorDOAdapter mAdapter;
    private RecyclerView recyclerView;
    private TextView tvKosong;

    private EditText etNoKendaraan,etNoDo,etKodeAsal,etKodeTujuan,etKoli,etBerat,etKuantitas,etDeskripsi;
    private SearchableSpinner spinAsal,spinTujuan;
    private ArrayList<String> alAsal,alTujuan;
    public JSONArray rAsal,rTujuan;
    private Button btnSimpan;
    private Button btnTambah,btnKembali;
    private String id,noKendaraan,noDO,asal,tujuan,kodeKendaraan,namaSupir,status,deskripsi;
    private Double koli,berat,kuantitas;
    private String title;
    private String disableButtonTambah,disableScanBarcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_retur_d_o);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        noKendaraan = intent.getStringExtra("noKendaraan");
        kodeKendaraan = intent.getStringExtra("kodeKendaraan");
        namaSupir = intent.getStringExtra("namaSupir");
        status = intent.getStringExtra("status");

            disableButtonTambah = intent.getStringExtra("disableButtonTambah");
            disableScanBarcode = intent.getStringExtra("disableScanBarcode");

        Log.d("TAG", "trTrackingId: " + id);

        title = getResources().getString(R.string.daftar_no_do);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title + " - " + noKendaraan);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager = new SessionManager();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        vtoken = defaultSharedPreferences.getString("token", null);
        vusername = defaultSharedPreferences.getString("username", null);
        vtypeLogin = defaultSharedPreferences.getString("typeLogin", null);
        vtypeUser = defaultSharedPreferences.getString("typeUser", null);

        btnTambahNoDO = findViewById(R.id.btn_TambahNoDO);
        tvKosong = findViewById(R.id.tv_Kosong);

        recyclerView = (RecyclerView) findViewById(R.id.rv_daftar_list);
        this.listNomorDOModels = new ArrayList();
        this.mAdapter = new ListNomorDOAdapter(listNomorDOModels, this, new ListNomorDOAdapter.Onclick() {
            @Override
            public void onEvent(ListNomorDOModel modelItem, int pos) {

                String id = listNomorDOModels.get(pos).getId();
                String nomorDo = listNomorDOModels.get(pos).getDoNumber();
                String codeDriver = listNomorDOModels.get(pos).getCodeDriver();
                String codeVehicle = listNomorDOModels.get(pos).getCodeVehicle();
                String namaSupir = listNomorDOModels.get(pos).getNamaSupir();
                String asal = listNomorDOModels.get(pos).getNameOrigin();
                String tujuan = listNomorDOModels.get(pos).getNameDestination();
                String status = listNomorDOModels.get(pos).getStatus();

                Log.d("TAG", "id kendaraan: " + id + status);

                Intent intent = new Intent(DaftarReturDOActivity.this, DaftarScanNomorDOActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("nomorDo", nomorDo);
                intent.putExtra("codeDriver", codeDriver);
                intent.putExtra("codeVehicle", codeVehicle);
                intent.putExtra("namaSupir", namaSupir);
                intent.putExtra("asal", asal);
                intent.putExtra("tujuan", tujuan);
                intent.putExtra("status", status);
                intent.putExtra("disableScanBarcode", disableScanBarcode);

                startActivity(intent);
            }
        }, new ListNomorDOAdapter.Onclick() {
            @Override
            public void onEvent(ListNomorDOModel modelItem, int pos) {

                Intent intent = new Intent(DaftarReturDOActivity.this, DetailNomorDOActivity.class);
                intent.putExtra("id", listNomorDOModels.get(pos).getId());
                intent.putExtra("nomorKendaraan", listNomorDOModels.get(pos).getCodeVehicle());

                startActivity(intent);

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

        btnTambahNoDO.setOnClickListener(new View.OnClickListener() {
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

                Intent intent = new Intent(DaftarReturDOActivity.this, NavMenuActivity.class);
                startActivity(intent);
                finish();

                return true;
            }
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
            listNomorDOModels.clear();
            tvKosong.setVisibility(View.GONE);
            getListDataNomorDO();
            mAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void getAsal() {
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
        sb.append(ApiConfig.DaftarTujuan);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), jSONObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    rAsal = jSONObject.getJSONArray("data");

                    for (int i = 0; i < rAsal.length(); i++) {
                        try {
                            jSONObject = rAsal.getJSONObject(i);
                            //Log.d("TAG", "data tujuan: " + jSONObject);
                            ArrayList<String> arrayList = alAsal;
                            StringBuilder sb = new StringBuilder();
                            sb.append(jSONObject.getString("code"));
                            sb.append(" - ");
                            sb.append(jSONObject.getString("name"));
                            arrayList.add(sb.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    spinAsal.setAdapter(new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, alAsal));

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
    private String getKodeAsal(int i) {
        String str = "";
        try {
            str = rAsal.getJSONObject(i).getString("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    }

    private void getTujuan() {

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
        sb.append(ApiConfig.DaftarTujuan);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), jSONObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    rTujuan = jSONObject.getJSONArray("data");

                    for (int i = 0; i < rTujuan.length(); i++) {
                        try {
                            jSONObject = rTujuan.getJSONObject(i);
                            //Log.d("TAG", "data tujuan: " + jSONObject);
                            ArrayList<String> arrayList = alTujuan;
                            StringBuilder sb = new StringBuilder();
                            sb.append(jSONObject.getString("code"));
                            sb.append(" - ");
                            sb.append(jSONObject.getString("name"));
                            arrayList.add(sb.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    spinTujuan.setAdapter(new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, alTujuan));

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
            str = rTujuan.getJSONObject(i).getString("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    }


    public void DialogBoxTambahKendaraan(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_tambah_nomor_d_o,
                (LinearLayout) findViewById(R.id.dialog_tambah_nomor_d_o));
        AlertDialog alert = builder.create();
        alert.setView(view);

        etNoKendaraan = view.findViewById(R.id.et_NoKendaraan);
        etNoDo = view.findViewById(R.id.et_NoDo);

        etKodeAsal = view.findViewById(R.id.et_KodeAsal);
        spinAsal = view.findViewById(R.id.spin_Asal);

        etKodeTujuan = view.findViewById(R.id.et_KodeTujuan);
        spinTujuan = view.findViewById(R.id.spin_Tujuan);

        etKoli = view.findViewById(R.id.et_Koli);
        etBerat = view.findViewById(R.id.et_Berat);
        etKuantitas = view.findViewById(R.id.et_Kuantitas);
        etDeskripsi = view.findViewById(R.id.et_Deskripsi);

        spinAsal.setTitle("Pilih Asal");
        spinAsal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //String item = parentView.getItemAtPosition(position).toString();
                etKodeAsal.setText(getKodeAsal(position));
            }
            public void onNothingSelected(AdapterView<?> parentView) {
                //etKodeTujuan.setText(getKodeTujuan(0));
            }
        });
        alAsal = new ArrayList<>();
        getAsal();

        spinTujuan.setTitle("Pilih Tujuan");
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

        etNoKendaraan.setText(noKendaraan);


        btnTambah = (Button) view.findViewById(R.id.btn_Tambah);
        btnKembali = (Button) view.findViewById(R.id.btn_Kembali);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    noDO = etNoDo.getText().toString();
                    asal = etKodeAsal.getText().toString();

                    if (etKodeTujuan.getText().toString().equals("")) {
                        tujuan = "KTRPST";
                    }else{
                        tujuan = etKodeTujuan.getText().toString();
                    }

                    koli = Double.parseDouble(etKoli.getText().toString());
                    berat = Double.parseDouble(etBerat.getText().toString());
                    kuantitas = Double.parseDouble(etKuantitas.getText().toString());
                    deskripsi = etDeskripsi.getText().toString();

                    //Log.d("TAG", "DialogBoxTambahKendaraan: " + id + " " + noDO + " " + asal + " " + tujuan + " " + koli + " " + berat + " " + kuantitas + " " + deskripsi );


                    if (noDO.isEmpty() || asal.isEmpty() || tujuan.isEmpty() ||
                            koli.equals("") || berat.equals("") || kuantitas.equals("") || deskripsi.isEmpty()){
                        new SweetAlertDialog(DaftarReturDOActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                                .setContentText("Data tidak boleh kosong")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.cancel();
                                    }
                                }).show();
                    }else {
                        //Log.d("TAG", "simpanNoDO: " + idKendaraan + noKendaraan + " - " + noDO + " - " + asal + " - " + tujuan +  " - "  +  koli + " - " + berat + " - " + kuantitas + " - " + deskripsi);
                        new SweetAlertDialog(DaftarReturDOActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                                .setContentText("Yakin akan simpan data Nomor DO ?")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        getTambahNoDORetur(id,noDO,asal,tujuan,koli,berat,kuantitas,deskripsi);
                                        sweetAlertDialog.cancel();
                                        alert.dismiss();
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


                }catch (Exception e){
                    new SweetAlertDialog(DaftarReturDOActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                            .setContentText("Data tidak boleh kosong")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();
                                }
                            }).show();
                }
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

    public void getListDataNomorDO(){
        swipeRefreshLayout.setRefreshing(true);
        RequestQueue newRequestQueue = Volley.newRequestQueue(this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("trTrackingId", id);
            jSONObject.put("page", 1);
            jSONObject.put("pageSize", 10000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.DaftarNomorDO);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), jSONObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                Date date1;
                try {
                    listNomorDOModels.clear();
                    JSONArray jSONArray = jSONObject.getJSONArray("data");
                    //Log.d("TAG", "aa: " + jSONArray);


                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jsobj = jSONArray.getJSONObject(i);

                        String xid = jsobj.getString("id");
                        String xtrTrackingId = jsobj.getString("trTrackingId");
                        String xtransNumber = jsobj.getString("transNumber");
                        String xdoNumber = jsobj.getString("doNumber");
                        String xcodeOrigin = jsobj.getString("codeOrigin");
                        String xnameOrigin = jsobj.getJSONObject("dcOrigin").getString("name");
                        String xdcDestination = jsobj.getString("dcDestination");
                        String xnameDestination = jsobj.getJSONObject("dcDest").getString("name");
                        String xkoli = jsobj.getString("koli");
                        String xkg = jsobj.getString("kg");
                        String xqty = jsobj.getString("qty");
                        String xtotalQrCode = jsobj.getString("qtyBarcode");
                        String xdescription = jsobj.getString("description");
                        String xstatus = jsobj.getString("status");

                        String xcreatedAt = jsobj.getString("createdAt");
                        Date time1 = Calendar.getInstance().getTime();
                        try {
                            date1 = simpleDateFormat.parse(xcreatedAt);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            date1 = time1;
                        }

                        ListNomorDOModel listdata = new ListNomorDOModel(xid,xtrTrackingId,xtransNumber,xdoNumber,xcodeOrigin,xnameOrigin,xdcDestination,xnameDestination,xkoli,xkg,xqty,xtotalQrCode,xdescription,xstatus,kodeKendaraan,noKendaraan,namaSupir,date1);
                        listNomorDOModels.add(listdata);

                    }

                    if (listNomorDOModels.isEmpty()){
                        Log.d("TAG", "Data : " + " Data Tidak ditemukan");

                        tvKosong.setVisibility(View.VISIBLE);
                        tvKosong.setText(title + " (" + noKendaraan + ") " + getResources().getString(R.string.kosong));

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
                listNomorDOModels.clear();
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

    public void getTambahNoDORetur(String id, String noDo, String asal, String tujuan, Double koli, Double berat, Double kuantitas, String deskirpsi){

        Log.d("TAG", "simpanNoDO: " + id + " - " + noDO);

        RequestQueue newRequestQueue = Volley.newRequestQueue(DaftarReturDOActivity.this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("trTrackingId", id);
            jSONObject.put("doNumber", noDo);
            jSONObject.put("codeOrigin", asal);
            jSONObject.put("dcDestination", tujuan);
            jSONObject.put("koli", koli);
            jSONObject.put("kg", berat);
            jSONObject.put("qty", kuantitas);
            jSONObject.put("description", deskripsi);
            String a = jSONObject.toString().replaceAll("\\\\", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString().replaceAll("\\\\", "");

        Log.d("TAG", "getTambahNomorDO: " + jSONObject2);

        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.TambahNomorDO);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    String msg = jSONObject.getString("message");
                    JSONObject data = jSONObject.getJSONObject("data");
                    String id = data.getString("id");

                    Log.d("TAG", "id tracking creat: ");

                    new SweetAlertDialog(DaftarReturDOActivity.this, 2).setTitleText("Informasi")
                            .setContentText(msg)
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    //onRefresh();
                                    //disini pindah otomatis

                                    Intent intent = new Intent(DaftarReturDOActivity.this, DaftarScanNomorDOActivity.class);

                                    intent.putExtra("id", id);
                                    intent.putExtra("nomorDo", noDO);
                                    intent.putExtra("codeDriver", kodeKendaraan);
                                    intent.putExtra("codeVehicle", noKendaraan);
                                    intent.putExtra("tujuan", tujuan);
                                    intent.putExtra("status", status);

                                    intent.putExtra("disableScanBarcode", disableScanBarcode);

                                    (DaftarReturDOActivity.this).finish();
                                    intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    startActivity(intent);

                                    sweetAlertDialog.cancel();

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
                new SweetAlertDialog(DaftarReturDOActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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
}