package com.github.mzule.activityrouter;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mzule.activityrouter.router.RouterCallbackProvider;
import com.github.mzule.activityrouter.router.Routers;

/**
 * Created by CaoDongping on 4/7/16.
 */
public class LaunchActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        for (int i = 0; i < container.getChildCount(); i++) {
            final View view = container.getChildAt(i);
            if (view instanceof TextView) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*
                        // 第三方app通过url打开本app的activity时，通过ACTION_VIEW的方式
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(((TextView) view).getText().toString()));
                        startActivity(intent);
                        */
                        // app内打开页面可以使用Routers.open(Context, Uri)
                        Routers.open(LaunchActivity.this, Uri.parse(((TextView) view).getText().toString()), ((RouterCallbackProvider) getApplication()).provideRouterCallback());
                    }
                });
            }
        }
    }
}
