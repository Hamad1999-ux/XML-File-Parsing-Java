import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DOM {
    public static void main(String[] args) {
        try {
            // Loading the XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("ccdDocument.XML");

            // Pointing at the root element of the XML file
            Element root = doc.getDocumentElement();

            // Iterating through all the child elements
            NodeList children = root.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                if (children.item(i) instanceof Element) {
                    Element child = (Element) children.item(i);

                    // If it finds an element with the tag name "component"
                    if ("component".equals(child.getTagName())) {

                        // component > nonXMLBody > text
                        Element nonXMLBody = (Element) child.getElementsByTagName("nonXMLBody").item(0);
                        Element text = (Element) nonXMLBody.getElementsByTagName("text").item(0);

                        // Extracting the base64 string from it
                        String base64STR = text.getTextContent();

                        // Decoding the base64 string to raw binary data
                        byte[] decodedData = Base64.getDecoder().decode(base64STR);

                        // Writing the raw binary data to a binary file
                        try (FileOutputStream fos = new FileOutputStream("Data.pdf")) {
                            fos.write(decodedData);
                        }

                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
