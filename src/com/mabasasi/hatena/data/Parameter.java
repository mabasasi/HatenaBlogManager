
package com.mabasasi.hatena.data;

import com.mabasasi.hatena.utility.JSONConverter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * マップベースのクラス.
 * @author mabasasi
 * @param <E> キーのenum
 */
public class Parameter<E extends Enum<E>> {
    private Class<E> _class;
    private HashMap<E, String> param;

    /**
     * 新規のコンストラクタ.
     */
    public Parameter(Class<E> enumType) {
        this._class = enumType;
        param = new HashMap<>();   
    }
    
    /**
     * JSON文字列を指定して、初期値を指定するコンストラクタ.
     * @param jsonText JSON文字列
     * @param enumType enumクラス
     */
    public Parameter(String jsonText, Class<E> enumType) {
        this(enumType);
        if (jsonText != null) {
            // JSONを読み込む
            Map json = JSONConverter.stringToJson(jsonText);

            // 要素がParamに存在するなら取得
            for (Object keyObj : json.keySet()) {
                try {
                    E key = E.valueOf(enumType, keyObj.toString());
                    this.param.put(key, (String) json.get(keyObj));
                } catch (RuntimeException ex) { ex.printStackTrace();}
            }
        }
    }

    
    /**
     * パラメータを追加する.
     * <p>同キーは上書き。nullは無視する。</p>
     * @param key キー
     * @param value 値
     */
    public void setParameter(E key, String value) {
        if (key == null || value == null) return;
        
        this.param.put(key, value);
    }

    /**
     * パラメータを取得する.
     * @param key キー
     * @return パラメータ nullで存在しない
     */
    public String getParameter(E key) {
        return this.param.get(key);
    }
    
    /**
     * パラメータを取得する.
     * @param key キー
     * @return パラメータ nullの場合は空文字
     */
    public String getText(E key) {
        String value = this.param.get(key);
        return (value == null) ? "" : value;
    }
    
    /**
     * パラメータを削除する.
     * @param key キー
     * @return 削除したパラメータ nullでキーは存在しなかった
     */
    public String removeParameter(E key) {
        return this.param.remove(key);
    }
    
    
    /**
     * MAPをJSON文字列に変換する.
     * @return JSON文字列
     */
    public String getJsonString() {
        return JSONConverter.jsonToString(param);
    }
    
    
    
    @Override
    public String toString() {
        String n = System.getProperty("line.separator");
                
        StringBuilder sb = new StringBuilder();
        sb.append("/************************************************************************/").append(n);
        sb.append("【").append(this.getClass().getSimpleName()).append("】").append(n);
        for (E p : _class.getEnumConstants()) {
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
