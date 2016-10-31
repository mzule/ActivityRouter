package com.github.mzule.activityrouter.module;

import com.github.mzule.activityrouter.annotation.Router;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by CaoDongping on 30/10/2016.
 */

@Router("module")
public class ModuleActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText("this is ModuleActivity");
        setContentView(textView);
    }
}
