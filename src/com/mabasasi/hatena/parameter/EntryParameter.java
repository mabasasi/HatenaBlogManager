
package com.mabasasi.hatena.parameter;

/**
 * レスポンスXMLのパラメータ一覧.
 * @author mabasasi
 */
public enum EntryParameter {
    //管理用パラメータ-----------------------------------------------------------
    
    /**作成日時*/
    generatedDate("generated/text()"),
    
    //投稿時必要なパラメータ-----------------------------------------------------
    
    /**エントリの種類*/
    contentType("content/@type"),
    /**エントリの内容*/
    contentText("content/text()"),

    /**タイトル*/
    title("title/text()"),
    /**著者*/
    author("author/name/text()"),
    /**投稿日*/
    updatedDate("updated/text()"),
    /**カテゴリ*/
    // TODO: カテゴリが一つしか取れていない
    category("category/@term"),
    /**下書き(下書きならyes, 本書きならno)*/
    draft("control/draft/text()"),
    
    
    //取得できるその他のパラメータ------------------------------------------------
            
    /**概要*/
    summaryText("summary/text()"),
    
    /**エントリID*/
    id("id/text()"),
    /**AtomPub用URL*/
    editUrl("link[@rel='edit']/@href"),
    /**エントリアクセス用URL*/
    alternateUrl("link[@rel='alternate']/@href"),
    
    /**オリジナルのエントリのタイプ*/
    alternateType("link[@rel='alternate']/@type"),
    
    /**フォーマットされたエントリの種類*/
    formatType("formatted-content/@type"),
    /**フォーマットされたエントリの内容*/
    formatText("formatted-content/text()"),
    
    /**作成日*/
    publishedDate("published/text()"),
    /**最終更新日*/
    editedDate("edited/text()"),
    
    // TODO: 名前空間の取得方法がいまいち掴めない
//    /**XML namespace*/
//    xmlns(""),
//    /**XML:app namespace*/
//    xmlns_app(""),
//    /**XML:hatena namespace*/
//    xmlns_hatena("formatted-content/@xmlns:hatena"),
    ;
    
    
    private String xpath;

    private EntryParameter(String xpath) {
        this.xpath = xpath;
    }
    
    public String getXPath() {
        return this.xpath;
    }
}
