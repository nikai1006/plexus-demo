package com.nikai.demo.plexus.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class DomUtil {

    public static Document readDom(String xml) throws DocumentException {
        SAXReader builder = new SAXReader();
        Document doc = builder.read(xml);
        return doc;
    }

    public static Document readDom(File file)
        throws IOException, DocumentException {
        SAXReader builder = new SAXReader();
        Document doc = builder.read(file);
        return doc;
    }

    /**
     * @param indent 空格
     * @param newlines 是否换行
     */
    public static void writeDom2File(File file, Document doc, String indent, boolean newlines)
        throws IOException, FileNotFoundException {
        XMLWriter out = null;
        try {
            out = new XMLWriter(new FileOutputStream(file), new OutputFormat(indent, newlines));
            out.write(doc);
            out.flush();
        } catch (UnsupportedEncodingException e) {
            throw e;
        } catch (FileNotFoundException e) {
            throw e;
        } finally {
            out.close();
        }

    }

    public static void writeDom2File(File file, Document doc, boolean newlines)
        throws IOException, FileNotFoundException {
        writeDom2File(file, doc, "    ", newlines);
    }

    public static void writeDom2File(File file, Document doc)
        throws IOException, FileNotFoundException {
        writeDom2File(file, doc, true);
    }

}
