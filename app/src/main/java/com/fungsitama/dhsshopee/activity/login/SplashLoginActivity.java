package com.fungsitama.dhsshopee.activity.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.core.app.NotificationCompat;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.NetworkError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fungsitama.dhsshopee.util.ApiConfig;
import com.fungsitama.dhsshopee.util.SessionManager;
import com.fungsitama.dhsshopee.R;
import com.fungsitama.dhsshopee.activity.main.NavMenuActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.mateware.snacky.Snacky;

public class SplashLoginActivity extends Activity{
    public Context context;

    SessionManager manager;
    String username,token,msg,typeLogin,typeUser,masterCode;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_splash_login);
        getWindow().setStatusBarColor(Color.WHITE);

        this.context = this;
        this.manager = new SessionManager();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.token = defaultSharedPreferences.getString("token", null);
        this.msg = defaultSharedPreferences.getString("message", null);
        this.username = defaultSharedPreferences.getString("username", null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    //sleep(2000);
                    String preferences = SplashLoginActivity.this.manager.getPreferences(SplashLoginActivity.this, NotificationCompat.CATEGORY_STATUS);
                    if (preferences.equals("1")) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(ApiConfig.Get_Detail_User);
                        StringRequest r1 = new StringRequest(1, sb.toString(), new Response.Listener<String>() {
                            public void onResponse(String str) {
                                try {
                                    JSONObject jSONObject = new JSONObject(str).getJSONObject("data");
                                    typeLogin = jSONObject.getString("typeLogin");
                                    typeUser = jSONObject.getString("typeUser");
                                    masterCode = jSONObject.getString("codeMaster");
                                    //Log.d("TAG", "onResponseData: " + );
                                    Log.d("TAG", "onResponseData: "+typeLogin +" - "+ typeUser);
                                    SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(SplashLoginActivity.this).edit();
                                    edit.putString("token", SplashLoginActivity.this.token);
                                    edit.putString("message", SplashLoginActivity.this.msg);
                                    edit.putString("username", SplashLoginActivity.this.username);
                                    edit.putString("typeLogin", SplashLoginActivity.this.typeLogin);
                                    edit.putString("typeUser", SplashLoginActivity.this.typeUser);
                                    edit.putString("codeMaster", SplashLoginActivity.this.masterCode);
                                    edit.commit();
                                    SplashLoginActivity.this.startActivity(new Intent(SplashLoginActivity.this.context, NavMenuActivity.class));
                                    SplashLoginActivity.this.overridePendingTransition(0, 0);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Context applicationContext = SplashLoginActivity.this.getApplicationContext();
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("Error: ");
                                    sb.append(e.getMessage());
                                    Toast.makeText(applicationContext, sb.toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            public void onErrorResponse(VolleyError volleyError) {
                                volleyError.printStackTrace();
                                Toast.makeText(SplashLoginActivity.this, "Gagal", Toast.LENGTH_LONG).show();
                            }
                        }) {
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap hashMap = new HashMap();
                                StringBuilder sb = new StringBuilder();
                                sb.append("Bearer ");
                                sb.append(SplashLoginActivity.this.token);
                                hashMap.put("Authorization", sb.toString());
                                return hashMap;
                            }
                        };
                        Volley.newRequestQueue(SplashLoginActivity.this).add(r1);
                    } else {
                        SplashLoginActivity.this.startActivity(new Intent(SplashLoginActivity.this, LoginActivity.class));
                        SplashLoginActivity.this.overridePendingTransition(0, 0);
                    }
                    SplashLoginActivity.this.finish();
                } catch (Exception unused) {
                }
            }
        }, 2000);
       /* new Thread() {
            public void run() {

            }
        }.start();*/
    }
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }
}
