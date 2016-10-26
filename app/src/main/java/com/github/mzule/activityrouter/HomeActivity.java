package com.github.mzule.activityrouter;

import com.github.mzule.activityrouter.annotation.Router;

import android.content.Intent;

/**
 * Created by CaoDongping on 4/7/16.
 */
//@Router(value = "home/:homeName", stringParams = "o")
public class HomeActivity extends DumpExtrasActivity {

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("msg", "goodbye");
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
