package com.example.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.example.entity.User;
import com.example.entity.Proj;
import com.example.entity.Task;
import com.example.entity.Token;

import javax.xml.parsers.*;
import javax.xml.xpath.*;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import javax.xml.stream.util.*;
import javax.xml.namespace.QName;

public class HibernateUtil {
    private static SessionFactory sessionFactory;
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            XmlResourceDataPuller xmlData = new XmlResourceDataPuller("hibernate.cfg.xml");
            xmlData.getDocumentFromXmlFile();
            String baseTags = "//session-factory/property";
            try {
                Configuration configuration = new Configuration();
                // Hibernate settings equivalent to hibernate.cfg.xml's properties
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, xmlData.getAttFromCfg(baseTags+"[@name=\"connection.driver_class\"]"));
                settings.put(Environment.URL, xmlData.getAttFromCfg(baseTags+"[@name=\"connection.url\"]"));
                settings.put(Environment.USER, xmlData.getAttFromCfg(baseTags+"[@name=\"connection.username\"]"));
                //settings.put(Environment.PASS, "passpass");
                settings.put(Environment.PASS, xmlData.getAttFromCfg(baseTags+"[@name=\"connection.password\"]"));
                settings.put(Environment.DIALECT, xmlData.getAttFromCfg(baseTags+"[@name=\"dialect\"]"));
                settings.put(Environment.SHOW_SQL, xmlData.getAttFromCfg(baseTags+"[@name=\"show_sql\"]"));
                //settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                settings.put(Environment.HBM2DDL_AUTO, xmlData.getAttFromCfg(baseTags+"[@name=\"hbm2ddl.auto\"]"));
                settings.put(Environment.AUTOCOMMIT, xmlData.getAttFromCfg(baseTags+"[@name=\"hibernate.connection.autocommit\"]"));
                configuration.setProperties(settings);
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Proj.class);
                configuration.addAnnotatedClass(Task.class);
                configuration.addAnnotatedClass(Token.class);
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}

class XmlResourceDataPuller{
    ClassLoader classLoader;
    InputStream inputStream;
    String fileName = "";
	
	DocumentBuilderFactory factory;
	DocumentBuilder builder;
	Document document;
	Element root;
	
	public XmlResourceDataPuller(String _fileName) {
		fileName = _fileName;
        classLoader = getClass().getClassLoader();
        
        factory = DocumentBuilderFactory.newInstance();
	}
	
	private void getInputStream() {
        inputStream = classLoader.getResourceAsStream(fileName);
	}
	
	public void getDocumentFromXmlFile() {
		try {
			builder = factory.newDocumentBuilder();
	        inputStream = classLoader.getResourceAsStream(fileName);
		    document = builder.parse(inputStream);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
    
    public String getAttFromCfg(String pathXpr) {

        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
    	XPathExpression expr;
    	NodeList nl = null;
		try {
			expr = xpath.compile(pathXpr);
			nl = ((NodeList) expr.evaluate(document, XPathConstants.NODESET));
	        System.out.println("Trying to get "+pathXpr);
			if(nl != null && nl.getLength() != 0)
				System.out.println("found: "+nl.item(0).getTextContent());
			else
				return "";
		} catch (XPathExpressionException e1) {
			e1.printStackTrace();
			return "";
		}
        return nl.item(0).getTextContent();
    }
}