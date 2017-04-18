package widac.cis350.upenn.edu.widac;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {
    int REQUEST_PAIR_DEVICE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
