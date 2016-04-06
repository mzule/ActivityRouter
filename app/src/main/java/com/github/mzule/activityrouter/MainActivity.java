package com.github.mzule.activityrouter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mzule.activityrouter.annotation.Router;
import com.github.mzule.activityrouter.router.Routers;

@Router(value = "main", longExtra = {"id", "updateTime"}, boolExtra = "web")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Routers.init(this);
    }
}
