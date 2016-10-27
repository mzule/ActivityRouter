package com.github.mzule.activityrouter.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by CaoDongping on 4/6/16.
 */
public class Routers {

    public static String KEY_RAW_URL = "com.github.mzule.activityrouter.router.KeyRawUrl";

    private static List<Mapping> mappings = new ArrayList<>();

    private static void initIfNeed() {
        if (!mappings.isEmpty()) {
            return;
        }
        RouterMapping.map();
    }

    static void map(String format, Class<? extends Activity> activity, ExtraTypes extraTypes) {
        mappings.add(new Mapping(format, activity, extraTypes));
    }

    static void sort() {
        // ensure that user/collection is rank top over user/:userId
        // so scheme://user/collection will match user/collection not user/:userId
        Collections.sort(mappings, new Comparator<Mapping>() {
            @Override
            public int compare(Mapping lhs, Mapping rhs) {
                return lhs.getFormat().compareTo(rhs.getFormat()) * -1;
            }
        });
    }

    public static boolean open(Context context, String url) {
        return open(context, Uri.parse(url));
    }

    public static boolean open(Context context, String url, RouterCallback callback) {
        return open(context, Uri.parse(url), callback);
    }

    public static boolean open(Context context, Uri uri) {
        return open(context, uri, null);
    }

    public static boolean open(Context context, Uri uri, RouterCallback callback) {
        return open(context, uri, -1, callback);
    }

    public static boolean openForResult(Activity activity, String url, int requestCode) {
        return openForResult(activity, Uri.parse(url), requestCode);
    }

    public static boolean openForResult(Activity activity, String url, int requestCode, RouterCallback callback) {
        return openForResult(activity, Uri.parse(url), requestCode, callback);
    }

    public static boolean openForResult(Activity activity, Uri uri, int requestCode) {
        return openForResult(activity, uri, requestCode, null);
    }

    public static boolean openForResult(Activity activity, Uri uri, int requestCode, RouterCallback callback) {
        return open(activity, uri, requestCode, callback);
    }

    private static boolean open(Context context, Uri uri, int requestCode, RouterCallback callback) {
        boolean success = false;
        try {
            if (callback != null) {
                callback.beforeOpen(context, uri);
            }
            success = doOpen(context, uri, requestCode);
            if (callback != null) {
                if (success) {
                    callback.afterOpen(context, uri);
                } else {
                    callback.notFound(context, uri);
                }
            }
        } catch (Throwable e) {
            if (callback != null) {
                callback.error(context, uri, e);
            }
        }
        return success;
    }

    private static boolean doOpen(Context context, Uri uri, int requestCode) {
        initIfNeed();

        if (uri == null) {
            return false;
        }
//
//        List<String> uriStringList = uri.getPathSegments();
//
//        String scheme = uri.getScheme();
//        String a= uri.getAuthority();
//        String b= uri.getHost();
//        String c = uri.getSchemeSpecificPart();
//        String d = uri.getUserInfo();


        List<Uri> uriList = new ArrayList<>();
        HashMap<Uri, Mapping> uriMap = new HashMap<>();
        //----------------------------------------------------------------
        //--------------------------------注释--------------------------------
        //----------------------------------------------------------------
        String pathConstant = "://";
        String scheme = uri.getScheme().concat(pathConstant);
        String allPath = uri.toString();
        if (allPath.contains(pathConstant)) {
            allPath = allPath.substring(allPath.indexOf(pathConstant) + pathConstant.length());
        }

        String[] pathArray = allPath.split("/");

        int start = 0;
        int end = start + 1;
        int arrayLength = pathArray.length;

        StringBuilder stringBuilder;

        //-


        Uri tUri = null;
        Mapping tMapping = null;

        Mapping itemMapping = null;

        Uri itemUri = null;
        String itemPath = null;

        for (String tPath : pathArray) {
            stringBuilder = new StringBuilder(scheme);
            stringBuilder.append(tPath);

            tUri = Uri.parse(stringBuilder.toString());
            tMapping = findMap(tUri);

            for (; end < arrayLength; end++) {
                if (end - 1 >= 0 && pathArray[end - 1].contains("?")) {
                    System.out.print("hhh");
                    break;
                }
                itemPath = stringBuilder.append("/").append(pathArray[end]).toString();
                itemUri = Uri.parse(itemPath);
                itemMapping = findMap(itemUri);

                if (itemMapping != null) {
                    tMapping = itemMapping;
                    tUri = itemUri;
                }
            }

            if (tMapping != null && !uriMap.containsValue(tMapping)) {
                uriList.add(tUri);
                uriMap.put(tUri, tMapping);
            }


            start++;
            end = start + 1;
        }

//        return doOpenList(tMappingList, context, uri, requestCode);
        return doOpenList(uriList, uriMap, context, requestCode);
    }

    private static Mapping findMap(Uri uri) {
        Path path = Path.create(uri);

        for (Mapping mapping : mappings) {
            if (mapping.match(path)) {
                return mapping;
            }
        }

        return null;
    }

//    private static boolean doOpenList(List<Mapping> tMappings, Context context, Uri uri, int requestCode) {
//        if (tMappings == null || tMappings.size() == 0) {
//            return false;
//        }
//        for (Mapping mapping : tMappings) {
//            Intent intent = new Intent(context, mapping.getActivity());
//            intent.putExtras(mapping.parseExtras(uri));
//            intent.putExtra(KEY_RAW_URL, uri.toString());
//            if (!(context instanceof Activity)) {
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            }
//            if (requestCode >= 0) {
//                if (context instanceof Activity) {
//                    ((Activity) context).startActivityForResult(intent, requestCode);
//                } else {
//                    throw new RuntimeException("can not startActivityForResult context " + context);
//                }
//            } else {
//                context.startActivity(intent);
//            }
//        }
//
//        return true;
//    }

    private static boolean doOpen(Mapping map, Context context, Uri uri, int requestCode) {
        Intent intent = new Intent(context, map.getActivity());
        intent.putExtras(map.parseExtras(uri));
        intent.putExtra(KEY_RAW_URL, uri.toString());
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (requestCode >= 0) {
            if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, requestCode);
            } else {
                throw new RuntimeException("can not startActivityForResult context " + context);
            }
        } else {
            context.startActivity(intent);
        }

        return true;
    }

    private static boolean doOpenList(List<Uri> uriList, HashMap<Uri, Mapping> uriMap, Context context, int requestCode) {
        if (uriMap == null || uriMap.size() == 0 || uriList == null || uriList.size() == 0) {
            return false;
        }

        for (Uri uri : uriList) {
            if (uri == null) {
                continue;
            }

            doOpen(uriMap.get(uri), context, uri, requestCode);
        }

//        Iterator<Map.Entry<Uri, Mapping>> iterator = uriMap.entrySet().iterator();
//
//        Map.Entry<Uri, Mapping> entry = null;
//        while (iterator.hasNext()) {
//            entry = iterator.next();
//            if (entry != null) {
//                doOpen(entry.getValue(), context, entry.getKey(), requestCode);
//            }
//        }

        return true;
    }
}
