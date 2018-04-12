package com.zyh.demo.http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author 占迎辉 (zhanyinghui@parkingwang.com)
 * @version 2018/4/9
 */

public class NonNullJson {
    public static String getNonNullString(JSONObject json, String key) {
        final String value = json.getString(key);
        return value == null ? "" : value;
    }

    public static JSONArray getNonNullJsonArray(JSONObject json, String key) {
        final JSONArray list = json.getJSONArray(key);
        return list == null ? new JSONArray(0) : list;
    }
}
