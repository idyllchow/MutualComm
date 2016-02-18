package com.sponia.foundationmoudle.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;

import com.sponia.foundationmoudle.common.Common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

/**
 * @packageName com.sponia.foundationmoudle.utils
 * @description sp管理类
 * @date 15/9/9
 * @auther shibo
 */
public class SponiaSpUtil {

    private static SharedPreferences defaultSP = null;

    private static synchronized SharedPreferences getDefaultSpInstance() {
        if (null == defaultSP) {
            defaultSP = PreferenceManager
                    .getDefaultSharedPreferences(Common.application);
        }
        return defaultSP;
    }
    
    /**
     * 根据指定的SPName,获取特定的SP对象
     *
     * @param spName
     * @return
     */
    public static final SharedPreferences getSharedPreferences(String spName) {
        return Common.application.getSharedPreferences(spName,
                Context.MODE_PRIVATE);
    }

    /***
     * 在指定的SP文件里存储相关值
     *
     * @param sp
     *            SharedPreferences
     * @param key
     *            所存value值的key名
     * @param obj
     *            所存value值
     */
    public static final void setValue(SharedPreferences sp, String key,
                                      Object obj) {
        SharedPreferences.Editor editor = sp.edit();
        if (obj instanceof String) {
            editor.putString(key, (String) obj);
        } else if (obj instanceof Boolean) {
            editor.putBoolean(key, (Boolean) obj);
        } else if (obj instanceof Integer) {
            editor.putInt(key, (Integer) obj);
        } else if (obj instanceof Float) {
            editor.putFloat(key, (Float) obj);
        } else if (obj instanceof Long) {
            editor.putLong(key, (Long) obj);
        } else if (obj instanceof Set) {
            editor.putStringSet(key, (Set) obj);
        }
        editor.commit();
    }

    /***
     * 在指定的SP文件里存储多个值
     * @param sp SharedPreferences
     * @param key  键的数组
     * @param obj  值的数组
     * key的数组与obj数组中的值一一对应
     */
    public static final void setValue(SharedPreferences sp, String[] key,
                                      Object[] obj) {
        SharedPreferences.Editor editor = sp.edit();
        for (int i = 0; i < key.length; i++) {
            if (obj[i] instanceof String) {
                editor.putString(key[i], (String) obj[i]);
            } else if (obj[i] instanceof Boolean) {
                editor.putBoolean(key[i], (Boolean) obj[i]);
            } else if (obj[i] instanceof Integer) {
                editor.putInt(key[i], (Integer) obj[i]);
            } else if (obj[i] instanceof Float) {
                editor.putFloat(key[i], (Float) obj[i]);
            } else if (obj[i] instanceof Long) {
                editor.putLong(key[i], (Long) obj[i]);
            } else if (obj[i] instanceof Set) {
                editor.putStringSet(key[i], (Set) obj[i]);
            }
        }
        editor.commit();
    }

    /***
     * 在指定的SP文件里存储相关值
     *
     * @param spName
     *            sp的名字
     * @param key
     *            所存value值的key名
     * @param obj
     *            所存value值
     */
    public static final void setValue(String spName, String key, Object obj) {

        SharedPreferences.Editor editor = Common.application.getSharedPreferences(spName,
                Context.MODE_PRIVATE).edit();

        if (obj instanceof String) {
            editor.putString(key, (String) obj);
        } else if (obj instanceof Boolean) {
            editor.putBoolean(key, (Boolean) obj);
        } else if (obj instanceof Integer) {
            editor.putInt(key, (Integer) obj);
        } else if (obj instanceof Float) {
            editor.putFloat(key, (Float) obj);
        } else if (obj instanceof Long) {
            editor.putLong(key, (Long) obj);
        } else if (obj instanceof Set) {
            editor.putStringSet(key, (Set) obj);
        }
        editor.commit();
    }

