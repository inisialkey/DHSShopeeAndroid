package com.fungsitama.dhsshopee.activity.dhs.Unloading;

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
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
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
import com.fungsitama.dhsshopee.activity.dhs.Loading.DaftarGambarBarcodeManualActivity;
import com.fungsitama.dhsshopee.activity.dhs.Loading.DetailListBarcodeActivity;
import com.fungsitama.dhsshopee.activity.dhs.Loading.ScanBarcodeLoadingActivity;
import com.fungsitama.dhsshopee.activity.dhs.Loading.TambahBarcodeManualActivity;
import com.fungsitama.dhsshopee.adapter.ListBarcodeLoadingAdapter;
import com.fungsitama.dhsshopee.adapter.ListBarcodeUnloadingAdapter;
import com.fungsitama.dhsshopee.model.ListBarcodeLoadingModel;
import com.fungsitama.dhsshopee.util.ApiConfig;
import com.fungsitama.dhsshopee.util.SessionManager;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraSettings;

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

public class ScanBarcodeUnloadingActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    SessionManager manager;
    private static String vtoken,vusername,vtypeLogin,vtypeUser;

    public SwipeRefreshLayout swipeRefreshLayout;

    private DecoratedBarcodeView barcodeView;
    String stat = "";
    public Switch scanQrCode;
    public Boolean scan;
    private LinearLayout viewScanQrCode,lyScaner;
    private TextView hasilcari;
    private String disableScanBarcode;
    private CameraSettings settings;

    public List<ListBarcodeLoadingModel> listDataModel;
    public ListBarcodeUnloadingAdapter mAdapter;
    private RecyclerView recyclerView;
    private TextView tvKosong,totalQRLoading,totalQRUnloading;

    private String id, noTrans;

    private Button btn_TambahManual,btn_Detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode_unloading);

        Intent intent = getIntent();

        id = intent.getStringExtra("id");
        noTrans = intent.getStringExtra("noTrans");

        Log.d("TAG", "id: " + id);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Scan Bongkar Muat Barang "+ noTrans);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager = new SessionManager();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        vtoken = defaultSharedPreferences.getString("token", null);
        vusername = defaultSharedPreferences.getString("username", null);
        vtypeLogin = defaultSharedPreferences.getString("typeLogin", null);
        vtypeUser = defaultSharedPreferences.getString("typeUser", null);

        viewScanQrCode = findViewById(R.id.view_ScanQrCode);
        scanQrCode = findViewById(R.id.scan_QrCode);
        hasilcari = findViewById(R.id.hasilcari);
        tvKosong = findViewById(R.id.tv_Kosong);

        btn_TambahManual = findViewById(R.id.btn_TambahManual);
        btn_Detail = findViewById(R.id.btn_Detail);

        totalQRLoading = findViewById(R.id.totalQRLoading);
        totalQRUnloading = findViewById(R.id.totalQRUnloading);
        // Camera settings
        settings = new CameraSettings();
        settings.setFocusMode(CameraSettings.FocusMode.CONTINUOUS);
        // Use this function to change the autofocus interval (default is 5 secs)

        //Barcode settings
        lyScaner = (LinearLayout) findViewById(R.id.scanner);
        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        // barcodeView.getBarcodeView().getCameraSettings().setContinuousFocusEnabled(true);
        // barcodeView.getBarcodeView().getCameraSettings().setAutoFocusEnabled(false);

        barcodeView.decodeContinuous(callback);

        barcodeView.setStatusText(getString(R.string.scan_qr_code));

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

        recyclerView = (RecyclerView) findViewById(R.id.rv_daftar_list);
        this.listDataModel = new ArrayList();
        this.mAdapter = new ListBarcodeUnloadingAdapter(listDataModel, this, new ListBarcodeUnloadingAdapter.Onclick() {
            @Override
            public void onEvent(ListBarcodeLoadingModel modelItem, int pos) {
                if (listDataModel.get(pos).getManual().equals("true")){
                    if (listDataModel.get(pos).getStatus().equals("Loading")){
                        Intent intent = new Intent(ScanBarcodeUnloadingActivity.this, DaftarGambarBarcodeManualActivity.class);

                        intent.putExtra("id", listDataModel.get(pos).getId());
                        intent.putExtra("barcode", listDataModel.get(pos).getQrcode());
                        intent.putExtra("trCargoId", id);
                        intent.putExtra("noTrans", noTrans);
                        intent.putExtra("vstatus", "Unloading");

                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(ScanBarcodeUnloadingActivity.this, DaftarGambarBarcodeManualActivity.class);

                        intent.putExtra("id", listDataModel.get(pos).getId());
                        intent.putExtra("barcode", listDataModel.get(pos).getQrcode());
                        intent.putExtra("trCargoId", id);
                        intent.putExtra("noTrans", noTrans);
                        intent.putExtra("vstatus", "Unloading");

                        startActivity(intent);
                    }
                }

                //Toast.makeText(ScanBarcodeUnloadingActivity.this, "Pindah Gambar", Toast.LENGTH_SHORT).show();

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

        btn_TambahManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ScanBarcodeUnloadingActivity.this, TambahBarcodeManualUnloadingActivity.class);

                intent.putExtra("id", id);
                intent.putExtra("noTrans", noTrans);
                intent.putExtra("barcode", "");

                startActivity(intent);

            }
        });

        btn_Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ScanBarcodeUnloadingActivity.this, DetailListBarcodeUnloadingActivity.class);

                intent.putExtra("id", id);
                intent.putExtra("noTrans", noTrans);

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


    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                barcodeView.pause();
                if (stat != result.getText()) {

                    String hasilScane = result.getText();
                    getBarcodeLoading(id, hasilScane);

                    //Toast.makeText(ScanBarcodeLoadingActivity.this, ""+ hasilScane , Toast.LENGTH_SHORT).show();
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

    public void onRefresh() {
        try {
            listDataModel.clear();
            tvKosong.setVisibility(View.GONE);
            getDaftarBarcodeLoading();
            getJumlahBarcodeLoading();
            getJumlahBarcodeUnloading();
            mAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getBarcodeLoading(String xId, String xHasilScane) {

        RequestQueue newRequestQueue = Volley.newRequestQueue(this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("trCargoId", xId);
            jSONObject.put("qrcode", xHasilScane);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString();

        Log.d("TAG", "getNomorDObyScan: " + jSONObject2);

        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.UpdateUnloading);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), jSONObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    String msg = jSONObject.getString("message");
                    MediaPlayer mp = MediaPlayer.create(ScanBarcodeUnloadingActivity.this, R.raw.sound);
                    mp.start();
                    onRefresh();

                    Log.d("TAG", "onResponseScan: " + msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("TAG", "onResponseScan1: " + e.toString());
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
                MediaPlayer mp = MediaPlayer.create(ScanBarcodeUnloadingActivity.this, R.raw.sound_tet);
                mp.start();

                Log.d("TAG", "onResponseScan2: " + msg.toString());

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


    private void getDaftarBarcodeLoading() {

        swipeRefreshLayout.setRefreshing(true);
        RequestQueue newRequestQueue = Volley.newRequestQueue(ScanBarcodeUnloadingActivity.this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("trCargoId", id);
            jSONObject.put("page", 1);
            jSONObject.put("pageSize", 10);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.DaftarBarcodeLoading);
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

                        ListBarcodeLoadingModel listdata = new ListBarcodeLoadingModel(xid,xqrcode,xstatus,xrowNumber,xscan,xmanual,ximport,date1);
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
                new SweetAlertDialog(ScanBarcodeUnloadingActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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

    private void getJumlahBarcodeLoading() {
        RequestQueue newRequestQueue = Volley.newRequestQueue(ScanBarcodeUnloadingActivity.this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("trCargoId", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.JumlahBarcodeLoading);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), jSONObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {

                try {

                    String jumlahData = jSONObject.getString("data");
                    totalQRLoading.setText(jumlahData);

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

    private void getJumlahBarcodeUnloading() {
        RequestQueue newRequestQueue = Volley.newRequestQueue(ScanBarcodeUnloadingActivity.this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("trCargoId", id);
            jSONObject.put("status", "Unloading");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.JumlahBarcodeLoading);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), jSONObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {

                try {

                    String jumlahData = jSONObject.getString("data");
                    totalQRUnloading.setText(jumlahData);

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