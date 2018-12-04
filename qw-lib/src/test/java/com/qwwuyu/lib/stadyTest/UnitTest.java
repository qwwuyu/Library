package com.qwwuyu.lib.stadyTest;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by qiwei on 2018/4/24 18:08
 * Description 工具测试.
 */
public class UnitTest {
    @Test
    public void test() throws Exception {
        String version = "versionA";
         Map<String, String> messages = new HashMap<>();
        messages.put("key1","value1");
        messages.put("key2","value2");
        messages.put("key3","value3");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element rootElement = document.createElement("object");
        document.appendChild(rootElement);

        Element versionElement = document.createElement("string");
        versionElement.setAttribute("name", "version");
        versionElement.appendChild(document.createTextNode(version));
        rootElement.appendChild(versionElement);

        Element arrayElement = document.createElement("array");
        arrayElement.setAttribute("name", "messages");
        Element objectElement = document.createElement("object");
        for (Map.Entry<String, String> entry : messages.entrySet()) {
            Element string = document.createElement("string");
            string.setAttribute("name", entry.getKey());
            string.appendChild(document.createTextNode(entry.getValue()));
            objectElement.appendChild(string);
        }
        arrayElement.appendChild(objectElement);
        rootElement.appendChild(arrayElement);

        TransformerFactory factory1 = TransformerFactory.newInstance();
        Transformer transformer = factory1.newTransformer();
        Properties outFormat = new Properties();
        outFormat.setProperty(OutputKeys.INDENT, "yes");
        outFormat.setProperty(OutputKeys.METHOD, "xml");
        outFormat.setProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperties(outFormat);
        DOMSource domSource = new DOMSource(document.getDocumentElement());
        OutputStream output = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(output);
        transformer.transform(domSource, result);
        System.out.println(output.toString());
    }

    public static void printStackTrace(Throwable e) {
        e.printStackTrace();
    }

    @Test
    public void sumNN() throws Exception {
        Ngraphy.printIS(14);
    }

    @Test
    public void getAngle() throws Exception {
        System.out.println(getAngle(0, 0, 1, 1));
        System.out.println(getAngle(0, 0, -1, 1));
        System.out.println(getAngle(0, 0, 1, -1));
        System.out.println(getAngle(0, 0, -1, -1));
    }

    private static double getAngle(double centerX, double centerY, double targetX, double targetY) {
        double angle = Math.toDegrees(Math.atan2(targetY - centerY, targetX - centerX));
        if (angle < 0) angle += 360;
        return angle;
    }

    @Test
    public void str2ascii() throws Exception {
        String s = "测试";
        for (int i = 0; i < s.length(); i++) {
            System.out.print("\\u" + Integer.toHexString((int) s.charAt(i)));
        }
    }

    @Test
    public void ascii2str() throws Exception {
        String s = "\u6d4b\u8bd5";
        System.out.println(s);
    }
}