package com.github.mzule.activityrouter.router;

import android.content.Context;
import android.net.Uri;

/**
 * Created by CaoDongping on 4/8/16.
 */
public class SimpleRouterCallback implements RouterCallback {
    @Override
    public void notFound(Context context, Uri uri) {
    }

    @Override
    public void beforeOpen(Context context, Uri uri) {
    }

    @Override
    public void afterOpen(Context context, Uri uri) {
    }

    @Override
    public void error(Context context, Uri uri, Throwable e) {
    }
}
