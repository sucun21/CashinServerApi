/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.mc.common;

import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 *
 * @author haint3
 */
public class HttpHelper {

    private static final Gson _gson = new GsonBuilder().create();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType FORM_URL_ENCODED = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    
    public static String sendHttpPost(String url, RequestBody rbody, int timeout) throws IOException {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        if (timeout > 0) {
            clientBuilder.connectTimeout(timeout, TimeUnit.MILLISECONDS);
            clientBuilder.writeTimeout(timeout, TimeUnit.MILLISECONDS);
            clientBuilder.readTimeout(timeout, TimeUnit.MILLISECONDS);
        }
        
        OkHttpClient client = clientBuilder.build();
        Request request = new Request.Builder()
                    .url(url)
                    .post(rbody)
                    .build();
        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        System.out.println("Finish");
        return body.string();
    }
    
    public static String sendHttpPostJson(String url, String postBody, int timeout) throws IOException {
        RequestBody rbody = RequestBody.create(JSON, postBody);
        return sendHttpPost(url, rbody, timeout);
    }
    
    public static String sendHttpPostJson(String url, JsonObject postBodyObject, int timeout) throws IOException {
        RequestBody rbody = RequestBody.create(JSON, _gson.toJson(postBodyObject));
        return sendHttpPost(url, rbody, timeout);
    }
    
    public static String sendHttpPostFormData(String url, String postBody, int timeout) throws IOException {
        RequestBody rbody = RequestBody.create(FORM_URL_ENCODED, postBody);
        return sendHttpPost(url, rbody, timeout);
    }
    
    public static String sendHttpPostFormData(String url, Map<String, String> postBodyMap, int timeout) throws IOException {
        System.out.println("Begin");
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        postBodyMap.forEach( (key, value) -> {
            formBodyBuilder.add(key, value);
        });
        
        return sendHttpPost(url, formBodyBuilder.build(), timeout);
    }
    
    public static String sendHttpPostFormData(String url, JsonObject postBodyObject, int timeout) throws IOException {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        postBodyObject.entrySet().forEach( entry -> {
            formBodyBuilder.add(entry.getKey(), entry.getValue().getAsString());
        });
        
        return sendHttpPost(url, formBodyBuilder.build(), timeout);
    }
    
    public static Map<String, String> parseQueryString(String queryString) {
        return Splitter
                .on("&")
                .withKeyValueSeparator("=")
                .split(queryString);
    }
}
