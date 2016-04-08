package com.github.mzule.activityrouter.router;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by CaoDongping on 4/6/16.
 */
public class RouterActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RouterCallback callback = getRouterCallback();

        Uri uri = getIntent().getData();
        if (uri != null) {
            if (callback == null) {
                Routers.open(this, uri);
            } else {
                callback.beforeOpen(this, uri);
                boolean success = Routers.open(this, uri);
                if (success) {
                    callback.afterOpen(this, uri);
                } else {
                    callback.notFound(this, uri);
                }
            }
        }
        finish();
    }

    @Nullable
    private RouterCallback getRouterCallback() {
        if (getApplication() instanceof RouterCallbackProvider) {
            return ((RouterCallbackProvider) getApplication()).provideRouterCallback();
        }
        return null;
    }
}
