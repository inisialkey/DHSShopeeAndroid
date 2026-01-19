package com.fungsitama.dhsshopee.activity.driver.KENDARAAN;

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
import android.widget.EditText;

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
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TambahNomorDOActivity extends AppCompatActivity {

    SessionManager manager;
    private static String vtoken,vusername,vtypeLogin,vtypeUser,vidKendaraan,vnoKendaraan,vkodeKendaraan,vstatus,disableScanBarcode;

    private EditText etNoKendaraan,etNoDo,etKodeAsal,etKodeTujuan,etKoli,etBerat,etKuantitas,etDeskripsi;
    private SearchableSpinner spinAsal,spinTujuan;
    private ArrayList<String> alAsal,alTujuan;
    public JSONArray rAsal,rTujuan;
    private Button btnSimpan;
    private String idKendaraan,noKendaraan,kodeKendaraan,noDO,asal,tujuan,deskripsi;
    private Double koli,berat,kuantitas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_nomor_d_o);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.tambah_nomor_do);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager = new SessionManager();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        vtoken = defaultSharedPreferences.getString("token", null);
        vusername = defaultSharedPreferences.getString("username", null);
        vtypeLogin = defaultSharedPreferences.getString("typeLogin", null);
        vtypeUser = defaultSharedPreferences.getString("typeUser", null);
        vidKendaraan = defaultSharedPreferences.getString("idKendaraan", null);
        vnoKendaraan = defaultSharedPreferences.getString("noKendaraan", null);
        vkodeKendaraan = defaultSharedPreferences.getString("kodeKendaraan", null);
        vstatus = defaultSharedPreferences.getString("status", null);

        disableScanBarcode = defaultSharedPreferences.getString("disableScanBarcode",null);

        Log.d("TAG", "id Nomor Kendaraan: " + vidKendaraan);

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

        etNoKendaraan.setText(vnoKendaraan);

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

        btnSimpan = (Button) findViewById(R.id.btn_Simpan);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    idKendaraan = vidKendaraan;
                    noKendaraan = vnoKendaraan;
                    kodeKendaraan = vkodeKendaraan;
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
                        new SweetAlertDialog(TambahNomorDOActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                            .setContentText("Data tidak boleh kosong")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();
                                }
                            }).show();
                    }else {
                        //Log.d("TAG", "simpanNoDO: " + idKendaraan + noKendaraan + " - " + noDO + " - " + asal + " - " + tujuan +  " - "  +  koli + " - " + berat + " - " + kuantitas + " - " + deskripsi);
                        new SweetAlertDialog(TambahNomorDOActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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

                    new SweetAlertDialog(TambahNomorDOActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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

    public void getTambahNomorDO(){

        //Log.d("TAG", "simpanNoDO: " + idKendaraan + noKendaraan + " - " + noDO + " - " + asal + " - " + tujuan +  " - "  +  koli + " - " + berat + " - " + kuantitas + " - " + deskripsi);

        RequestQueue newRequestQueue = Volley.newRequestQueue(TambahNomorDOActivity.this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("trTrackingId", idKendaraan);
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
                    String id = data.getString("id");

                    Log.d("TAG", "id tracking creat: ");

                    new SweetAlertDialog(TambahNomorDOActivity.this, 2).setTitleText("Informasi")
                        .setContentText(msg)
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                Intent intent = new Intent(TambahNomorDOActivity.this, DaftarScanNomorDOActivity.class);

                                intent.putExtra("id", id);
                                intent.putExtra("idKendaraan", idKendaraan);
                                intent.putExtra("nomorDo", noDO);
                                intent.putExtra("codeDriver", kodeKendaraan);
                                intent.putExtra("codeVehicle", noKendaraan);
                                intent.putExtra("tujuan", tujuan);
                                intent.putExtra("status", vstatus);

                                intent.putExtra("disableScanBarcode", disableScanBarcode);

                                (TambahNomorDOActivity.this).finish();
                                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);

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
                new SweetAlertDialog(TambahNomorDOActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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