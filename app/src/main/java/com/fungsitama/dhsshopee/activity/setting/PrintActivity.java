package com.fungsitama.dhsshopee.activity.setting;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fungsitama.dhsshopee.BtPrint;
import com.fungsitama.dhsshopee.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class PrintActivity extends AppCompatActivity implements Runnable {
    public static final int QRcodeWidth = 350;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    protected static final String TAG = "TAG";

    public static OutputStream os;
    String EditTextValue;
    byte FONT_TYPE;
    Button Gen;
    int QRCODE_IMAGE_HEIGHT = 255;
    int QRCODE_IMAGE_WIDTH = 255;
    private UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    Bitmap bitmap;
    Bitmap bmOverlay;
    ImageView imageView;
    BluetoothAdapter mBluetoothAdapter;
    /* access modifiers changed from: private */
    public ProgressDialog mBluetoothConnectProgressDialog;
    BluetoothDevice mBluetoothDevice;
    /* access modifiers changed from: private */
    public BluetoothSocket mBluetoothSocket;
    Button mDisc;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            PrintActivity.this.mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(PrintActivity.this, "Device Connected", Toast.LENGTH_SHORT).show();
        }
    };
    Button mPrint, mScan, nPrint, prnGen, printtest, btn;
    EditText message;

    private BtPrint btPrint;
    private HashMap hm;
    private Switch sw;
    private ProgressBar pb;
    TextView tw, li, valprint;
    private Uri data;
    private String uri;
    private String[] kata, awb, token, url;
    static String abc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.r_layout);
        //vService = new MyPrintService();
        sw = (Switch) findViewById(R.id.switch1);
        pb = (ProgressBar) this.findViewById(R.id.printLoading);
        li = (TextView) this.findViewById(R.id.loadingInfo);
        tw = (TextView) this.findViewById(R.id.printInfo);
        btn = (Button) this.findViewById(R.id.transactionButton);
        valprint = (TextView) this.findViewById(R.id.valprint);
        btPrint = new BtPrint(sw, pb, li, tw, btn);

        data = this.getIntent().getData();


        if (data != null && data.isHierarchical()) {
            // uri = this.getIntent().getDataString().substring(19).replace("/", "");

            uri = this.getIntent().getDataString();
            //valprint.setText(uri);
            String text = uri.toString();
            kata = text.split("&");

            final String part1 = kata[0]; //awb
            final String part2 = kata[1]; //token
            final String part3 = kata[2]; //url

            awb = part1.split("\\?");
            final String awbpart0 = awb[0]; //awb
            final String awbpart1 = awb[1]; //awb

            token = part2.split("\\?");
            final String tokenpart0 = token[0]; //token
            final String tokenpart1 = token[1]; //token

            url = part3.split("\\?");
            final String urlpart0 = url[0]; //url
            final String urlpart1 = url[1]; //url

            if (awbpart0.equals("fct.print://printsmu")) {
                RequestQueue newRequestQueue = Volley.newRequestQueue(this);
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("smuNumber", awbpart1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String jSONObject2 = jSONObject.toString();
                StringBuilder sb = new StringBuilder();
                sb.append(getString(R.string.API));
                sb.append("https://sigap.api.fungsitama.web.id/api/modules/private/print/smu_checkin");
                JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject jSONObject) {
                        Log.i("Response", String.valueOf(jSONObject));
                        try {
                            JSONObject jSONObject3 = new JSONObject(jSONObject.getString("data"));
                            final String awb = jSONObject3.getString("smuNumber").substring(0, 3);
                            final String codeAirline = jSONObject3.getString("codeAirline");
                            final String nameAirline = jSONObject3.getString("nameAirline");
                            final String sequenceId = jSONObject3.getString("sequenceId");
                            final String smuNumber = jSONObject3.getString("smuNumber");
                            final String codeOrigin = jSONObject3.getString("codeOrigin");
                            final String codeDestination = jSONObject3.getString("codeDestination");

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btn.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
                                        public final void onClick(View it) {
                                            PrintActivity.access$getBtPrint$p(PrintActivity.this).socketConnect((Function1) (new Function1() {
                                                // $FF: synthetic method
                                                // $FF: bridge method
                                                public Object invoke(Object var1) {
                                                    this.invoke((HashMap) var1);
                                                    return Unit.INSTANCE;
                                                }

                                                public final void invoke(@NotNull final HashMap result) {
                                                    Intrinsics.checkParameterIsNotNull(result, "result");
                                                    if (Intrinsics.areEqual(result.get("success"), false)) {
                                                        PrintActivity.this.runOnUiThread((Runnable) (new Runnable() {
                                                            public final void run() {
                                                                TextView var10000 = (TextView) PrintActivity.this._$_findCachedViewById(R.id.printInfo);
                                                                Intrinsics.checkExpressionValueIsNotNull(var10000, "printInfo");
                                                                var10000.setText((CharSequence) String.valueOf(result.get("text")));
                                                                Switch var1 = (Switch) PrintActivity.this._$_findCachedViewById(R.id.switch1);
                                                                Intrinsics.checkExpressionValueIsNotNull(var1, "printSwitch");
                                                                sw.setChecked(false);
                                                                Toast.makeText((Context) PrintActivity.this, (CharSequence) "Please enable Service", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }));
                                                    } else {
                                                        PrintActivity.access$getBtPrint$p(PrintActivity.this).printJudul("AWB" + "\n", true);
                                                        PrintActivity.access$getBtPrint$p(PrintActivity.this).printAirlineCode(codeAirline, true);
                                                        PrintActivity.access$getBtPrint$p(PrintActivity.this).printAirlineName("- " + nameAirline + "\n", true);
                                                        PrintActivity.access$getBtPrint$p(PrintActivity.this).printSequeceId(sequenceId , true);
                                                        PrintActivity.access$getBtPrint$p(PrintActivity.this).printSmuNumber(smuNumber + "\n", true);
                                                        PrintActivity.access$getBtPrint$p(PrintActivity.this).printAirportOrigin(codeOrigin + "- ", true);
                                                        PrintActivity.access$getBtPrint$p(PrintActivity.this).printAirportDestination(codeDestination + "\n\n\n", true);

                                                    }
                                                }
                                            }));

                                        }
                                    }));
                                    btn.performClick();
                                    finish();
                                }
                            }, 3000);

                            //  PrintActivity.mAdapter.notifyDataSetChanged();
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        }
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
                        sb.append(tokenpart1);
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
            else if (awbpart0.equals("fct.print://printcoli")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
                            public final void onClick(View it) {
                                PrintActivity.access$getBtPrint$p(PrintActivity.this).socketConnect((Function1) (new Function1() {
                                    // $FF: synthetic method
                                    // $FF: bridge method
                                    public Object invoke(Object var1) {
                                        this.invoke((HashMap) var1);
                                        return Unit.INSTANCE;
                                    }

                                    public final void invoke(@NotNull final HashMap result) {
                                        Intrinsics.checkParameterIsNotNull(result, "result");
                                        if (Intrinsics.areEqual(result.get("success"), false)) {
                                            PrintActivity.this.runOnUiThread((Runnable) (new Runnable() {
                                                public final void run() {
                                                    TextView var10000 = (TextView) PrintActivity.this._$_findCachedViewById(R.id.printInfo);
                                                    Intrinsics.checkExpressionValueIsNotNull(var10000, "printInfo");
                                                    var10000.setText((CharSequence) String.valueOf(result.get("text")));
                                                    Switch var1 = (Switch) PrintActivity.this._$_findCachedViewById(R.id.switch1);
                                                    Intrinsics.checkExpressionValueIsNotNull(var1, "printSwitch");
                                                    sw.setChecked(false);
                                                    Toast.makeText((Context) PrintActivity.this, (CharSequence) "Please enable Service", Toast.LENGTH_SHORT).show();
                                                }
                                            }));
                                        } else {
                                            RequestQueue newRequestQueue = Volley.newRequestQueue(PrintActivity.this);
                                            JSONObject jSONObject = new JSONObject();
                                            try {
                                                jSONObject.put("smuNumber", awbpart1);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            final String jSONObject2 = jSONObject.toString();
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("https://sigap.api.fungsitama.web.id/api/modules/private/print/coli_checkin");
                                            JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
                                                public void onResponse(JSONObject jSONObject) {
                                                    Log.i("Response", String.valueOf(jSONObject));
                                                    try {
                                                        JSONObject jSONObject2 = new JSONObject(jSONObject.getString("data"));
                                                        JSONArray jSONArray = jSONObject.getJSONArray("detail");
                                                        for (int i = 0; i < jSONArray.length(); i++) {
                                                            JSONObject jArray = jSONArray.getJSONObject(i);
                                                            final String awb = jSONObject2.getString("smuNumber").substring(0, 3);
                                                            final String codeAirline = jSONObject2.getString("codeAirline");
                                                            final String nameAirline = jSONObject2.getString("nameAirline");
                                                            final String sequenceId = jArray.getString("sequenceId");
                                                            final String smuNumber = jArray.getString("qrCode");
                               /* final String sequenceId = jSONObject2.getString("sequenceId");
                                final String smuNumber = jSONObject2.getString("smuNumber");*/
                                                            final String codeOrigin = jSONObject2.getString("codeOrigin");
                                                            final String codeDestination = jSONObject2.getString("codeDestination");

                                                            PrintActivity.access$getBtPrint$p(PrintActivity.this).printJudul("" + "\n", true);
                                                            PrintActivity.access$getBtPrint$p(PrintActivity.this).printAirlineCode(codeAirline, true);
                                                            PrintActivity.access$getBtPrint$p(PrintActivity.this).printAirlineName("- " + nameAirline + "\n", true);
                                                            PrintActivity.access$getBtPrint$p(PrintActivity.this).printSequeceId(sequenceId , true);
                                                            PrintActivity.access$getBtPrint$p(PrintActivity.this).printSmuNumber(smuNumber + "\n", true);
                                                            PrintActivity.access$getBtPrint$p(PrintActivity.this).printAirportOrigin(codeOrigin + "- ", true);
                                                            PrintActivity.access$getBtPrint$p(PrintActivity.this).printAirportDestination(codeDestination + "\n\n\n", true);

                                                        }


                                                        //  PrintActivity.mAdapter.notifyDataSetChanged();
                                                    } catch (JSONException e2) {
                                                        e2.printStackTrace();
                                                    }
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
                                                    sb.append(tokenpart1);
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
                                    }
                                }));

                            }
                        }));
                        btn.performClick();
                        finish();
                    }
                }, 3000);
            }
            else if(awbpart0.equals("fct.print://printvehicle")){
                RequestQueue newRequestQueue = Volley.newRequestQueue(this);
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("id", awbpart1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String jSONObject2 = jSONObject.toString();
                StringBuilder sb = new StringBuilder();
                sb.append(getString(R.string.API));
                sb.append("modules/private/m_vehicle/get");
                JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject jSONObject) {
                        Log.i("Response", String.valueOf(jSONObject));
                        try {
                            JSONObject jSONObject3 = new JSONObject(jSONObject.getString("data"));
                            final String code = jSONObject3.getString("code");
                            final String vehicleTypeName = jSONObject3.getString("vehicleTypeName");
                            final String tenantTypeName = jSONObject3.getString("tenantTypeCode");

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btn.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
                                        public final void onClick(View it) {
                                            PrintActivity.access$getBtPrint$p(PrintActivity.this).socketConnect((Function1) (new Function1() {
                                                // $FF: synthetic method
                                                // $FF: bridge method
                                                public Object invoke(Object var1) {
                                                    this.invoke((HashMap) var1);
                                                    return Unit.INSTANCE;
                                                }

                                                public final void invoke(@NotNull final HashMap result) {
                                                    Intrinsics.checkParameterIsNotNull(result, "result");
                                                    if (Intrinsics.areEqual(result.get("success"), false)) {
                                                        PrintActivity.this.runOnUiThread((Runnable) (new Runnable() {
                                                            public final void run() {
                                                                TextView var10000 = (TextView) PrintActivity.this._$_findCachedViewById(R.id.printInfo);
                                                                Intrinsics.checkExpressionValueIsNotNull(var10000, "printInfo");
                                                                var10000.setText((CharSequence) String.valueOf(result.get("text")));
                                                                Switch var1 = (Switch) PrintActivity.this._$_findCachedViewById(R.id.switch1);
                                                                Intrinsics.checkExpressionValueIsNotNull(var1, "printSwitch");
                                                                sw.setChecked(false);
                                                                Toast.makeText((Context) PrintActivity.this, (CharSequence) "Please enable Service", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }));
                                                    } else {

                                                        PrintActivity.access$getBtPrint$p(PrintActivity.this).printJudul(vehicleTypeName.toUpperCase()+"\n", true);
                                                        PrintActivity.access$getBtPrint$p(PrintActivity.this).printAirlineCode(tenantTypeName + "\n", true);
                                                        PrintActivity.access$getBtPrint$p(PrintActivity.this).printSequeceId(code.trim(), true);
                                                        PrintActivity.access$getBtPrint$p(PrintActivity.this).printAirportDestination(code.replace("VHC","") + "\n\n\n", true);

                                                    }
                                                }
                                            }));

                                        }
                                    }));
                                    btn.performClick();
                                    finish();
                                }
                            }, 3000);

                            //  PrintActivity.mAdapter.notifyDataSetChanged();
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        }
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
                        sb.append(tokenpart1);
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
            else if(awbpart0.equals("fct.print://printdriver")){
                RequestQueue newRequestQueue = Volley.newRequestQueue(this);
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("id", awbpart1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String jSONObject2 = jSONObject.toString();
                StringBuilder sb = new StringBuilder();
                sb.append(getString(R.string.API));
                sb.append("modules/private/m_driver/get");
                JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject jSONObject) {
                        Log.i("Response", String.valueOf(jSONObject));
                        try {
                            JSONObject jSONObject3 = new JSONObject(jSONObject.getString("data"));
                            final String code = jSONObject3.getString("code");
                            final String name = jSONObject3.getString("name");
                            final String tenantTypeName = jSONObject3.getString("tenantTypeCode");

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btn.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
                                        public final void onClick(View it) {
                                            PrintActivity.access$getBtPrint$p(PrintActivity.this).socketConnect((Function1) (new Function1() {
                                                // $FF: synthetic method
                                                // $FF: bridge method
                                                public Object invoke(Object var1) {
                                                    this.invoke((HashMap) var1);
                                                    return Unit.INSTANCE;
                                                }

                                                public final void invoke(@NotNull final HashMap result) {
                                                    Intrinsics.checkParameterIsNotNull(result, "result");
                                                    if (Intrinsics.areEqual(result.get("success"), false)) {
                                                        PrintActivity.this.runOnUiThread((Runnable) (new Runnable() {
                                                            public final void run() {
                                                                TextView var10000 = (TextView) PrintActivity.this._$_findCachedViewById(R.id.printInfo);
                                                                Intrinsics.checkExpressionValueIsNotNull(var10000, "printInfo");
                                                                var10000.setText((CharSequence) String.valueOf(result.get("text")));
                                                                Switch var1 = (Switch) PrintActivity.this._$_findCachedViewById(R.id.switch1);
                                                                Intrinsics.checkExpressionValueIsNotNull(var1, "printSwitch");
                                                                sw.setChecked(false);
                                                                Toast.makeText((Context) PrintActivity.this, (CharSequence) "Please enable Service", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }));
                                                    } else {

                                                        PrintActivity.access$getBtPrint$p(PrintActivity.this).printJudul("DRIVER"+"\n", true);
                                                        PrintActivity.access$getBtPrint$p(PrintActivity.this).printAirlineCode(tenantTypeName + "\n", true);
                                                        PrintActivity.access$getBtPrint$p(PrintActivity.this).printSequeceId(code.trim() , true);
                                                        PrintActivity.access$getBtPrint$p(PrintActivity.this).printAirportDestination(code.replace("DRV","") + " - "+ name +"\n\n\n", true);

                                                    }
                                                }
                                            }));

                                        }
                                    }));
                                    btn.performClick();
                                    finish();
                                }
                            }, 3000);

                            //  PrintActivity.mAdapter.notifyDataSetChanged();
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        }
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
                        sb.append(tokenpart1);
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
            else if(awbpart0.equals("fct.print://printuld")){
                RequestQueue newRequestQueue = Volley.newRequestQueue(this);
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("id", awbpart1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String jSONObject2 = jSONObject.toString();
                StringBuilder sb = new StringBuilder();
                sb.append(getString(R.string.API));
                sb.append("modules/private/m_uld/get");
                JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), null, new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject jSONObject) {
                        Log.i("Response", String.valueOf(jSONObject));
                        try {
                            JSONObject jSONObject3 = new JSONObject(jSONObject.getString("data"));
                            final String code = jSONObject3.getString("code");
                            final String type = jSONObject3.getString("type");
                            final String nameLocation = jSONObject3.getString("codeLocation");

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btn.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
                                        public final void onClick(View it) {
                                            PrintActivity.access$getBtPrint$p(PrintActivity.this).socketConnect((Function1) (new Function1() {
                                                // $FF: synthetic method
                                                // $FF: bridge method
                                                public Object invoke(Object var1) {
                                                    this.invoke((HashMap) var1);
                                                    return Unit.INSTANCE;
                                                }

                                                public final void invoke(@NotNull final HashMap result) {
                                                    Intrinsics.checkParameterIsNotNull(result, "result");
                                                    if (Intrinsics.areEqual(result.get("success"), false)) {
                                                        PrintActivity.this.runOnUiThread((Runnable) (new Runnable() {
                                                            public final void run() {
                                                                TextView var10000 = (TextView) PrintActivity.this._$_findCachedViewById(R.id.printInfo);
                                                                Intrinsics.checkExpressionValueIsNotNull(var10000, "printInfo");
                                                                var10000.setText((CharSequence) String.valueOf(result.get("text")));
                                                                Switch var1 = (Switch) PrintActivity.this._$_findCachedViewById(R.id.switch1);
                                                                Intrinsics.checkExpressionValueIsNotNull(var1, "printSwitch");
                                                                sw.setChecked(false);
                                                                Toast.makeText((Context) PrintActivity.this, (CharSequence) "Please enable Service", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }));
                                                    } else {

                                                        PrintActivity.access$getBtPrint$p(PrintActivity.this).printJudul(type.toUpperCase()+"\n", true);
                                                        PrintActivity.access$getBtPrint$p(PrintActivity.this).printAirlineCode(nameLocation + "\n", true);
                                                        PrintActivity.access$getBtPrint$p(PrintActivity.this).printSequeceId(code.trim(), true);
                                                        PrintActivity.access$getBtPrint$p(PrintActivity.this).printAirportDestination(code.trim() + "\n\n\n", true);

                                                    }
                                                }
                                            }));

                                        }
                                    }));
                                    btn.performClick();
                                    finish();
                                }
                            }, 3000);

                            //  PrintActivity.mAdapter.notifyDataSetChanged();
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        }
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
                        sb.append(tokenpart1);
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
            else {
                Toast.makeText(PrintActivity.this, "scheme not found", Toast.LENGTH_SHORT).show();
            }
        }

    }

    // $FF: synthetic method
    public static final BtPrint access$getBtPrint$p(PrintActivity $this) {
        BtPrint var10000 = $this.btPrint;
        if (var10000 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btPrint");
        }

        return var10000;
    }

    // $FF: synthetic method
   /* public static final void access$setBtPrint$p(PrintActivity $this, BtPrint var1) {
        $this.btPrint = var1;
    }*/

    public View _$_findCachedViewById(int var1) {
        if (this.hm == null) {
            this.hm = new HashMap();
        }

        View var2 = (View) this.hm.get(var1);
        if (var2 == null) {
            var2 = this.findViewById(var1);
            this.hm.put(var1, var2);
        }

        return var2;
    }

    public void _$_clearFindViewByIdCache() {
        if (this.hm != null) {
            this.hm.clear();
        }

    }


    public void onBackPressed() {
        try {
            if (this.mBluetoothSocket != null) {
                this.mBluetoothSocket.close();
            }
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
        setResult(0);
        finish();
    }


    public void run() {
        try {
            this.mBluetoothSocket = this.mBluetoothDevice.createRfcommSocketToServiceRecord(this.applicationUUID);
            this.mBluetoothAdapter.cancelDiscovery();
            this.mBluetoothSocket.connect();
            this.mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.d(TAG, "Could Not Connect To Socket", eConnectException);
            closeSocket(this.mBluetoothSocket);
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "Socket Closed");
        } catch (IOException e) {
            Log.d(TAG, "Could Not Close Socket");
        }
    }

}
