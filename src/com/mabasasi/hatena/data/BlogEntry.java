
package com.mabasasi.hatena.data;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * ブログエントリ保持クラス
 * @author mabasasi
 */
public class BlogEntry {
    private HashMap<EntryParameter, String> param;

    public BlogEntry() {
        param = new HashMap<>();
        
    }
    
    /**
     * パラメータを追加する.
     * <p>同キーは上書き。nullは無視する。</p>
     * @param key キー
     * @param value 値
     */
    public void setParameter(EntryParameter key, String value) {
        if (key == null || value == null) return;
        
        // categoryだけ重複可のため、csv形式で保存
//        if (key == EntryParameter.category) {
//            String category = getParameter(EntryParameter.category);
//            if (category != null) {
//                category += "," + value;
//                value = category;
//            }
//        }
        
        this.param.put(key, value);
    }

    /**
     * パラメータを取得する.
     * @param key
     * @return 
     */
    public String getParameter(EntryParameter key) {
        return this.param.get(key);
    }
    
    
    
    
    
    @Override
    public String toString() {
        String n = System.getProperty("line.separator");
                
        StringBuilder sb = new StringBuilder();
        sb.append("/*BlogEntry**************************************************************/").append(n);
        for (EntryParameter p : EntryParameter.values()) {
            String value = this.param.get(p);
            if (value != null){
                if (value.length() >= 100)   value = value.substring(0, 28) + "…";
                value = value.replaceAll(Pattern.quote(n), value);
            }
            
            sb.append(p.name()).append(" : ").append(value).append(n);
        }
        sb.append("/************************************************************************/");
        
        return sb.toString();
    }
}
