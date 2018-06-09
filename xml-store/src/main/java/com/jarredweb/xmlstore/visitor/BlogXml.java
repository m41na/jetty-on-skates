package com.jarredweb.xmlstore.visitor;

import com.jarredweb.xmlstore.model.Blog;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class BlogXml {
    
    public static void main(String...args){        
        Blog blog = null;
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try {
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(Blog.class.getResourceAsStream("blogs.xml"));
            while(xmlEventReader.hasNext()){
                XMLEvent xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent.isStartElement()){
                   StartElement startElement = xmlEvent.asStartElement();
                   if(startElement.getName().getLocalPart().equals("blog")){
                       blog = new Blog();
                   }
                }
            }
        }
        catch (XMLStreamException e) {
            e.printStackTrace(System.err);
        }
    }
}
