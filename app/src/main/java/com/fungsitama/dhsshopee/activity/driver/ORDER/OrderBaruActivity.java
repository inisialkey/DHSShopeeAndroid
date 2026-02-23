package com.fungsitama.dhsshopee.activity.driver.ORDER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fungsitama.dhsshopee.activity.driver.KENDARAAN.DaftarPengirimanActivity;
import com.fungsitama.dhsshopee.util.ApiConfig;
import com.fungsitama.dhsshopee.util.SessionManager;
import com.fungsitama.dhsshopee.R;
import com.fungsitama.dhsshopee.activity.main.NavMenuActivity;
import com.fungsitama.dhsshopee.adapter.ListDetailNomorDOAdapter;
import com.fungsitama.dhsshopee.model.ListDetailNomorDOModel;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
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

public class OrderBaruActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    SessionManager manager;
    private static String vtoken,vusername,vtypeLogin,vtypeUser;

    private LinearLayout lyTambahKendaraan,lyTambahDO,lyformTambahNomorDO,lyKodeDOdanTujuan,lyDaftarBarcode;

    public SwipeRefreshLayout swipeRefreshLayout;

    public TextView tvKodeKendaraan,iconCheklistKendaraan;
    private SearchableSpinner spinKendaraan;
    private ArrayList<String> alKendaraan;
    public JSONArray rKendaraan;
    private String idTracking,codeVehicle;

    private TextView txtnoDO,txtTujuan,iconCheklistNomorDO;
    private EditText etNoKendaraan,etNoDo,etKodeAsal,etKodeTujuan,etKoli,etBerat,etKuantitas,etDeskripsi;
    private SearchableSpinner spinAsal,spinTujuan;
    private ArrayList<String> alAsal,alTujuan;
    public JSONArray rAsal,rTujuan;
    private Button btnSimpan;
    private String idNoDO,doNumber,dcDestination,idKendaraan,noKendaraan,kodeKendaraan,noDO,asal,tujuan,deskripsi;
    private Double koli,berat,kuantitas;


    private DecoratedBarcodeView barcodeView;
    String stat = "";
    public Switch scanQrCode;
    public Boolean scan;
    private LinearLayout viewScanQrCode,lyScaner;
    private TextView hasilcari;
    private String disableScanBarcode;

    public List<ListDetailNomorDOModel> listDetailNomorDOModels;
    public ListDetailNomorDOAdapter mAdapter;
    private RecyclerView recyclerView;
    private TextView tvKosong;

    private Button btnPilihKendaraan,btnSimpanDO,btnUpdateStatus,btnTambahNoDO;
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_baru);

        title = getResources().getString(R.string.order_baru);
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

        lyTambahKendaraan = findViewById(R.id.ly_TambahKendaraan);
        lyTambahDO = findViewById(R.id.ly_TambahDO);
        lyformTambahNomorDO = findViewById(R.id.ly_formTambahNomorDO);
        lyKodeDOdanTujuan = findViewById(R.id.ly_KodeDOdanTujuan);
        lyDaftarBarcode = findViewById(R.id.ly_DaftarBarcode);

        iconCheklistKendaraan = findViewById(R.id.icon_CheklistKendaraan);
        iconCheklistNomorDO = findViewById(R.id.icon_CheklistNomorDO);

        tvKodeKendaraan = findViewById(R.id.tv_KodeKendaraan);
        etKodeTujuan = findViewById(R.id.et_KodeKendaraan);
        spinKendaraan = findViewById(R.id.spin_Kendaraan);

        spinKendaraan.setTitle("Pilih Kendaraan");
        spinKendaraan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //String item = parentView.getItemAtPosition(position).toString();
                etKodeTujuan.setText(getKodeKendaraan(position));
            }

            public void onNothingSelected(AdapterView<?> parentView) {
                //etKodeTujuan.setText(getKodeTujuan(0));
            }
        });
        alKendaraan = new ArrayList<>();

        getDaftarKendaraan();




        etNoKendaraan = findViewById(R.id.et_NoKendaraan);
        etNoDo = findViewById(R.id.et_NoDo);

        etKodeAsal = findViewById(R.id.et_KodeAsal);
        spinAsal = findViewById(R.id.spin_Asal);

        etKodeTujuan = findViewById(R.id.et_KodeTujuan);
        spinTujuan = findViewById(R.id.spin_Tujuan);

        etKoli = findViewById(R.id.et_Koli);
        etBerat = findViewById(R.id.et_Berat);
        etKuantitas = findViewById(R.id.et_Kuantitas);
        etDeskripsi = findViewById(R.id.et_Deskripsi);


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

        txtnoDO = findViewById(R.id.txt_noDO);
        txtTujuan = findViewById(R.id.txt_Tujuan);


        viewScanQrCode = findViewById(R.id.view_ScanQrCode);
        scanQrCode = findViewById(R.id.scan_QrCode);
        hasilcari = findViewById(R.id.hasilcari);
        tvKosong = findViewById(R.id.tv_Kosong);

        lyScaner = (LinearLayout) findViewById(R.id.scanner);
        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        barcodeView.decodeContinuous(callback);
        barcodeView.setStatusText(getString(R.string.scan_qr_code));

        recyclerView = (RecyclerView) findViewById(R.id.rv_daftar_list);
        this.listDetailNomorDOModels = new ArrayList();
        this.mAdapter = new ListDetailNomorDOAdapter(listDetailNomorDOModels, this, new ListDetailNomorDOAdapter.Onclick() {
            @Override
            public void onEvent(ListDetailNomorDOModel modelItem, int pos) {
                String id = listDetailNomorDOModels.get(pos).getId();

                /*Log.d("TAG", "id truck: " + id);

                Intent intent = new Intent(DaftarNomorDOActivity.this, ScanNomorDOActivity.class);
                intent.putExtra("id", id);

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

        scanQrCode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    scan = Boolean.valueOf("true");
                    viewScanQrCode.setVisibility(View.VISIBLE);
                    onResume();
                } else {
                    scan = Boolean.valueOf("false");
                    viewScanQrCode.setVisibility(View.GONE);
                    onPause();
                }
            }
        });


        btnPilihKendaraan = findViewById(R.id.btn_PilihKendaraan);
        btnSimpanDO = findViewById(R.id.btn_SimpanDO);
        btnUpdateStatus = findViewById(R.id.btn_UpdateStatus);
        btnTambahNoDO = findViewById(R.id.btn_TambahNoDO);

        btnPilihKendaraan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                kodeKendaraan = etKodeTujuan.getText().toString();


                new SweetAlertDialog(OrderBaruActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                    .setContentText("Yakin kendaraan " + kodeKendaraan + " akan dipilih ?")
                    .setConfirmText("Ya")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            getTambahKendaraan(kodeKendaraan);
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




                /*spinKendaraan.setVisibility(View.GONE);
                tvKodeKendaraan.setVisibility(View.VISIBLE);
                iconCheklistKendaraan.setVisibility(View.VISIBLE);
                lyTambahDO.setVisibility(View.VISIBLE);

                btnPilihKendaraan.setVisibility(View.GONE);
                btnSimpanDO.setVisibility(View.VISIBLE);*/


            }
        });

        btnSimpanDO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    noDO = etNoDo.getText().toString();

                    if (etKodeAsal.getText().toString().equals("")) {
                        asal = "KTRPST";
                    }else{
                        asal = etKodeAsal.getText().toString();
                    }

                    tujuan = etKodeTujuan.getText().toString();
                    koli = Double.parseDouble(etKoli.getText().toString());
                    berat = Double.parseDouble(etBerat.getText().toString());
                    kuantitas = Double.parseDouble(etKuantitas.getText().toString());
                    deskripsi = etDeskripsi.getText().toString();


                    if (noDO.isEmpty() || asal.isEmpty() || tujuan.isEmpty() ||
                            koli.equals("") || berat.equals("") || kuantitas.equals("") || deskripsi.isEmpty()){
                        new SweetAlertDialog(OrderBaruActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                                .setContentText("Data tidak boleh kosong")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.cancel();
                                    }
                                }).show();
                    }else {
                        //Log.d("TAG", "simpanNoDO: " + idKendaraan + noKendaraan + " - " + noDO + " - " + asal + " - " + tujuan +  " - "  +  koli + " - " + berat + " - " + kuantitas + " - " + deskripsi);
                        new SweetAlertDialog(OrderBaruActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                                .setContentText("Yakin akan simpan data Nomor DO ?")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        getTambahNomorDO();
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

                }catch (Exception e){
                    e.printStackTrace();

                    new SweetAlertDialog(OrderBaruActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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

        btnUpdateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateStatusOtw(idTracking);

                //Toast.makeText(OrderBaruActivity.this, "Berhasil Di Update", Toast.LENGTH_SHORT).show();


            }
        });

        btnTambahNoDO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(OrderBaruActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                        .setContentText("Yakin akan tambah DO baru ?")
                        .setConfirmText("Ya")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                lyformTambahNomorDO.setVisibility(View.VISIBLE);

                                etNoDo.setText("");
                                etKoli.setText("");
                                //spinTujuan.dispatchDisplayHint(View.VISIBLE);
                                etBerat.setText("");
                                etKuantitas.setText("");
                                etDeskripsi.setText("");

                                iconCheklistNomorDO.setVisibility(View.GONE);
                                lyKodeDOdanTujuan.setVisibility(View.GONE);

                                btnSimpanDO.setVisibility(View.VISIBLE);

                                btnUpdateStatus.setVisibility(View.GONE);
                                btnTambahNoDO.setVisibility(View.GONE);

                                lyDaftarBarcode.setVisibility(View.GONE);
                                scanQrCode.setChecked(false);
                                scan = Boolean.valueOf("false");
                                viewScanQrCode.setVisibility(View.GONE);
                                onPause();
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

            Intent intent = new Intent(OrderBaruActivity.this, NavMenuActivity.class);
            startActivity(intent);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
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
                    spinKendaraan.setAdapter(new ArrayAdapter(OrderBaruActivity.this, android.R.layout.simple_list_item_1, alKendaraan));

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
    private String getKodeKendaraan(int i) {
        String str = "";
        try {
            str = rKendaraan.getJSONObject(i).getString("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
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

    public void getTambahKendaraan(String nomorKendaraan){
        RequestQueue newRequestQueue = Volley.newRequestQueue(OrderBaruActivity.this);
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

                    JSONObject jsobj = jSONObject.getJSONObject("data");

                    idTracking = jsobj.getString("id");
                    codeVehicle = jsobj.getString("codeVehicle");


                    new SweetAlertDialog(OrderBaruActivity.this, 2).setTitleText("Informasi")
                            .setContentText(msg)
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                public void onClick(SweetAlertDialog sweetAlertDialog) {


                                    spinKendaraan.setVisibility(View.GONE);

                                    tvKodeKendaraan.setVisibility(View.VISIBLE);
                                    tvKodeKendaraan.setText(codeVehicle);

                                    iconCheklistKendaraan.setVisibility(View.VISIBLE);
                                    lyTambahDO.setVisibility(View.VISIBLE);

                                    btnPilihKendaraan.setVisibility(View.GONE);
                                    btnSimpanDO.setVisibility(View.VISIBLE);

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
                new SweetAlertDialog(OrderBaruActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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

    public void getTambahNomorDO(){

        //Log.d("TAG", "simpanNoDO: " + idKendaraan + noKendaraan + " - " + noDO + " - " + asal + " - " + tujuan +  " - "  +  koli + " - " + berat + " - " + kuantitas + " - " + deskripsi);

        RequestQueue newRequestQueue = Volley.newRequestQueue(OrderBaruActivity.this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("trTrackingId", idTracking);
            jSONObject.put("doNumber", noDO);
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
                    idNoDO = data.getString("id");
                    doNumber = data.getString("doNumber");
                    dcDestination = data.getString("nameDestination");


                    Log.d("TAG", "id tracking creat: ");

                    new SweetAlertDialog(OrderBaruActivity.this, 2).setTitleText("Informasi")
                            .setContentText(msg)
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                public void onClick(SweetAlertDialog sweetAlertDialog) {


                                    iconCheklistNomorDO.setVisibility(View.VISIBLE);
                                    lyformTambahNomorDO.setVisibility(View.GONE);

                                    lyKodeDOdanTujuan.setVisibility(View.VISIBLE);

                                    txtnoDO.setText(doNumber);
                                    txtTujuan.setText(dcDestination);

                                    lyDaftarBarcode.setVisibility(View.VISIBLE);

                                    btnSimpanDO.setVisibility(View.GONE);
                                    btnUpdateStatus.setVisibility(View.VISIBLE);
                                    btnTambahNoDO.setVisibility(View.VISIBLE);

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
                new SweetAlertDialog(OrderBaruActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                barcodeView.pause();
                if (stat != result.getText()) {
                    MediaPlayer mp = MediaPlayer.create(OrderBaruActivity.this, R.raw.sound);
                    mp.start();

                    String hasilScane =  result.getText();
                    //Toast.makeText(ScanNomorDOActivity.this, "Barcode" + result.getText(), Toast.LENGTH_SHORT).show();

                    Log.d("TAG", "barcodeResult: " + idNoDO + " - " + hasilScane );

                        //Toast.makeText(DaftarScanNomorDOActivity.this, "Buat Do Detail", Toast.LENGTH_SHORT).show();
                        getNomorDObyScan(idNoDO, hasilScane);


                }
                barcodeView.resume();
            }
            barcodeView.resume();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    private void getNomorDObyScan(String xDoId, String xHasilScane) {

        RequestQueue newRequestQueue = Volley.newRequestQueue(this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("doId", xDoId);
            jSONObject.put("qrcode", xHasilScane);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString();

        Log.d("TAG", "getNomorDObyScan: " + jSONObject2);

        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.ScaneNomorDOByQRCode);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), jSONObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    String msg = jSONObject.getString("message");
                    new SweetAlertDialog(OrderBaruActivity.this, 2).setTitleText("Informasi")
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

                new SweetAlertDialog(OrderBaruActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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


    public void onRefresh() {
        try {
            listDetailNomorDOModels.clear();
            tvKosong.setVisibility(View.GONE);
            getDaftarScaneByQrCode();
            mAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getDaftarScaneByQrCode() {

        swipeRefreshLayout.setRefreshing(true);
        RequestQueue newRequestQueue = Volley.newRequestQueue(OrderBaruActivity.this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("doId", idNoDO);
            jSONObject.put("page", 1);
            jSONObject.put("pageSize", 10000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.DaftarNomorDOByQRCode);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), jSONObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                Date date1;
                try {
                    listDetailNomorDOModels.clear();
                    JSONArray jSONArray = jSONObject.getJSONArray("data");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jsobj = jSONArray.getJSONObject(i);
                        String xid = jsobj.getString("id");
                        String xdoId = jsobj.getString("doId");
                        String xdoNumber = jsobj.getString("doNumber");
                        String xqrcode = jsobj.getString("qrcode");
                        String xstatus = jsobj.getString("status");
                        String xcodeDriver = "";
                        String xcodeVehicle = "";
                        String xnamaSupir = "";
                        String xasal = "";
                        String xtujuan = "";

                        String xcreatedAt = jsobj.getString("createdAt");
                        Date time1 = Calendar.getInstance().getTime();
                        try {
                            date1 = simpleDateFormat.parse(xcreatedAt);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            date1 = time1;
                        }

                        ListDetailNomorDOModel listdata = new ListDetailNomorDOModel(xid,xdoId,xdoNumber,xqrcode,xstatus,xcodeDriver,xcodeVehicle,xnamaSupir,xasal,xtujuan,date1);
                        listDetailNomorDOModels.add(listdata);
                    }

                    if (listDetailNomorDOModels.isEmpty()){
                        Log.d("TAG", "Data : " + " Data Tidak ditemukan");

                        tvKosong.setVisibility(View.VISIBLE);
                        //tvKosong.setText(title + " (" + nomorDo + ") " + getResources().getString(R.string.kosong));

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
                    //Toast.makeText(applicationContext, sb.toString(), Toast.LENGTH_LONG).show();
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
                new SweetAlertDialog(OrderBaruActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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

    private void updateStatusOtw(String id){
        new SweetAlertDialog(OrderBaruActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                .setContentText("Yakin akan update Status ke OTW ?")
                .setConfirmText("Ya")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        RequestQueue newRequestQueue = Volley.newRequestQueue(OrderBaruActivity.this);
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
                                    new SweetAlertDialog(OrderBaruActivity.this, 2).setTitleText("Informasi")
                                            .setContentText(msg)
                                            .setConfirmText("Ok")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                    sweetAlertDialog.cancel();

                                                    Intent intent = new Intent(OrderBaruActivity.this, DaftarPengirimanActivity.class);
                                                    (OrderBaruActivity.this).finish();
                                                    intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                    startActivity(intent);

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
                                new SweetAlertDialog(OrderBaruActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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