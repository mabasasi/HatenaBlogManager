
package com.mabasasi.hatena.parameter;

/**
 * はてなブログのパラメーター.
 * @author mabasasi
 */
public enum HatenaParameter {
    //管理用パラメータ-----------------------------------------------------------
    
    
    //JSON取得ユーザーパラメータ--------------------------------------------------
    /**識別子*/
    urlName,
    /**ニックネーム*/
    displayName,
    /**プロフィールアイコンURL*/
    profileImageUrl,
    
    //ブログパラメータ-----------------------------------------------------------
    /**ブログのタイトル*/
    title,
    /**サブタイトル*/
    subtitle,
    /**著者*/
    author,
    /**最終投稿日*/
    updatedDate,

    
    /**ブログアクセス用URL*/
    alternateUrl,
    
    /**XML namespace*/
    xmlns,
    /**XML:app namespace*/
    xmlns_app,
    /**XML;hatena namespace*/
    xmlns_hatena,
    ;
    
}
