/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.mc.common;

/**
 *
 * @author tamvh
 */
public class MessageType {
    public static final int MSG_REQUEST = 0; // message type request from client follow range 0-1000
    public static final int MSG_RESPONSE = 1000; // message type response from server follow range >1000
    
    //define message type of request
    public static final int  MSG_PING = MSG_REQUEST + 1;    
    //define message type of response
    public static final int  MSG_PONG = MSG_RESPONSE + 1;
    
    public static final int  MSG_TRANSFER_CASH = MSG_REQUEST + 2;
    public static final int  MSG_TRANSFER_CASH_BY_TYPE = MSG_REQUEST + 3;
}
