package com.fungsitama.dhsshopee.activity.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fungsitama.dhsshopee.R;
import com.fungsitama.dhsshopee.activity.dhs.Unloading.DaftarUnloadingActivity;
import com.fungsitama.dhsshopee.activity.main.NavMenuActivity;
import com.fungsitama.dhsshopee.util.ApiConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.mateware.snacky.Snacky;

public class AktivasiAkunActivity extends AppCompatActivity {

    private EditText et_Akun;

    private TextView txt_IdAkun,txt_NamaPengguna,txt_Email,txt_NomorHp,status_Akun,status_Akun1,txt_AktivasiBy,txt_Login;

    private CardView cv_cardAkun,cv_cardAkun1;
    private RelativeLayout tombol;
    private LinearLayout ly_textWarning01;

    private Button btn_CekAkun,buttonGetOTP;

    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktivasi_akun);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Konfirmasi Registrasi");
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        et_Akun = findViewById(R.id.et_Akun);

        txt_IdAkun = findViewById(R.id.txt_IdAkun);
        txt_NamaPengguna = findViewById(R.id.txt_NamaPengguna);
        txt_Email = findViewById(R.id.txt_Email);
        txt_NomorHp = findViewById(R.id.txt_NomorHp);
        txt_AktivasiBy = findViewById(R.id.txt_AktivasiBy);

        cv_cardAkun = findViewById(R.id.cv_cardAkun);
        status_Akun = findViewById(R.id.status_Akun);

        cv_cardAkun1 = findViewById(R.id.cv_cardAkun1);
        status_Akun1 = findViewById(R.id.status_Akun1);

        ly_textWarning01 = findViewById(R.id.ly_textWarning01);

        btn_CekAkun = findViewById(R.id.btn_CekAkun);

        btn_CekAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfoAkun();
            }
        });

        tombol = findViewById(R.id.tombol);
        tombol.setVisibility(View.GONE);

        buttonGetOTP = findViewById(R.id.buttonGetOTP);
        buttonGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(AktivasiAkunActivity.this);

                alert.setTitle("Password");
                alert.setMessage("Masukan password akun anda ?");

                // Set an EditText view to get user input
                final EditText input = new EditText(AktivasiAkunActivity.this);
                input.setPaddingRelative(50,0,50,20);
                input.requestFocus();
                input.setInputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);

                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        password = input.getText().toString();

                        Log.d("TAG", "Password 1: " + password);

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                        Intent intent = new Intent(AktivasiAkunActivity.this, VerifyOTPActivity.class);

                        intent.putExtra("id", txt_IdAkun.getText().toString());
                        intent.putExtra("noHp", txt_NomorHp.getText().toString());
                        intent.putExtra("email", txt_Email.getText().toString());
                        intent.putExtra("username", txt_NamaPengguna.getText().toString());
                        intent.putExtra("password", password);
                        intent.putExtra("registerBy", txt_AktivasiBy.getText().toString());

                        startActivity(intent);

                        // Do something with value!
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                });

                input.setText(password);

                alert.setCancelable(false);
                alert.show();

                input.setSelection(input.getText().length());
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            }
        });

        txt_Login = (TextView) findViewById(R.id.txt_Login);

        txt_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AktivasiAkunActivity.this, LoginActivity.class);
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


    public void getInfoAkun(){
        RequestQueue newRequestQueue = Volley.newRequestQueue(AktivasiAkunActivity.this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("id", et_Akun.getText().toString());
            jSONObject.put("username", et_Akun.getText().toString());
            jSONObject.put("email", et_Akun.getText().toString());
            jSONObject.put("phone_number", et_Akun.getText().toString());
            String a = jSONObject.toString().replaceAll("\\\\", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString().replaceAll("\\\\", "");

        Log.d("TAG", "aktivasi: " + jSONObject2);

        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.InfoAkun);

        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            public void onResponse(JSONObject jSONObject) {
                try {

                    String id = jSONObject.getJSONObject("data").getString("id");
                    String username = jSONObject.getJSONObject("data").getString("username");
                    String email = jSONObject.getJSONObject("data").getString("email");
                    String phone_number = jSONObject.getJSONObject("data").getString("phone_number");
                    String lastOtpRegisterBy = jSONObject.getJSONObject("data").getString("lastOtpRegisterBy");

                    String password = jSONObject.getJSONObject("data").getString("password");
                    String isactive = jSONObject.getJSONObject("data").getString("isactive");
                    String lastOtp = jSONObject.getJSONObject("data").getString("lastOtp");

                    txt_IdAkun.setText(id);
                    txt_NamaPengguna.setText(username);
                    txt_Email.setText(email);
                    txt_NomorHp.setText(phone_number);
                    txt_AktivasiBy.setText(lastOtpRegisterBy);
                    Log.d("TAG", "status 1: " + isactive);


                    if (lastOtp.equals("null")){
                        cv_cardAkun.setCardBackgroundColor(Color.parseColor("#DD2111"));
                        status_Akun.setText("Akun anda belum konfirmasi registrasi");
                        tombol.setVisibility(View.VISIBLE);

                        cv_cardAkun.setVisibility(View.VISIBLE);
                        cv_cardAkun1.setVisibility(View.GONE);

                        ly_textWarning01.setVisibility(View.GONE);

                    }else if(isactive.equals("1")){
                        cv_cardAkun.setCardBackgroundColor(Color.parseColor("#2196F3"));
                        status_Akun.setText("Akun anda sudah aktif");
                        tombol.setVisibility(View.GONE);

                        cv_cardAkun.setVisibility(View.VISIBLE);
                        cv_cardAkun1.setVisibility(View.GONE);

                        ly_textWarning01.setVisibility(View.GONE);

                    }else{
                        cv_cardAkun1.setCardBackgroundColor(Color.parseColor("#DD2111"));
                        status_Akun1.setText("Akun anda belum aktif");
                        tombol.setVisibility(View.GONE);

                        cv_cardAkun.setCardBackgroundColor(Color.parseColor("#FFC107"));
                        status_Akun.setText("Akun anda sudah konfirmasi registrasi");
                        tombol.setVisibility(View.GONE);

                        cv_cardAkun.setVisibility(View.VISIBLE);
                        cv_cardAkun1.setVisibility(View.VISIBLE);

                        ly_textWarning01.setVisibility(View.VISIBLE);

                    }/*else{
                        cv_cardAkun.setCardBackgroundColor(Color.parseColor("#DD2111"));
                        status_Akun.setText("Akun anda sudah konfirmasi registrasi");
                        tombol.setVisibility(View.GONE);

                        cv_cardAkun.setVisibility(View.VISIBLE);
                        cv_cardAkun1.setVisibility(View.VISIBLE);

                    }*/

                    /*if (lastOtp.equals("null")){

                        *//*cv_cardAkun.setVisibility(View.VISIBLE);
                        cv_cardAkun1.setVisibility(View.GONE);*//*

                        cv_cardAkun.setCardBackgroundColor(Color.parseColor("#DD2111"));
                        status_Akun.setText("Akun anda belum konfirmasi registrasi");
                        tombol.setVisibility(View.VISIBLE);
                    }else{

                        *//*cv_cardAkun.setVisibility(View.VISIBLE);
                        cv_cardAkun1.setVisibility(View.VISIBLE);*//*

                        cv_cardAkun.setCardBackgroundColor(Color.parseColor("#FF5722"));
                        status_Akun.setText("Akun anda sudah konfirmasi registrasi");
                        tombol.setVisibility(View.GONE);
                    }


                    if (isactive.equals("0")){

                        *//*cv_cardAkun.setVisibility(View.VISIBLE);
                        cv_cardAkun1.setVisibility(View.VISIBLE);*//*

                        cv_cardAkun1.setCardBackgroundColor(Color.parseColor("#DD2111"));
                        status_Akun1.setText("Akun anda belum aktif");
                        tombol.setVisibility(View.GONE);
                    }else{

                        *//*cv_cardAkun.setVisibility(View.GONE);
                        cv_cardAkun1.setVisibility(View.VISIBLE);*//*

                        cv_cardAkun1.setCardBackgroundColor(Color.parseColor("#2196F3"));
                        status_Akun1.setText("Akun anda sudah aktif");
                        tombol.setVisibility(View.GONE);
                    }*/

                } catch (JSONException e) {
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
                    new SweetAlertDialog(AktivasiAkunActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                            .setContentText(msg)
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();
                                }
                            }).show();
                }else if(volleyError instanceof NetworkError){

                    Snacky.builder()
                            .setActivity(AktivasiAkunActivity.this)
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