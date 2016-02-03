
package com.mabasasi.hatena.utility;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * XMLの変換クラス.
 * <p>
 * String, InputStream, dom.Node &lt;-&gt; dom.document.
 * </p>
 * @author mabasasi
 */
public class XMLConverter {
    /**文字コード*/
    public static Charset charset = StandardCharsets.UTF_8;
    /**インデント数*/
    public static int numOfIndent = 4;
    
    private Document document;
    
    /**
     * XMLを読み込む.
     * @param f ファイルパス
     * @throws com.mabasasi.hatena.utility.XmlParseException コンバートエラー
     */
    public XMLConverter(File f) throws XmlParseException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            this.document = builder.parse(f);
            this.document.setXmlStandalone(true);
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            throw new XmlParseException(ex.getMessage());
        }
    }
    
    /**
     * ドキュメントを取得する.
     * @return ドキュメント
     */
    public Document getDocument(){
        return this.document;
    }
    
    /**
     * DocumentをStringに変換する.
     * @return 文字列（UTF-8）
     * @throws javax.xml.transform.TransformerException コンバートエラー
     */
    public String xmlToString() throws TransformerException {
        try {
            // transformer作成
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer(); 
                transformer.setOutputProperty("encoding", charset.displayName());
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", String.valueOf(numOfIndent));
            
            // ドキュメントを文字列に
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            
            return writer.toString();
        } catch (TransformerException ex) {
            throw ex;
        }
    }
}