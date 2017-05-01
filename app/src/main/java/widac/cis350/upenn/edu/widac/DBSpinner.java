package widac.cis350.upenn.edu.widac;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by ashutosh on 4/30/17.
 */

public class DBSpinner extends AppCompatSpinner {

    OnItemSelectedListener listener;
    private AdapterView<?> lastParent;
    private View lastView;
    private long lastId;

    public DBSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initlistner();
    }

    @Override
    public void setSelection(int position) {
        if (position == getSelectedItemPosition() && listener != null) {
            listener.onItemSelected(lastParent, lastView, position, lastId);
        } else {
            super.setSelection(position);
        }
    }

    public void setOnItemSelectedEvenIfUnchangedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    private void initlistner() {
        super.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                lastParent = parent;
                lastView = view;
                lastId = id;
                if (listener != null) {
                    listener.onItemSelected(parent, view, position, id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
                if (listener != null) {
                    listener.onNothingSelected(parent);
                }
            }
        });

    }
}
