package com.github.mzule.activityrouter;

import com.github.mzule.activityrouter.annotation.Router;

@Router(value = {"http://mzule.com/main", "main", "home"}, longExtra = {"id", "updateTime"}, boolExtra = "web")
public class MainActivity extends DumpExtrasActivity {
}
