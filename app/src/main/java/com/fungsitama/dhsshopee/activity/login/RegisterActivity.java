package com.fungsitama.dhsshopee.activity.login;

import android.Manifest;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fungsitama.dhsshopee.util.ApiConfig;
import com.fungsitama.dhsshopee.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.mateware.snacky.Snacky;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNamaLengkap,etNamapengguna,etKataSandi,etEmail,inputMobile;

    private String namaLengkap,namaPengguna,kataSandi,email,nomorHp;

    private TextView txt_Login;

    private ProgressBar progressBar;

    private Button buttonGetOTP;

    private CardView cv_chekBox,cv_foto;
    private TextView txt_chekBox;
    private Switch checkbox_Switch;
    public Boolean scan;

    private SearchableSpinner codeAA;
    private ArrayList<String> listAktivasiAkun;
    public EditText codeAktivasiAkun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNamaLengkap = (EditText) findViewById(R.id.et_NamaLengkap);
        etNamapengguna = (EditText) findViewById(R.id.et_NamaPengguna);
        etKataSandi = (EditText) findViewById(R.id.et_KataSandi);
        etEmail = (EditText) findViewById(R.id.et_Email);
        inputMobile = findViewById(R.id.inputMobile);
        codeAA = (SearchableSpinner) findViewById(R.id.codeAA);
        codeAktivasiAkun = (EditText) findViewById(R.id.codeAktivasiAkun);

        codeAA.setTitle("Pilih Aktivasi");
        codeAA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String item = parentView.getItemAtPosition(position).toString();
                codeAktivasiAkun.setText(item);

                if (item.equals("SMS")){
                    requestPermissionsSMS();
                }

            }

            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        listAktivasiAkun = new ArrayList<>();
        getListAktivasiAkun();

        buttonGetOTP = findViewById(R.id.buttonGetOTP);
        progressBar = findViewById(R.id.progressBar);

        txt_Login = (TextView) findViewById(R.id.txt_Login);

        txt_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        cv_chekBox = findViewById(R.id.cv_chekBox);
        txt_chekBox = findViewById(R.id.txt_chekBox);
        checkbox_Switch = findViewById(R.id.checkbox_Switch);

        buttonGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent intent = new Intent(RegisterActivity.this, VerifyOTPActivity.class);

                intent.putExtra("id", "id");
                intent.putExtra("noHp", "phone_number");
                intent.putExtra("email", "email");
                intent.putExtra("username", "username");
                intent.putExtra("password", "password");
                intent.putExtra("registerBy", "Email");

                startActivity(intent);*/

            if (etNamaLengkap.getText().toString().trim().isEmpty()){
                Toast.makeText(RegisterActivity.this, "Masukan Nama Lengkap!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (etNamapengguna.getText().toString().trim().isEmpty()){
                Toast.makeText(RegisterActivity.this, "Masukan Nama Pengguna!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (etKataSandi.getText().toString().trim().isEmpty()){
                Toast.makeText(RegisterActivity.this, "Masukan Kata Sand!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (etEmail.getText().toString().trim().isEmpty()){
                Toast.makeText(RegisterActivity.this, "Masukan Email!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (inputMobile.getText().toString().trim().isEmpty()){
                Toast.makeText(RegisterActivity.this, "Masukan Nomor HP!", Toast.LENGTH_SHORT).show();
                return;
            }

            new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah data anda sudah benar?").setContentText("")
                .setCancelText("Tidak")
                .setConfirmText("Ya")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                public void onClick(SweetAlertDialog sweetAlertDialog) {

                    progressBar.setVisibility(View.VISIBLE);
                    buttonGetOTP.setVisibility(View.INVISIBLE);

                    String verifikasi = codeAktivasiAkun.getText().toString();

                    if (verifikasi.equals("Email")){

                        simpan(verifikasi);

                    }else if (verifikasi.equals("SMS")){

                        simpan(verifikasi);

                    }else if (verifikasi.equals("WhatsApp")){

                        simpan(verifikasi);

                    }else{
                        progressBar.setVisibility(View.GONE);
                        buttonGetOTP.setVisibility(View.VISIBLE);
                        Toast.makeText(RegisterActivity.this, "Pilih Aktivasi Akun Terlebih Dahulu !", Toast.LENGTH_SHORT).show();
                    }

                    sweetAlertDialog.cancel();
                }
            }).show();
        }
        });




    }

    private void requestPermissionsSMS() {
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{
                    Manifest.permission.RECEIVE_SMS
            },100);
        }
    }

    private void getListAktivasiAkun() {
        ArrayList<String> arrayList = listAktivasiAkun;
        arrayList.add("Email");
        arrayList.add("SMS");
        //arrayList.add("WhatsApp");

        codeAA.setAdapter(new ArrayAdapter(RegisterActivity.this, android.R.layout.simple_spinner_item, listAktivasiAkun));
    }

    public void simpan(String byVerifikasi) {

        namaLengkap = etNamaLengkap.getText().toString();
        namaPengguna = etNamapengguna.getText().toString();
        kataSandi = etKataSandi.getText().toString();
        email = etEmail.getText().toString();
        nomorHp = "+62" + inputMobile.getText().toString();

        Log.d("TAG", "simpan: " + namaLengkap
                + namaPengguna
                        + kataSandi
                        + email
                + nomorHp);

        RequestQueue newRequestQueue = Volley.newRequestQueue(RegisterActivity.this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("name", namaLengkap);
            jSONObject.put("username", namaPengguna);
            jSONObject.put("password", kataSandi);
            jSONObject.put("email", email);
            jSONObject.put("otpBy", byVerifikasi);
            jSONObject.put("phone_number", nomorHp);
            String a = jSONObject.toString().replaceAll("\\\\", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString().replaceAll("\\\\", "");

        Log.d("TAG", "simpan: " + jSONObject2);

        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.Registrasi);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {

                    progressBar.setVisibility(View.GONE);
                    buttonGetOTP.setVisibility(View.VISIBLE);

                    String msg = jSONObject.getString("message");
                    String id = jSONObject.getJSONObject("data").getString("id");
                    String username = jSONObject.getJSONObject("data").getString("username");
                    String registerBy = jSONObject.getJSONObject("data").getString("otpBy");
                    String phone_number = jSONObject.getJSONObject("data").getString("phone_number");
                    String password = kataSandi;

                    new SweetAlertDialog(RegisterActivity.this, 2).setTitleText("Informasi")
                            .setContentText(msg)
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                    Intent intent = new Intent(RegisterActivity.this, VerifyOTPActivity.class);

                                    intent.putExtra("id", id);
                                    intent.putExtra("noHp", phone_number);
                                    intent.putExtra("email", email);
                                    intent.putExtra("username", username);
                                    intent.putExtra("password", password);
                                    intent.putExtra("registerBy", registerBy);

                                    startActivity(intent);

                                    sweetAlertDialog.cancel();
                                }
                            }).show();


                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    buttonGetOTP.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {

                if(volleyError instanceof ClientError){
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
                    new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                            .setContentText(msg)
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    progressBar.setVisibility(View.GONE);
                                    buttonGetOTP.setVisibility(View.VISIBLE);
                                    sweetAlertDialog.cancel();
                                }
                            }).show();
                }else if(volleyError instanceof NetworkError){
                        /*new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                                .setContentText("No Internet Connection")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new OnSweetClickListener() {
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.cancel();
                                    }
                                }).show();*/
                    progressBar.setVisibility(View.GONE);
                    buttonGetOTP.setVisibility(View.VISIBLE);

                    Snacky.builder()
                            .setActivity(RegisterActivity.this)
                            .setText("Tidak ada koneksi internet")
                            .setDuration(Snacky.LENGTH_INDEFINITE)
                            .setActionText("OK")
                            .error()
                            .show();
                }
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                StringBuilder sb = new StringBuilder();
                sb.append("Bearer ");
                sb.append("");
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