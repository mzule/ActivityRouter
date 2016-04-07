package com.github.mzule.activityrouter;

import com.github.mzule.activityrouter.annotation.Router;

/**
 * Created by CaoDongping on 4/7/16.
 */
@Router({"user/:userId", "user/:nickname/city/:city/gender/:gender/age/:age"})
public class UserActivity extends DumpExtrasActivity {
}
