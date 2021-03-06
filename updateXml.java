
package connectionTest;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * updateXml.java for Write the Property Value in xml For Profile.
 *
 * @author Sunil Borad(boradsunil007@gmail.com).
 * @version 1.0
 */
public class updateXml {

    static LoadProperty obj = new LoadProperty();
    static updateXml upadatexml = new updateXml();

//    public static void main(String[] args) {
//        obj.FetchProperty();
//        upadatexml.writeXml();
//    }

    public void writeXml() {
        try {
            String filepath = "./settings/MyXml.xml";
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);
            //Root node.
            Element root = doc.getDocumentElement();
            //Network_name node.
            Node networkName = doc.getElementsByTagName("name").item(0);
            networkName.setTextContent(LoadProperty.networkName);
            //for Ssid_name, HexName and Security key. 
            updateElementValue(doc);

            //for console Display and Write in xml file.
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            System.out.println("-----------Modified File-----------");
            StreamResult consoleResult = new StreamResult(System.out);
            StreamResult result1 = new StreamResult(new File(filepath));

            transformer.transform(source, consoleResult);
            transformer.transform(source, result1);
            System.out.println("Done");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * To Update Property Value in Xml for Profile. 
     * @param doc 
     */
    public static void updateElementValue(Document doc) {
        NodeList ssidConfig = doc.getElementsByTagName("SSIDConfig");
        Element ssidName = null;
       
        for (int i = 0; i < ssidConfig.getLength(); i++) {
            ssidName = (Element) ssidConfig.item(i);
            Node hexName = ssidName.getElementsByTagName("hex").item(0).getLastChild();
            hexName.setNodeValue(LoadProperty.HexsString);
            Node name = ssidName.getElementsByTagName("name").item(0).getLastChild();
            name.setNodeValue(LoadProperty.ssid);
        }
        
        NodeList secure_Key = doc.getElementsByTagName("MSM");
        Element key = null;
        for (int i = 0; i < secure_Key.getLength(); i++) {
            key = (Element) secure_Key.item(i);
            Node keyMaterial = key.getElementsByTagName("keyMaterial").item(0).getLastChild();
            keyMaterial.setNodeValue(LoadProperty.networkKey);
            System.out.println("Key::"+LoadProperty.networkKey);
        }
    }
}
