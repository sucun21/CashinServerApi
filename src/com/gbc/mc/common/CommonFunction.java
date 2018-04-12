/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.mc.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Key;
import java.util.concurrent.atomic.AtomicLong;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import com.gbc.mc.model.InvoiceModel;
import java.util.logging.Level;

/**
 *
 * @author diepth
 */
public class CommonFunction {

    protected static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CommonFunction.class);
    private static String _currentDayFormat = "";
    public static final String KEY_TOKEN_LOGIN = "token_login";
    public static final String KEY_USERNAME = "username";
    private static final String ALGORITHM = "AES";
    private static final String secretkey = "123sasr52423gsf@";

    public static String getCurrentDateTimeString() {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fmt.setCalendar(cal);
        String currDateTime = fmt.format(cal.getTimeInMillis());

        return currDateTime;
    }

    public static long getCurrentDateTimeNum() {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        return cal.getTimeInMillis();
    }

    public static String getCurrentDayFormat() {
        //if (_currentDayFormat.isEmpty()) {
        long currentTime = getCurrentDateTimeNum();
        SimpleDateFormat fmt = new SimpleDateFormat("yyMMdd");
        _currentDayFormat = fmt.format(currentTime);
        //}

        return _currentDayFormat;
    }

    public static Map<String, String> convertQueryMap(Map<String, String[]> paramMap) {
        Map<String, String> tempMap = new HashMap<>();
        paramMap.forEach((key, values) -> {
            if (values.length == 0) {
                tempMap.put(key, "");
            } else {
                tempMap.put(key, values[0]);
            }
        });
        return tempMap;
    }

    public static void deleteSession(HttpServletRequest req) {

        try {
            HttpSession session = req.getSession(true);
            String sid = (String) session.getAttribute(KEY_TOKEN_LOGIN);
            if (sid != null) {
                session.removeAttribute(sid);
            }

            session.removeAttribute(KEY_TOKEN_LOGIN);
        } catch (Exception ex) {
            logger.info("Ex deleteSession: " + ex.toString());
        }
    }

    public static String getUserSession(HttpServletRequest req) {

        String ret = null;
        try {
            HttpSession session = req.getSession(true);
            String sid = (String) session.getAttribute(KEY_TOKEN_LOGIN);
            if (sid != null) {
                String usernameSession = (String) session.getAttribute(sid);
                if (usernameSession != null) {
                    ret = usernameSession;
                }
            }
        } catch (Exception ex) {

        }

        return ret;
    }

    public static void setSession(HttpServletRequest req, String username, String sid) {

        try {
            HttpSession session = req.getSession(true);
            session.setAttribute(KEY_TOKEN_LOGIN, sid);
            session.setAttribute(sid, username);
        } catch (Exception ex) {

        }
    }

    public static String toMD5(String data) {

        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(data.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getStringCurrentTimeMillis() {

        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        return Long.toString(cal.getTimeInMillis());
    }

    public static long getCurrentTimeMillis() {

        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        return cal.getTimeInMillis();
    }

    public static long getTimeMillis(String strDateTime) {

        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fmt.setCalendar(cal);

        try {
            Date date = fmt.parse(strDateTime);
            return date.getTime();
        } catch (Exception ex) {

        }

        return 0;
    }

    public static Boolean checkSession(HttpServletRequest req) {

        Boolean ret = false;
        try {
            HttpSession session = req.getSession(true);
            String sid = (String) session.getAttribute(KEY_TOKEN_LOGIN);
            if (sid != null) {
                //String username = (String)session.getAttribute(sid);
                ret = true;
            }
        } catch (Exception ex) {

        }

        return ret;
    }

    public static String encode(String valueToEnc) throws Exception {
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encValue = c.doFinal(valueToEnc.getBytes());
            byte[] encodedBytes = Base64.encodeBase64URLSafe(encValue);
            String encryptedValue = new String(encodedBytes);
            return encryptedValue;
        } catch (Exception e) {
        }
        return null;
    }

    public static String decode(String encryptedValue) throws Exception {
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, key);

            byte[] decordedValue = Base64.decodeBase64(encryptedValue.getBytes());
            byte[] decValue = c.doFinal(decordedValue);
            String decryptedValue = new String(decValue);
            return decryptedValue;
        } catch (Exception e) {
        }
        return null;
    }

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(secretkey.getBytes(), ALGORITHM);
        return key;
    }
}
