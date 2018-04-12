/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.mc.data;

/**
 *
 * @author tamvh
 */
public class Invoice {
    private String invoice_code;
    private long invoice_index;
    private String amount;
    private String transfer_type;
    private String machine_name;
    private String date_order;
    private String reciever;
    private String zptransid;

    public String getZptransid() {
        return zptransid;
    }

    public void setZptransid(String zptransid) {
        this.zptransid = zptransid;
    }
    
    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getMachine_name() {
        return machine_name;
    }

    public void setMachine_name(String machine_name) {
        this.machine_name = machine_name;
    }

    public String getInvoice_code() {
        return invoice_code;
    }

    public void setInvoice_code(String invoice_code) {
        this.invoice_code = invoice_code;
    }

    public long getInvoice_index() {
        return invoice_index;
    }

    public void setInvoice_index(long invoice_index) {
        this.invoice_index = invoice_index;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransfer_type() {
        return transfer_type;
    }

    public void setTransfer_type(String transfer_type) {
        this.transfer_type = transfer_type;
    }

    public String getDate_order() {
        return date_order;
    }

    public void setDate_order(String date_order) {
        this.date_order = date_order;
    }
}
