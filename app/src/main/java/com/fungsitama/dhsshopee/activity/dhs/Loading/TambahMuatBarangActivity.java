package com.fungsitama.dhsshopee.activity.dhs.Loading;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fungsitama.dhsshopee.R;
import com.fungsitama.dhsshopee.activity.driver.KENDARAAN.DaftarScanNomorDOActivity;
import com.fungsitama.dhsshopee.activity.driver.KENDARAAN.TambahNomorDOActivity;
import com.fungsitama.dhsshopee.activity.main.NavMenuActivity;
import com.fungsitama.dhsshopee.util.ApiConfig;
import com.fungsitama.dhsshopee.util.SessionManager;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TambahMuatBarangActivity extends AppCompatActivity {

    SessionManager manager;
    private static String vtoken,vusername,vtypeLogin,vtypeUser,vidKendaraan,vnoKendaraan,vkodeKendaraan,vstatus,disableScanBarcode;

    private EditText etKodeAsal,etKodeTujuan,etKodeTransit,etNamaSupir,etNomorTruck;
    private SearchableSpinner spinAsal,spinTujuan,spinTransit;
    private ArrayList<String> alAsal,alTujuan,alTransit;
    public JSONArray rAsal,rTujuan,rTransit;

    private LinearLayout ly_Transit;

    private String gudangAsal,gudangTujuan,gudangTransit,namaSupir,nomorTruck;

    private CheckBox chekTransit;

    private Button btnAddNext;
    String Text = "AAAA";

    private RadioGroup rbGroup_pilih;
    private String status = "FCL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_muat_barang);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Muat Barang");
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager = new SessionManager();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        vtoken = defaultSharedPreferences.getString("token", null);
        vusername = defaultSharedPreferences.getString("username", null);
        vtypeLogin = defaultSharedPreferences.getString("typeLogin", null);
        vtypeUser = defaultSharedPreferences.getString("typeUser", null);

        etKodeAsal = findViewById(R.id.et_KodeAsal);
        spinAsal = findViewById(R.id.spin_Asal);

        etKodeTujuan = findViewById(R.id.et_KodeTujuan);
        spinTujuan = findViewById(R.id.spin_Tujuan);

        etKodeTransit = findViewById(R.id.et_KodeTransit);
        spinTransit = findViewById(R.id.spin_Transit);

        etNamaSupir = findViewById(R.id.et_NamaSupir);
        etNomorTruck = findViewById(R.id.et_NoTruck);

        ly_Transit = findViewById(R.id.ly_Transit);


        chekTransit = findViewById(R.id.selectTransit);
        chekTransit.setChecked(false);
        chekTransit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ly_Transit.setVisibility(View.VISIBLE);

                }else{
                    ly_Transit.setVisibility(View.GONE);
                    etKodeTransit.setText("");
                    //Text = spinTransit.getSelectedItem().toString();

                }
            }
        });


        spinAsal.setTitle("Pilih Gudang Asal");
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


        spinTujuan.setTitle("Pilih Gudang Tujuan");
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


        spinTransit.setTitle("Pilih Gudang Transit");
        spinTransit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //String item = parentView.getItemAtPosition(position).toString();
                etKodeTransit.setText(getKodeTransit(position));
            }
            public void onNothingSelected(AdapterView<?> parentView) {
                //etKodeTujuan.setText(getKodeTujuan(0));
            }
        });
        alTransit = new ArrayList<>();
        getTransit();


        rbGroup_pilih = (RadioGroup) findViewById(R.id.rbGroup_pilih);
        rbGroup_pilih.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = rbGroup_pilih.findViewById(checkedId);
                int index = rbGroup_pilih.indexOfChild(radioButton);

                switch (index) {
                    case 0: // #1
                        status = "LCL";
                        //Toast.makeText(TambahMuatBarangActivity.this, "Pilih " + status, Toast.LENGTH_SHORT).show();
                        break;
                    case 1: // #2
                        status = "FCL";
                        //Toast.makeText(TambahMuatBarangActivity.this, "Pilih " + status, Toast.LENGTH_SHORT).show();
                        break;
                    case 3: // #3
                        //Toast.makeText(TambahMuatBarangActivity.this, "No Action", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        btnAddNext = findViewById(R.id.btn_Selanjutnya);

        btnAddNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gudangAsal = etKodeAsal.getText().toString();
                gudangTujuan = etKodeTujuan.getText().toString();
                gudangTransit = etKodeTransit.getText().toString();
                namaSupir = etNamaSupir.getText().toString();
                nomorTruck = etNomorTruck.getText().toString();

              /*  Toast.makeText(TambahMuatBarangActivity.this, "Data : " +
                gudangAsal + " - " + gudangTujuan + " - " + gudangTransit + " - " + namaSupir + " - " + nomorTruck
                        , Toast.LENGTH_SHORT).show();*/

                if (gudangAsal.isEmpty() || gudangTujuan.isEmpty() || namaSupir.isEmpty() || nomorTruck.isEmpty()){
                    new SweetAlertDialog(TambahMuatBarangActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                        .setContentText("Form masih ada yang belum di isi, mohon lengkapi terlebih dahulu !")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        }).show();
                }else{
                    //Toast.makeText(TambahMuatBarangActivity.this, "Berhasil Save", Toast.LENGTH_SHORT).show();
                    getTambahLoading(gudangAsal,gudangTujuan,gudangTransit,namaSupir,nomorTruck,status);
                    btnAddNext.setEnabled(false);
                }



               /* Intent intent = new Intent(TambahMuatBarangActivity.this, ScanBarcodeLoadingActivity.class);
                startActivity(intent);*/


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



    private void getAsal() {
        RequestQueue newRequestQueue = Volley.newRequestQueue(this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("page", 1);
            jSONObject.put("pageSize", 10000);
            JSONObject filters = new JSONObject();
            filters.put("owner", "Shopee");
            jSONObject.put("filters", filters);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.listWarehouseGudang);
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
            JSONObject filters = new JSONObject();
            filters.put("owner", "Shopee");
            jSONObject.put("filters", filters);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.listWarehouseGudang);
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

    private void getTransit() {

        RequestQueue newRequestQueue = Volley.newRequestQueue(this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("page", 1);
            jSONObject.put("pageSize", 10000);
            JSONObject filters = new JSONObject();
            filters.put("owner", "DHS");
            jSONObject.put("filters", filters);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //final String jSONObject2 = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.listWarehouseGudang);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), jSONObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    rTransit = jSONObject.getJSONArray("data");

                    for (int i = 0; i < rTransit.length(); i++) {
                        try {
                            jSONObject = rTransit.getJSONObject(i);
                            //Log.d("TAG", "data tujuan: " + jSONObject);
                            ArrayList<String> arrayList = alTransit;
                            StringBuilder sb = new StringBuilder();
                            sb.append(jSONObject.getString("code"));
                            sb.append(" - ");
                            sb.append(jSONObject.getString("name"));
                            arrayList.add(sb.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    spinTransit.setAdapter(new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, alTransit));

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
    private String getKodeTransit(int i) {
        String str = "";
        try {
            str = rTransit.getJSONObject(i).getString("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    }


    public void getTambahLoading(String asal, String tujuan, String transit, String supir, String noTruck, String statusType){

        //Log.d("TAG", "simpanNoDO: " + idKendaraan + noKendaraan + " - " + noDO + " - " + asal + " - " + tujuan +  " - "  +  koli + " - " + berat + " - " + kuantitas + " - " + deskripsi);

        RequestQueue newRequestQueue = Volley.newRequestQueue(TambahMuatBarangActivity.this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("codeOrigin", asal);
            jSONObject.put("codeDestination", tujuan);
            jSONObject.put("codeTransit", transit);
            jSONObject.put("nameDriver", supir);
            jSONObject.put("codeVehicle", noTruck);
            jSONObject.put("loadType", statusType);
            String a = jSONObject.toString().replaceAll("\\\\", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString().replaceAll("\\\\", "");

        Log.d("TAG", "getTambahNomorDO: " + jSONObject2);

        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.TambahLoading);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    String msg = jSONObject.getString("message");
                    JSONObject data = jSONObject.getJSONObject("data");
                    String id = data.getString("id");
                    String noTrans = data.getString("transNumber");

                    Log.d("TAG", "id tracking creat: ");

                    Intent intent = new Intent(TambahMuatBarangActivity.this, ScanBarcodeLoadingActivity.class);

                    intent.putExtra("id", id);
                    intent.putExtra("noTrans", noTrans);

                    (TambahMuatBarangActivity.this).finish();
                    intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);

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
                new SweetAlertDialog(TambahMuatBarangActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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