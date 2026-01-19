package com.fungsitama.dhsshopee.activity.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatButton;

import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.ClientError;
import com.android.volley.NetworkError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fungsitama.dhsshopee.util.ApiConfig;
import com.fungsitama.dhsshopee.util.SessionManager;
import com.fungsitama.dhsshopee.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;
import de.mateware.snacky.Snacky;

import static android.content.ContentValues.TAG;

public class LoginActivity extends Activity implements LocationListener {
    public static final String KEY_SHARED = "Login.KEY";
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    static String tokenid,user,vPwd,vUser;
    ImageButton btnnext,btnnext2;
    public AppCompatButton buttonLogin;
    public Context context;
    public EditText editTextPassword;
    private EditText editTextUser;
    TextView imei;
    public Activity mActivity;
    SessionManager manager;
    TelephonyManager tel;
    private Double currentLatitude;
    private Double currentLongitude;
    LocationManager locationManager;
    public String vLatitude;
    public String vLongitude;
    public String provider;
    static String vimei;
    static String deviceId;
    static private FirebaseAuth mAuth;

    private TextView txt_Aktivasi;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_login);
        mActivity = this;
        manager = new SessionManager();
        context = this;

        editTextUser = (EditText) findViewById(R.id.editTextUser);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        btnnext = (ImageButton) findViewById(R.id.next);
        btnnext2 = (ImageButton) findViewById(R.id.next2);
        imei = (TextView) findViewById(R.id.txtimei);

        this.buttonLogin = (AppCompatButton) findViewById(R.id.buttonLogin);
        vUser = this.editTextUser.getText().toString();
        vPwd = this.editTextPassword.getText().toString();
        checkPermission();
        statusCheck();
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        this.provider = this.locationManager.getBestProvider(new Criteria(), false);
        if (this.provider == null || this.provider.equals("")) {
          //  Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!this.provider.contains("gps")) {
            Intent intent = new Intent();
            intent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            intent.addCategory("android.intent.category.ALTERNATIVE");
            intent.setData(Uri.parse("3"));
            sendBroadcast(intent);
        }
        Location lastKnownLocation = this.locationManager.getLastKnownLocation("network");
        this.locationManager.requestLocationUpdates("network", 500, 0.0f, this);
        if (lastKnownLocation != null) {
            onLocationChanged(lastKnownLocation);
        } else {
            lastKnownLocation = this.locationManager.getLastKnownLocation(this.provider);
        }
        if (lastKnownLocation != null) {
            onLocationChanged(lastKnownLocation);
        } else {
            Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_LONG).show();
        }
       /* buttonLogin.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                //LoginActivity.this.ProsesLogin();
            }
        });*/
        btnnext.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                LoginActivity.this.editTextPassword.requestFocus();
            }
        });
        btnnext2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                    LoginActivity.this.buttonLogin.setFocusable(true);
                    LoginActivity.this.buttonLogin.setFocusableInTouchMode(true);
                    LoginActivity.this.buttonLogin.requestFocus();

            }
        });

        new Thread(new Runnable() {
            public void run() {
                try {
                    FirebaseInstanceId.getInstance().deleteInstanceId();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        final int reqcode = 1;


        txt_Aktivasi = findViewById(R.id.btntext_Aktivasi);



    }

    public void text_Aktivasi(View view){
        /*Toast.makeText(LoginActivity.this, "AAAA", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Aktivasi: " + " Berhasil");*/

        Intent intent = new Intent(LoginActivity.this, AktivasiAkunActivity.class);
        startActivity(intent);
    }


    @SuppressLint("MissingPermission")
    public void startLogin(View view) {
        deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        //tokenid = FirebaseInstanceId.getInstance().getToken();
        //Toast.makeText(context, tokenid + "imei: "+ deviceId, Toast.LENGTH_SHORT).show();
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this,new OnSuccessListener<InstanceIdResult>() {
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
                        Editor edit = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
                        edit.putString("token", string);
                        edit.putString("message", string2);
                        edit.putString("username", editTextUser.getText().toString().toUpperCase());
                        edit.commit();
                        LoginActivity.this.manager.setPreferences(LoginActivity.this, NotificationCompat.CATEGORY_STATUS, "1");
                        Log.d(NotificationCompat.CATEGORY_STATUS, LoginActivity.this.manager.getPreferences(LoginActivity.this, NotificationCompat.CATEGORY_STATUS));
                        LoginActivity.this.startActivity(new Intent(LoginActivity.this.context, SplashLoginActivity.class));
                        LoginActivity.this.overridePendingTransition(0, 0);
                        editTextUser.setText("");
                        editTextPassword.setText("");
                        editTextUser.requestFocus();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Context applicationContext = LoginActivity.this.getApplicationContext();
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
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                            .setContentText(msg)
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new OnSweetClickListener() {
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
                                .setActivity(LoginActivity.this)
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
                    hashMap.put("username", editTextUser.getText().toString());
                    hashMap.put("password", editTextPassword.getText().toString());
                    hashMap.put("imei", deviceId);
                    hashMap.put("token", tokenid);
                    hashMap.put("language", "id-ID");
                    return hashMap;
                }
            };
            Volley.newRequestQueue(this).add(r0);
    }


    public void startRegistrasi(View view) {
        LoginActivity.this.startActivity(new Intent(LoginActivity.this.context, RegisterActivity.class));
    }


    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this.mActivity, "android.permission.READ_PHONE_STATE") +
                ContextCompat.checkSelfPermission(this.mActivity, "android.permission.CAMERA") +
                //ContextCompat.checkSelfPermission(this.mActivity, "android.permission.RECEIVE_SMS") +
             /*   ContextCompat.checkSelfPermission(this.mActivity, "android.permission.ACCESS_FINE_LOCATION") +
                ContextCompat.checkSelfPermission(this.mActivity, "android.permission.ACCESS_COARSE_LOCATION") +*/
                ContextCompat.checkSelfPermission(this.mActivity, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            //this.imei.setText(this.tel.getDeviceId().toString());
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this.mActivity,
                "android.permission.READ_PHONE_STATE") ||
                ActivityCompat.shouldShowRequestPermissionRationale(this.mActivity, "android.permission.CAMERA")||
               // ActivityCompat.shouldShowRequestPermissionRationale(this.mActivity, "android.permission.RECEIVE_SMS")||
               /* ActivityCompat.shouldShowRequestPermissionRationale(this.mActivity, "android.permission.ACCESS_FINE_LOCATION")||
                ActivityCompat.shouldShowRequestPermissionRationale(this.mActivity, "android.permission.ACCESS_COARSE_LOCATION") ||*/
                ActivityCompat.shouldShowRequestPermissionRationale(this.mActivity, "android.permission.WRITE_EXTERNAL_STORAGE")){
            Builder builder = new Builder(this.mActivity);
            builder.setMessage("Camera, Read Location and Write External Storage permissions are required to do the task.");
            builder.setTitle("Please grant those permissions");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(LoginActivity.this.mActivity, new String[]{"android.permission.READ_PHONE_STATE",
                            "android.permission.CAMERA",
                           /* "android.permission.ACCESS_FINE_LOCATION",
                            "android.permission.ACCESS_COARSE_LOCATION",*/
                           "android.permission.WRITE_EXTERNAL_STORAGE"}, LoginActivity.MY_PERMISSIONS_REQUEST_CODE);
                }
            });
            builder.setNeutralButton("Cancel", null);
            builder.create().show();
        } else {
            ActivityCompat.requestPermissions(this.mActivity, new String[]{"android.permission.CAMERA",
                           /* "android.permission.ACCESS_FINE_LOCATION",
                            "android.permission.ACCESS_COARSE_LOCATION",*/
                            //"android.permission.RECEIVE_SMS",
                            "android.permission.READ_PHONE_STATE",
                            "android.permission.WRITE_EXTERNAL_STORAGE"},
                    MY_PERMISSIONS_REQUEST_CODE);  }
    }

    public void statusCheck() {
        if (!((LocationManager) getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled("gps")) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        Builder builder = new Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                LoginActivity.this.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == MY_PERMISSIONS_REQUEST_CODE) {
            if (iArr.length <= 0 || iArr[0] + iArr[1] + iArr[2] != 0) {
                Toast.makeText(this, "Permissions denied.", Toast.LENGTH_SHORT).show();
            } else {
                new SweetAlertDialog(this, 3).setTitleText("Permissions granted").setContentText("please restart this application").setConfirmText("Ok").showCancelButton(true).setConfirmClickListener(new OnSweetClickListener() {
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        LoginActivity.this.finishAffinity();
                        sweetAlertDialog.cancel();
                    }
                }).show();
            }
        }
    }

    public void onLocationChanged(Location location) {
        this.currentLatitude = Double.valueOf(location.getLatitude());
        this.currentLongitude = Double.valueOf(location.getLongitude());
        this.vLatitude = String.valueOf(this.currentLatitude);
        this.vLongitude = String.valueOf(this.currentLongitude);
    }

    public void onStatusChanged(String str, int i, Bundle bundle) {
        Toast.makeText(this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    public void onProviderEnabled(String str) {
    }
    public void onProviderDisabled(String str) {
        if (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 0);
        }
    }

    public void appExit() {
        finish();
        this.manager.setPreferences(this, NotificationCompat.CATEGORY_STATUS, "0");
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onBackPressed() {
        super.onBackPressed();
        appExit();
    }
}
