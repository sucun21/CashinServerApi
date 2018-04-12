/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.mc.controller;

import com.gbc.mc.common.AppConst;
import com.gbc.mc.common.CommonModel;
import com.gbc.mc.common.JsonParserUtil;
import com.gbc.mc.model.ZaloPayUserModel;
import com.google.gson.JsonObject;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author tamvh
 */
public class ZaloPayUserController extends HttpServlet{
    protected final Logger logger = Logger.getLogger(this.getClass());
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req, resp);
    }
    
    private void handle(HttpServletRequest req, HttpServletResponse resp) {
        try {
            processs(req, resp);
        } catch (IOException ex) {
            logger.error(getClass().getSimpleName() + ".handle: " + ex.getMessage(), ex);
        }
    }

    private void processs(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = (req.getPathInfo() != null) ? req.getPathInfo() : "";
        String cmd = req.getParameter("cm") != null ? req.getParameter("cm") : "";
        String data = req.getParameter("dt") != null ? req.getParameter("dt") : "";
        String content = "";
        logger.info("ZaloPayUserController.processs, pathInfo:  " + pathInfo);
        logger.info("ZaloPayUserController.processs, cmd:       " + cmd);
        logger.info("ZaloPayUserController.processs, data:      " + data);
        CommonModel.prepareHeader(resp, CommonModel.HEADER_JS);
        switch (cmd) {            
            case "getzalopayuserinfobyzpid":
                content = getZaloPayUserInfoByZpId(req, data);
                break;
            case "getzalopayuserinfobyphone":
                content = getZaloPayUserInfoByPhone(req, data);
                break;
            case "checkuserexist":
                content=checkUserExist(req,data);
                break;
        }
        CommonModel.out(content, resp);
    }

    private String getZaloPayUserInfoByZpId(HttpServletRequest req, String data) {
        String content = null;
        String zpid=null;
        int ret = AppConst.ERROR_GENERIC;
        try {
            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {
                if(jsonObject.has("zpid")){
                    zpid = jsonObject.get("zpid").getAsString();
                    if (zpid.isEmpty()) {
                        content = CommonModel.FormatResponse(ret, "Invalid parameter");
                    } else {
                    content = ZaloPayUserModel.getInstance().getUserInfoByZpId(zpid);
                    }
                }else{
                    content = CommonModel.FormatResponse(ret, "Wrong params");
                }
                
            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".getZaloPayUserInfoByZpId: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }
        return content;
    }

    private String getZaloPayUserInfoByPhone(HttpServletRequest req, String data) {
        String content = null;
        int ret = AppConst.ERROR_GENERIC;
        try {
            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {                
                String phone_number = jsonObject.get("phone_number").getAsString();
                if (phone_number.isEmpty()) {
                    content = CommonModel.FormatResponse(ret, "Invalid parameter");
                } else {
                    content = ZaloPayUserModel.getInstance().getUserInfoByPhone(phone_number);
                }
            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".getZaloPayUserInfoByPhone: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }
        return content;
    }
    
    private String checkUserExist(HttpServletRequest req, String data) {
        String content = null;
        String user=null;
        int ret = AppConst.ERROR_GENERIC;
        try {
            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {
                if(jsonObject.has("user")){
                    user = jsonObject.get("user").getAsString();
                    if (user.isEmpty()) {
                        content = CommonModel.FormatResponse(ret, "Invalid parameter");
                    } else {
                    content = ZaloPayUserModel.getInstance().checkUserExist(user);
                    }
                }else{
                    content = CommonModel.FormatResponse(ret, "Wrong params");
                }
                
            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".checkUserExist: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }
        return content;
    }
}
