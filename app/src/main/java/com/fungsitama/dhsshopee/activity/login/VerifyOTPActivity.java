package com.fungsitama.dhsshopee.activity.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fungsitama.dhsshopee.R;
import com.fungsitama.dhsshopee.activity.dhs.Loading.ScanBarcodeLoadingActivity;
import com.fungsitama.dhsshopee.activity.dhs.Loading.TambahBarcodeManualActivity;
import com.fungsitama.dhsshopee.activity.dhs.Loading.TambahMuatBarangActivity;
import com.fungsitama.dhsshopee.activity.main.NavMenuActivity;
import com.fungsitama.dhsshopee.util.ApiConfig;
import com.fungsitama.dhsshopee.util.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.mateware.snacky.Snacky;

import static android.content.ContentValues.TAG;

public class VerifyOTPActivity extends AppCompatActivity {

    private EditText inputCode1,inputCode2,inputCode3,inputCode4,inputCode5,inputCode6,inputCode7,inputCode8;
    private String verificationId,id,status;

    static String deviceId,tokenid,username,password,registerBy,email,noHp;

    private TextView txt_Login,txt_SubTitle,title_kirimKode,textMobile;

    private EditText textOTP;

    SessionManager manager;

    String otpByotp;

    private TextView mTextView;
    private CountDownTimer mCountDownTimer;

    private static final long INTERVAL = 1000L;
    private long timeRemaining = 4000L;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Konfirmasi Registrasi");
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputCode1 = findViewById(R.id.inputCode1);
        inputCode2 = findViewById(R.id.inputCode2);
        inputCode3 = findViewById(R.id.inputCode3);
        inputCode4 = findViewById(R.id.inputCode4);
        inputCode5 = findViewById(R.id.inputCode5);
        inputCode6 = findViewById(R.id.inputCode6);
        inputCode7 = findViewById(R.id.inputCode7);
        inputCode8 = findViewById(R.id.inputCode8);

        setupOTPinput();

        manager = new SessionManager();

        final ProgressBar progressBar = findViewById(R.id.progressBar);
        final Button buttonVerivy = findViewById(R.id.buttonVerify);

        txt_SubTitle = findViewById(R.id.txt_SubTitle);
        title_kirimKode = findViewById(R.id.title_kirimKode);
        textMobile = findViewById(R.id.textMobile);


        id = getIntent().getStringExtra("id");
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");
        registerBy = getIntent().getStringExtra("registerBy");
        email = getIntent().getStringExtra("email");
        noHp = getIntent().getStringExtra("noHp");

        mTextView = (TextView)findViewById(R.id.text_view);
        mTextView.setVisibility(View.GONE);

        Log.d(TAG, "cek Data: " + noHp + " " + email + " " + registerBy);


        txt_SubTitle.setText("Untuk konfirmasi registrasi, Cek " + registerBy + " Anda !");

        if (registerBy.equals("SMS")){
            title_kirimKode.setText("Kode OTP dikirimkan ke Nomor HP");
            textMobile.setText(noHp);
        }/*else if (registerBy.equals("WhatsApp")){
            title_kirimKode.setText("Kode OTP dikirimkan ke Nomor" + registerBy);
            textMobile.setText(noHp);
        }*/else{
            title_kirimKode.setText("Kode OTP dikirimkan ke " + registerBy);
            textMobile.setText(email);
        }

        Log.d("TAG", "cekId: " + id + " " + status);


        textOTP = findViewById(R.id.textOTP);

        new OTPReceiver().setEditText_otp(textOTP);
        //textOTP.setText("12345678");

        textOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){

                    otpByotp = textOTP.getText().toString();

                    Log.d("TAG", "otpByotp: (" + otpByotp +")");

                    inputCode1.setText(otpByotp.substring(0,1));
                    inputCode2.setText(otpByotp.substring(1,2));
                    inputCode3.setText(otpByotp.substring(2,3));
                    inputCode4.setText(otpByotp.substring(3,4));
                    inputCode5.setText(otpByotp.substring(4,5));
                    inputCode6.setText(otpByotp.substring(5,6));
                    inputCode7.setText(otpByotp.substring(6,7));
                    inputCode8.setText(otpByotp.substring(7,8));
                    inputCode8.setSelection(inputCode8.getText().length());

                    String codeOtp = inputCode1.getText().toString() + inputCode2.getText().toString() +
                            inputCode3.getText().toString() + inputCode4.getText().toString() +
                            inputCode5.getText().toString() + inputCode6.getText().toString() +
                            inputCode7.getText().toString() + inputCode8.getText().toString();


                    mCountDownTimer = new CountDownTimer(timeRemaining, INTERVAL) {
                        @Override
                        public void onTick(long l) {
                            mTextView.setText(String.format(Locale.getDefault(), "%d sec.", l / 1000L));

                            progressBar.setVisibility(View.VISIBLE);
                            buttonVerivy.setVisibility(View.GONE);

                            timeRemaining = l;
                        }

                        @Override
                        public void onFinish() {

                            progressBar.setVisibility(View.GONE);
                            buttonVerivy.setVisibility(View.INVISIBLE);

                            mTextView.setText("Done.");
                            getAktivasi(codeOtp);
                        }
                    };
                    mCountDownTimer.start();
                }



                }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        buttonVerivy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (inputCode1.getText().toString().trim().isEmpty() || inputCode2.getText().toString().trim().isEmpty()
                            || inputCode3.getText().toString().trim().isEmpty() || inputCode4.getText().toString().trim().isEmpty()
                            || inputCode4.getText().toString().trim().isEmpty() || inputCode5.getText().toString().trim().isEmpty()
                            || inputCode6.getText().toString().trim().isEmpty() || inputCode7.getText().toString().trim().isEmpty()
                            | inputCode8.getText().toString().trim().isEmpty()){
                        progressBar.setVisibility(View.GONE);
                        buttonVerivy.setVisibility(View.VISIBLE);
                        Toast.makeText(VerifyOTPActivity.this, "Mohon Masukan Kode OTP !", Toast.LENGTH_SHORT).show();
                    }else{

                        String codeOtp = inputCode1.getText().toString() + inputCode2.getText().toString() +
                                inputCode3.getText().toString() + inputCode4.getText().toString() +
                                inputCode5.getText().toString() + inputCode6.getText().toString() +
                                inputCode7.getText().toString() + inputCode8.getText().toString();

                        getAktivasi(codeOtp);

                    }
                }catch (Exception e){
                    progressBar.setVisibility(View.GONE);
                    buttonVerivy.setVisibility(View.VISIBLE);
                    Log.d(TAG, "Exception: "+e.toString());
                }
            }
        });

        TextView textResendOTP = findViewById(R.id.textResendOTP);
        textResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(VerifyOTPActivity.this, "Di Klik", Toast.LENGTH_SHORT).show();

                getSendOTP();

            }
        });


        txt_Login = (TextView) findViewById(R.id.txt_Login);

        txt_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerifyOTPActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }


    private void setupOTPinput(){
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode7.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode8.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void getAktivasi(String otp){

        RequestQueue newRequestQueue = Volley.newRequestQueue(VerifyOTPActivity.this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("id", id);
            jSONObject.put("otp", otp);
            //jSONObject.put("language", "");
            String a = jSONObject.toString().replaceAll("\\\\", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString().replaceAll("\\\\", "");

        Log.d("TAG", "aktivasi: " + jSONObject2);

        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.ActivationRegsitrasi);

        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    String msg = jSONObject.getString("message");
                    new SweetAlertDialog(VerifyOTPActivity.this, 2).setTitleText("Informasi")
                            .setContentText(msg)
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                    Intent intent = new Intent(VerifyOTPActivity.this, PeringatanAkunActivity.class);

                                    (VerifyOTPActivity.this).finish();
                                    intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    startActivity(intent);

                                    //startLogin();

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
                new SweetAlertDialog(VerifyOTPActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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

    public void getSendOTP(){
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Yakin kirim ulang kode OTP ?").setContentText("")
                .setCancelText("Cancel")
                .setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        RequestQueue newRequestQueue = Volley.newRequestQueue(VerifyOTPActivity.this);
                        JSONObject jSONObject = new JSONObject();
                        try {
                            jSONObject.put("id", id);
                            jSONObject.put("otpBy", registerBy);
                            String a = jSONObject.toString().replaceAll("\\\\", "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        final String jSONObject2 = jSONObject.toString().replaceAll("\\\\", "");

                        Log.d("TAG", "aktivasi: " + jSONObject2);

                        StringBuilder sb = new StringBuilder();
                        sb.append(ApiConfig.ResendActivation);

                        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
                            public void onResponse(JSONObject jSONObject) {
                                try {
                                    String msg = jSONObject.getString("message");
                                    new SweetAlertDialog(VerifyOTPActivity.this, 2).setTitleText("Informasi")
                                            .setContentText(msg)
                                            .setConfirmText("Ok")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
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
                                new SweetAlertDialog(VerifyOTPActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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
                        sweetAlertDialog.cancel();
                    }
                }).show();
    }

    public void startLogin() {

        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        //tokenid = FirebaseInstanceId.getInstance().getToken();
        //Toast.makeText(context, tokenid + "imei: "+ deviceId, Toast.LENGTH_SHORT).show();
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(VerifyOTPActivity.this,new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                tokenid = instanceIdResult.getToken();
                Log.d(TAG, "tokenid: "+tokenid);
            }
        });

        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.Login);
        StringRequest r0 = new StringRequest(1, sb.toString(), new Response.Listener<String>() {
            public void onResponse(String str) {
                try {
                    JSONObject jSONObject = new JSONObject(str);
                    String string = jSONObject.getString("token");
                    String string2 = jSONObject.getString("message");

                    //Toast.makeText(LoginActivity.this, string + " "+ string2, Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(VerifyOTPActivity.this).edit();
                    edit.putString("token", string);
                    edit.putString("message", string2);
                    edit.putString("username", username.toUpperCase());
                    edit.commit();
                    VerifyOTPActivity.this.manager.setPreferences(VerifyOTPActivity.this, NotificationCompat.CATEGORY_STATUS, "1");
                    Log.d(NotificationCompat.CATEGORY_STATUS, VerifyOTPActivity.this.manager.getPreferences(VerifyOTPActivity.this, NotificationCompat.CATEGORY_STATUS));
                    VerifyOTPActivity.this.startActivity(new Intent(VerifyOTPActivity.this, SplashLoginActivity.class));
                    VerifyOTPActivity.this.overridePendingTransition(0, 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Context applicationContext = VerifyOTPActivity.this.getApplicationContext();
                    StringBuilder sb = new StringBuilder();
                    sb.append("Error: ");
                    sb.append(e.getMessage());
                    Toast.makeText(applicationContext, sb.toString(), Toast.LENGTH_LONG).show();
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
                    new SweetAlertDialog(VerifyOTPActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                        .setContentText(msg)
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                Intent intent = new Intent(VerifyOTPActivity.this, LoginActivity.class);
                                (VerifyOTPActivity.this).finish();
                                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);

                                sweetAlertDialog.cancel();
                            }
                        }).show();
                }else if(volleyError instanceof NetworkError){

                    new SweetAlertDialog(VerifyOTPActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                        .setContentText("Tidak ada koneksi internet")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                Intent intent = new Intent(VerifyOTPActivity.this, LoginActivity.class);
                                (VerifyOTPActivity.this).finish();
                                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                                sweetAlertDialog.cancel();

                            }
                        }).show();
            }
            }
        }) {
            /* access modifiers changed from: protected */
            public Map<String, String> getParams() {
                HashMap hashMap = new HashMap();
                hashMap.put("username", username);
                hashMap.put("password", password);
                hashMap.put("imei", deviceId);
                hashMap.put("token", tokenid);
                hashMap.put("language", "id-ID");
                return hashMap;
            }
        };
        Volley.newRequestQueue(this).add(r0);
    }

}