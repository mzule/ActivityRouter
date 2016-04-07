package com.github.mzule.activityrouter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mzule.activityrouter.annotation.Router;

/**
 * Created by CaoDongping on 4/7/16.
 */
@Router("user/:userId")
public class UserActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(UserActivity.this, "userId:".concat(getIntent().getStringExtra("userId")), Toast.LENGTH_SHORT).show();
    }
}