    /***
     * 在指定的SP文件里存储多个值
     * @param spName
     * @param key  键的数组
     * @param obj  值的数组
     * key的数组与obj数组中的值一一对应
     */
    public static final void setValue(String spName, String[] key,
                                      Object[] obj) {
        SharedPreferences.Editor editor = Common.application.getSharedPreferences(spName,
                Context.MODE_PRIVATE).edit();
        for (int i = 0; i < key.length; i++) {
            if (obj[i] instanceof String) {
                editor.putString(key[i], (String) obj[i]);
            } else if (obj[i] instanceof Boolean) {
                editor.putBoolean(key[i], (Boolean) obj[i]);
            } else if (obj[i] instanceof Integer) {
                editor.putInt(key[i], (Integer) obj[i]);
            } else if (obj[i] instanceof Float) {
                editor.putFloat(key[i], (Float) obj[i]);
            } else if (obj[i] instanceof Long) {
                editor.putLong(key[i], (Long) obj[i]);
            } else if (obj[i] instanceof Set) {
                editor.putStringSet(key[i], (Set) obj[i]);
            }
        }
        editor.commit();
    }

    /***
     * 在指定的SP文件里根据key获取相关值
     *
     * @param spName
     *            sp的名字
     * @param key
     *            所取value值的key名
     * @return Object 所取的value值
     */
    public static final Object getValue(String spName, String key) {
        SharedPreferences sp = Common.application.getSharedPreferences(spName,
                Context.MODE_PRIVATE);
        return sp.getAll().get(key);
    }

    /***
     * 在指定的SP文件里根据key获取相关值
     *
     * @param sp
     *            SharedPreferences
     * @param key
     *            SharedPreferences
     * @return
     */
    public static final Object getValue(SharedPreferences sp, String key) {
        return sp.getAll().get(key);
    }

