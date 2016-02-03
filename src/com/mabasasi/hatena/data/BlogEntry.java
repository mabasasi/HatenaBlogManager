
package com.mabasasi.hatena.data;

import com.mabasasi.hatena.parameter.EntryParameter;

/**
 * ブログエントリ保持クラス.
 * @author mabasasi
 */
public final class BlogEntry extends Parameter<EntryParameter>{

    public BlogEntry() {
        super(EntryParameter.class);
    }

    public BlogEntry(String jsonText) {
        super(jsonText, EntryParameter.class);
    }
}
