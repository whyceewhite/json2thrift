package org.gee.thrifty;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Tests the Merger class.
 * </p>
 */
public class MergerTest {
   
   private Logger logger = LoggerFactory.getLogger(getClass());

   /**
    * <p>
    * Verify that the other scopes are ignored when 
    * {@link org.gee.thrifty.NamespaceScope#ALL} is added to the namespace map.
    * </p>
    */
   @Test
   public void namespaceWithAllAndOthers() throws Exception {
      
      Merger m = new Merger();
      m.setInputFile(new File("src//test//resources//simple.json"));
      m.addNamespace(NamespaceScope.ALL, "hello.there");
      m.addNamespace(NamespaceScope.JAVA, "hello.there.java");
      m.addNamespace(NamespaceScope.PYTHON, "hello.there.py");
      m.addNamespace(NamespaceScope.CPP, "hello.there.cpp");
      
      try {
         String results = m.process();
         List<NamespaceLine> namespaces = this.parseForNamespaces(results);
         Assert.assertEquals("1", 1, namespaces.size());
         Assert.assertEquals("2", NamespaceScope.ALL.getCode(), namespaces.get(0).getNamespaceScope());
         Assert.assertEquals("3", "hello.there", namespaces.get(0).getIdentifier());
      } catch (Exception e) {
         logger.error("", e);
         throw e;
      }
   }
   
   /**
    * <p>
    * Verify that all scopes show up if {@link org.gee.thrifty.NamespaceScope#ALL}
    * is not added to the namespace map.
    * </p>
    */
   public void namespaceWithOthersButWithoutAll() throws Exception {
   }

   /**
    * <p>
    * Verify the {@link org.gee.thrifty.NamespaceScope#ALL} namespace. 
    * </p>
    */
   public void namespaceAll() {
   }
   
   /**
    * <p>
    * Verify the {@link org.gee.thrifty.NamespaceScope#CPP} namespace. 
    * </p>
    */
   public void namespaceCpp() {
   }

   /**
    * <p>
    * Verify the {@link org.gee.thrifty.NamespaceScope#COCOA} namespace. 
    * </p>
    */
   public void namespaceCocoa() {
   }

   /**
    * <p>
    * Verify the {@link org.gee.thrifty.NamespaceScope#CSHARP} namespace. 
    * </p>
    */
   public void namespaceCsharp() {
   }
   
   /**
    * <p>
    * Verify the {@link org.gee.thrifty.NamespaceScope#JAVA} namespace. 
    * </p>
    */
   public void namespaceJava() {
   }

   /**
    * <p>
    * Verify the {@link org.gee.thrifty.NamespaceScope#PERL} namespace. 
    * </p>
    */
   public void namespacePerl() {
   }

   /**
    * <p>
    * Verify the {@link org.gee.thrifty.NamespaceScope#PYTHON} namespace. 
    * </p>
    */
   public void namespacePython() {
   }

   /**
    * <p>
    * Verify the {@link org.gee.thrifty.NamespaceScope#RUBY} namespace. 
    * </p>
    */
   public void namespaceRuby() {
   }
   
   /**
    * <p>
    * </p>
    * 
    * @param definition The resulting generated thrift definition. 
    * @param ns The namespace value.
    */
   private List<NamespaceLine> parseForNamespaces(String definition) {
      
      ArrayList<NamespaceLine> namespaceList = new ArrayList<NamespaceLine>();
      Pattern pattern = Pattern.compile(createNamespaceLinePattern());
      
      Scanner scanner = new Scanner(definition);
      while (scanner.hasNextLine()) {
         String line = scanner.nextLine();
         Matcher matcher = pattern.matcher(line);
         if (matcher.matches()) {
            NamespaceLine ns = new NamespaceLine(matcher.group(2), matcher.group(3));
            namespaceList.add(ns);
         }
      }
      scanner.close();
      
      return namespaceList;
   }
   
   /**
    * <p>
    * Returns the namespace regex pattern as it would appear in a thrift
    * definition file.
    * </p>
    * 
    * @return  The regex pattern string for the namespace line in a thrift
    *          definition file.
    */
   private String createNamespaceLinePattern() {
      
      return new StringBuffer()
         .append("(namespace) ")
         .append("(")
         .append("\\").append(NamespaceScope.ALL.getCode()).append('|')
         .append(NamespaceScope.CPP.getCode()).append('|')
         .append(NamespaceScope.JAVA.getCode()).append('|')
         .append(NamespaceScope.PYTHON.getCode()).append('|')
         .append(NamespaceScope.PERL.getCode()).append('|')
         .append(NamespaceScope.RUBY.getCode()).append('|')
         .append(NamespaceScope.COCOA.getCode()).append('|')
         .append(NamespaceScope.CSHARP.getCode())
         .append(") ")
         .append("([\\w.]+)")
         .toString();
   }
   
   static class NamespaceLine {
      
      String namespaceScope;
      String identifier;
      
      public NamespaceLine(String namespaceScope, String identifier) {
         
         this.namespaceScope = namespaceScope;
         this.identifier = identifier;
      }
      
      public String getNamespaceScope() {
         return namespaceScope;
      }
      
      public void setNamespaceScope(String namespaceScope) {
         this.namespaceScope = namespaceScope;
      }
      
      public String getIdentifier() {
         return identifier;
      }
      
      public void setIdentifier(String identifier) {
         this.identifier = identifier;
      }
      
   }
}