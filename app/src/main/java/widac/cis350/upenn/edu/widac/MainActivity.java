package widac.cis350.upenn.edu.widac;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    
    public final static String COMPOSITE_KEY = "compositeKey";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    public void onSearchButtonClick(View v) {

        Intent i = new Intent(this, SearchActivity.class);
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
