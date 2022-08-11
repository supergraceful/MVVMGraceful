package me.magical.mvvmgraceful.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.text.TextUtils;

import com.tencent.mmkv.MMKV;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KVUtil {

    private static StorageType storageType = StorageType.SharedPreferences;
    private static MMKV mmkv;
    private static SharedPreferences sp;
    private Context context;
    private static KVUtil mmkvUtil;
    private static final String FILE_NAME = "android_kv_data";

    private KVUtil() {
    }

    public static KVUtil getInstance() {
        if (mmkvUtil == null) {
            synchronized (KVUtil.class) {
                if (mmkvUtil == null) {
                    mmkvUtil = new KVUtil();
                }
            }
        }
        return mmkvUtil;
    }

    public void init(){
        init(StorageType.SharedPreferences);
    }

    public void init(StorageType storageType) {
        init(UtilsBridge.getApplication(), storageType);
    }

    public void init(Context context, StorageType storageType) {
        init(context, "", storageType);
    }

    public void init(Context context, String fileName, StorageType storageType) {
        this.context = context;
        this.storageType = storageType;
        switch (storageType) {
            case SharedPreferences:
                if (TextUtils.isEmpty(fileName)) {
                    sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
                } else {
                    sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
                }
                break;
            case MMKV:
                MMKV.initialize(context);
                if (TextUtils.isEmpty(fileName)) {
//                    mmkv = MMKV.defaultMMKV();
                    mmkv = MMKV.mmkvWithID(FILE_NAME);
                } else {
                    mmkv = MMKV.mmkvWithID(fileName);
                }
                break;
            case DataStore:
                break;
        }
    }

    public static MMKV getMMKV() throws Exception {
        if (mmkv==null){
            throw new Exception("MMKV is null,please reinitialize");
        }
        return mmkv;
    }

    public static SharedPreferences getSp() throws Exception{
        if (mmkv==null){
            throw new Exception("SharedPreferences is null,please reinitialize");
        }
        return mmkv;
    }

    public static void changeSaveFile(Context context, String fileName) throws IOException {

        if (TextUtils.isEmpty(fileName)){
            throw new IOException("save file name is null");
        }
        switch (storageType) {
            case SharedPreferences:
                sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
                break;
            case MMKV:
                mmkv = MMKV.mmkvWithID(fileName);
        }
    }

    public static void remove(String k) {
        switch (storageType) {
            case SharedPreferences:
                SharedPreferences.Editor editor = sp.edit();
                editor.remove(k);
                editor.apply();
                break;
            case MMKV:
                mmkv.removeValueForKey(k);
                break;
            case DataStore:
                break;
            default:
                break;
        }
    }

    public static void removes(List<String> ks) {
        switch (storageType) {
            case SharedPreferences:
                for (String k : ks) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.remove(k);
                    editor.apply();
                }
                break;
            case MMKV:
                mmkv.removeValuesForKeys((String[]) ks.toArray());
                break;
            case DataStore:
                break;
            default:
                break;
        }
    }

    public static void clear() {
        switch (storageType) {
            case SharedPreferences:
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                break;
            case MMKV:
                mmkv.clear();
                break;
            case DataStore:
                break;
            default:
                break;
        }
    }

    public static boolean contains(String key) {
        switch (storageType) {
            case SharedPreferences:
                return sp.contains(key);
            case MMKV:
                return mmkv.contains(key);
            case DataStore:
                break;
            default:
                break;
        }
        return false;

    }

    public static Map<String, ?> getAll() {
        switch (storageType) {
            case SharedPreferences:
                return sp.getAll();
            case MMKV:
                return mmkv.getAll();
            case DataStore:
                break;
            default:
                break;
        }
        return null;
    }

    public static boolean put(String k, String v) {
        switch (storageType) {
            case SharedPreferences:
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(k, v);
                editor.apply();
                return true;
            case MMKV:
                return mmkv.encode(k, v);
            case DataStore:
            default:
                return false;
        }
    }

    public static boolean put(String k, int v) {
        switch (storageType) {
            case SharedPreferences:
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt(k, v);
                editor.apply();
                return true;
            case MMKV:
                return mmkv.encode(k, v);
            case DataStore:
            default:
                return false;
        }
    }

    public static boolean put(String k, boolean v) {
        switch (storageType) {
            case SharedPreferences:
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean(k, v);
                editor.apply();
                return true;
            case MMKV:
                return mmkv.encode(k, v);
            case DataStore:
            default:
                return false;
        }
    }

    public static boolean put(String k, long v) {
        switch (storageType) {
            case SharedPreferences:
                SharedPreferences.Editor editor = sp.edit();
                editor.putLong(k, v);
                editor.apply();
                return true;
            case MMKV:
                return mmkv.encode(k, v);
            case DataStore:
            default:
                return false;
        }
    }

    public static boolean put(String k, float v) {
        switch (storageType) {
            case SharedPreferences:
                SharedPreferences.Editor editor = sp.edit();
                editor.putFloat(k, v);
                editor.apply();
                return true;
            case MMKV:
                return mmkv.encode(k, v);
            case DataStore:
            default:
                return false;
        }
    }

    public static boolean put(String k, double v) {
        switch (storageType) {
            case MMKV:
                return mmkv.encode(k, v);
            default:
                return false;
        }
    }

    public static boolean put(String k, Set<String> v) {
        switch (storageType) {
            case SharedPreferences:
                SharedPreferences.Editor editor = sp.edit();
                editor.putStringSet(k, v);
                editor.apply();
                return true;
            case MMKV:
                return mmkv.encode(k, v);
            case DataStore:
            default:
                return false;
        }
    }

    public static boolean put(String k, byte[] v) {
        switch (storageType) {
            case MMKV:
                return mmkv.encode(k, v);
            default:
                return false;
        }
    }

