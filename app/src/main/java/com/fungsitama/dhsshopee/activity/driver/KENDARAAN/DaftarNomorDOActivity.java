package com.fungsitama.dhsshopee.activity.driver.KENDARAAN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class DaftarNomorDOActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    SessionManager manager;
    private static String vtoken,vusername,vtypeLogin,vtypeUser;

    public SwipeRefreshLayout swipeRefreshLayout;

    FloatingActionButton btnTambahNoDO;
    public List<ListNomorDOModel> listNomorDOModels;
    public ListNomorDOAdapter mAdapter;
    private RecyclerView recyclerView;
    private TextView tvKosong;

    public TextView tvKodeTujuan;
    private EditText etNoKendaraan,etNoDo,etKodeTujuan,etKoli,etBerat,etKuantitas,etDeskripsi;
    private SearchableSpinner spinTujuan;
    private ArrayList<String> alTujuan;
    public JSONArray rTujuan;
    private Button btnTambah,btnKembali;
    private String id,noKendaraan,noDO,koli,asal,tujuan,berat,kuantitas,deskripsi,kodeKendaraan,namaSupir,status;
    private String title;
    private String disableButtonTambah,disableScanBarcode;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_nomor_d_o);

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

        Log.d("TAG", "disableButtonTambah: " + disableButtonTambah + " " + disableScanBarcode);

        if (disableButtonTambah.equals("0")){
            btnTambahNoDO.setVisibility(View.GONE);
        }else{
            btnTambahNoDO.setVisibility(View.VISIBLE);
        }

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

                Log.d("TAG", "id kendaraan: " + id);

                Intent intent = new Intent(DaftarNomorDOActivity.this, DaftarScanNomorDOActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("nomorDo", nomorDo);
                intent.putExtra("codeDriver", codeDriver);
                intent.putExtra("codeVehicle", codeVehicle);
                intent.putExtra("namaSupir", namaSupir);
                intent.putExtra("asal", asal);
                intent.putExtra("tujuan", tujuan);
                intent.putExtra("status", status);
                intent.putExtra("disableButtonTambah", disableButtonTambah);
                intent.putExtra("disableScanBarcode", disableScanBarcode);

                startActivity(intent);
            }
        }, new ListNomorDOAdapter.Onclick() {
            @Override
            public void onEvent(ListNomorDOModel modelItem, int pos) {


                Intent intent = new Intent(DaftarNomorDOActivity.this, DetailNomorDOActivity.class);
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

                //DialogBoxTambahKendaraan();

                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                edit.putString("vtoken", vtoken);
                edit.putString("vusername", vusername);
                edit.putString("vtypeLogin", vtypeLogin);
                edit.putString("vtypeUser", vtypeUser);
                edit.putString("idKendaraan", id);
                edit.putString("noKendaraan", noKendaraan);
                edit.putString("kodeKendaraan", kodeKendaraan);
                edit.putString("status", status);

                edit.putString("disableScanBarcode", disableScanBarcode);

                edit.commit();
                startActivity(new Intent(getApplicationContext(), TambahNomorDOActivity.class));

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
                    DaftarNomorDOActivity.this,
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
            listNomorDOModels.clear();
            tvKosong.setVisibility(View.GONE);
            getListDataNomorDO();
            mAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
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
                    spinTujuan.setAdapter(new ArrayAdapter(DaftarNomorDOActivity.this, android.R.layout.simple_spinner_item, alTujuan));

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

        etNoKendaraan = findViewById(R.id.et_NoKendaraan);
        etNoDo = findViewById(R.id.et_NoDo);

        /*tvKodeTujuan = view.findViewById(R.id.tv_KodeTujuan);
        etKodeTujuan = view.findViewById(R.id.et_KodeTujuan);
        spinTujuan = view.findViewById(R.id.spin_Tujuan);

        etKoli = view.findViewById(R.id.et_Koli);
        etBerat = view.findViewById(R.id.et_Berat);
        etKuantitas = view.findViewById(R.id.et_Kuantitas);
        etDeskripsi = view.findViewById(R.id.et_Deskripsi);*/

        spinTujuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //String item = parentView.getItemAtPosition(position).toString();
                etKodeTujuan.setText(getKodeTujuan(position));
            }

            public void onNothingSelected(AdapterView<?> parentView) {
                etKodeTujuan.setText(getKodeTujuan(0));
            }
        });
        alTujuan = new ArrayList<>();

        getTujuan();

        btnTambah = (Button) view.findViewById(R.id.btn_Tambah);
        btnKembali = (Button) view.findViewById(R.id.btn_Kembali);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                noKendaraan = etNoKendaraan.getText().toString();
                noDO = etNoDo.getText().toString();
                asal = "DCR0001";
                tujuan = etKodeTujuan.getText().toString();
                koli = etKoli.getText().toString();
                berat = etBerat.getText().toString();
                kuantitas = etKuantitas.getText().toString();
                deskripsi = etDeskripsi.getText().toString();

                Log.d("TAG", "DialogBoxTambahKendaraan: " + noKendaraan + " " + noDO + " " + koli + " " + asal + " " + tujuan + " " + berat + " " + kuantitas + " " + deskripsi);
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

}