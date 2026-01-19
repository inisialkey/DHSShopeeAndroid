package com.fungsitama.dhsshopee.activity.driver.KENDARAAN;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class DetailNomorDOActivity extends AppCompatActivity {

    SessionManager manager;
    private static String vtoken,vusername,vtypeLogin,vtypeUser;

    private TextView tvTanggalTransaksi,tvNoTransaksi,tvNoKendaraan,tvNoDo,tvAsal,tvTujuan,tvStatus,tvKoli,tvBerat,tvKuantitas,tvQrCode,tvDeskripsi;
    private String sId,sNoTransaksi,sNoKendaraan,sNoDo,sTujuan,sStatus,sKoli,sBerat,sKuantitas,sqQrCode,sDeskripsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_nomor_d_o);

        manager = new SessionManager();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        vtoken = defaultSharedPreferences.getString("token", null);
        vusername = defaultSharedPreferences.getString("username", null);
        vtypeLogin = defaultSharedPreferences.getString("typeLogin", null);
        vtypeUser = defaultSharedPreferences.getString("typeUser", null);

        Intent intent = getIntent();
        sId = intent.getStringExtra("id");
        sNoKendaraan = intent.getStringExtra("nomorKendaraan");

        Log.d("TAG", "id do: " + sId);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.detail_no_do);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTanggalTransaksi = findViewById(R.id.tv_TanggalTransaksi);
        tvNoTransaksi = findViewById(R.id.tv_NoTransaksi);
        tvNoKendaraan = findViewById(R.id.tv_NoKendaraan);
        tvNoDo = findViewById(R.id.tv_NoDo);
        tvAsal = findViewById(R.id.tv_Asal);
        tvTujuan = findViewById(R.id.tv_Tujuan);
        tvStatus = findViewById(R.id.tv_Status);
        tvKoli = findViewById(R.id.tv_Koli);
        tvBerat = findViewById(R.id.tv_Berat);
        tvKuantitas = findViewById(R.id.tv_Kuantitas);
        tvQrCode = findViewById(R.id.tv_QrCode);
        tvDeskripsi = findViewById(R.id.tv_Deskripsi);

        getDetailNoDo();

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

                Intent intent = new Intent(DetailNomorDOActivity.this, NavMenuActivity.class);
                startActivity(intent);
                finish();

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }






    public void getDetailNoDo(){

        RequestQueue newRequestQueue = Volley.newRequestQueue(this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("id", sId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString();

        Log.d("TAG", "getDetailNoDo: " + jSONObject2);

        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.DetailDO);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), jSONObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                Date date1;
                try {

                    JSONObject jsobj = jSONObject.getJSONObject("data");
                    Log.d("TAG", "Data: " + jsobj);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

                    String xid = jsobj.getString("id");
                    String xtransNumber = jsobj.getString("transNumber");
                    String xcodeOrigin = jsobj.getString("codeOrigin");
                    String dcDestination = jsobj.getString("dcDestination");
                    String xnameOrigin = jsobj.getJSONObject("dcOrigin").getString("name");
                    String xnameDestination = jsobj.getJSONObject("dcDest").getString("name");
                    String xdoNumber = jsobj.getString("doNumber");
                    String xkoli = jsobj.getString("koli");
                    String xkg = jsobj.getString("kg");
                    String xqty = jsobj.getString("qty");
                    String xtotalQrCode = jsobj.getString("qtyBarcode");
                    String xdescription = jsobj.getString("description");
                    String xstatus = jsobj.getString("status");

                    String xtransactionDate = jsobj.getString("createdAt");
                    Date time1 = Calendar.getInstance().getTime();
                    try {
                        date1 = simpleDateFormat.parse(xtransactionDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        date1 = time1;
                    }

                    String tanggal = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date1);

                    tvTanggalTransaksi.setText(tanggal);
                    tvNoTransaksi.setText(xtransNumber);
                    tvNoKendaraan.setText(sNoKendaraan);
                    tvNoDo.setText(xdoNumber);
                    tvAsal.setText(xnameOrigin);
                    tvTujuan.setText(xnameDestination);
                    tvStatus.setText(xstatus);
                    tvKoli.setText(xkoli + " Koli");
                    tvBerat.setText(xkg.substring(0, xkg.length()).replace(".00", "") + " Kg");
                    tvKuantitas.setText(xqty);
                    tvQrCode.setText(xtotalQrCode);
                    tvDeskripsi.setText(xdescription);



                } catch (JSONException e) {
                    e.printStackTrace();
                    Context applicationContext = getApplicationContext();
                    StringBuilder sb = new StringBuilder();
                    sb.append("Error: ");
                    sb.append(e.getMessage());
                    Toast.makeText(applicationContext, sb.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("Error: ", volleyError.getMessage());
                // Toast.makeText( ListDataEntryActivity.this, "e" + volleyError.toString(), Toast.LENGTH_LONG).show();
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