    /***
     * 在指定的SP文件里根据key获取相关String值
     *
     * @param spName
     *            sp的名字
     * @param key
     *            所取value值的key名
     * @param defaultValue
     *            默认值
     * @return
     */
    public static final String getValue(String spName, String key,
                                        String defaultValue) {
        SharedPreferences sp = Common.application.getSharedPreferences(spName,
                Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    /***
     * 在指定的SP文件里根据key获取相关String值
     *
     * @param sp
     * @param key
     * @param defaultValue
     * @return
     */
    public static final String getValue(SharedPreferences sp, String key,
                                        String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    /***
     * 在指定的SP文件里根据key获取相关Boolean值
     *
     * @param spName
     *            sp的名字
     * @param key
     *            所取value值的key名
     * @param defaultValue
     *            默认值
     * @return
     */
    public static final Boolean getValue(String spName, String key,
                                         Boolean defaultValue) {
        SharedPreferences sp = Common.application.getSharedPreferences(spName,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    /***
     * 在指定的SP文件里根据key获取相关Boolean值
     *
     * @param sp
     * @param key
     * @param defaultValue
     * @return
     */
    public static final Boolean getValue(SharedPreferences sp, String key,
                                         Boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    /***
     * 在指定的SP文件里根据key获取相关Integer值
     *
     * @param spName
     *            sp的名字
     * @param key
     *            所取value值的key名
     * @param defaultValue
     *            默认值
     * @return
     */
    public static final Integer getValue(String spName, String key,
                                         int defaultValue) {
        SharedPreferences sp = Common.application.getSharedPreferences(spName,
                Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    /***
     * 在指定的SP文件里根据key获取相关Integer值
     *
     * @param sp
     * @param key
     * @param defaultValue
     * @return
     */
    public static final Integer getValue(SharedPreferences sp, String key,
                                         int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    /***
     * 在指定的SP文件里根据key获取相关Float值
     *
     * @param spName
     *            sp的名字
     * @param key
     *            所取value值的key名
     * @param defaultValue
     *            默认值
     * @return
     */
    public static final Float getValue(String spName, String key,
                                       Float defaultValue) {
        SharedPreferences sp = Common.application.getSharedPreferences(spName,
                Context.MODE_PRIVATE);
        return sp.getFloat(key, defaultValue);
    }

    /***
     * 在指定的SP文件里根据key获取相关Float值
     *
     * @param sp
     * @param key
     * @param defaultValue
     * @return
     */
    public static final Float getValue(SharedPreferences sp, String key,
                                       Float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    /***
     * 在指定的SP文件里根据key获取相关Long值
     *
     * @param spName
     *            sp的名字
     * @param key
     *            所取value值的key名
     * @param defaultValue
     *            默认值
     * @return
     */
    public static final Long getValue(String spName, String key,
                                      Long defaultValue) {
        SharedPreferences sp = Common.application.getSharedPreferences(spName,
                Context.MODE_PRIVATE);
        return sp.getLong(key, defaultValue);
    }

    /***
     * 在指定的SP文件里根据key获取相关Long值
     *
     * @param sp
     * @param key
     * @param defaultValue
     * @return
     */
    public static final Long getValue(SharedPreferences sp, String key,
                                      Long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    /***
     *
     * @param key
     *            所取value值的key名
     * @param obj
     *            所存value值
     */
    public static final void setDefaultSpValue(String key, Object obj) {
        setValue(getDefaultSpInstance(), key, obj);
    }

    /***
     * 在默认的SP文件里根据key获取相关值
     *
     * @param key
     *            所取value值的key名
     * @return Object 所取的value值
     */
    public static final Object getDefaultSpValue(String key) {
        return getValue(getDefaultSpInstance(), key);
    }

    /***
     * 在默认的SP文件里根据key获取相关String值
     *
     * @param key
     *            所取value值的key名
     * @param defaultValue
     *            默认值
     * @return
     */
    public static final String getDefaultSpValue(String key, String defaultValue) {
        return getValue(getDefaultSpInstance(), key, defaultValue);
    }

    /***
     * 在默认的SP文件里根据key获取相关Boolean值
     *
     * @param key
     *            所取value值的key名
     * @param defaultValue
     *            默认值
     * @return
     */
    public static final Boolean getDefaultSpValue(String key,
                                                  Boolean defaultValue) {
        return getValue(getDefaultSpInstance(), key, defaultValue);
    }

    /***
     * 在默认的SP文件里根据key获取相关Integer值
     *
     * @param key
     *            所取value值的key名
     * @param defaultValue
     *            默认值
     * @return
     */
    public static final Integer getDefaultSpValue(String key, int defaultValue) {
        return getValue(getDefaultSpInstance(), key, defaultValue);
    }

    /***
     * 在默认的SP文件里根据key获取相关Float值
     *
     * @param key
     *            所取value值的key名
     * @param defaultValue
     *            默认值
     * @return
     */
    public static final Float getDefaultSpValue(String key, Float defaultValue) {
        return getValue(getDefaultSpInstance(), key, defaultValue);
    }

    /***
     * 在指定的SP文件里根据key获取相关Long值
     *
     * @param key
     *            所取value值的key名
     * @param defaultValue
     *            默认值
     * @return
     */
    public static final Long getDefaultSpValue(String key, Long defaultValue) {
        return getValue(getDefaultSpInstance(), key, defaultValue);
    }

    /**
     *
     * @param path
     * @param bmp
     * @param desy
     * @return
     */
    public static long saveUploadImage(String path, Bitmap bmp, int desy) {
        if (bmp == null)
            return 0;
        File f = new File(path);
        FileOutputStream fOut = null;
        try {
            if (f.exists())
                f.delete();
            f.createNewFile();
            fOut = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.JPEG, desy, fOut);
            LogUtil.defaultLog("length : " + f.length()
                    + ", path :" + path);
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (fOut != null) {
                try {
                    fOut.flush();
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return f.length();
    }
}
