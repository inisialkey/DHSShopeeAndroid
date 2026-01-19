package com.fungsitama.dhsshopee.activity.printer;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.fungsitama.dhsshopee.R;

import java.util.Set;

public class DeviceListActivity extends Activity {
    protected static final String TAG = "TAG";
    /* access modifiers changed from: private */
    public BluetoothAdapter mBluetoothAdapter;
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View mView, int mPosition, long mLong) {
            try {
                DeviceListActivity.this.mBluetoothAdapter.cancelDiscovery();
                String mDeviceInfo = ((TextView) mView).getText().toString();
                String mDeviceAddress = mDeviceInfo.substring(mDeviceInfo.length() - 17);
                Log.v(DeviceListActivity.TAG, "Device_Address " + mDeviceAddress);
                Bundle mBundle = new Bundle();
                mBundle.putString("DeviceAddress", mDeviceAddress);
                Intent mBackIntent = new Intent();
                mBackIntent.putExtras(mBundle);
                DeviceListActivity.this.setResult(-1, mBackIntent);
                DeviceListActivity.this.finish();
            } catch (Exception e) {
            }
        }
    };
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle mSavedInstanceState) {
        super.onCreate(mSavedInstanceState);
        requestWindowFeature(5);
        setContentView(R.layout.device_list);
        setResult(0);
        this.mPairedDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.device_name);
        ListView mPairedListView = (ListView) findViewById(R.id.paired_devices);
        mPairedListView.setAdapter(this.mPairedDevicesArrayAdapter);
        mPairedListView.setOnItemClickListener(this.mDeviceClickListener);
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> mPairedDevices = this.mBluetoothAdapter.getBondedDevices();
        if (mPairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.GONE);
            for (BluetoothDevice mDevice : mPairedDevices) {
                this.mPairedDevicesArrayAdapter.add(mDevice.getName() + "\n" + mDevice.getAddress());
            }
            return;
        }
        this.mPairedDevicesArrayAdapter.add("None Paired");
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (this.mBluetoothAdapter != null) {
            this.mBluetoothAdapter.cancelDiscovery();
        }
    }
}
