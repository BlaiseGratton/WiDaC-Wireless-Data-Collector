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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import widac.cis350.upenn.edu.widac.data.remote.RetrofitClient;
import widac.cis350.upenn.edu.widac.data.remote.WidacService;
import widac.cis350.upenn.edu.widac.models.Sample;
import widac.cis350.upenn.edu.widac.models.Samples;

public class SearchActivity extends AppCompatActivity {

    public final static String TAG = "SearchActivity";

//    private final static String AREA_EASTING_HINT = "Area Easting";
//    private final static String AREA_NORTHING_HINT = "Area Northing";
//    private final static String CONTEXT_NUMBER_HINT = "Context Number";
//    private final static String SAMPLE_NUMBER_HINT = "Sample Number";

    private TextView weight;
    private Button next, prev, bluetooth, manual;
    private DBSpinner area_easting, area_northing, context_number, sample_number;
    private ProgressBar progressBar;

    private List<String> areaEastingData, areaNorthingData, contextNumberData, sampleNumberData;
    private String areaEastingSelection, areaNorthingSelection, contextNumberSelection, sampleNumberSelection;
    ArrayAdapter<String> areaEastingAdapter, areaNorthingAdapter, contextNumberAdapter, sampleNumberAdapter;
    WidacService service;

    private Sample sample;
    DBConnection db;

    public BluetoothService bluetoothService;
    public BluetoothDevice device = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        areaEastingData = new ArrayList<>();
        areaNorthingData = new ArrayList<>();
        contextNumberData = new ArrayList<>();
        sampleNumberData = new ArrayList<>();

        service = RetrofitClient.getClient().create(WidacService.class);

        Intent i = this.getIntent();
        areaEastingSelection = i.getStringExtra("Area Easting");
        areaNorthingSelection = i.getStringExtra("Area Northing");
        contextNumberSelection = i.getStringExtra("Context Number");
        sampleNumberSelection = i.getStringExtra("Sample Number");

        Toast toast = Toast.makeText(getApplicationContext(), "Selected composite key: " +
                areaEastingSelection + "-" + areaNorthingSelection + "-"
                + contextNumberSelection + "-" + sampleNumberSelection, Toast.LENGTH_SHORT);
        toast.show();

        // check if we've been given a totally new composite key
        Call<Sample> call = service.getSample(areaEastingSelection + "-"
                + areaNorthingSelection + "-" + contextNumberSelection + "-" + sampleNumberSelection);
        call.enqueue(new Callback<Sample>() {

            @Override
            public void onResponse(Call<Sample> call, Response<Sample> response) {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                Sample sample = response.body();
                if (sample == null) {
                    createSample();
                }
            }

            @Override
            public void onFailure(Call<Sample> call, Throwable t) {
                System.out.println("Failure");
            }
        });


        initializeProgressBar();
        initializeButtons();
        initializeSpinners();

        db = new DBConnection();

        Session.asyncPullNewEntry(Session.searchQuery, sampleCallback);
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

