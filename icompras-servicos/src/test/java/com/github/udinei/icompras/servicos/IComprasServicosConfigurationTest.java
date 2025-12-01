package com.github.udinei.icompras.servicos;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Unit tests to verify iCompras Servicos configuration
 * Requirements: 5.1, 5.2, 5.3, 5.4, 5.5, 5.6, 5.7, 5.8, 5.9, 5.10
 */
class IComprasServicosConfigurationTest {

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
    void testPomXmlHasCorrectArtifactNameAndGroupId() throws Exception {
        Document doc = parsePomXml();
        Element root = doc.getDocumentElement();
        
        String artifactId = getDirectChildValue(root, "artifactId");
        assertEquals("icompras-servicos", artifactId, "Artifact ID should be 'icompras-servicos'");
        
        String name = getDirectChildValue(root, "name");
        assertEquals("icompras-servicos", name, "Project name should be 'icompras-servicos'");
        
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

    @Test
    void testDirectoryStructureExists() {
        Path dockerPostgres = Paths.get("docker", "postgres");
        Path dockerBroker = Paths.get("docker", "broker");
        Path dockerStorage = Paths.get("docker", "storage");
        
        assertTrue(Files.exists(dockerPostgres), "docker/postgres directory should exist");
        assertTrue(Files.isDirectory(dockerPostgres), "docker/postgres should be a directory");
        
        assertTrue(Files.exists(dockerBroker), "docker/broker directory should exist");
        assertTrue(Files.isDirectory(dockerBroker), "docker/broker should be a directory");
        
        assertTrue(Files.exists(dockerStorage), "docker/storage directory should exist");
        assertTrue(Files.isDirectory(dockerStorage), "docker/storage should be a directory");
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
            if (children.item(i) instanceof Element child) {
                if (child.getTagName().equals(tagName)) {
                    return child.getTextContent().trim();
                }
            }
        }
        return null;
    }
}
