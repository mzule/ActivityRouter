package com.github.mzule.activityrouter.router;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by CaoDongping on 04/11/2016.
 */

public interface MethodInvoker {
    void invoke(Context context, Bundle bundle, int requestCode);
}
