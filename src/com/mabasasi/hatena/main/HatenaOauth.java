package com.mabasasi.hatena.main;



import com.mabasasi.hatena.data.SystemData;
import static com.mabasasi.hatena.parameter.SystemParameter.*;
import com.mabasasi.hatena.utility.HttpConnectionException;
import com.mabasasi.hatena.utility.NetIO;
import static com.mabasasi.hatena.utility.NetIO.convertToString;
import java.awt.Component;
import java.awt.Desktop;
import java.io.*;
import java.net.*;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import static oauth.signpost.OAuth.OUT_OF_BAND;
import oauth.signpost.exception.*;



/**
 * Oauth認証関連クラス.
 * @author mabasasi
 */
public class HatenaOauth {
    private final OAuthConsumer consumer;
    private OAuthProvider provider;
//
//    private String accessToken;
//    private String accessTokenSecret;
//        
    /**
     * OAuthの初期化.
     * <p>
     * キーは、開発側が予めサイトから取得する。<br>
     * プロパティがある場合は、ついでに読み込む。
     * </p>
     * @param consumerKey 公開鍵
     * @param consumerSecret 秘密鍵
     */
    public HatenaOauth(String consumerKey, String consumerSecret){
        // consumer作成
        consumer = new DefaultOAuthConsumer(consumerKey, consumerSecret);
    }
    
    public HatenaOauth(SystemData system) {
        this(system.getText(consumerKey), system.getText(consumerSecret));
        this.createProvider(system.getText(temporaryRequestURL), system.getText(tokenRequestURL), system.getText(authorizationURL), system.getText(accessScope));
        this.setAccessToken(system.getText(accessToken), system.getText(tokenSecret));
    }
    
    /**
     *
     * @param temporaryRequestURL
     * @param tokenRequestUrl
     * @param AuthorizationUrl
     * @param scope
     */
    public void createProvider(String temporaryRequestURL, String tokenRequestUrl, String AuthorizationUrl, String scope) {
        // RequestTokenURLにscopeを付ける
        String request = temporaryRequestURL + ((scope.equals("")) ? "" : "?scope="+scope);

        // provider作成
        this.provider = new DefaultOAuthProvider(request, tokenRequestUrl, AuthorizationUrl);
    }
    
    /**
     *
     * @param accessToken
     * @param tokenSecret
     */
    public void setAccessToken(String accessToken, String tokenSecret){
        consumer.setTokenWithSecret(accessToken, tokenSecret);
    }
    
    public String getToken() {
        return consumer.getToken();
    }
    
    public String getTokenSecret() {
        return consumer.getTokenSecret();
    }
    
    
    
    
    
    
    
    
    
    
    

    /**
     * ユーザー情報が読み取れるか確認.
     * @return ユーザー情報　nullで取得失敗
     */
    public String getUserJsonData(String informationURL){
        // HTTPリクエストを作成する
        URL url = null;
        try {
            url = new URL(informationURL);
            return convertToString(NetIO.getHttpResponse(url, "GET", consumer));
        } catch (MalformedURLException | HttpConnectionException | UnsupportedEncodingException ex) {
            // 取得できなかったらOAuth認証を試してみる
            ex.printStackTrace();
            try {
                getAccessToken();
                return convertToString(NetIO.getHttpResponse(url, "GET", consumer));
            } catch (OAuthException | IOException | URISyntaxException | IllegalAccessException | HttpConnectionException exx) {
                exx.printStackTrace();
            }
        }
        
        return null;
    }
    
    
    
    
    
    /**
     * アクセストークンを取得する.
     * <p>
     * 認証用URLにアクセスして、トークンを取得してくる。<br>
     * ブラウザで認証用URLを開く。<br>
     * PIN入力用ダイアログが出るので、ユーザーに入力させる。
     * </p>
     * @param parent 親コンポーネント
     * @throws oauth.signpost.exception.OAuthException OAuth認証失敗
     * @throws java.io.IOException URLが開けない
     * @throws java.net.URISyntaxException URLが開けない
     * @throws UnexpectedValueException PIN入力失敗
     */
    public void getAccessToken() throws OAuthException, IOException, URISyntaxException, IllegalAccessException {
        // ブラウザで認証させる
        String url = provider.retrieveRequestToken(consumer, OUT_OF_BAND);
        Desktop desktop = Desktop.getDesktop();
        desktop.browse(new URI(url));

        // pin設定
        boolean pinResult = pinInputDialog(provider, null);
        if (!pinResult){
            throw new IllegalAccessException("PIN入力に失敗しました。");
        }
        
        // AccessToken関連付け
        String accessToken = consumer.getToken();
        String tokenSecret = consumer.getTokenSecret();
        this.setAccessToken(accessToken, tokenSecret);
    }
    
    /**
     * PIN値を取得する.
     * @param provider プロバイダー
     * @param parent 親コンポーネント
     * @return PIN入力成功可否
     */
    private boolean pinInputDialog(OAuthProvider provider, Component parent){
        // ダイアログ作成
        String title = "Hatena Authorization";
        String message = "ブラウザで認証してください。\nPIN:";
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("logo_oauth.png"));
        
        while(true){
            // TODO : 最前面、ブラウザ再表示などを付ける
            Object result = JOptionPane.showInputDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE, icon, null, null);
            if (result == null){
                // キャンセルされた
                return false;
            } else {
                // 入力したら問い合わせて、成功したら戻る
                try {
                    String pin = (String) result;
                    provider.retrieveAccessToken(consumer, pin);
                    return true;
                } catch (OAuthException ex){ }
            }
        }
    }
}
