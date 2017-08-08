package com.marsXTU.music.callback;

import com.alibaba.fastjson.JSONObject;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * 封装Json
 * Created by whd on 2016/3/10.
 */
public abstract class JsonCallback<T> extends Callback<T> {
    private Class<T> mClass;

    public JsonCallback(Class<T> clazz) {
        this.mClass = clazz;
    }

    @Override
    public T parseNetworkResponse(Response response) throws IOException {
        try {
            String jsonString = response.body().string();
            return JSONObject.parseObject(jsonString, mClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
