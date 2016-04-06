package com.github.mzule.activityrouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by CaoDongping on 4/6/16.
 */
public class Routers {
    private static Routers routers;
    private Set<Mapping> mappings;
    private Context context;

    static Routers create(Context context) {
        if (routers == null) {
            routers = new Routers(context);
        }
        return routers;
    }

    public static Routers getRouters() {
        return routers;
    }

    private Routers(Context context) {
        this.context = context;
        this.mappings = new HashSet<>();
    }

    public void map(String format, Class<? extends Activity> clazz, ExtraTypes extraTypes) {
        mappings.add(new Mapping(format, clazz, extraTypes));
    }

    public void open(String url) {
        for (Mapping mapping : mappings) {
            if (mapping.match(url)) {
                Intent intent = new Intent(context, mapping.getActivity());
                intent.putExtras(mapping.parseExtras(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
            }
        }
    }
}
