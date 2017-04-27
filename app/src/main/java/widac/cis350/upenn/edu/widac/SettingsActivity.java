package widac.cis350.upenn.edu.widac;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Set;

import widac.cis350.upenn.edu.widac.data.remote.WidacService;

import static android.R.id.list;
import static java.security.AccessController.getContext;
import static java.sql.Types.NULL;
import static widac.cis350.upenn.edu.widac.R.id.connectedDB;

public class SettingsActivity extends AppCompatActivity {
    int REQUEST_PAIR_DEVICE = 1;
    //Map<String, String> devices = new HashMap<>();
    String[] devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Populate the currenlty paired devices list
        //Add currently paired devices to list
        getPairedDevices();

        if (BluetoothAdapter.getDefaultAdapter() != null) {
            ListView list = (ListView)findViewById(R.id.paired_devices_list);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(list.getContext(),
                    android.R.layout.simple_list_item_1, devices);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
                    if (pairedDevices.size() > 0) {
                        // There are paired devices. Get the name and address of each paired device.
                        for (BluetoothDevice device : pairedDevices) {
                            String deviceName = device.getName();
                            if (deviceName.equals(devices[position])) {
                                //Session.device = device;
                                Session.deviceName = deviceName;
                            }

                            TextView connectedDevice = (TextView) findViewById(R.id.connected_device);
                            if (Session.deviceName != null) {
                                connectedDevice.setText("Device: " + Session.deviceName);
                            }
                        }
                    }
                }
            });
        }

        TextView connectedDB = (TextView) findViewById(R.id.connectedDB);
        connectedDB.setText("Database: " + WidacService.ENDPOINT);

        TextView connectedDevice = (TextView) findViewById(R.id.connected_device);
        if (Session.deviceName != null) {
            connectedDevice.setText("Device: " + Session.deviceName);
        }
    }

    private void getPairedDevices() {

        if (BluetoothAdapter.getDefaultAdapter() == null) {
            devices = new String[0];
            Toast.makeText(this, "Bluetooth is not enabled on this device.", Toast.LENGTH_SHORT).show();
        } else {
            Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
            if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                devices = new String[pairedDevices.size()];
                int index = 0;
                for (BluetoothDevice device : pairedDevices) {
                    String deviceName = device.getName();
                    devices[index] = deviceName;
                    index++;
                }
            }
        }

    }

    // Opens phone settings to pair devices
    public void onPairDeviceClick(View v) {
        Intent intentOpenBluetoothSettings = new Intent();
        intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivity(intentOpenBluetoothSettings);
    }

    public void onSettingsSearchButtonClick(View v) {
        if (Session.deviceName == null) {
            Toast.makeText(this, "Please connect a device", Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent(this, SearchActivity.class);
            startActivityForResult(i, 1);
        }
    }

}
