package com.github.mzule.activityrouter;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.github.mzule.activityrouter.router.RouterCallback;
import com.github.mzule.activityrouter.router.RouterCallbackProvider;
import com.github.mzule.activityrouter.router.SimpleRouterCallback;

/**
 * Created by CaoDongping on 4/6/16.
 */
public class App extends Application implements RouterCallbackProvider {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public RouterCallback provideRouterCallback() {
        return new SimpleRouterCallback() {
            @Override
            public void beforeOpen(Context context, Uri uri) {
                context.startActivity(new Intent(context, LaunchActivity.class));
            }

            @Override
            public void notFound(Context context, Uri uri) {
                context.startActivity(new Intent(context, NotFoundActivity.class));
            }
        };
    }
}
