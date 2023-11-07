package soya.framework.action.actions.text;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

public class FormattingAction extends TextUtilAction {

    public static final String PLAIN_TEXT_FORMAT = "TEXT";
    public static final String JSON_FORMAT = "JSON";
    public static final String XML_FORMAT = "XML";

    private String contentType;

    @Override
    public String execute() throws Exception {
        String format = contentType == null ? findContentType(text) : contentType.toUpperCase();
        switch (format) {
            case JSON_FORMAT:
                return formatJson(text);

            case XML_FORMAT:
                return formatXml(text);

            default:
                return text;
        }
    }

    private String findContentType(String src) {
        return JSON_FORMAT;
    }

    private String formatJson(String src) throws Exception {
        return new GsonBuilder().setPrettyPrinting().create().toJson(JsonParser.parseString(text));
    }

    private String formatXml(String src) throws Exception {
        Source xmlInput = new StreamSource(new StringReader(src));
        StringWriter stringWriter = new StringWriter();
        StreamResult xmlOutput = new StreamResult(stringWriter);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", 2);
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(xmlInput, xmlOutput);
        return xmlOutput.getWriter().toString();
    }
}
