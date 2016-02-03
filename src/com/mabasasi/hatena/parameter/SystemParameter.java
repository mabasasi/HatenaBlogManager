
package com.mabasasi.hatena.parameter;

/**
 * システムパラメータ.
 * @author mabasasi
 */
public enum SystemParameter {
    //接続URLパラメータ----------------------------------------------------------
    /**RequestTokenの取得URL*/
    temporaryRequestURL,
    /**リダイレクト先URL(PC)*/
    authorizationURL,
    /**AccessTokenの取得URL*/
    tokenRequestURL,
    
    /**アクセススコープ*/
    accessScope,
    
    /**ユーザー情報取得URL*/
    userInfomationURL,
    
    
    //OAuthパラメータ------------------------------------------------------------
    /**コンシューマーキー*/
    consumerKey,
    /**コンシューマーシークレット*/
    consumerSecret,
    /**アクセスークン*/
    accessToken,
    /**トークンシークレット*/
    tokenSecret,
    
}
