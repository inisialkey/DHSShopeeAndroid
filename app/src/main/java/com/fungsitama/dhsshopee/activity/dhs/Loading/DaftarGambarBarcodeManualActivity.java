package com.fungsitama.dhsshopee.activity.dhs.Loading;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.fungsitama.dhsshopee.R;
import com.fungsitama.dhsshopee.adapter.ListGambarAdapter;
import com.fungsitama.dhsshopee.model.ListGambarModel;
import com.fungsitama.dhsshopee.util.ApiConfig;
import com.fungsitama.dhsshopee.util.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class DaftarGambarBarcodeManualActivity extends AppCompatActivity {

    SessionManager manager;
    private static String vtoken,vusername,vtypeLogin,vtypeUser,vorder;

    String id,trCargoId,barcode,noTrans,vstatus;

    private TextView txt_transNumber,txt_transactionDate,txt_bacode,txt_status;

    private LinearLayout ly_noImage;

    private Button btn_tambahGambar;

    private RecyclerView recyclerView;
    private ListGambarAdapter mAdapter;
    private ArrayList<ListGambarModel> images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_gambar_barcode_manual);

        Intent intent = getIntent();

        id = intent.getStringExtra("id");
        trCargoId = intent.getStringExtra("trCargoId");
        noTrans = intent.getStringExtra("noTrans");
        barcode = intent.getStringExtra("barcode");
        vstatus = intent.getStringExtra("vstatus");

        Log.d("TAG", "barcode 1: " + barcode);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Detail Barcode Manual");
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager = new SessionManager();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        vtoken = defaultSharedPreferences.getString("token", null);
        vusername = defaultSharedPreferences.getString("username", null);
        vtypeLogin = defaultSharedPreferences.getString("typeLogin", null);
        vtypeUser = defaultSharedPreferences.getString("typeUser", null);

        txt_transNumber = findViewById(R.id.txt_transNumber);
        txt_transactionDate = findViewById(R.id.txt_transactionDate);
        txt_bacode = findViewById(R.id.txt_bacode);
        txt_status = findViewById(R.id.txt_status);

        ly_noImage = findViewById(R.id.ly_noImage);

        btn_tambahGambar = findViewById(R.id.btn_tambahGambar);

        if (vstatus.equals("Loading")){
            btn_tambahGambar.setVisibility(View.VISIBLE);
        }else{
            btn_tambahGambar.setVisibility(View.GONE);
        }

        btn_tambahGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DaftarGambarBarcodeManualActivity.this, TambahBarcodeManualActivity.class);

                intent.putExtra("id", trCargoId);
                intent.putExtra("idGambar", id);
                intent.putExtra("barcode", barcode);
                intent.putExtra("noTrans", noTrans);

                startActivity(intent);

                //Toast.makeText(DaftarGambarBarcodeManualActivity.this, "Tambah Gambar", Toast.LENGTH_SHORT).show();
            }
        });

        getID();


        images = new ArrayList<>();
        mAdapter = new ListGambarAdapter(this, images);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new ListGambarAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new ListGambarAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int i) {

            }
        }));



    }


    public void getID() {
        RequestQueue newRequestQueue = Volley.newRequestQueue(DaftarGambarBarcodeManualActivity.this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.detailBarcodeManual);

        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                Date date1;
                Log.d("TAG", "onResponse: "+jSONObject);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                try {

                    JSONObject jSONObject3 = new JSONObject(jSONObject.getString("data"));

                    String xid = jSONObject3.getString("id");
                    String xtransNumber = jSONObject3.getString("transNumber");
                    String xqrcode = jSONObject3.getString("qrcode");
                    String xscan = jSONObject3.getString("scan");
                    String xmanual = jSONObject3.getString("manual");
                    String xstatus = jSONObject3.getString("status");

                    String xtransactionDate = jSONObject3.getString("createdAt");
                    Date time1 = Calendar.getInstance().getTime();
                    try {
                        date1 = simpleDateFormat.parse(xtransactionDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        date1 = time1;
                    }

                    String tanggal = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date1);


                    txt_transNumber.setText(xtransNumber);
                    txt_transactionDate.setText(tanggal);
                    txt_bacode.setText(xqrcode);
                    txt_status.setText(xstatus);


                    JSONArray arr = jSONObject.getJSONArray("listFile");
                    for(int i=0; i<arr.length(); i++){
                        String o = arr.getString(i);
                        System.out.println(o);
                        Log.d("TAG", "onResponsedata 1: "+o);
                        ListGambarModel image = new ListGambarModel();
                        image.setUrl(o);
                        images.add(image);
                    }

                    if (arr.toString().equals("[]")){
                        ly_noImage.setVisibility(View.VISIBLE);
                        //Toast.makeText(DaftarGambarBarcodeManualActivity.this, "Gambar Kosong", Toast.LENGTH_SHORT).show();
                    }else{
                        ly_noImage.setVisibility(View.GONE);
                        //Toast.makeText(DaftarGambarBarcodeManualActivity.this, "Ada Gambar", Toast.LENGTH_SHORT).show();
                    }

                    //Log.d("TAG", "data foto: " + "[]" + " - " + arr.toString());

                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();
                //NewBagtagActivity.this.swipeRefreshLayout.setRefreshing(false);
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

            public byte[] getBody() {
                byte[] bArr = null;
                try {
                    if (jSONObject2 != null) {
                        bArr = jSONObject2.getBytes("utf-8");
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



    public boolean onSupportNavigateUp(){
        //code it to launch an intent to the activity you want
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        } else {
            onBackPressed(); //replaced
        }
        return true;
    }

    public void onBackPressed() {
        finish();
    }

}