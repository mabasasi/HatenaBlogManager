
package com.mabasasi.hatena.main;

import com.mabasasi.hatena.data.BlogEntry;
import com.mabasasi.hatena.utility.XMLConverter;
import com.mabasasi.hatena.utility.XMLParser;
import com.mabasasi.hatena.utility.XmlParseException;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * はてなブログマネージャー.
 * <p>
 * ブログの記事とローカルのファイルを同期するのが目的。<br>
 * 基本コマンドはCUIベースで、簡易エディタ機能も付け加える予定。<br>
 * 流れとしては、[OAuth認証]->[ユーザー情報取得]->[ファイル同期]
 * <p>
 * @author mabasasi
 */
public class HatenaBlogManager {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //XMLConverter converter = new XMLConverter(new File("C:\\Users\\shunichi\\Desktop\\contents.xml"));
            XMLConverter converter = new XMLConverter(new File("C:\\Users\\shunichi\\Desktop\\データ\\6653586347151828561.xml"));
            
            
            BlogEntry entry = XMLParser.getBlogEntryParameter(converter.getDocument());
            System.out.println(entry);
        
        
        } catch (Exception ex) {
            Logger.getLogger(HatenaBlogManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
