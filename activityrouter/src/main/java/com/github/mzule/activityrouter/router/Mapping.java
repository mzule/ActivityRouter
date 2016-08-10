package com.github.mzule.activityrouter.router;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import java.util.Set;

/**
 * Created by CaoDongping on 4/6/16.
 */
public class Mapping {
    private final String format;
    private final Class<? extends Activity> activity;
    private final ExtraTypes extraTypes;
    private Path formatPath;

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
        if (format.toLowerCase().startsWith("http://") || format.toLowerCase().startsWith("https://")) {
            this.formatPath = Path.create(Uri.parse(format));
        } else {
            this.formatPath = Path.create(Uri.parse("helper://".concat(format)));
        }
    }

    public Class<? extends Activity> getActivity() {
        return activity;
    }

    public String getFormat() {
        return format;
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
            return that.format.equals(((Mapping) o).format);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return format.hashCode();
    }

    public boolean match(Path fullLink) {
        if (formatPath.isHttp()) {
            return Path.match(formatPath, fullLink);
        } else {
            // fullLink without host
            boolean match = Path.match(formatPath.next(), fullLink.next());
            if (!match && fullLink.next() != null) {
                // fullLink with host
                match = Path.match(formatPath.next(), fullLink.next().next());
            }
            return match;
        }
    }

    public Bundle parseExtras(Uri uri) {
        Bundle bundle = new Bundle();
        // path segments // ignore scheme
        Path p = formatPath.next();
        Path y = Path.create(uri).next();
        while (p != null) {
            if (p.isArgument()) {
                put(bundle, p.argument(), y.value());
            }
            p = p.next();
            y = y.next();
        }
        // parameter
        Set<String> names = UriCompact.getQueryParameterNames(uri);
        for (String name : names) {
            String value = uri.getQueryParameter(name);
            put(bundle, name, value);
        }
        return bundle;
    }

    private void put(Bundle bundle, String name, String value) {
        int type = extraTypes.getType(name);
        name = extraTypes.transfer(name);
        if (type == ExtraTypes.STRING) {
            type = extraTypes.getType(name);
        }
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
            case ExtraTypes.BYTE:
                bundle.putByte(name, Byte.parseByte(value));
                break;
            case ExtraTypes.CHAR:
                bundle.putChar(name, value.charAt(0));
                break;
            default:
                bundle.putString(name, value);
                break;
        }
    }
}
