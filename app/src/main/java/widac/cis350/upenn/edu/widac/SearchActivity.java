package widac.cis350.upenn.edu.widac;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import widac.cis350.upenn.edu.widac.models.Sample;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
        db = new DBConnection();
        Intent i = this.getIntent();

        /*
        sample = db.retrieveSample(i.getStringExtra("compositeKey"));
        TextView itemName = (TextView) findViewById(R.id.item_name);
        itemName.setText(sample.getCompositeKey());
        TextView itemWeight = (TextView) findViewById(R.id.itemWeight);
        String displayWeight = (sample.getWeight() == 0) ? "No Data" : "" + sample.getWeight();
        itemWeight.setText(displayWeight);
        */

        db.getSample(i.getStringExtra("id"), sampleCallback);

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

    Callback sampleCallback = new Callback<Sample>(){

        @Override
        public void onResponse(Call<Sample> call, Response<Sample> response) {
            int code = response.code();
            if (code == 200) {
                SearchActivity.this.sample = response.body();
                TextView itemName = (TextView) findViewById(R.id.item_name);
                itemName.setText(sample.getCompositeKey());
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
