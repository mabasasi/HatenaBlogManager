
package com.mabasasi.hatena.utility;

import com.mabasasi.hatena.data.BlogEntry;
import com.mabasasi.hatena.data.EntryParameter;
import javax.xml.xpath.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author mabasasi
 */
public class XMLParser {

    /**
     * エントリのパラメータを取得する.
     * @param document XML文章
     * @return ブログエントリ
     * @throws XPathExpressionException
     */
    public static BlogEntry getBlogEntryParameter(Document document) throws XPathExpressionException {
        BlogEntry entry = new BlogEntry();
        
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xp = xpf.newXPath();
        
        // ルートに移動する
        XPathExpression rXpe = xp.compile("/entry");
        Node root = (Node) rXpe.evaluate(document, XPathConstants.NODE);
        
        // パラメータの数だけ処理をする
        for (EntryParameter param : EntryParameter.values()) {
            String path = param.getXPath();
            if (path != null && !"".equals(path)) {
                XPathExpression xpe = xp.compile(path);
                String content = (String) xpe.evaluate(root, XPathConstants.STRING);
                entry.setParameter(param, content);
            }
        }
        
        return entry;
    }
}
