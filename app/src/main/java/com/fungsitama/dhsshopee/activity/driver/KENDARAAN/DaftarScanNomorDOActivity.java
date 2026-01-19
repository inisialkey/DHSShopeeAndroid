package com.fungsitama.dhsshopee.activity.driver.KENDARAAN;

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
import android.widget.CompoundButton;
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

public class DaftarScanNomorDOActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    SessionManager manager;
    private static String vtoken,vusername,vtypeLogin,vtypeUser;

    public SwipeRefreshLayout swipeRefreshLayout;

    public List<ListDetailNomorDOModel> listDetailNomorDOModels;
    public ListDetailNomorDOAdapter mAdapter;
    private RecyclerView recyclerView;
    private TextView tvKosong;

    String id,idKendaraan,nomorDo,codeDriver,codeVehicle,namaSupir,asal,tujuan,status;
    private DecoratedBarcodeView barcodeView;
    String stat = "";
    public Switch scanQrCode;
    public Boolean scan;
    private LinearLayout viewScanQrCode,lyScaner;
    private TextView hasilcari;
    private String title;
    private String disableScanBarcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_scan_nomor_d_o);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        idKendaraan = intent.getStringExtra("idKendaraan");
        nomorDo = intent.getStringExtra("nomorDo");
        codeDriver = intent.getStringExtra("codeDriver");
        codeVehicle = intent.getStringExtra("codeVehicle");
        namaSupir = intent.getStringExtra("namaSupir");
        asal = intent.getStringExtra("asal");
        tujuan = intent.getStringExtra("tujuan");
        status = intent.getStringExtra("status");

        disableScanBarcode = intent.getStringExtra("disableScanBarcode");

        Log.d("TAG", "trTrackingId Detail: " + id + status);
        Log.d("TAG", "disableScanBarcode: " + disableScanBarcode);

        title = getResources().getString(R.string.daftar_scan_do);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title +" "+ nomorDo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager = new SessionManager();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        vtoken = defaultSharedPreferences.getString("token", null);
        vusername = defaultSharedPreferences.getString("username", null);
        vtypeLogin = defaultSharedPreferences.getString("typeLogin", null);
        vtypeUser = defaultSharedPreferences.getString("typeUser", null);

        manager = new SessionManager();
        viewScanQrCode = findViewById(R.id.view_ScanQrCode);
        scanQrCode = findViewById(R.id.scan_QrCode);
        hasilcari = findViewById(R.id.hasilcari);
        tvKosong = findViewById(R.id.tv_Kosong);

        lyScaner = (LinearLayout) findViewById(R.id.scanner);
        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        barcodeView.decodeContinuous(callback);
        barcodeView.setStatusText(getString(R.string.scan_qr_code));

        if (disableScanBarcode.equals("0")){
            lyScaner.setVisibility(View.GONE);
        }else{
            lyScaner.setVisibility(View.VISIBLE);
        }

        recyclerView = (RecyclerView) findViewById(R.id.rv_daftar_list);
        this.listDetailNomorDOModels = new ArrayList();
        this.mAdapter = new ListDetailNomorDOAdapter(listDetailNomorDOModels, this, new ListDetailNomorDOAdapter.Onclick() {
            @Override
            public void onEvent(ListDetailNomorDOModel modelItem, int pos) {

                String barCode = listDetailNomorDOModels.get(pos).getQrcode();

              /*  new SweetAlertDialog(DaftarScanNomorDOActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Yakin akan hapus data ?")
                        .setConfirmText("Iya")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                getHapusData(id, barCode);

                            }
                        })
                        .setCancelText("Tidak")
                        .show();
*/
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

                Intent intent = new Intent(DaftarScanNomorDOActivity.this, NavMenuActivity.class);
                startActivity(intent);
                finish();

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }





    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                barcodeView.pause();
                if (stat != result.getText()) {
                    MediaPlayer mp = MediaPlayer.create(DaftarScanNomorDOActivity.this, R.raw.sound);
                    mp.start();

                    String hasilScane =  result.getText();
                    //Toast.makeText(ScanNomorDOActivity.this, "Barcode" + result.getText(), Toast.LENGTH_SHORT).show();

                    Log.d("TAG", "barcodeResult: " + id + " - " + hasilScane + " " + status);

                    if (status.equals("Pick-Up")){
                        //Toast.makeText(DaftarScanNomorDOActivity.this, "Buat Do Detail", Toast.LENGTH_SHORT).show();
                        getNomorDObyScan(id, hasilScane);
                    }else if (status.equals("Pick-Up Retur")){
                        //Toast.makeText(DaftarScanNomorDOActivity.this, "Update Do Detail", Toast.LENGTH_SHORT).show();
                        getNomorDObyScan(id, hasilScane);
                    }else if (status.equals("OTW")){
                        //Toast.makeText(DaftarScanNomorDOActivity.this, "Update Do Detail", Toast.LENGTH_SHORT).show();
                        updateStatusDelevered(id, hasilScane);
                    }

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
                    new SweetAlertDialog(DaftarScanNomorDOActivity.this, 2).setTitleText("Informasi")
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

                new SweetAlertDialog(DaftarScanNomorDOActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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

    private void updateStatusDelevered(String xDoId, String xHasilScane) {

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
        sb.append(ApiConfig.UpdateStatusDelevered);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), jSONObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    String msg = jSONObject.getString("message");
                    new SweetAlertDialog(DaftarScanNomorDOActivity.this, 2).setTitleText("Informasi")
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

                new SweetAlertDialog(DaftarScanNomorDOActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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
        RequestQueue newRequestQueue = Volley.newRequestQueue(DaftarScanNomorDOActivity.this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("doId", id);
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
                        String xcodeDriver = codeDriver;
                        String xcodeVehicle = codeVehicle;
                        String xnamaSupir = namaSupir;
                        String xasal = asal;
                        String xtujuan = tujuan;

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
                        tvKosong.setText(title + " (" + nomorDo + ") " + getResources().getString(R.string.kosong));

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
                new SweetAlertDialog(DaftarScanNomorDOActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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

    private void getHapusData(String xDoId, String xBarCode){
        RequestQueue newRequestQueue = Volley.newRequestQueue(this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("doId", xDoId);
            jSONObject.put("qrcode", xBarCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString();

        //Log.d("TAG", "getNomorDObyScan: " + jSONObject2);

        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.HapusBarCode);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), jSONObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    String msg = jSONObject.getString("message");
                    new SweetAlertDialog(DaftarScanNomorDOActivity.this, 2).setTitleText("Informasi")
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

                new SweetAlertDialog(DaftarScanNomorDOActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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