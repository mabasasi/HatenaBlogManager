//package com.mabasasi.hatena.main;
//
//
//import com.mabasasi.hatena.data.BlogEntry;
//import com.mabasasi.hatena.data.SystemData;
//import com.mabasasi.hatena.parameter.HatenaParameter;
//import com.mabasasi.hatena.utility.XmlParseException;
//import java.io.*;
//import java.net.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import oauth.signpost.OAuthConsumer;
//import oauth.signpost.exception.OAuthException;
//
//
///**
// * はてなブログのAtom Publishing Protocol.
// * @author mabasasi
// */
//public class AtomPub {
//    private static final String BASE_URL = "https://blog.hatena.ne.jp/";
//    
//    // AtomPub アクセス用URL
//    private String rootEndPoint;
//    
//    private OAuthConsumer consumer;
//    
//    /**
//     * はてなブログのAtomPub接続.
//     * @param profile はてなユーザー情報
//     */
//    public AtomPub(OAuthConsumer consumer, SystemData system) {        
//        this.rootEndPoint = BASE_URL  + system.getParameter(HatenaParameter.urlName) + "/" + profile.blogId + "/atom";
//    }
//    
//    
//    /**
//     * すべてのブログエントリを再帰的に取得.
//     * @return エントリでデータ nullで失敗
//     */
//    public BlogEntry[] getRecursionBlogAllEntry(){
//        List<BlogEntry> entries = new ArrayList<>();
//        
//        BlogEntryList blogList;
//        long no = -1;
//        do {
//            blogList = getBlogAllEntry(no);
//            entries.addAll(blogList.entries);
//        } while ((no = blogList.getNextPage()) != -1);
//        
//        // TODO: 何故かtoArrayが機能しないので、通常変換
//        BlogEntry[] re = new BlogEntry[entries.size()];
//        for (int i=0; i<re.length; i++) {
//            re[i] = entries.get(i);
//        }
//        
//        return re;
//    }
//    
//    
//    /**
//     * 全てのブログエントリーを取得.
//     * @param pageNo ページNO（-1でトップページ）
//     * @return ブログデータ　nullで失敗
//     */
//    public BlogEntryList getBlogAllEntry(long pageNo){
//        try {
//            String pg = (pageNo != -1) ? "?page="+pageNo : "";
//            URL url = new URL(rootEndPoint+"/entry"+pg);
//            String source = convertToString(getHttpResponse(url, "GET", profile.Consumer));
//            System.out.println(source);
//            
//            // エントリーを読み込む
//            BlogEntryList data = new BlogEntryList(source);  
//            return data;
//        } catch (HttpConnectionException | MalformedURLException | UnsupportedEncodingException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//    }
//    
//    /**
//     * ブログエントリーを取得.
//     * @param entryId 取得したいエントリーID（生ID）
//     * @return ブログエントリー　nullで失敗
//     */
//    public BlogEntry getBlogEntry(long entryId){
//        try {
//            URL url = new URL(rootEndPoint+"/entry/" + entryId);
//            String source = convertToString(getHttpResponse(url, "GET", profile.Consumer));
//            System.out.println(source);
//            
//            // エントリーを読み込む
//            BlogEntry entry = new BlogEntry(source);
//            return entry;
//        } catch (HttpConnectionException | MalformedURLException | UnsupportedEncodingException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//    }
//    
//    
//    /**
//     * コレクション一覧を取得.
//     * // TODO: 使い道が分からない...
//     */
//    public void getBlogCollection(){
//        try {
//            URL url = new URL(rootEndPoint);
//            String source = convertToString(getHttpResponse(url, "GET", profile.Consumer));
//            System.out.println(source);
//        } catch (HttpConnectionException | MalformedURLException | UnsupportedEncodingException ex) {
//            ex.printStackTrace();
//            Logger.getLogger(AtomPub.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//    
//    /**
//     * カテゴリ一覧を取得.
//     * @return カテゴリ配列　nullで失敗
//     */
//    public String[] getBlogCategory(){
//        try {
//            URL url = new URL(rootEndPoint + "/category");
//            String source = convertToString(getHttpResponse(url, "GET", profile.Consumer));
//            
//            // カテゴリを読み込む
//            XmlBuilder loader = new XmlBuilder(source);
//            XmlParser parser = new XmlParser(loader.getDocument());
//            
//            return parser.getAllNodeAttrString("atom:category", "term");
//        } catch (HttpConnectionException | MalformedURLException | XmlParseException | UnsupportedEncodingException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//    }
//    
//    /**
//     * ブログエントリを更新.
//     * @param entryId 取得したいエントリーID（生ID）
//     * @param ent エントリデータ
//     * @return ブログエントリー　nullで失敗
//     */
//    public BlogEntry putBlogEntry(long entryId, BlogEntry ent) {
//        try {
//            URL url = new URL(rootEndPoint+"/entry/"+entryId);
//            String xml = new XmlBuilder(ent, false).xmlToString();
//            String source = convertToString(getHttpResponse(url, "PUT", xml, profile.Consumer));
//            
//            // エントリーを読み込む
//            BlogEntry entry = new BlogEntry(source);
//            return entry;
//        } catch (XmlParseException | MalformedURLException | HttpConnectionException | UnsupportedEncodingException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//    }
//    
//    /**
//     * エントリを投稿する.
//     * @param ent エントリデータ
//     * @return エントリデータ
//     */
//    public BlogEntry postBlogEntry(BlogEntry ent) {
//        try {
//            URL url = new URL(rootEndPoint+"/entry");
//            String xml = new XmlBuilder(ent, false).xmlToString();
//            String source = convertToString(getHttpResponse(url, "POST", xml, profile.Consumer));
//           
//            // エントリーを読み込む
//            BlogEntry entry = new BlogEntry(source);
//            return entry;
//        } catch (XmlParseException | NullPointerException | MalformedURLException | HttpConnectionException | UnsupportedEncodingException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//    }
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    /**
//     * ブログエントリーを削除.
//     * @param entryId 削除したいエントリーID（生ID）
//     * @return ブログエントリー　nullで失敗
//     */
//    public boolean deletetBlogEntry(long entryId){
//        try {
//            URL url = new URL(rootEndPoint+"/entry/" + entryId);
//            String source = convertToString(getHttpResponse(url, "DELETE", profile.Consumer));
//            System.out.println(source);
//            
//            return true;
//        } catch (HttpConnectionException | MalformedURLException | UnsupportedEncodingException ex) {
//            return false;
//        }
//    }
//    
//    
//
//    
//    
//
//}
