package com.github.mzule.activityrouter;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mzule.activityrouter.annotation.Router;

/**
 * Created by CaoDongping on 04/11/2016.
 */

public class NonUIActions {

    @Router("logout")
    public static void logout(Context context, Bundle bundle, int requestCode) {
        Toast.makeText(context, "logout", Toast.LENGTH_SHORT).show();
    }

    @Router("upload")
    public static void uploadLog(Context context, Bundle bundle, int requestCode) {
        Toast.makeText(context, "upload", Toast.LENGTH_SHORT).show();
    }
}
