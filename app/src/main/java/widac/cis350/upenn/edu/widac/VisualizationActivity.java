package widac.cis350.upenn.edu.widac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import widac.cis350.upenn.edu.widac.models.Sample;
import widac.cis350.upenn.edu.widac.models.SampleStaging;

public class VisualizationActivity extends AppCompatActivity {
    PieChart pieChart;
    Map<String, Integer> chartData = new HashMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout
        setContentView(R.layout.activity_visualization);

        // Create the pie chart to be populated
        pieChart = (PieChart) findViewById(R.id.typesCollectedPieChart);
        // Make a request to the database for the data
        Session.asyncPullFromDB(collectEntries);
    }

    // Gets entry data from database
    // Samples are stored in a "staging area", when this is called are ready to be retrieved
    Callback collectEntries = new Callback<Sample>(){
        @Override
        public void onResponse(Call<Sample> call, Response<Sample> response) {
            int code = response.code();
            if (code == 200) {
                // Staging area now has all the samples, retrieve them
                parseArtifacts(SampleStaging.retrieveSamples());
                createChart();
            } else {
                // Could not create visualization
                Toast.makeText(VisualizationActivity.this, "Unable to pull full database", Toast.LENGTH_SHORT).show();
                Log.d("VisualizationActivity", "Did not work: " + String.valueOf(code));
            }
        }

        @Override
        public void onFailure(Call<Sample> call, Throwable t) {
            Log.d("VisualizationActivity", "Create report failure");
        }
    };

    // Build a chart showing the various pieces collected during the current session
    private void createChart() {
        Log.d("VisualizationActivity", "createChart");
        List<PieEntry> entries = new ArrayList<>();

        // Convert data from session into data workable with PieChart
        Log.d("Visualization", "createChart: Populating with " + chartData.keySet().size() );

        // Populate the pie chart with recovered entries
        for (String k: chartData.keySet()) {
            Log.d("Visualization", "createChart: Adding entry=" + k + ", " + chartData.get(k));
            entries.add(new PieEntry(chartData.get(k), k));
        }

        // Add data to the PieChart
        PieDataSet set = new PieDataSet(entries, "Session Results");
        set.setSliceSpace(2);

        // Define default colors for materials
        set.setColors(new int[] {getResources().getColor(android.R.color.holo_blue_dark),
                                 getResources().getColor(android.R.color.holo_green_dark),
                                 getResources().getColor(android.R.color.holo_orange_dark),
                                 getResources().getColor(android.R.color.holo_red_dark),
                                 getResources().getColor(android.R.color.holo_purple)});
        PieData data = new PieData(set);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    private void parseArtifacts(Set<Sample> artifacts) {
        Log.d("VisualizationActivity", "parseArtifacts: " + artifacts.size());

        // Store the recovered data from the database
        Iterator<Sample> i = artifacts.iterator();
        while (i.hasNext()) {
            Sample d = i.next();
            if (chartData.containsKey(d.getMaterial())) {
                chartData.put(d.getMaterial(), chartData.get(d.getMaterial()) + 1);
            } else {
                chartData.put(d.getMaterial(), 1);
            }
        }
    }
}
