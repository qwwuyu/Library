package com.qwwuyu.lib.stadyTest;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
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
        System.out.println(getClass().getResource("").getPath());
        System.out.println(getClass().getResource("/").getPath());
        System.out.println(new File("").getAbsolutePath());
        System.out.println(System.getProperty("user.dir"));
        URL location = UnitTest.class.getProtectionDomain().getCodeSource().getLocation();
        System.out.println(location.getFile());
    }

    @Test
    public void xml() throws Exception {
        String version = "versionA";
        Map<String, String> messages = new HashMap<>();
        messages.put("key1", "value1");
        messages.put("key2", "value2");
        messages.put("key3", "value3");

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

    @Test
    public void xyz4() throws Exception {        // 4 * (i + j) * (j + k) * (k + i) == (i * (i + j) * (i + k) + j * (j + i) * (j + k) + k * (k + i) * (k + j))
        BigDecimal b1 = new BigDecimal("154476802108746166441951315019919837485664325669565431700026634898253202035277999");
        BigDecimal b2 = new BigDecimal("36875131794129999827197811565225474825492979968971970996283137471637224634055579");
        BigDecimal b3 = new BigDecimal("4373612677928697257861252602371390152816537558161613618621437993378423467772036");
        BigDecimal a1 = b1.add(b2);
        BigDecimal a2 = b2.add(b3);
        BigDecimal a3 = b3.add(b1);
        System.out.println(a1.multiply(a2).multiply(a3).multiply(new BigDecimal("4")));
        BigDecimal m1 = b1.multiply(a1).multiply(a3);
        BigDecimal m2 = b2.multiply(a1).multiply(a2);
        BigDecimal m3 = b3.multiply(a3).multiply(a2);
        System.out.println(m1.add(m2).add(m3));
    }
}