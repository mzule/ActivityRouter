package com.github.mzule.activityrouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by CaoDongping on 8/9/16.
 */
public class ErrorStackActivity extends Activity {
    public static Intent makeIntent(Context context, Uri uri, Throwable e) {
        Intent intent = new Intent(context, ErrorStackActivity.class);
        intent.putExtra("uri", uri);
        intent.putExtra("error", e);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Throwable e = (Throwable) getIntent().getSerializableExtra("error");
        Uri uri = getIntent().getParcelableExtra("uri");

        TextView textView = new TextView(this);
        textView.setText(String.format("Error on open uri %s\n", uri));
        textView.append(Log.getStackTraceString(e));
        textView.setGravity(Gravity.START);
        setContentView(textView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
