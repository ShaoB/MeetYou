package com.example.android.framework.log;

import android.util.Log;

import com.example.android.framework.utils.LogUtils;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Founder: shaobin
 * Create Date: 2020/1/19
 * Profile: Xml Log
 */
public class MrXmlLog {
    public static void printXml(String tag, String xml, String headString) {

        tag = LogUtils.PREFIX + tag;

        if (xml != null) {
            xml = MrXmlLog.formatXML(xml);
            xml = headString + "\n" + xml;
        } else {
            xml = headString + LogUtils.NULL_TIPS;
        }

        MrPrintUtil.printLine(tag, true);
        String[] lines = xml.split(LogUtils.LINE_SEPARATOR);
        for (String line : lines) {
            if (!MrPrintUtil.isEmpty(line)) {
                Log.e(tag,"║ " + line);
            }
        }
        MrPrintUtil.printLine(tag, false);
    }

    public static String formatXML(String inputXML) {
        try {
            Source xmlInput = new StreamSource(new StringReader(inputXML));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString().replaceFirst(">", ">\n");
        } catch (Exception e) {
            e.printStackTrace();
            return inputXML;
        }
    }
}
