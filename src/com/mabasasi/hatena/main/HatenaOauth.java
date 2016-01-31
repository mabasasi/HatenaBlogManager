package com.mabasasi.hatena.main;


import utility.PropertiesLoader;
import exception.HttpConnectionException;
import exception.UnexpectedValueException;
import data.UserProfile;
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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import static utility.HttpConnectionUtility.convertToString;
import static utility.HttpConnectionUtility.getHttpResponse;



/**
 * Oauth認証関連クラス.
 * @author mabasasi
 */
public class HatenaOauth {
    private static final String TEMPORARY_REQUEST_URL = "https://www.hatena.com/oauth/initiate";
    private static final String AUTHORIZATION_URL = "https://www.hatena.ne.jp/touch/oauth/authorize";
    private static final String TOKEN_REQUEST_URL = "https://www.hatena.com/oauth/token";
    
    private String informationURL = "http://n.hatena.com/applications/my.json";
    
    
    
    private final OAuthConsumer consumer;
    private PropertiesLoader prop;

    private String accessToken;
    private String accessTokenSecret;
    
    
    
    private String scope;
    private String blogId;
        
    /**
     * OAuthの初期化.
     * <p>
     * キーは、開発側が予めサイトから取得する。<br>
     * プロパティがある場合は、ついでに読み込む。
     * </p>
     * @param consumerKey 公開鍵
     * @param consumerSecret 秘密鍵ｓ
     */
    public HatenaOauth(String consumerKey, String consumerSecret){
        // consumer作成
        consumer = new DefaultOAuthConsumer(consumerKey, consumerSecret);
        
        // プロパティ読み込み
        try {
            prop = new PropertiesLoader("oauth.properties", false);
            
            accessToken       = prop.getProperty("oauth_token");
            accessTokenSecret = prop.getProperty("oauth_token_secret");
            scope             = prop.getProperty("access_scope");
            blogId            = prop.getProperty("blog_id");
            
        } catch (IOException ex) {
            // TODO: 現状必要ないので、握りつぶしている
            ex.printStackTrace();
        }
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
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("source\\logo_oauth.png"));
        
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
    public void getAccessToken(Component parent) throws OAuthException, IOException, URISyntaxException, UnexpectedValueException {
        // RequestTokenURLにscopeを付ける
        String requestURL = TEMPORARY_REQUEST_URL + ((scope.equals("")) ? "" : "?scope="+scope);

        // provider作成
        OAuthProvider provider = new DefaultOAuthProvider(requestURL, TOKEN_REQUEST_URL, AUTHORIZATION_URL);

        // ブラウザで認証させる
        String url = provider.retrieveRequestToken(consumer, OUT_OF_BAND);
        Desktop desktop = Desktop.getDesktop();
        desktop.browse(new URI(url));

        // pin設定
        boolean pinResult = pinInputDialog(provider, parent);
        if (!pinResult){
            throw new UnexpectedValueException("PIN入力に失敗しました。");
        }

        // アクセストークン取得
        accessToken = consumer.getToken();
        accessTokenSecret = consumer.getTokenSecret();
    }
    
    /**
     * ユーザー情報が読み取れるか確認.
     * @return ユーザー情報　nullで取得失敗
     */
    public UserProfile getUserJsonData(){
        consumer.setTokenWithSecret(accessToken, accessTokenSecret);
        
        // HTTPリクエストを作成する
        try {
            URL url = new URL(informationURL);
            String source = convertToString(getHttpResponse(url, "GET", consumer));
            
            UserProfile profile = getHatenaUserData(source);
            if (profile != null){
                profile.Consumer = consumer;
                profile.blogId = blogId;
                
                return profile;
            }
        } catch (MalformedURLException | HttpConnectionException | UnsupportedEncodingException ex) {
            // 例外出てもnull
            ex.printStackTrace();
        }
        
        return null;
    }

    /**
     * はてなユーザーデータをパースする.
     * @param reader JSONデータ
     */
    private UserProfile getHatenaUserData(String str) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(str);

            UserProfile user = new UserProfile();
            user.profileImageUrl = (String) obj.get("profile_image_url");
            user.urlName = (String) obj.get("url_name");
            user.displayName = (String) obj.get("display_name");

            return user;
        } catch (ParseException ex) {
            return null;
        }
    }

    
    /**
     * Propertyファイル保存.
     * @return プロパティ成功可否
     */
    public boolean saveProperties(){
        try {
            prop.setProperty("oauth_token", accessToken);
            prop.setProperty("oauth_token_secret", accessTokenSecret);
            prop.store("OAuth Properties");
            return true;
        } catch (Exception ex){
            return false;
        }
    }
}
