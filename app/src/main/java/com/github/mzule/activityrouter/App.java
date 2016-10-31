package com.github.mzule.activityrouter;

import com.github.mzule.activityrouter.annotation.Modules;
import com.github.mzule.activityrouter.router.RouterCallback;
import com.github.mzule.activityrouter.router.RouterCallbackProvider;
import com.github.mzule.activityrouter.router.SimpleRouterCallback;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by CaoDongping on 4/6/16.
 */
@Modules({"app", "sdk"})
public class App extends Application implements RouterCallbackProvider {
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

            @Override
            public void error(Context context, Uri uri, Throwable e) {
                context.startActivity(ErrorStackActivity.makeIntent(context, uri, e));
            }
        };
    }
}
