package widac.cis350.upenn.edu.widac;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import widac.cis350.upenn.edu.widac.models.Sample;

public class SearchActivity extends AppCompatActivity {
    
    private Sample sample;
    DBConnection db;
    private int itemNumber;

    public BluetoothService bluetoothService;
    public BluetoothDevice device = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        db = new DBConnection();
        Intent i = this.getIntent();
        Session.asyncPullNewEntry(Session.searchQuery, sampleCallback);
        itemNumber = 123;

        if (bluetoothService != null) {
            bluetoothService.closeThread();
        }
        bluetoothService = null;
        device = null;

        Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice pairedDv : pairedDevices) {
                String deviceName = pairedDv.getName();
                if (deviceName.equals(Session.deviceName)) {
                    device = pairedDv;
                    bluetoothService = new BluetoothService(this);
                    bluetoothService.reconnect(device);
                }
            }
        }

        sample = db.retrieveSample(i.getStringExtra("compositeKey"));
        sample  = db.retrieveSample(i.getStringExtra("compositeKey"));
        TextView itemWeight = (TextView) findViewById(R.id.itemWeight);
        String displayWeight = (sample.getWeight() == 0) ? "No Data" : "" + sample.getWeight();
        itemWeight.setText(displayWeight);

        Toast.makeText(this, "Connected to: " + Session.deviceName, Toast.LENGTH_SHORT).show();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(mReceiver, filter);
    }

    //The BroadcastReceiver that listens for bluetooth broadcasts
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //... //Device found
            }
            else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show(); //Device is now connected
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //... //Done searching
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                Toast.makeText(context, "Disconnect requested", Toast.LENGTH_SHORT).show(); //Device is about to disconnect
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                bluetoothService.reconnect(device);
                Toast.makeText(context, "Disconnected from scale: please restart the scale", Toast.LENGTH_SHORT).show(); //Device has disconnected
            }
        }
    };
    
    public void onUpdateBluetoothButtonClick(View v) {
        ((TextView)findViewById(R.id.itemWeight)).setText("Weight: 234g");
        runBluetooth();
        //Toast.makeText(this, "Weight updated", Toast.LENGTH_SHORT).show();
    }

    public void onReconnectClick(View v) {
        Toast.makeText(this, "Reconnecting", Toast.LENGTH_SHORT).show();
        bluetoothService.reconnect(device);
    }
    
    public void onUpdateManualButtonClick(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Manual Update");
        alert.setMessage("Input weight in grams:");

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String newWeight = input.getText().toString();
                try {
                    Double.parseDouble(newWeight);
                    ((TextView)findViewById(R.id.itemWeight)).setText("Weight: " + newWeight + "g");
                } catch (Exception e) {
                    Toast.makeText(SearchActivity.this,
                            "Input must be a number.", Toast.LENGTH_LONG).show();
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    public void onPrevButtonClick(View v) {
        //String prevName = itemName.getText().toString();
        TextView weightText = (TextView) findViewById(R.id.itemWeight);
        weightText.setText("Weight: 121g");
    }

    public void onNextButtonClick(View v) {
        TextView weightText = (TextView) findViewById(R.id.itemWeight);
        weightText.setText("Weight: 235g");
    }

    public void runBluetooth() {
        bluetoothService.runService(device);
        TextView weightText = (TextView) findViewById(R.id.itemWeight);
        weightText.setText("Weight: " + BluetoothService.currWeight + "g");
    }

    Callback sampleCallback = new Callback<Sample>(){

        @Override
        public void onResponse(Call<Sample> call, Response<Sample> response) {
            int code = response.code();
            if (code == 200) {
                SearchActivity.this.sample = response.body();
                TextView itemWeight = (TextView) findViewById(R.id.itemWeight);
                String displayWeight = (sample.getWeight() == 0) ? "No Data" : "" + sample.getWeight();
                itemWeight.setText("Weight: " + displayWeight + "g");
                Log.d("DBConnection", "Got the sample: " + sample.toString() + " Material: " + sample.getMaterial());
            } else {
                Log.d("DBConnection", "Did not work: " + String.valueOf(code));
            }
        }

        @Override
        public void onFailure(Call<Sample> call, Throwable t) {
            Log.d("DBConnection", "Get sample failure");
        }
    };

}
