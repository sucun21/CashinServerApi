/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.mc.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

/**
 *
 * @author tamvh
 */
public class LocalServiceAPI {
    private static final Logger logger = Logger.getLogger(LocalServiceAPI.class);
    private static final Gson _gson = new Gson();
    private static final String _ppServiceURL = Config.getParam("passport_service", "uri");
    private static final String _pid = Config.getParam("passport_service", "product_id");
    private static final String _apikey = Config.getParam("passport_service", "api_key");
    private static final String _privatekey = Config.getParam("passport_service", "private_key");
    
    private static final String _vposServiceURL = Config.getParam("vpos_service", "uri");
    
    private static String sendHttpPost(String url, List<NameValuePair> urlParameters) {
        
        try {
            
            String proxyHost = System.getProperty("http.proxyHost");
            String proxyPort = System.getProperty("http.proxyPort");
            
            HttpClientBuilder httpBuilder = HttpClientBuilder.create();
            
            if (proxyHost != null || proxyPort != null) {
                int port = Integer.parseInt(proxyPort);
                HttpHost proxy = new HttpHost(proxyHost, port);
                httpBuilder.setProxy(proxy);
            }
            
            HttpClient client = httpBuilder.build();
            HttpPost post = new HttpPost(url);
            post.setHeader("User-Agent", "VPOS Backend");
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse response = client.execute(post);
            
            BufferedReader rd = new BufferedReader(
	        new InputStreamReader(response.getEntity().getContent()));

            StringBuilder result = new StringBuilder();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            
            return result.toString();
            
        } catch (UnsupportedEncodingException ex) {
            logger.error("LocalServiceAPI.sendHttpPost: " + ex.getMessage(), ex);
        } catch (IOException ex) {
            logger.error("LocalServiceAPI.sendHttpPost: " + ex.getMessage(), ex);
        }
        
        return "";
    }
    
    public static String sendPPLogin(String account, String password) {
         
        List<NameValuePair> urlParams = new ArrayList<>();
        String t = Long.toString(CommonFunction.getCurrentTimeMillis()/1000);
        String s = RandomStringUtils.randomAlphanumeric(17);
        String c = CommonFunction.toMD5 (_privatekey + account + password + _apikey + t + s);
        
        urlParams.add(new BasicNameValuePair("longtime", "1"));
        urlParams.add(new BasicNameValuePair("acn", account));
        urlParams.add(new BasicNameValuePair("pwd", password));
        urlParams.add(new BasicNameValuePair("apikey", _apikey));
        urlParams.add(new BasicNameValuePair("pid", _pid));
        urlParams.add(new BasicNameValuePair("s", s));
        urlParams.add(new BasicNameValuePair("t", t));
        urlParams.add(new BasicNameValuePair("c", c));

        return sendHttpPost(_ppServiceURL, urlParams);
    }
    
    public static String sendVPOSRefreshItem(String merchantCode, long itemId) {
         
        JsonObject jsonData = new JsonObject();
        jsonData.addProperty("merchant_code", merchantCode);
        jsonData.addProperty("item_id", itemId);
        
        List<NameValuePair> urlParams = new ArrayList<>();
        urlParams.add(new BasicNameValuePair("cm", "refresh_item"));
        urlParams.add(new BasicNameValuePair("dt", _gson.toJson(jsonData)));
        String url = _vposServiceURL + "vpos/api/common/";
        return sendHttpPost(url, urlParams);
    }
    
    public static String sendVPOSRefundInvoice(String merchantCode, String invoiceCode) {
         
        JsonObject jsonData = new JsonObject();
        jsonData.addProperty("merchant_code", merchantCode);
        jsonData.addProperty("invoice_code", invoiceCode);
        
        List<NameValuePair> urlParams = new ArrayList<>();
        urlParams.add(new BasicNameValuePair("cm", "refund_invoice"));
        urlParams.add(new BasicNameValuePair("dt", _gson.toJson(jsonData)));
        String url = _vposServiceURL + "vpos/api/common/";
        return sendHttpPost(url, urlParams);
    }
}
