package org.gee.thrifty;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MergerTest {
   
   private Logger logger = LoggerFactory.getLogger(getClass());

   /**
    * <p>
    * Verify that the other scopes are ignored when 
    * {@link org.gee.thrifty.NamespaceScope#ALL} is added to the namespace map.
    * </p>
    */
   @Test
   public void namespaceWithAllAndOthers() {
      Merger m = new Merger();
      m.setInputFile(new File("src//test//resources//simple.json"));
      m.addNamespace(NamespaceScope.ALL, "hello.there");
      m.addNamespace(NamespaceScope.JAVA, "hello.there.java");
      m.addNamespace(NamespaceScope.PYTHON, "hello.there.py");
      m.addNamespace(NamespaceScope.CPP, "hello.there.cpp");
      try {
         String results = m.merge();
         logger.info(results);
         int index = results.indexOf("namespace * hello.there");
         Assert.assertTrue(index >= 0);
      } catch (IOException e) {
         logger.error("", e);
         Assert.fail("An unexpected exception occurred.");
      }
   }
   
   /**
    * <p>
    * Verify that all scopes show up if {@link org.gee.thrifty.NamespaceScope#ALL}
    * is not added to the namespace map.
    * </p>
    */
   public void namespaceWithOthersButWithoutAll() {
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
}