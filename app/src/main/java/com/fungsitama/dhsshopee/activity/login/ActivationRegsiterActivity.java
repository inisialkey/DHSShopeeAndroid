package com.fungsitama.dhsshopee.activity.login;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.fungsitama.dhsshopee.util.ApiConfig;
import com.fungsitama.dhsshopee.R;
import com.fungsitama.dhsshopee.util.SessionManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.mateware.snacky.Snacky;

import static android.content.ContentValues.TAG;

public class ActivationRegsiterActivity extends AppCompatActivity {

    private String id,token,email;
    private TextView txtToken;
    private EditText etToken;
    private Button btnAktivasi;

    private LinearLayout ly_Register;

    private TextView txt_Login,txtEmail;

    static String deviceId,tokenid,username,password;

    SessionManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation_regsiter);

        Intent intent = getIntent();

        id = intent.getStringExtra("id");
        email = intent.getStringExtra("email");

        txtToken = (TextView) findViewById(R.id.txt_Token);

        txtEmail = (TextView) findViewById(R.id.txt_Email);

        txtEmail.setText(email);

        etToken = (EditText) findViewById(R.id.et_Token);
        btnAktivasi = (Button) findViewById(R.id.btn_Aktivasi);

        //ly_Register =  (LinearLayout) findViewById(R.id.ly_Register);
        txt_Login = (TextView) findViewById(R.id.txt_Login);


        /*ly_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivationRegsiterActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });*/

        txt_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivationRegsiterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        txtToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getToken();
            }
        });



        btnAktivasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAktivasi();
            }
        });

    }


    public void getToken(){

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Yakin kirim ulang token ?").setContentText("")
                .setCancelText("Cancel")
                .setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        RequestQueue newRequestQueue = Volley.newRequestQueue(ActivationRegsiterActivity.this);
                        JSONObject jSONObject = new JSONObject();
                        try {
                            jSONObject.put("id", id);
                            jSONObject.put("otpBy", "Email");
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
                                    new SweetAlertDialog(ActivationRegsiterActivity.this, 2).setTitleText("Informasi")
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
                                new SweetAlertDialog(ActivationRegsiterActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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


    public void getAktivasi(){

        token = etToken.getText().toString();

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Yakin akun Anda diaktivasi?").setContentText("")
            .setCancelText("Cancel")
            .setConfirmText("Yes")
            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    RequestQueue newRequestQueue = Volley.newRequestQueue(ActivationRegsiterActivity.this);
                    JSONObject jSONObject = new JSONObject();
                    try {
                        jSONObject.put("id", id);
                        jSONObject.put("otp", token);
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
                                new SweetAlertDialog(ActivationRegsiterActivity.this, 2).setTitleText("Informasi")
                                        .setContentText(msg)
                                        .setConfirmText("Ok")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                /*Intent intent = new Intent(ActivationRegsiterActivity.this, LoginActivity.class);

                                                startActivity(intent);*/

                                                startLogin();

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
                            new SweetAlertDialog(ActivationRegsiterActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
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
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(ActivationRegsiterActivity.this,new OnSuccessListener<InstanceIdResult>() {
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
                    SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(ActivationRegsiterActivity.this).edit();
                    edit.putString("token", string);
                    edit.putString("message", string2);
                    edit.putString("username", username.toUpperCase());
                    edit.commit();
                    ActivationRegsiterActivity.this.manager.setPreferences(ActivationRegsiterActivity.this, NotificationCompat.CATEGORY_STATUS, "1");
                    Log.d(NotificationCompat.CATEGORY_STATUS, ActivationRegsiterActivity.this.manager.getPreferences(ActivationRegsiterActivity.this, NotificationCompat.CATEGORY_STATUS));
                    ActivationRegsiterActivity.this.startActivity(new Intent(ActivationRegsiterActivity.this, SplashLoginActivity.class));
                    ActivationRegsiterActivity.this.overridePendingTransition(0, 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Context applicationContext = ActivationRegsiterActivity.this.getApplicationContext();
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
                    new SweetAlertDialog(ActivationRegsiterActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                            .setContentText(msg)
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
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

                    Snacky.builder()
                            .setActivity(ActivationRegsiterActivity.this)
                            .setText("Tidak ada koneksi internet")
                            .setDuration(Snacky.LENGTH_INDEFINITE)
                            .setActionText("OK")
                            .error()
                            .show();
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