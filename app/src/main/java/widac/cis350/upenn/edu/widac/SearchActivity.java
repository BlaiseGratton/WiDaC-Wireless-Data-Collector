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
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBConnection();
        Intent i = this.getIntent();
        sample  = db.retrieveSample(i.getStringExtra("compositeKey"));
        TextView itemName = (TextView) findViewById(R.id.item_name);
        itemName.setText(sample.getCompositeKey());
        setContentView(R.layout.activity_search);
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
}
