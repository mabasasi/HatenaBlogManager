
package com.mabasasi.hatena.data;

/**
 * レスポンスXMLのパラメータ一覧.
 * @author mabasasi
 */
public enum EntryListParameter {
    //TODO: アクセスIDが何を意味しているのか調査
    
    //管理用パラメータ-----------------------------------------------------------
    
//    /**作成日時*/
//    generatedDate,
    
    //取得できるその他のパラメータ------------------------------------------------
    
    /**ブログのタイトル*/
    title,
    /**サブタイトル*/
    subtitle,
    /**著者*/
    author,
    /**最終投稿日*/
    updatedDate,

    /**アクセスID(?)*/
    id,
    /**ブログアクセス用URL*/
    alternateUrl,
    /**AtomPub用先頭URL*/
    firstUrl,
    /**AtomPub用次ページURL(10件ごと)*/
    nextUrl,
    
    /**XML namespace*/
    xmlns,
    /**XML:app namespace*/
    xmlns_app,
    ;
}
