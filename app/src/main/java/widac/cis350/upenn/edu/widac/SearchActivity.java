package widac.cis350.upenn.edu.widac;
import widac.cis350.upenn.edu.widac.models.Sample;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import static java.security.AccessController.getContext;

public class SearchActivity extends AppCompatActivity {
    
    private Sample sample;
    DBConnection db;
    private int itemNumber;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Create new intent
        Intent i = this.getIntent();

        // Pull from database
        sample = Session.pullNewEntryFromDB(i.getStringExtra(("compositeKey")));
        // sample  = db.retrieveSample(i.getStringExtra("compositeKey"));

        // Update
        TextView itemName = (TextView) findViewById(R.id.item_name);
        itemName.setText(sample.getCompositeKey());
        TextView itemWeight = (TextView) findViewById(R.id.itemWeight);
        String displayWeight = (sample.getWeight() == 0) ? "No Data" : "" + sample.getWeight();
        itemWeight.setText(displayWeight);

        itemNumber = 123;
    }
    
    public void onUpdateBluetoothButtonClick(View v) {
        ((TextView)findViewById(R.id.itemWeight)).setText("Weight: 234g");
        Toast.makeText(this, "Weight updated", Toast.LENGTH_LONG).show();
    }
    
    public void onUpdateManualButtonClick(View v) {
        EditText editText = (EditText) findViewById(R.id.new_weight);
        String newWeight = editText.getText().toString();
        ((TextView)findViewById(R.id.itemWeight)).setText("Weight: " + newWeight + "g");
        Toast.makeText(this, "Weight updated", Toast.LENGTH_LONG).show();
    }

    public void onPrevButtonClick(View v) {
        TextView itemName = (TextView) findViewById(R.id.item_name);
        //String prevName = itemName.getText().toString();
        itemName.setText("Item 122");
        TextView weightText = (TextView) findViewById(R.id.itemWeight);
        weightText.setText("Weight: 121g");
    }

    public void onNextButtonClick(View v) {
        TextView itemName = (TextView) findViewById(R.id.item_name);
        //String prevName = itemName.getText().toString();
        itemName.setText("Item 124");
        TextView weightText = (TextView) findViewById(R.id.itemWeight);
        weightText.setText("Weight: 235g");
    }
}
