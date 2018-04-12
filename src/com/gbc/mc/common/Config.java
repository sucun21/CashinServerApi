/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.mc.common;

import com.vng.csm.configuration.ConfigException;
import com.vng.csm.configuration.Configuration;
import com.vng.csm.configuration.INIFileConfiguration;

/**
 *
 * @author diepth
 */
public class Config {
    
     private static Configuration conf = null;
    
    public static void init(String file) throws ConfigException {
        String confFile = System.getProperty("configuration");
        if (confFile == null) {
                confFile = file;
        }
        conf = new INIFileConfiguration(confFile);
    }
    
    public static String getParam(String section, String key) {
        return conf.getConfig(section, key);
    }
}
