package com.fungsitama.dhsshopee.util;

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
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            try {
                DeviceListActivity.this.mBluetoothAdapter.cancelDiscovery();
                String charSequence = ((TextView) view).getText().toString();
                String substring = charSequence.substring(charSequence.length() - 17);
                String str = DeviceListActivity.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Device_Address ");
                sb.append(substring);
                Log.v(str, sb.toString());
                Bundle bundle = new Bundle();
                bundle.putString("DeviceAddress", substring);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                DeviceListActivity.this.setResult(-1, intent);
                DeviceListActivity.this.finish();
            } catch (Exception unused) {
            }
        }
    };
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(5);
        setContentView(R.layout.device_list);
        setResult(0);
        this.mPairedDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.device_name);
        ListView listView = (ListView) findViewById(R.id.paired_devices);
        listView.setAdapter(this.mPairedDevicesArrayAdapter);
        listView.setOnItemClickListener(this.mDeviceClickListener);
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> bondedDevices = this.mBluetoothAdapter.getBondedDevices();
        if (bondedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice bluetoothDevice : bondedDevices) {
                ArrayAdapter<String> arrayAdapter = this.mPairedDevicesArrayAdapter;
                StringBuilder sb = new StringBuilder();
                sb.append(bluetoothDevice.getName());
                sb.append("\n");
                sb.append(bluetoothDevice.getAddress());
                arrayAdapter.add(sb.toString());
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
