package com.github.mzule.activityrouter.router;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by CaoDongping on 4/6/16.
 */
public class Mapping {
    private String format;
    private Class<? extends Activity> activity;
    private ExtraTypes extraTypes;

    public Mapping(String format, Class<? extends Activity> activity, ExtraTypes extraTypes) {
        if (format == null) {
            throw new NullPointerException("format can not be null");
        }
        if (activity == null) {
            throw new NullPointerException("activity can not be null");
        }
        this.format = format;
        this.activity = activity;
        this.extraTypes = extraTypes;
    }

    public String getFormat() {
        return format;
    }

    public Class<? extends Activity> getActivity() {
        return activity;
    }

    @Override
    public String toString() {
        return String.format("%s => %s", format, activity);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Mapping) {
            Mapping that = (Mapping) o;
            return that.format.equals(format);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return format.hashCode();
    }

    public boolean match(String url) {
        //TODO implementation
        return true;
    }

    public Bundle parseExtras(String url) {
        // TODO implementation
        return new Bundle();
    }
}
