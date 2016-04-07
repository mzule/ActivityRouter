package com.github.mzule.activityrouter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mzule.activityrouter.annotation.Router;

@Router(value = {"main", "root", "home/:name"}, longExtra = {"id", "updateTime"}, boolExtra = "web")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = new TextView(this);
        textView.append("\nid:" + getIntent().getLongExtra("id", 0));
        textView.append("\nupdateTime:" + getIntent().getLongExtra("updateTime", 0));
        textView.append("\nweb:" + getIntent().getBooleanExtra("web", false));
        setContentView(textView);
    }
}
