/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.mc.model;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import com.gbc.mc.common.AppConst;
import com.gbc.mc.common.CommonFunction;
import com.gbc.mc.common.CommonModel;
import com.gbc.mc.common.Config;
import com.gbc.mc.common.HttpHelper;
import com.gbc.mc.common.JsonParserUtil;
import com.gbc.mc.hmacutil.HMACUtil;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

/**
 *
 * @author tamvh
 */
public class CashInModel {

    private static CashInModel _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected static final Logger logger = Logger.getLogger(CashInModel.class);
    private static final Gson gson = new Gson();
    private static String serverUrl = Config.getParam("zalopayserver", "url");

    public static CashInModel getInstance() throws IOException {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new CashInModel();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }

    public String transferCashByType(String reciever, String amout, String transfer_type, String invoice_code) {
        String content;
        int ret = AppConst.ERROR_GENERIC;
        int retCode=0;
        Timer timer =new Timer();
        try {
            long currentTime = CommonFunction.getCurrentDateTimeNum();
            String mid = AppConst.MID;
            String mtransid = CommonFunction.getCurrentDayFormat() + "_" + mid + "_" + invoice_code;
            String mzalopayid = AppConst.M_ZALOPAY_ID;
            String mzalopaypin = AppConst.M_ZALOPAY_P_IN;
            String receiver = reciever;
            String amount = amout;
            String reqdate = String.valueOf(currentTime);
            String description = AppConst.TRANSFER_DESC;
            String hmac = HMACUtil.HMacHexStringEncode("HmacSHA256", AppConst.HMAC_SHA256_KEY,
                    mtransid + "|"
                    + mid + "|"
                    + mzalopayid + "|"
                    + mzalopaypin + "|"
                    + receiver + "|"
                    + amount + "|"
                    + reqdate + "|"
                    + description);
            
            Map<String, String> params = new HashMap<String, String>();
            params.put("mtransid", mtransid);
            params.put("mid", mid);
            params.put("mzalopayid", mzalopayid);
            params.put("mzalopaypin", mzalopaypin);
            params.put("type", transfer_type);
            params.put("receiver", receiver);
            params.put("amount", amount);
            params.put("reqdate", reqdate);
            params.put("description", description);
            params.put("mac", hmac);
            logger.info(params);
            String transerByTypeURL = serverUrl + "transfercashbytype";
            logger.info("url: " +transerByTypeURL);
            String rs = HttpHelper.sendHttpPostFormData(transerByTypeURL, params, 10000);
            JsonObject jsonObject = JsonParserUtil.parseJsonObject(rs);
            logger.info(rs);
            content = rs;
            String zpTransId="";
            boolean isprocessing=false;
            if(jsonObject.has("zptransid")){
               zpTransId =jsonObject.get("zptransid").getAsString();
            }
            if(jsonObject.has("isprocessing")){
                isprocessing=jsonObject.get("isprocessing").getAsBoolean();
            }
            retCode=jsonObject.get("returncode").getAsInt();
            //check isprocessing=true
            if(isprocessing==true){
                long startTime = System.currentTimeMillis();
                long elapsedTime = 0L;
                while (elapsedTime < 30*60*1000) {
                    if(elapsedTime%(1*60*1000)==0){
                        String rs1=getTransferStatus(zpTransId);
                        JsonObject jsonStatus = JsonParserUtil.parseJsonObject(rs1);
                        content=rs1;
                        logger.info("Get status transId :"+zpTransId);
                        logger.info("content :" +content);
                        isprocessing=jsonStatus.get("isprocessing").getAsBoolean();
                        retCode=jsonStatus.get("returncode").getAsInt();
                    }
                    if(isprocessing==false){
                        elapsedTime=30*60*1000;
                    }else{
                        elapsedTime = (new Date()).getTime() - startTime;
                    }
                    
                }
            }
            InvoiceModel.getInstance().updateInvoiceStatus(invoice_code, zpTransId,retCode);
            
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(CashInModel.class.getName()).log(Level.SEVERE, null, ex);
            content = CommonModel.FormatResponseFromZp(ret, ex.getMessage());
        }
        return content;
    }
    
    public String getTransferStatus(String zptransid) {
        String content;
        int ret = AppConst.ERROR_GENERIC;
        try {
            long currentTime = CommonFunction.getCurrentDateTimeNum();
            String mid = AppConst.MID;
            String mzalopayid = AppConst.M_ZALOPAY_ID;
            String reqdate = String.valueOf(currentTime);
            String hmac = HMACUtil.HMacHexStringEncode("HmacSHA256", AppConst.HMAC_SHA256_KEY,
                            mid + "|" +
                            mzalopayid + "|" +
                            zptransid + "|" +
                            reqdate);
            
            Map<String, String> params = new HashMap<String, String>();
            params.put("mid", mid);
            params.put("mzalopayid", mzalopayid);
            params.put("zptransid", zptransid);
            params.put("time", reqdate);
            params.put("mac", hmac);
            String getStatusUrl = serverUrl + "gettransferstatus";
            String rs = HttpHelper.sendHttpPostFormData(getStatusUrl, params, 10000);
            content = rs;
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(CashInModel.class.getName()).log(Level.SEVERE, null, ex);
            content = CommonModel.FormatResponseFromZp(ret, ex.getMessage());
        }
        return content;
    }
    public String getBalance(String mid,String mzalopayid) {
        String content;
        int ret = AppConst.ERROR_GENERIC;
        try {
            long currentTime = CommonFunction.getCurrentDateTimeNum();
            if(mid.isEmpty()){
                mid = AppConst.MID;
            }
            if(mzalopayid.isEmpty()){
                mzalopayid = AppConst.M_ZALOPAY_ID;
            }
            String reqdate = String.valueOf(currentTime);
            String hmac = HMACUtil.HMacHexStringEncode("HmacSHA256", AppConst.HMAC_SHA256_KEY,
                            mid + "|" +
                            mzalopayid + "|" +
                            reqdate);
            
            Map<String, String> params = new HashMap<String, String>();
            params.put("mid", mid);
            params.put("mzalopayid", mzalopayid);
            params.put("time", reqdate);
            params.put("mac", hmac);
            logger.info(params);
            String getStatusUrl = serverUrl + "getbalance";
            String rs = HttpHelper.sendHttpPostFormData(getStatusUrl, params, 10000);
            content = rs;
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(CashInModel.class.getName()).log(Level.SEVERE, null, ex);
            content = CommonModel.FormatResponseFromZp(ret, ex.getMessage());
        }
        return content;
    }
    
}
