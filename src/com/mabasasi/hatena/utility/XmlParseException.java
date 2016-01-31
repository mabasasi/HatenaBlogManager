package com.mabasasi.hatena.utility;


/**
 * XMLのパースエラー.
 * @author mabasasi
 */
public class XmlParseException extends Exception{
    private static final long serialVersionUID = -6978562082202975455L;
    
    /**
     * XMLのパースエラー.
     * @param message メッセージ
     */
    public XmlParseException(String message) {
        super(message);
    }
}
