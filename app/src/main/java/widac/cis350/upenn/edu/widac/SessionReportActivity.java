package widac.cis350.upenn.edu.widac;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by J. Patrick Taggart on 2/17/2017.
 * ===================================================
 * Expected data
 *  -Total number of items collected
 *  -A list of items collected
 *      -List should either contain a id to be used to query database or all information needed
 */

public class SessionReportActivity extends AppCompatActivity {
    private Set<String> currentSession; // List of ids
    private Map<String, TypeData> types = new HashMap<String, TypeData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_report);

        currentSession = Session.getCurrentSessionIDs();
        createSessionReport();
    }

    private void createSessionReport() {
        // Goind to need to get data from current session somehow
        parseEntries(fakeDBcall());
        generateStatistics();
    }

    private void parseEntries(Set<DummyEntry> entries) {
        types.put("A", new TypeData("A"));
        types.put("B", new TypeData("B"));
        types.put("C", new TypeData("C"));
        types.put("D", new TypeData("D"));
        types.put("E", new TypeData("E"));

        for (DummyEntry e: entries) {
            Log.d("SessionReport", "Creating entry of type: " + e.type);
            types.get(e.type).addInstance(e);
        }
    }

    // Provide overview of data collected in recent session
    private void generateStatistics() {
        int itemsCollected = 10;

        // Better to store all text views as local variables, just have one textview variable that changes, or one for each?
        // Instead make sections with drop downs that show the data?
        TextView tv = (TextView)findViewById(R.id.TotalNum);
        tv.setText("Total:" + itemsCollected);

        setNumCollected();
        setAvgSize();
        setAvgWt();
    }

    private void setNumCollected() {
        Log.d("SessionReport", "Attempt NumA");
        TextView tv = (TextView)findViewById(R.id.NumA);
        Log.d("SessionReport", "Got NumA");
        Log.d("SessionReport", "Test=" + types.get("A"));
        tv.setText("" + types.get("A").num);
        tv = (TextView)findViewById(R.id.NumB);
        tv.setText("" + types.get("B").num);
        tv = (TextView)findViewById(R.id.NumC);
        tv.setText("" + types.get("C").num);
        tv = (TextView)findViewById(R.id.NumD);
        tv.setText("" + types.get("D").num);
        tv = (TextView)findViewById(R.id.NumE);
        tv.setText("" + types.get("E").num);
    }

    private void setAvgSize() {
        TextView tv = (TextView)findViewById(R.id.AvgSizeA);
        tv.setText(String.format( "%.2f", types.get("A").avgSize ));
        tv = (TextView)findViewById(R.id.AvgSizeB);
        tv.setText(String.format( "%.2f", types.get("B").avgSize ));
        tv = (TextView)findViewById(R.id.AvgSizeC);
        tv.setText(String.format( "%.2f", types.get("C").avgSize ));
        tv = (TextView)findViewById(R.id.AvgSizeD);
        tv.setText(String.format( "%.2f", types.get("D").avgSize ));
        tv = (TextView)findViewById(R.id.AvgSizeE);
        tv.setText(String.format( "%.2f", types.get("E").avgSize ));

        tv = (TextView)findViewById(R.id.DevAvgSizeA);
        tv.setText(String.format( "%.2f", types.get("A").stdDevSize() ));
        tv = (TextView)findViewById(R.id.DevAvgSizeB);
        tv.setText(String.format( "%.2f", types.get("B").stdDevSize()));
        tv = (TextView)findViewById(R.id.DevAvgSizeC);
        tv.setText(String.format( "%.2f", types.get("C").stdDevSize() ));
        tv = (TextView)findViewById(R.id.DevAvgSizeD);
        tv.setText(String.format( "%.2f", types.get("D").stdDevSize() ));
        tv = (TextView)findViewById(R.id.DevAvgSizeE);
        tv.setText(String.format( "%.2f", types.get("E").stdDevSize() ));
    }

    private void setAvgWt() {
        TextView tv = (TextView)findViewById(R.id.AvgWtA);
        tv.setText(String.format( "%.2f", types.get("A").avgWt ));
        tv = (TextView)findViewById(R.id.AvgWtB);
        tv.setText(String.format( "%.2f", types.get("B").avgWt ));
        tv = (TextView)findViewById(R.id.AvgWtC);
        tv.setText(String.format( "%.2f", types.get("C").avgWt ));
        tv = (TextView)findViewById(R.id.AvgWtD);
        tv.setText(String.format( "%.2f", types.get("D").avgWt ));
        tv = (TextView)findViewById(R.id.AvgWtE);
        tv.setText(String.format( "%.2f", types.get("E").avgWt ));

        tv = (TextView)findViewById(R.id.DevAvgWtA);
        tv.setText(String.format( "%.2f", types.get("A").stdDevWeight() ));
        tv = (TextView)findViewById(R.id.DevAvgWtB);
        tv.setText(String.format( "%.2f", types.get("B").stdDevWeight() ));
        tv = (TextView)findViewById(R.id.DevAvgWtC);
        tv.setText(String.format( "%.2f", types.get("C").stdDevWeight() ));
        tv = (TextView)findViewById(R.id.DevAvgWtD);
        tv.setText(String.format( "%.2f", types.get("D").stdDevWeight() ));
        tv = (TextView)findViewById(R.id.DevAvgWtE);
        tv.setText(String.format( "%.2f", types.get("E").stdDevWeight() ));
    }

    private Set<DummyEntry> fakeDBcall() {
        String[] types = {"A", "B", "C", "D", "E"};
        Set<DummyEntry> entries = new HashSet<DummyEntry>();

        for (int i = 0; i < 10; i++) {
            String type = types[(int)(Math.random() * types.length)];
            DummyEntry d = new DummyEntry(("" + i), type, "DUMMY", Math.random() * 2, Math.random() * 4);
            entries.add(d);
        }
        return entries;
    }

    private class TypeData {
        String type;
        int num = 0;
        double avgSize = 0;
        double avgWt = 0;

        double devWt;
        double devSize;
        boolean changed = false;
        Set<DummyEntry> instances;

        TypeData() {
            instances = new HashSet<DummyEntry>();
        }

        TypeData(String type) {
            this.type = type;
            instances = new HashSet<DummyEntry>();
        }
        TypeData(String type, Set<DummyEntry> instances) {
            this.type = type;
            this.instances = instances;

            double wt = 0, sz = 0;
            if (!instances.isEmpty()) {
                for (DummyEntry d: instances) {
                    wt += d.weight;
                    sz += d.size;
                }

                num = instances.size();
                avgWt = wt/num;
                avgSize = sz/num;
            }
            changed = true;
        }

        void addInstance(DummyEntry d) {
            Log.d("SessionReport", "Before Instance: Type=" + type + ", wt=" + avgWt + ", sz=" + avgSize + ", num=" + num);
            avgWt = ((avgWt * num) + d.weight)/(num + 1);
            avgSize = ((avgSize * num) + d.size) / (num + 1);
            instances.add(d);
            num++;
            changed = true;
            Log.d("SessionReport", "Added Instance: Type=" + type + ", wt=" + avgWt + ", sz=" + avgSize + ", num=" + num);
        }

        double stdDevWeight() {
            if (changed) {
                recalculate();
            }

            return devWt;
        }

        double stdDevSize() {
            if (changed) {
                recalculate();
            }

            return devSize;
        }

        private void recalculate() {
            devWt = 0;
            devSize = 0;
            for (DummyEntry d: instances) {
                devWt += Math.pow(avgWt - d.weight, 2);
                devSize += Math.pow(avgSize - d.size, 2);
            }

            devWt = Math.sqrt(devWt);
            devSize = Math.sqrt(devSize);
            changed = false;
        }

    }
}
