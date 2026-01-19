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
import android.widget.ToggleButton;

import com.fungsitama.dhsshopee.BtPrint;
import com.fungsitama.dhsshopee.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.UUID;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class SettingActivity extends AppCompatActivity implements Runnable {
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
            SettingActivity.this.mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(SettingActivity.this, "Device Connected", Toast.LENGTH_SHORT).show();
        }
    };
    Button mPrint, mScan, nPrint, prnGen, printtest, btn;
    EditText message;

    private BtPrint btPrint;
    private HashMap hm;
    private Switch sw;
    private ProgressBar pb;
    TextView tw, li, valprint,txttoggle;
    private Uri data;
    private String uri;
    ToggleButton toggleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_action_sigap);
        getSupportActionBar().setTitle(" Service Bluetooth");
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.r_layout);
        //vService = new MyPrintService();
        sw = (Switch) findViewById(R.id.switch1);
        pb = (ProgressBar) this.findViewById(R.id.printLoading);
        li = (TextView) this.findViewById(R.id.loadingInfo);
        tw = (TextView) this.findViewById(R.id.printInfo);
        txttoggle = (TextView) this.findViewById(R.id.txttoggle);
        btn = (Button) this.findViewById(R.id.transactionButton);
        valprint = (TextView) this.findViewById(R.id.valprint);

        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        btPrint = new BtPrint(sw, pb, li, tw, btn);

        data = this.getIntent().getData();

        if (data != null && data.isHierarchical()) {
            uri = this.getIntent().getDataString().substring(19).replace("/", "");

            //uri = this.getIntent().getDataString();
            valprint.setText(uri);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    // Toast.makeText(MainActivity.this, "Test", Toast.LENGTH_SHORT).show();
                    btn.performClick();
                }
            }, 3000);
        }

        btn.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public final void onClick(View it) {
                SettingActivity.access$getBtPrint$p(SettingActivity.this).socketConnect((Function1) (new Function1() {
                    // $FF: synthetic method
                    // $FF: bridge method
                    public Object invoke(Object var1) {
                        this.invoke((HashMap) var1);
                        return Unit.INSTANCE;
                    }

                    public final void invoke(@NotNull final HashMap result) {
                        Intrinsics.checkParameterIsNotNull(result, "result");
                        if (Intrinsics.areEqual(result.get("success"), false)) {
                            SettingActivity.this.runOnUiThread((Runnable) (new Runnable() {
                                public final void run() {
                                    TextView var10000 = (TextView) SettingActivity.this._$_findCachedViewById(R.id.printInfo);
                                    Intrinsics.checkExpressionValueIsNotNull(var10000, "printInfo");
                                    var10000.setText((CharSequence) String.valueOf(result.get("text")));
                                    Switch var1 = (Switch) SettingActivity.this._$_findCachedViewById(R.id.switch1);
                                    Intrinsics.checkExpressionValueIsNotNull(var1, "printSwitch");
                                    sw.setChecked(false);
                                    Toast.makeText((Context) SettingActivity.this, (CharSequence) "Please enable Service", Toast.LENGTH_SHORT).show();
                                }
                            }));
                        } else {
                          /* MainActivity.access$getBtPrint$p(MainActivity.this).doPrint(Build.MODEL + "\n", true);
                            MainActivity.access$getBtPrint$p(MainActivity.this).doPrint(Build.BRAND + "\n\n\n", true);*/

                            SettingActivity.access$getBtPrint$p(SettingActivity.this).doPrint(valprint.getText().toString() + "\n\n\n", true);
                        }
                    }
                }));

            }
        }));
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
    // $FF: synthetic method
    public static final BtPrint access$getBtPrint$p(SettingActivity $this) {
        BtPrint var10000 = $this.btPrint;
        if (var10000 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("btPrint");
        }

        return var10000;
    }

    // $FF: synthetic method
    public static final void access$setBtPrint$p(SettingActivity $this, BtPrint var1) {
        $this.btPrint = var1;
    }

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
}
