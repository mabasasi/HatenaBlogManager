package com.mabasasi.hatena.data;

import oauth.signpost.OAuthConsumer;


/**
 * はてなのユーザー情報.
 * <p>
 * 基本は「"http://n.hatena.com/applications/my.json"」から取得したJSONデータ。<br>
 * その他、atompubを稼働させるのに必要なデータ。
 * </p>
 * @author mabasasi
 */
public class UserProfile {
    /**識別子*/
    public String urlName;
    /**ニックネーム*/
    public String displayName;
    /**プロフィールアイコンURL*/
    public String profileImageUrl;
    
    /**OAuth認証データ*/
    public OAuthConsumer Consumer;
    /**ブログID*/
    public String blogId;
    
    
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("url_name=").append(urlName).append("\n");
        sb.append("display_name=").append(displayName).append("\n");
        sb.append("profile_image_url=").append(profileImageUrl).append("\n");
        sb.append("consumer=").append((Consumer != null)).append("\n");
        if (Consumer != null){
            sb.append("  consumerKey=").append(Consumer.getConsumerKey()).append("\n");
            sb.append("  consumerSecret=").append(Consumer.getConsumerSecret()).append("\n");
            sb.append("  token=").append(Consumer.getToken()).append("\n");
            sb.append("  tokenSecret=").append(Consumer.getTokenSecret()).append("\n");
            sb.append("  param=").append(Consumer.getRequestParameters()).append("\n");
        }
        sb.append("blog_id=").append(blogId);
        return sb.toString();
    }
}
