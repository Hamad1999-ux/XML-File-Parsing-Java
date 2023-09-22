import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class SAX {
    public static void main(String[] args) {
        try {
            // Creating a SAX parser
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            // Parsing the XML file
            saxParser.parse("ccdDocument.XML", new CustomHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class CustomHandler extends DefaultHandler {
        private boolean inComponent = false;
        private boolean inNonXMLBody = false;
        private boolean inText = false;
        private StringBuilder textContent = new StringBuilder();

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if ("component".equals(qName)) {
                inComponent = true;
            } else if (inComponent && "nonXMLBody".equals(qName)) {
                inNonXMLBody = true;
            } else if (inNonXMLBody && "text".equals(qName)) {
                inText = true;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            if (inText) {
                textContent.append(ch, start, length);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            if ("component".equals(qName)) {
                inComponent = false;
            } else if (inComponent && "nonXMLBody".equals(qName)) {
                inNonXMLBody = false;
            } else if (inNonXMLBody && "text".equals(qName)) {
                inText = false;

                // Extracting the base64 string from the text content
                String base64STR = textContent.toString().trim();

                // Decoding the base64 string to raw binary data
                byte[] decodedData = Base64.getDecoder().decode(base64STR);

                // Writing the raw binary data to a binary file
                try (FileOutputStream fos = new FileOutputStream("Data.pdf")) {
                    fos.write(decodedData);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Resetting the text content for the next element
                textContent.setLength(0);
            }
        }
    }
}