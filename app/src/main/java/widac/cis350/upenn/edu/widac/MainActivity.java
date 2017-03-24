package widac.cis350.upenn.edu.widac;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    
    public final static String COMPOSITE_KEY = "compositeKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Session.newSession();

        /*
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }*/
        MyBluetoothService bluetoothService = new MyBluetoothService();
    }
    
    public void onSearchButtonClick(View v) {
        Intent i = new Intent(this, SearchActivity.class);
//        String queryKey = ((EditText)findViewById(R.id.searchBox)).getText().toString();
//        i.putExtra(COMPOSITE_KEY, queryKey);
        EditText editText = (EditText) findViewById(R.id.searchBox);
        String queryKey = editText.getText().toString();
        //i.putExtra(COMPOSITE_KEY, queryKey);
        i.putExtra("id", queryKey);
        startActivityForResult(i, 1);
    }
    
    public void onSettingsButtonClick(View v) {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivityForResult(i, 1);
    }
    
    public void onVisualizationButtonClick(View v) {
        Intent i = new Intent(this, VisualizationActivity.class);
        startActivityForResult(i, 1);
    }
    
    public void onSessionReportButtonClick(View v) {
        Intent i = new Intent(this, SessionReportActivity.class);
        startActivityForResult(i, 1);
    }
}
