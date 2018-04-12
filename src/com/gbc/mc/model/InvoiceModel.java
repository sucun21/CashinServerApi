/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.mc.model;

import com.gbc.mc.common.CommonFunction;
import com.gbc.mc.database.MySqlFactory;
import com.gbc.mc.data.Invoice;
import com.google.gson.Gson;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author tamvh
 */
public class InvoiceModel {
    private static InvoiceModel _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected static final Logger logger = Logger.getLogger(InvoiceModel.class);
    private static final Gson gson = new Gson();

    public static InvoiceModel getInstance() throws IOException {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new InvoiceModel();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }
    
    public long getLastInvoiceIndex(String curr_date) {
        long lastInvoiceIndex = 0;
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String queryStr;
            String tableName = "tb_invoice";
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();

            queryStr = String.format("SELECT `invoice_index` FROM %1$s"
                    + " WHERE `date_order` = '%2$s' ORDER BY `invoice_index` DESC LIMIT 1",
                    tableName, curr_date);
            System.out.println("Query getLastInvoiceCode: " + queryStr);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();
            if (rs != null) {
                if (rs.next()) {
                    lastInvoiceIndex = rs.getLong("invoice_index");
                }
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(InvoiceModel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return lastInvoiceIndex;
    }
    
    public int insertInvoice(Invoice invoice) {
        int ret = -1;
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String queryStr;
            String tableName = "tb_invoice";
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            queryStr = String.format("INSERT INTO %1$s (invoice_code, invoice_index, amount, reciever, transfer_type, machine_name, date_order) VALUES ('%2$s', %3$d, '%4$s', '%5$s', '%6$s', '%7$s', '%8$s')",
                    tableName, invoice.getInvoice_code(), invoice.getInvoice_index(), invoice.getAmount(), invoice.getReciever(), invoice.getTransfer_type(), invoice.getMachine_name(), invoice.getDate_order());
            System.out.println("Query insertInvoice: " + queryStr);
            int result = stmt.executeUpdate(queryStr);
            if(result > 0) {
                ret = 0;
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(InvoiceModel.class.getName()).log(Level.SEVERE, null, ex);
            ret = -1;
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    }
    
    public int generateInvoiceCode(Invoice invoice) {
        int ret = -1;
        long _index = 0;
        try {
            Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
            long time = cal.getTimeInMillis();
            SimpleDateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd");
            fmt1.setCalendar(cal);
            String currDate = fmt1.format(time);
            invoice.setDate_order(currDate);

            _index = InvoiceModel.getInstance().getLastInvoiceIndex(currDate);
            SimpleDateFormat fmt = new SimpleDateFormat("yyMMdd");
            fmt.setCalendar(cal);
            String preCode = fmt.format(time);

            String sufCode;
            _index = _index + 1;
            long invoice_index = _index;
            invoice.setInvoice_index(invoice_index);
            sufCode = String.format("%04d", invoice_index);
            String invoice_code = preCode + sufCode;
            invoice.setInvoice_code(invoice_code);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(CommonFunction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }
    public void updateInvoiceStatus(String invoice_code,String zpTransId,int retCode){
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String queryStr;
            String tableName1 = "tb_invoice";
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            if(retCode<0){
                retCode=0;
            }
            queryStr = String.format("UPDATE %1$s"
                    + " SET status= '%2$d',"
                    + " zptransid='%3$s'"
                    + " where invoice_code='%4$s' ",tableName1,retCode,zpTransId,invoice_code);
            logger.info("Query getSummaryInvoice: " + queryStr);
            stmt.execute(queryStr);
            int count=stmt.getUpdateCount();
            if(count>0){
                logger.info("update invoice status successfully !!!");
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(InvoiceModel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
    }
}
