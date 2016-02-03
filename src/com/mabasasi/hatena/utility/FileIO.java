
package com.mabasasi.hatena.utility;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ファイル入出力.
 * @author mabasasi
 */
public class FileIO {
    public static Charset charset = StandardCharsets.UTF_8;
    
    /**
     * テキストファイルを読み込む.
     * @param first 先頭パス
     * @param more more...
     * @return コンテンツ nullで失敗
     */
    public static String readTextFile(String first, String... more) {
        try {
            Path path = Paths.get(first, more);
            System.out.println("[read]" + path);
            
            byte[] bytes = Files.readAllBytes(path);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException | RuntimeException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * テキストファイルを書き込む.
     * @param contents 書き込む文字列
     * @param first 先頭パス
     * @param more more...
     * @return 成功可否
     */
    public static boolean writeTextFile(String contents, String first, String... more) {
        try {
            Path path = Paths.get(first, more);
            System.out.println("[write]" + path);
            
            Files.write(path, contents.getBytes(charset));
            return true;
        } catch (IOException | RuntimeException ex) {
            System.err.println(ex.toString() +":"+ ex.getMessage());
        }
        
        return false;
    }
}
