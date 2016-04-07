package com.github.mzule.activityrouter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mzule.activityrouter.annotation.Router;

/**
 * Created by CaoDongping on 4/7/16.
 */
@Router(value = "home/:id/:newUser", longExtra = "id", boolExtra = "newUser")
public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, getIntent().getLongExtra("id", 0) + " is " + getIntent().getBooleanExtra("newUser", false), Toast.LENGTH_SHORT).show();
    }
}