        Toast.makeText(this, "Connected to: " + Session.deviceName, Toast.LENGTH_SHORT).show();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(mReceiver, filter);
    }

    private void createSample() {

    }

    private void initializeButtons() {
        next = (Button) findViewById(R.id.next_item);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextButtonClick();
            }
        });

        prev = (Button) findViewById(R.id.prev_item);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPrevButtonClick();
            }
        });

        bluetooth = (Button) findViewById(R.id.update_bluetooth);
        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpdateBluetoothButtonClick();
            }
        });

        manual = (Button) findViewById(R.id.update_manual);
        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpdateManualButtonClick();
            }
        });
    }

    private void initializeProgressBar() {
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setIndeterminate(true);
    }

    private void initializeSpinners() {
        sample_number = (DBSpinner) findViewById(R.id.sample_number);
        sampleNumberAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, sampleNumberData);
        sample_number.setAdapter(sampleNumberAdapter);
        sample_number.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        context_number = (DBSpinner) findViewById(R.id.context_number);
        contextNumberAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, contextNumberData);
        context_number.setAdapter(contextNumberAdapter);
        context_number.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                contextNumberSelection = (String) adapterView.getItemAtPosition(i);
                // empty all lists for subsequent dropdowns
                sampleNumberData.clear();
                sampleNumberAdapter.notifyDataSetChanged();
                Log.d(TAG, "Selected context number: " + contextNumberSelection);

                refreshSampleNumberData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        area_northing = (DBSpinner) findViewById(R.id.area_northing);
        areaNorthingAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, areaNorthingData);
        area_northing.setAdapter(areaNorthingAdapter);
        area_northing.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                areaNorthingSelection = (String) adapterView.getItemAtPosition(i);
                // empty all lists for subsequent dropdowns
                contextNumberData.clear();
                sampleNumberData.clear();
                contextNumberAdapter.notifyDataSetChanged();
                sampleNumberAdapter.notifyDataSetChanged();

                Log.d(TAG, "Selected area_northing: " + areaNorthingSelection);
                refreshContextNumberData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // initialize the area_easting spinner and load initial data
        area_easting = (DBSpinner) findViewById(R.id.area_easting);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        areaEastingAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, areaEastingData);
        area_easting.setAdapter(areaEastingAdapter);
        Call<Samples> call = service.getAllSamples();
        call.enqueue(new Callback<Samples>() {

            @Override
            public void onResponse(Call<Samples> call, Response<Samples> response) {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                Samples samples = response.body();
                List<String> compositeKeys = samples.getCompositeKeys();
                areaEastingData.clear();
                Set<String> uniqueAreaEastings = new HashSet<>();
                for (String compositeKey: compositeKeys) {
                    String[] splitComposite = compositeKey.split("-");
                    if (!uniqueAreaEastings.contains(splitComposite[0])) {
                        areaEastingData.add(splitComposite[0]);
                    }
                    uniqueAreaEastings.add(splitComposite[0]);
                }
                areaEastingData.add(0, areaEastingSelection);
                areaEastingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Samples> call, Throwable t) {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                System.out.println("Failure");
            }
        });

        // on selecting an area_easting, clear subsequent spinners and load data for area_northing
        area_easting.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                areaEastingSelection = (String) adapterView.getItemAtPosition(i);
                // empty all lists for subsequent dropdowns
                areaNorthingData.clear();
                contextNumberData.clear();
                sampleNumberData.clear();
                areaNorthingAdapter.notifyDataSetChanged();
                contextNumberAdapter.notifyDataSetChanged();
                sampleNumberAdapter.notifyDataSetChanged();

                Log.d(TAG, "Selected area_easting: " + areaEastingSelection);
                refreshAreaNorthingData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                System.out.println("Nothing Selected");
            }
        });
    }

    private void refreshAreaNorthingData() {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        Call<Samples> call = service.getAllSamples(Integer.parseInt(areaEastingSelection), null, null, null);
        call.enqueue(new Callback<Samples>() {

            @Override
            public void onResponse(Call<Samples> call, Response<Samples> response) {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                Samples samples = response.body();
                List<String> compositeKeys = samples.getCompositeKeys();
                areaNorthingData.clear();
                Set<String> uniqueValues = new HashSet<>();
                for (String compositeKey: compositeKeys) {
                    String[] splitComposite = compositeKey.split("-");
                    if (splitComposite[0].equalsIgnoreCase(areaEastingSelection) &&
                            !uniqueValues.contains(splitComposite[1])) {
                        areaNorthingData.add(splitComposite[1]);
                    }
                    uniqueValues.add(splitComposite[1]);
                }
                areaNorthingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Samples> call, Throwable t) {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                System.out.println("Failure");
            }
        });
    }

    private void refreshContextNumberData() {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        Call<Samples> call = service.getAllSamples(Integer.parseInt(areaEastingSelection),
                Integer.parseInt(areaNorthingSelection), null, null);
        call.enqueue(new Callback<Samples>() {

            @Override
            public void onResponse(Call<Samples> call, Response<Samples> response) {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                Samples samples = response.body();
                List<String> compositeKeys = samples.getCompositeKeys();
                contextNumberData.clear();
                Set<String> uniqueValues = new HashSet<>();
                for (String compositeKey: compositeKeys) {
                    String[] splitComposite = compositeKey.split("-");
                    if (splitComposite[0].equalsIgnoreCase(areaEastingSelection) &&
                            splitComposite[1].equalsIgnoreCase(areaNorthingSelection) &&
                            !uniqueValues.contains(splitComposite[2])) {
                        contextNumberData.add(splitComposite[2]);
                    }
                    uniqueValues.add(splitComposite[2]);
                }
                contextNumberAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Samples> call, Throwable t) {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                System.out.println("Failure");
            }
        });
    }

    private void refreshSampleNumberData() {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        Call<Samples> call = service.getAllSamples(Integer.parseInt(areaEastingSelection),
                Integer.parseInt(areaNorthingSelection), Integer.parseInt(contextNumberSelection),
                null);
        call.enqueue(new Callback<Samples>() {

            @Override
            public void onResponse(Call<Samples> call, Response<Samples> response) {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                Samples samples = response.body();
                List<String> compositeKeys = samples.getCompositeKeys();
                sampleNumberData.clear();
                Set<String> uniqueValues = new HashSet<>();
                for (String compositeKey: compositeKeys) {
                    String[] splitComposite = compositeKey.split("-");
                    if (splitComposite[0].equalsIgnoreCase(areaEastingSelection) &&
                            splitComposite[1].equalsIgnoreCase(areaNorthingSelection) &&
                            splitComposite[2].equalsIgnoreCase(contextNumberSelection) &&
                            !uniqueValues.contains(splitComposite[3])) {
                        sampleNumberData.add(splitComposite[3]);
                    }
                    uniqueValues.add(splitComposite[3]);
                }
                sampleNumberAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Samples> call, Throwable t) {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                System.out.println("Failure");
            }
        });
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
    
    public void onUpdateBluetoothButtonClick() {
        ((TextView)findViewById(R.id.itemWeight)).setText("Weight: 234g");
        runBluetooth();
        Toast.makeText(this, "Weight updated", Toast.LENGTH_SHORT).show();
    }

    public void onUpdateManualButtonClick() {
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

    public void onPrevButtonClick() {
        //String prevName = itemName.getText().toString();
        TextView weightText = (TextView) findViewById(R.id.itemWeight);
        weightText.setText("Weight: 121g");
    }

    public void onNextButtonClick() {
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
