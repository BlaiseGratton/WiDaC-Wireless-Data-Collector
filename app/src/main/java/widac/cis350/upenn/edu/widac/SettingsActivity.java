package widac.cis350.upenn.edu.widac;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SettingsActivity extends AppCompatActivity {
    int REQUEST_PAIR_DEVICE = 1;
    Map<String, String> devices = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Populate the currenlty paired devices list
        //Add currently paired devices to list
        getPairedDevices();
        ListView list = (ListView)findViewById(R.id.paired_devices_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(list.getContext(),
                android.R.layout.simple_list_item_1, (String[]) devices.keySet().toArray());

    }

    private void getPairedDevices() {
        Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                devices.put(deviceName, deviceHardwareAddress);
            }
        }
    }

    // Opens phone settings to pair devices
    public void onPairDeviceClick() {
        Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivityForResult(intent, REQUEST_PAIR_DEVICE);
    }

    // Select which device to connect to an attempt opening a connection
    public void onConnectDeviceButtonClick() {

    }


}
