package com.mabasasi.hatena.utility;


import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.*;


/**
 * HTTPコネクション用ユーティリティ.
 * TODO: 前の使い回しなので編集
 * @author mabasasi
 */
public class NetIO {
    //public static int PACKET_SIZE = 512;
    public static String ACCEPT_LANGUAGE = "jp";
    public static String USER_AGENT = "";
    public static boolean ALLOW_USER_INTERACTION = false;
    public static boolean INSTANCE_FOLLOW_REDIRECTS = true;
    
    public static int COOL_MILLI_TIME = 1000;
    
    private static long AccessTime = 0;
    
    
    
    
    // 接続にも埋め込む
    public static String CHAR_SET = "UTF-8";
    
    public static String convertToString(byte[] b) throws UnsupportedEncodingException {
        //System.err.println(new String(b));
        return new String(b);
    }
    

    
    
    
    
    
    
    
    /**
     * http/https 接続の結果をbyte配列で取得する.
     * @param url 接続先URL（絶対パス）
     * @param method 接続方法（GET, POST, PUT, DELETE）
     * @param consumer 接続署名用（使用しないならnullで）
     * @return 文字列（ソース）
     * @throws HttpConnectionException 接続失敗
     */
    public static byte[] getHttpResponse(URL url, String method, OAuthConsumer consumer) throws HttpConnectionException {
        return getHttpResponse(url, method, null, consumer);
    }
    
    /**
     * http/https 接続の結果をbyte配列で取得する.
     * @param url 接続先URL（絶対パス）
     * @param method 接続方法（GET, POST, PUT, DELETE）
     * @param body リクエストボディ（使用しないならnullで）
     * @param consumer 接続署名用（使用しないならnullで）
     * @return 文字列（ソース）
     * @throws HttpConnectionException 接続失敗
     */
    public static byte[] getHttpResponse(URL url, String method, String body, OAuthConsumer consumer) throws HttpConnectionException {
        HttpURLConnection conn = null;
        
        try {
            // 接続を確立
            conn = establishHttpConnection(url, method);
            if (body != null) {
                // データを送信する
                conn.addRequestProperty("Content-Type", "application/octed-stream");
                conn.setDoOutput(true);
            }
            
            if (consumer != null)   consumer.sign(conn);
            if (body != null)       setHttpBody(conn, body);
            
            conn.connect();

            // 取得データをパースする
            byte[] b = parseConnectionOutput(conn);
            return b;
        } catch (IOException | OAuthException | HttpConnectionException ex) {
            throw new HttpConnectionException(ex.getMessage());
        } finally {
            if (conn != null)   conn.disconnect();
        }
    }
    
    
    
    
    
    
    
    
    
    /**
     * HTTP ボディに書き込む.
     * @param conn  HTTPConnection
     * @param body 書き込む文章
     * @throws HttpConnectionException
     */
    private static void setHttpBody(HttpURLConnection conn, String body) throws HttpConnectionException{
        OutputStreamWriter out = null;
        BufferedWriter writer = null;
        
        // 空文字チェック
        if (!checkString(body))   return;
        
        try {
            // リクエストに書き込む
            out = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8);
            writer = new BufferedWriter(out);
        
            writer.write(body);
            writer.flush();
        } catch (IOException ex) {
            throw new HttpConnectionException(ex.getMessage());
        } finally {
            try { if (out    != null)    out.close(); } catch (Exception ex) { }
            try { if (writer != null) writer.close(); } catch (Exception ex) { }
        }
    }
    
    /**クール時間調節*/
    private static void TimeAdjustment() {
        long now = System.currentTimeMillis();
        
        long t = now - (AccessTime + COOL_MILLI_TIME);
        if (t < 0) {
            t *= -1;
            try {
                System.out.println("wait to "+(t+100)+" ms.");
                Thread.sleep(t + 100);
            } catch (InterruptedException ex) {   }
        }
        
        AccessTime = System.currentTimeMillis();
    }

    
    
    /**
     * http/https 接続を確立する.
     * @param url 接続先URL（絶対パス）
     * @param method 接続方法（GET, POST, PUT, DELETE）
     * @return HTTPConnection
     * @throws HttpConnectionException 接続失敗
     */
    private static HttpURLConnection establishHttpConnection(URL url, String method) throws HttpConnectionException{
        HttpURLConnection conn = null;

        TimeAdjustment();
        
        try {
            // コネクションベース作成
            String scheme = url.toURI().getScheme();
            switch(scheme){
                case "http":
                    conn = (HttpURLConnection) url.openConnection();
                    break;
                case "https":
                    conn = (HttpsURLConnection) url.openConnection();
                    break;
                default:
                    throw new IllegalArgumentException("スキーマが不明な値です。 scheme="+scheme);
            }

            // 接続設定
            conn.setRequestMethod(method);
            conn.setAllowUserInteraction(ALLOW_USER_INTERACTION);
            conn.setInstanceFollowRedirects(INSTANCE_FOLLOW_REDIRECTS);
            if (checkString(USER_AGENT))        conn.setRequestProperty("User-Agent", USER_AGENT);
            if (checkString(ACCEPT_LANGUAGE))   conn.setRequestProperty("Accept-Language", ACCEPT_LANGUAGE);
            
            // TODO: HTTP OUTPUT STRING
            System.out.println("[connect]" + url);
            return conn;
        } catch (IOException | URISyntaxException ex){
            throw new HttpConnectionException(ex.getMessage());
        }
    }
    
    /**
     * http/https 接続の結果を文字列に変換する.
     * @param conn HTTPConnection
     * @return byte配列
     * @throws HttpConnectionException 接続失敗
     */
    private static byte[] parseConnectionOutput(HttpURLConnection conn) throws HttpConnectionException {
        DataInputStream in = null;
        ByteArrayOutputStream baos = null;
                
        try {
            // httpStatus = 20* Successで実行
            if (conn.getResponseCode()/100 == 2) {
                // ストリームデータをパース
                in = new DataInputStream(conn.getInputStream());
                baos = new ByteArrayOutputStream();

                int b = -1;
                while((b = in.read()) != -1){
                    baos.write(b);
                }

                return baos.toByteArray();
            }
            
            in = new DataInputStream(conn.getErrorStream());
            baos = new ByteArrayOutputStream();

                int b = -1;
                while((b = in.read()) != -1){
                    baos.write(b);
                }
            System.err.println("!!"+(new String(baos.toByteArray()))+"!!");
            throw new HttpConnectionException(conn.getResponseCode(), conn.getResponseMessage(), conn.getURL());
        } catch (IOException | HttpConnectionException ex) {
            throw new HttpConnectionException(ex.getMessage());
        } finally {
            try {if (in   != null)   in.close(); } catch (Exception ex) { }
            try {if (baos != null) baos.close(); } catch (Exception ex) { }
        }
    }
    
    
    

    /**文字列が空でないかチェック*/
    private static boolean checkString(String str) {
        return str != null && !"".equals(str);
    }
}
