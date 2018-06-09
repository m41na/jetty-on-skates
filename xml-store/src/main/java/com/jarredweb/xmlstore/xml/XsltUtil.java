package com.jarredweb.xmlstore.xml;

public class XsltUtil {

    public static void main(String... args) {
        org.apache.xalan.xslt.Process.main(new String[]{"-IN", "www/public/xml/blogpost.xml", "-XSL", "www/public/xsl/blogpost.xsl", "-out", "www/public/blogpost.html"});
    }
}
