package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import baidubean.BaiduHistoryBean;

/**
 * Created by jrm on 2017-4-24.
 */

public class SharePresUtils {

    private static final String HISTORY_NAME ="recent_app_Infos";
    private static final String HISTORY_VALUE ="mihome_baidu_search_history";

    private static final String CHANNLE_KEY ="channle_key";
    private static final String CHANNLE_VALUE ="channle_value";
    /**
     * 存储和得到搜索历史记录
     * @param context
     * @return
     */
    public static <T> List<T> getSharePrefData(Context context,Class<T> cls){
        List<T> datalist=new ArrayList<T>();
        if (context == null) return null;
        SharedPreferences sp = context.getSharedPreferences(HISTORY_NAME, Context.MODE_PRIVATE);
        String strJson = sp.getString(HISTORY_VALUE, null);
        if (null == strJson) {
            return datalist;
        }
        datalist = SharePresUtils.fromJsonArray(strJson, cls); //解决泛型擦除的问题
        return datalist;
    }

    public static <T> void setSharePrefData(Context context,List<T> datalist) {
        if (context == null || null == datalist || datalist.size() <= 0) return;
        SharedPreferences sp = context.getSharedPreferences(HISTORY_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.clear();
        editor.putString(HISTORY_VALUE, strJson);
        editor.commit();
    }

    public static <T> List<T> fromJsonArray(String json, Class<T> clazz) {
        try {
            List<T> lst =  new ArrayList<T>();
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for(final JsonElement elem : array){
                lst.add(new Gson().fromJson(elem, clazz));
            }
            return lst;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }
    /**
     * 清除存入的数据
     */
    public static void clearShareValues(Context mContext){
        SharedPreferences sp = mContext.getSharedPreferences(HISTORY_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.commit();
    }

    /**
     * 存入key值
     */
    public static void setChannleKeyValue(Context mContext,String channleKey){
        if (mContext == null ) return;
        SharedPreferences sp = mContext.getSharedPreferences(CHANNLE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(CHANNLE_VALUE, channleKey);
        editor.commit();
    }
    /**
     * 取出key值
     */
    public static String getChannleKeyValue(Context mContext){
        if (mContext == null) return null;
        SharedPreferences sp = mContext.getSharedPreferences(CHANNLE_KEY, Context.MODE_PRIVATE);
        String str = sp.getString(CHANNLE_VALUE, null);
        if (str != null){
            return  str;
        }
        return  null;
    }
    /**
     * 清除key
     */
    public static void clearChannleKeyValue(Context mContext){
        SharedPreferences sp = mContext.getSharedPreferences(CHANNLE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.commit();
    }
}
