package com.github.mzule.activityrouter.router;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import java.util.List;
import java.util.Set;

/**
 * Created by CaoDongping on 4/6/16.
 */
public class Mapping {
    private String format;
    private Class<? extends Activity> activity;
    private ExtraTypes extraTypes;
    private String host;
    private List<String> pathArguments;

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

        Uri uri = Uri.parse("helper://".concat(format));
        this.host = uri.getHost();
        this.pathArguments = uri.getPathSegments();
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
        Uri uri = Uri.parse(url);
        return uri.getHost().equals(host) && uri.getPathSegments().size() == pathArguments.size();
    }

    public Bundle parseExtras(String url) {
        Bundle bundle = new Bundle();
        Uri uri = Uri.parse(url);
        Set<String> names = uri.getQueryParameterNames();
        for (String name : names) {
            String value = uri.getQueryParameter(name);
            int type = extraTypes.getType(name);
            switch (type) {
                case ExtraTypes.INT:
                    bundle.putInt(name, Integer.parseInt(value));
                    break;
                case ExtraTypes.LONG:
                    bundle.putLong(name, Long.parseLong(value));
                    break;
                case ExtraTypes.BOOL:
                    bundle.putBoolean(name, Boolean.parseBoolean(value));
                    break;
                case ExtraTypes.SHORT:
                    bundle.putShort(name, Short.parseShort(value));
                    break;
                case ExtraTypes.FLOAT:
                    bundle.putFloat(name, Float.parseFloat(value));
                    break;
                case ExtraTypes.DOUBLE:
                    bundle.putDouble(name, Double.parseDouble(value));
                    break;
                default:
                    bundle.putString(name, value);
                    break;
            }
        }
        return bundle;
    }
}
