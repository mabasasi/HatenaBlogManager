package com.mabasasi.hatena.utility;


import java.net.URL;

/**
 * http接続のエラー出力.
 * @author mabasasi
 */
public class HttpConnectionException extends Exception {
    private static final long serialVersionUID = -111962848856425710L;
    
    /**
     * http接続のエラー出力.
     * @param code ResposeCode
     * @param message ResponseMessage
     * @param url URL
     */
    public HttpConnectionException(int code, String message, URL url){
        super(code + "-" + message + " : " + url);
    }
    
    /**
     * http接続のエラー出力.
     * @param message メッセージ
     */
    public HttpConnectionException(String message){
        super(message);
    }
}
