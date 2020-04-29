import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class Main {
    public static void main(String[] args) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = builderFactory.newDocumentBuilder();
            Document doc = builder.parse("xmlFile.xml");
            XPathFactory pathFactory = XPathFactory.newInstance();
            XPath xpath = pathFactory.newXPath();
            //абитуриент, у которого специальность - биофизика, и балл 211
            List<String> entrant = getNameEntrant(doc, xpath, "Биофизика", 211);
            System.out.println("Имя абитуриента: " + entrant.get(0));
            //нахождение минимального балла по атрибуту
            List<String> score = getExamScore(doc, xpath, "Математика");
            System.out.println("Минимальный балл по экзамену: " + score.get(0));

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getNameEntrant(Document doc, XPath xpath, String specialty, Integer spmark) {
        List<String> list = new ArrayList<>();
        try {
            XPathExpression xExp = xpath.compile(String.format(
                    "//notes/note[specialty[namesp='" + specialty + "' and spmark = '" + spmark + "']]/entrant/text()"));
            NodeList nodes = (NodeList) xExp.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++) {
                list.add(nodes.item(i).getTextContent());
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> getExamScore(Document doc, XPath xpath, String exam) {
        List<String> list = new ArrayList<>();
        try {
            XPathExpression xExp = xpath.compile(String.format(
                    "//exams/exam[@nameexam='" + exam + "']/score/text()"));
            NodeList nodes = (NodeList) xExp.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++) {
                list.add(nodes.item(i).getTextContent());
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return list;
    }
}
