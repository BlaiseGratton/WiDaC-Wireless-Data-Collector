package widac.cis350.upenn.edu.widac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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

public class VisualizationActivity extends AppCompatActivity {
    PieChart pieChart;
    Map<String, Integer> chartData = new HashMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Most likely want to get session data from intent
        setContentView(R.layout.activity_visualization);

        /**
         * TESTING METHOD
         **/
        Session.initalizeTest();
        /**
         * Keep
         **/
        pieChart = (PieChart) findViewById(R.id.typesCollectedPieChart);
        createChart();
    }

    // Build a chart showing the various pieces collected during the current session
    private void createChart() {
        Log.d("VisualizationActivity", "createChart");
        // Get the session information and use it pull from DB
        Set<String> ids = Session.getCurrentSessionIDs();
        Set<DummyEntry> artifacts = pullFromDatabase(ids);
        parseArtifacts(artifacts);

        List<PieEntry> entries = new ArrayList<>();

        // Convert data from session into data workable with PieChart
        // Add checking for empty session?
        Log.d("Visualization", "createChart: Populating with " + chartData.keySet().size() );
        for (String k: chartData.keySet()) {
            Log.d("Visualization", "createChart: Adding entry=" + k + ", " + chartData.get(k));
            entries.add(new PieEntry(chartData.get(k), k));
        }

        // Add data to the PieChart
        PieDataSet set = new PieDataSet(entries, "Session Results");
        set.setSliceSpace(2);
        set.setColors(ColorTemplate.VORDIPLOM_COLORS);
        PieData data = new PieData(set);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    private void parseArtifacts(Set<DummyEntry> artifacts) {
        Log.d("VisualizationActivity", "parseArtifacts: " + artifacts.size());

        Iterator<DummyEntry> i = artifacts.iterator();
        while (i.hasNext()) {
            DummyEntry d = i.next();
            if (chartData.containsKey(d.type)) {
                chartData.put(d.type, chartData.get(d.type) + 1);
            } else {
                chartData.put(d.type, 1);
            }
        }
    }

//    private class ChartData {
//        String type;
//        int frequency;
//    }

    // Assume this function will be abstracted into an api in the future
    private Set<DummyEntry> pullFromDatabase(Set<String> ids) {
        // Dummy Data for testing
        String[] types = {"A", "B", "C"};
        Set<DummyEntry> entries = new HashSet<DummyEntry>();

        for (String id: ids) {
            String type = types[(int)(Math.random() * 3)];
            DummyEntry d = new DummyEntry(id, type, "DUMMY", -1, -1);
            Log.d("Visualization", "pullFromDatabase: entry created of type: " + type);
            entries.add(d);
        }
        return entries;
    }
}
