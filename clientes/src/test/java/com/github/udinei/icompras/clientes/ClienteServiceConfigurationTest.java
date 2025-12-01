package com.github.udinei.icompras.clientes;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests to verify Cliente Service configuration
 * Requirements: 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 1.10
 */
class ClienteServiceConfigurationTest {

    private static final String POM_PATH = "pom.xml";

    @Test
    void testPomXmlContainsJava21() throws Exception {
        Document doc = parsePomXml();
        
        // Check java.version property
        String javaVersion = getPropertyValue(doc, "java.version");
        assertEquals("21", javaVersion, "pom.xml should contain Java version 21");
        
        // Check maven.compiler.source
        String compilerSource = getPropertyValue(doc, "maven.compiler.source");
        assertEquals("21", compilerSource, "maven.compiler.source should be 21");
        
        // Check maven.compiler.target
        String compilerTarget = getPropertyValue(doc, "maven.compiler.target");
        assertEquals("21", compilerTarget, "maven.compiler.target should be 21");
    }

    @Test
    void testPomXmlContainsSpringBoot344() throws Exception {
        Document doc = parsePomXml();
        
        // Check parent version
        NodeList parentList = doc.getElementsByTagName("parent");
        assertTrue(parentList.getLength() > 0, "pom.xml should have a parent element");
        
        Element parent = (Element) parentList.item(0);
        String version = getElementValue(parent, "version");
        assertEquals("3.4.4", version, "Spring Boot version should be 3.4.4");
        
        String groupId = getElementValue(parent, "groupId");
        assertEquals("org.springframework.boot", groupId, "Parent should be Spring Boot");
    }

    @Test
    void testPomXmlContainsAllRequiredDependencies() throws Exception {
        Document doc = parsePomXml();
        NodeList dependencies = doc.getElementsByTagName("dependency");
        
        boolean hasSpringWeb = false;
        boolean hasSpringDataJpa = false;
        boolean hasLombok = false;
        boolean hasPostgresql = false;
        
        for (int i = 0; i < dependencies.getLength(); i++) {
            Element dependency = (Element) dependencies.item(i);
            String artifactId = getElementValue(dependency, "artifactId");
            
            if ("spring-boot-starter-web".equals(artifactId)) {
                hasSpringWeb = true;
            } else if ("spring-boot-starter-data-jpa".equals(artifactId)) {
                hasSpringDataJpa = true;
            } else if ("lombok".equals(artifactId)) {
                hasLombok = true;
            } else if ("postgresql".equals(artifactId)) {
                hasPostgresql = true;
            }
        }
        
        assertTrue(hasSpringWeb, "pom.xml should contain Spring Web dependency");
        assertTrue(hasSpringDataJpa, "pom.xml should contain Spring Data JPA dependency");
        assertTrue(hasLombok, "pom.xml should contain Lombok dependency");
        assertTrue(hasPostgresql, "pom.xml should contain PostgreSQL Driver dependency");
    }

    @Test
    void testPomXmlHasCorrectArtifactNameAndGroupId() throws Exception {
        Document doc = parsePomXml();
        Element root = doc.getDocumentElement();
        
        String artifactId = getDirectChildValue(root, "artifactId");
        assertEquals("clientes", artifactId, "Artifact ID should be 'clientes'");
        
        String name = getDirectChildValue(root, "name");
        assertEquals("clientes", name, "Project name should be 'clientes'");
        
        String groupId = getDirectChildValue(root, "groupId");
        assertEquals("com.github.udinei.icompras", groupId, "Group ID should be 'com.github.udinei.icompras'");
    }

    @Test
    void testPomXmlHasJarPackaging() throws Exception {
        Document doc = parsePomXml();
        Element root = doc.getDocumentElement();
        
        String packaging = getDirectChildValue(root, "packaging");
        assertEquals("jar", packaging, "Packaging should be JAR");
    }

    // Helper methods
    
    private Document parsePomXml() throws Exception {
        File pomFile = new File(POM_PATH);
        assertTrue(pomFile.exists(), "pom.xml file should exist at " + POM_PATH);
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(pomFile);
    }
    
    private String getPropertyValue(Document doc, String propertyName) {
        NodeList properties = doc.getElementsByTagName("properties");
        if (properties.getLength() > 0) {
            Element propertiesElement = (Element) properties.item(0);
            return getElementValue(propertiesElement, propertyName);
        }
        return null;
    }
    
    private String getElementValue(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent().trim();
        }
        return null;
    }
    
    private String getDirectChildValue(Element parent, String tagName) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Element) {
                Element child = (Element) children.item(i);
                if (child.getTagName().equals(tagName)) {
                    return child.getTextContent().trim();
                }
            }
        }
        return null;
    }
}
