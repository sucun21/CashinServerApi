/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.mc.common;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author diepth
 */
public class JsonParserUtil {
    public static String parseStringValue(String data, String elementName) {
        try { 
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonEle = jsonParser.parse(data);
            return jsonEle.getAsJsonObject()
                .get(elementName)
                .getAsString();
        } catch (Exception ex) {
            
        }
        return null;
    }
    
    public static String parseStringValue(String data, String objectName, String elementName) {
        try { 
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonEle = jsonParser.parse(data);
            return jsonEle.getAsJsonObject()
                    .getAsJsonObject(objectName)
                    .get(elementName)
                    .getAsString();
        } catch (Exception ex) {
            
        }
        
        return null;
    }
    
    public static long parseLongValue(String data, String elementName) {
        try { 
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonEle = jsonParser.parse(data);
            return jsonEle.getAsJsonObject()
                .get(elementName)
                .getAsLong();
        } catch (Exception ex) {
            
        }
        return 0;
    }
    
    public static long parseLongValue(String data, String objectName, String elementName) {
        try { 
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonEle = jsonParser.parse(data);
            return jsonEle.getAsJsonObject()
                    .getAsJsonObject(objectName)
                    .get(elementName)
                    .getAsLong();
        } catch (Exception ex) {
            
        }
        
        return 0;
    }
    
    public static int parseIntValue(String data, String elementName) {
        try { 
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonEle = jsonParser.parse(data);
            return jsonEle.getAsJsonObject()
                .get(elementName)
                .getAsInt();
        } catch (Exception ex) {
            
        }
        return -1;
    }
    
    public static JsonObject parseJsonObject(String data) {
        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonEle = jsonParser.parse(data);
            return (jsonEle.isJsonObject())?jsonEle.getAsJsonObject():null;
        } catch (Exception ex) {
            
        }
        return null;
    }
    
    public static JsonObject parseJsonObject(String data, String objectName) {
        try { 
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonEle = jsonParser.parse(data);
            return jsonEle.getAsJsonObject().getAsJsonObject(objectName);
        } catch (Exception ex) {
            
        }
        
        return null;
    }
}

