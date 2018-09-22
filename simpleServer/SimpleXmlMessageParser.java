package study.inno.simpleChat.simpleServer;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleXmlMessageParser {
    private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    public String tagName;
    public String from;
    public String message;
    private String warning;
    private DocumentBuilder builder = null;

    public SimpleXmlMessageParser() throws ParserConfigurationException {
        builder = factory.newDocumentBuilder();
    }

    private void clear() {
        tagName = "";
        from = "";
        message = "";
        warning = "";
    }

    public boolean parse(String xmlSrc) throws IOException, SAXException {
        clear();

        Document document = builder.parse(new ByteArrayInputStream(xmlSrc.getBytes()));
        NodeList nodes = document.getChildNodes();
        Node node = nodes.item(0);

        tagName = node.getNodeName();
        NamedNodeMap attrs = node.getAttributes();

        for (int iAttr = 0; iAttr < attrs.getLength(); iAttr++) {
            node = attrs.item(iAttr);
            switch (node.getNodeName()) {
                case "message":
                    message = node.getNodeValue();
                    break;

                case "from":
                    from = node.getNodeValue();
                    break;

                case "warning":
                    warning = node.getNodeValue();
                    break;

                default:
                    break;
            }
        }

        switch (tagName) {
            case "name":
            case "message":
                return message.length() > 0;

            case "nameRequest":
            case "ping":
            case "pong":
                return true;
            default:
                return false;
        }
    }
}
