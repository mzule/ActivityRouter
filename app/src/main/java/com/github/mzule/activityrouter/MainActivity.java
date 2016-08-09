package com.github.mzule.activityrouter;

import com.github.mzule.activityrouter.annotation.Router;

@Router(value = {"http://mzule.com/main", "main", "home"},
        longParams = {"id", "updateTime"},
        booleanParams = "web",
        transfer = "web=>fromWeb")
public class MainActivity extends DumpExtrasActivity {

}
