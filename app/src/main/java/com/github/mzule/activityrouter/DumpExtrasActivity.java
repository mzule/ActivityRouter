package com.github.mzule.activityrouter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Set;

/**
 * Created by CaoDongping on 4/7/16.
 */
public abstract class DumpExtrasActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Set<String> keys = extras.keySet();

            TextView textView = new TextView(this);
            int padding = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
            textView.setPadding(padding, padding, padding, padding);
            textView.setText(getClass().getSimpleName());
            textView.append("\n\n");

            for (String key : keys) {
                textView.append(key + "=>");
                Object v = extras.get(key);
                if (v != null) {
                    textView.append(v + "=>" + v.getClass().getSimpleName());
                } else {
                    textView.append("null");
                }
                textView.append("\n\n");
            }

            setContentView(textView);
        }
    }
}
