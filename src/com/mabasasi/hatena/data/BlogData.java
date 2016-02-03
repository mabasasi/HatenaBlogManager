
package com.mabasasi.hatena.data;

import com.mabasasi.hatena.parameter.HatenaParameter;
import java.util.ArrayList;

/**
 * ブログメタデータ保持クラス.
 * @author mabasasi
 */
public final class BlogData extends Parameter<HatenaParameter>{
    private ArrayList<BlogEntry> entries = new ArrayList<>();

    public BlogData() {
        super(HatenaParameter.class);
    }

    public BlogData(String jsonText) {
        super(jsonText, HatenaParameter.class);
    }
    
    
    
    /**
     * ブログエントリを追加する.
     * @param entry エントリ
     */
    public void setBlogEntry(BlogEntry entry) {
        this.entries.add(entry);
    }
    
    /**
     * ブログエントリを取得する.
     * 
     * @param idx インデックス番号
     * @return ブログエントリ nullは存在しない
     */
    public BlogEntry getBlogEntry(int idx) {
        try {
            return this.entries.get(idx);
        } catch (ArrayIndexOutOfBoundsException ex) {
            return null;
        }
    }
}
