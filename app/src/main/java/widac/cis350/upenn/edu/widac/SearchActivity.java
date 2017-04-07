package widac.cis350.upenn.edu.widac;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
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
    BluetoothService bluetoothService;
    BluetoothDevice device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        db = new DBConnection();
        Intent i = this.getIntent();


        sample = db.retrieveSample(i.getStringExtra("compositeKey"));
        TextView itemName = (TextView) findViewById(R.id.item_name);
        sample  = db.retrieveSample(i.getStringExtra("compositeKey"));
        itemName.setText(sample.getCompositeKey());
        TextView itemWeight = (TextView) findViewById(R.id.itemWeight);
        String displayWeight = (sample.getWeight() == 0) ? "No Data" : "" + sample.getWeight();
        itemWeight.setText(displayWeight);

        db.getSample(i.getStringExtra("id"), sampleCallback);

        itemNumber = 123;

        if (bluetoothService != null) {
            bluetoothService.closeThread();
        }
        bluetoothService = null;
        device = null;

        String scaleAddress = "0F:03:14:0A:03:9B";
        String scaleName = "nutriscale_1910";

        Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                if (deviceHardwareAddress.equals(scaleAddress)) {
                    this.device = device;
                    bluetoothService = new BluetoothService(this);
                }
            }
        }
    }
    
    public void onUpdateBluetoothButtonClick(View v) {
        ((TextView)findViewById(R.id.itemWeight)).setText("Weight: 234g");
        runBluetooth();
        Toast.makeText(this, "Weight updated", Toast.LENGTH_LONG).show();
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
        TextView itemName = (TextView) findViewById(R.id.item_name);
        //String prevName = itemName.getText().toString();
        itemName.setText("Item 122");
        TextView weightText = (TextView) findViewById(R.id.itemWeight);
        weightText.setText("Weight: 121g");
    }

    public void onNextButtonClick(View v) {
        TextView itemName = (TextView) findViewById(R.id.item_name);
        //String prevName = itemName.getText().toString();
        itemName.setText("Item 124");
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
                TextView itemName = (TextView) findViewById(R.id.item_name);
                itemName.setText(sample.getCompositeKey());
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
