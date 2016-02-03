
package com.mabasasi.hatena.main;

import com.mabasasi.hatena.data.BlogData;
import com.mabasasi.hatena.data.Parameter;
import com.mabasasi.hatena.data.SystemData;
import com.mabasasi.hatena.parameter.HatenaParameter;
import static com.mabasasi.hatena.parameter.SystemParameter.*;
import com.mabasasi.hatena.parameter.UserInformation;
import com.mabasasi.hatena.utility.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * はてなブログマネージャー.
 * <p>
 * ブログの記事とローカルのファイルを同期するのが目的。<br>
 * 基本コマンドはCUIベースで、簡易エディタ機能も付け加える予定。<br>
 * 流れとしては、[OAuth認証]->[ユーザー情報取得]->[ファイル同期]
 * <p>
 * @author mabasasi
 */
public class HatenaBlogManager {
    /**ファイル保存先*/
    public static final String CURRENT_DIRECTORY = System.getProperty("user.home") + "\\Desktop";
    
    public static final String CONFIG_FILE_NAME = "config.json";
    public static final String SYSTEM_FILE_NAME = "system.json";
    
    private static SystemData system;
    

    public static void main(String[] args) {
        
        try {
            // システムファイルの読み込み
            String systemJSon = FileIO.readTextFile(CURRENT_DIRECTORY, SYSTEM_FILE_NAME);
            if (systemJSon == null) systemJSon = FileIO.readTextFile(System.clearProperty("user.dir"), "src", SYSTEM_FILE_NAME);
            system = new SystemData(systemJSon);
            // 設定ファイルの読み込み
            String confJSon = FileIO.readTextFile(CURRENT_DIRECTORY, CONFIG_FILE_NAME);
            BlogData hatena = new BlogData(confJSon);

            // ユーザー認証（OAuth）
            HatenaOauth oauth = new HatenaOauth(system);
            String user = oauth.getUserJsonData(system.getParameter(userInfomationURL));            
            // ユーザーデータの取得
            Parameter<UserInformation> param = new Parameter<>(user, UserInformation.class);
            
            // 値更新
            system.setParameter(accessToken, oauth.getToken());
            system.setParameter(tokenSecret, oauth.getTokenSecret());
            
            hatena.setParameter(HatenaParameter.displayName, param.getParameter(UserInformation.display_name));
            hatena.setParameter(HatenaParameter.profileImageUrl, param.getParameter(UserInformation.profile_image_url));
            hatena.setParameter(HatenaParameter.urlName, param.getParameter(UserInformation.url_name));
            
            
            
            
            Thread.sleep(1000);
            System.out.println(system);
            System.out.println(hatena);
            
            // シャットダウン時の処理
            FileIO.writeTextFile(system.getJsonString(), CURRENT_DIRECTORY, SYSTEM_FILE_NAME);
            FileIO.writeTextFile(hatena.getJsonString(), CURRENT_DIRECTORY, CONFIG_FILE_NAME);
            
            
            
            
            
            
        } catch (Exception ex) {
            Logger.getLogger(HatenaBlogManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
