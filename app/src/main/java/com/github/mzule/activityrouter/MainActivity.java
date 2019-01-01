package com.github.mzule.activityrouter;

import com.github.mzule.activityrouter.annotation.Router;

import hugo.weaving.DebugLog;

@Router(value = {"http://mzule.com/main", "main", "home"},
        longParams = {"id", "updateTime"},
        booleanParams = "web",
        transfer = "web=>fromWeb")
@DebugLog
public class MainActivity extends DumpExtrasActivity {

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }
}
