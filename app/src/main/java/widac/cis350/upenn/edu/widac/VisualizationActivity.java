package widac.cis350.upenn.edu.widac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class VisualizationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Most likely want to get session data from intent
        setContentView(R.layout.activity_visualization);
    }

    // Build a chart showing the various pieces collected during the current session
    private void createChart() {

    }
}
