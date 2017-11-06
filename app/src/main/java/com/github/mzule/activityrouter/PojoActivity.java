package com.github.mzule.activityrouter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.github.mzule.activityrouter.annotation.Router;

import java.util.Set;

/**
 * @author CPPAlien
 */
@Router("test/parcelable")
public class PojoActivity extends Activity {
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
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
            PojoData pojoData = getIntent().getParcelableExtra("POJO");
            pojoData.getName();
            textView.append("\n\nPoJoData");
            textView.append("\nname = " + pojoData.getName());
            textView.append("\nage = " + pojoData.getAge());

            setContentView(textView);
        }
    }
}
