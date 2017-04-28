package widac.cis350.upenn.edu.widac;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import widac.cis350.upenn.edu.widac.data.remote.RetrofitClient;
import widac.cis350.upenn.edu.widac.data.remote.WidacService;
import widac.cis350.upenn.edu.widac.models.Sample;
import widac.cis350.upenn.edu.widac.models.Samples;

public class MainActivity extends AppCompatActivity {
    
    public final static String COMPOSITE_KEY = "compositeKey";

    Spinner area_easting, area_northing, context_number, sample_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeSpinners();
        Session.newSession();
    }

    private void initializeSpinners() {
//        WidacService service = RetrofitClient.getClient().create(WidacService.class);
//        Call<Samples> call = service.getAllSamples();
//        ArrayList<String> sampleContextNumbers;
//        call.enqueue(new Callback<Samples>() {
//
//            @Override
//            public void onResponse(Call<Samples> call, Response<Samples> response) {
//                Samples samples = response.body();
////                sampleContextNumbers = (ArrayList<String>) samples.getContextNumbers();
//            }
//
//            @Override
//            public void onFailure(Call<Samples> call, Throwable t) {
//                System.out.println("Failure");
//            }
//        });

//        area_easting = (Spinner)findViewById(R.id.area_easting_main);
//        String[] items1 = new String[]{"1", "2", "3", "5"};
//        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items1);
//        area_easting.setAdapter(adapter1);
//        area_easting.setOnItemSelectedListener(new AdapterViewCompat.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                // your code here
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });

        area_northing = (Spinner)findViewById(R.id.area_northing_main);
        String[] items2 = new String[]{"77", "16", "13", "45"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        area_northing.setAdapter(adapter2);

        context_number = (Spinner)findViewById(R.id.context_number_main);
        String[] items3 = new String[]{"100", "87"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items3);
        context_number.setAdapter(adapter3);

        sample_number = (Spinner)findViewById(R.id.sample_number_main);
        String[] items4 = new String[]{"1"};
        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items4);
        sample_number.setAdapter(adapter4);

    }

    public void onSearchButtonClick(View v) {
        Intent i = new Intent(this, SearchActivity.class);
//        String queryKey = ((EditText)findViewById(R.id.searchBox)).getText().toString();
//        i.putExtra(COMPOSITE_KEY, queryKey);
//        EditText editText = (EditText) findViewById(R.id.searchBox);
//        String queryKey = editText.getText().toString();
        //i.putExtra(COMPOSITE_KEY, queryKey);
//        i.putExtra("id", queryKey);
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