//    public static boolean put(String k, Parcelable v){
//        switch (storageType) {
//            case MMKV:
//                return mmkv.encode(k,v);
//            default:
//                return false;
//        }
//    }

    public static String getString(String k) {
        switch (storageType) {
            case SharedPreferences:
                return sp.getString(k, null);
            case MMKV:
                return mmkv.decodeString(k, null);
            default:
                return null;
        }
    }

    public static String getString(String k, String s) {
        switch (storageType) {
            case SharedPreferences:
                return sp.getString(k, s);
            case MMKV:
                return mmkv.decodeString(k, s);
            default:
                return null;
        }
    }

    public static int getInt(String k) {
        switch (storageType) {
            case SharedPreferences:
                return sp.getInt(k, 0);
            case MMKV:
                return mmkv.decodeInt(k, 0);
            default:
                return 0;
        }

    }

    public static int getInt(String k, int i) {
        switch (storageType) {
            case SharedPreferences:
                return sp.getInt(k, i);
            case MMKV:
                return mmkv.decodeInt(k, i);
            default:
                return 0;
        }

    }

    public static boolean getBoolean(String k) {
        switch (storageType) {
            case SharedPreferences:
                return sp.getBoolean(k, false);
            case MMKV:
                return mmkv.decodeBool(k, false);
            default:
                return false;
        }
    }

    public static boolean getBoolean(String k, boolean b) {
        switch (storageType) {
            case SharedPreferences:
                return sp.getBoolean(k, b);
            case MMKV:
                return mmkv.decodeBool(k, b);
            default:
                return false;
        }
    }

    public static long getLong(String k) {
        switch (storageType) {
            case SharedPreferences:
                return sp.getLong(k, 0);
            case MMKV:
                return mmkv.decodeLong(k, 0);
            default:
                return 0;
        }
    }

    public static long getLong(String k, long l) {
        switch (storageType) {
            case SharedPreferences:
                return sp.getLong(k, l);
            case MMKV:
                return mmkv.decodeLong(k, l);
            default:
                return 0;
        }
    }

    public static float getFloat(String k) {
        switch (storageType) {
            case SharedPreferences:
                return sp.getFloat(k, 0);
            case MMKV:
                return mmkv.decodeFloat(k, 0);
            case DataStore:
            default:
                return 0;
        }
    }

    public static float getFloat(String k, float f) {
        switch (storageType) {
            case SharedPreferences:
                return sp.getFloat(k, f);
            case MMKV:
                return mmkv.decodeFloat(k, f);
            case DataStore:
            default:
                return 0;
        }
    }

    public static double getDouble(String k) {
        switch (storageType) {
            case MMKV:
                return mmkv.decodeDouble(k, 0.0);
            default:
                return 0.0;
        }

    }

    public static double getDouble(String k, double d) {
        switch (storageType) {
            case MMKV:
                return mmkv.decodeDouble(k, d);
            default:
                return 0.0;
        }

    }

    public static Set<String> getSet(String k) {
        switch (storageType) {
            case SharedPreferences:
                return sp.getStringSet(k, null);
            case MMKV:
                return mmkv.getStringSet(k, null);
            case DataStore:
            default:
                return null;
        }
    }

    public static Set<String> getSet(String k, Set<String> set) {
        switch (storageType) {
            case SharedPreferences:
                return sp.getStringSet(k, set);
            case MMKV:
                return mmkv.getStringSet(k, set);
            case DataStore:
            default:
                return null;
        }
    }

    public static byte[] getBytes(String k) {
        switch (storageType) {
            case MMKV:
                return mmkv.getBytes(k, null);
            default:
                return null;
        }
    }

    public static byte[] getBytes(String k, byte[] b) {
        switch (storageType) {
            case MMKV:
                return mmkv.getBytes(k, b);
            default:
                return null;
        }
    }

    public void SharedPreferencesToMMVK(String fileName) {
        SharedPreferences old_man = context.getSharedPreferences(fileName, 0);
        mmkv.importFromSharedPreferences(old_man);
        old_man.edit().clear().commit();
    }

    public static enum StorageType {
        SharedPreferences,
        MMKV,
        DataStore,
        NULL
    }

}
