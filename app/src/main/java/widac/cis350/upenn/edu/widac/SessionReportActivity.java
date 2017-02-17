package widac.cis350.upenn.edu.widac;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

/**
 * Created by J. Patrick Taggart on 2/17/2017.
 * ===================================================
 * Expected data
 *  -Total number of items collected
 *  -A list of items collected
 *      -List should either contain a id to be used to query database or all information needed
 */

public class SessionReportActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_report);

        //createSessionReport();
    }

    private void createSessionReport() {
        // Goind to need to get data from current session somehow
        generateStatistics();
    }

    // Provide overview of data collected in recent session
    private void generateStatistics() {
        int itemsCollected = -1; //
        TextView itemsCollectedText = (TextView)findViewById(R.id.itemsCollected);
        itemsCollectedText.setText(itemsCollected);
    }

    // Load data on a piece recorded during this session
    private void loadDataView(String id) {

    }

    private DBEntry fakeDBcall() {
        return  new DBEntry("12RomanVase", "Pottery", "Herculaneum", 1.233);
    }

    private class DBEntry {
        String ID;
        String type; // Could define an enum for types
        String locationData;
        double weight; // assume in grams

        DBEntry(String ID, String type, String locationData, double weight) {
            this.ID = ID;
            this.type = type;
            this.locationData = locationData;
            this.weight = weight;
        }
    }
}
