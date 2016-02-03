
package com.mabasasi.hatena.utility;

import java.util.*;
import java.util.regex.Pattern;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * JSONの変換クラス.
 * //TODO: JSONArray対応は見送り
 * <p>
 * json &lt;-&gt; map.<br><br>
 * 
 * 参考：http://www.mkyong.com/java/json-simple-example-read-and-write-json/
 * </p>
 */
public class JSONConverter {
    
    /**
     * MAPをJSON文字列に変換する.
     * @param m マップ
     * @return JSON文字列
     */
    public static String jsonToString(Map m) {
        // JSON作成
        JSONObject obj = new JSONObject();
	obj.putAll(m);
        
        String jsonString = obj.toJSONString();
        
        // 改行挿入
        String n = System.getProperty("line.separator");
        String p = "  ";
        jsonString = jsonString.replaceAll(Pattern.quote(","), ","+n+p);
        jsonString = jsonString.replaceAll(Pattern.quote("{"), "{"+n+p);
        jsonString = jsonString.replaceAll(Pattern.quote("}"), n+"}");
        return jsonString;
    }
    
    /**
     * MAPをJSON文字列に変換する.
     * @param jsonText JSON文字列
     * @return Map(ベースはhashmap)
     */
    public static Map stringToJson(String jsonText) {
        HashMap hash = new HashMap();
            
        try {
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(jsonText);
            hash.putAll(obj);
        } catch (ParseException ex) {
        }
        
        return hash;
    }
}
