package puck.thrifty;

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

import puck.thrifty.Merger;
import puck.thrifty.NamespaceScope;

/**
 * <p>
 * Tests for the namespace functionality in the {@link org.gee.thrify.Merger}
 * class.
 * </p>
 */
public class MergerNamespaceTest {
   
   Logger logger = LoggerFactory.getLogger(getClass());

   /**
    * <p>
    * Verify that the other scopes are ignored when 
    * {@link puck.thrifty.NamespaceScope#ALL} is added to the namespace map.
    * </p>
    */
   @Test
   public void namespaceWithAllAndOthers() throws Exception {
      
      Merger m = new Merger();
      m.setInputFile(new File("src//test//resources//simple.json"));
      m.addNamespace(NamespaceScope.ALL, "hello.there");
      m.addNamespace(NamespaceScope.CPP, "hello.there.cpp");
      m.addNamespace(NamespaceScope.COCOA, "hello.there.cocoa");
      m.addNamespace(NamespaceScope.CSHARP, "hello.there.csharp");
      m.addNamespace(NamespaceScope.JAVA, "hello.there.java");
      m.addNamespace(NamespaceScope.PERL, "hello.there.perl");
      m.addNamespace(NamespaceScope.PYTHON, "hello.there.py");
      m.addNamespace(NamespaceScope.RUBY, "hello.there.ruby");
      
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
    * Verify that all scopes show up if {@link puck.thrifty.NamespaceScope#ALL}
    * is not added to the namespace map.
    * </p>
    */
   @Test
   public void namespaceWithOthersButWithoutAll() throws Exception {
      
      Merger m = new Merger();
      m.setInputFile(new File("src//test//resources//simple.json"));
      m.addNamespace(NamespaceScope.CPP, "hello.there.cpp");
      m.addNamespace(NamespaceScope.COCOA, "hello.there.cocoa");
      m.addNamespace(NamespaceScope.CSHARP, "hello.there.csharp");
      m.addNamespace(NamespaceScope.JAVA, "hello.there.java");
      m.addNamespace(NamespaceScope.PERL, "hello.there.perl");
      m.addNamespace(NamespaceScope.PYTHON, "hello.there.py");
      m.addNamespace(NamespaceScope.RUBY, "hello.there.ruby");
      
      String results = m.process();
      List<NamespaceLine> namespaces = this.parseForNamespaces(results);
      Assert.assertEquals("1", 7, namespaces.size());
      Assert.assertTrue("2", this.find(namespaces, NamespaceScope.CPP) != null);
      Assert.assertTrue("3", this.find(namespaces, NamespaceScope.COCOA) != null);
      Assert.assertTrue("4", this.find(namespaces, NamespaceScope.CSHARP) != null);
      Assert.assertTrue("5", this.find(namespaces, NamespaceScope.JAVA) != null);
      Assert.assertTrue("6", this.find(namespaces, NamespaceScope.PERL) != null);
      Assert.assertTrue("7", this.find(namespaces, NamespaceScope.PYTHON) != null);
      Assert.assertTrue("8", this.find(namespaces, NamespaceScope.RUBY) != null);
   }
   
   /**
    * <p>
    * Verify that only distinct namespace scopes appear in the thrift output
    * even when the scope is added multiple times.
    * </p>
    */
   @Test
   public void addDuplicateNamespaceScopes() throws Exception {
      
      Merger m = new Merger();
      m.setInputFile(new File("src//test//resources//simple.json"));
      m.addNamespace(NamespaceScope.PERL, "hello.there.perl1");
      m.addNamespace(NamespaceScope.COCOA, "hello.there.cocoa1");
      m.addNamespace(NamespaceScope.PERL, "hello.there.perl2");
      m.addNamespace(NamespaceScope.COCOA, "hello.there.cocoa2");
      m.addNamespace(NamespaceScope.PERL, "hello.there.perl3");
     
      String results = m.process();
      List<NamespaceLine> namespaces = this.parseForNamespaces(results);
      Assert.assertEquals("1", 2, namespaces.size());
      try {
         Assert.assertTrue("2", this.find(namespaces, NamespaceScope.PERL) != null);
      } catch (DuplicateException e) {
         Assert.fail("The PERL namespace appears more than once.");
      }
      try {
         Assert.assertTrue("3", this.find(namespaces, NamespaceScope.COCOA) != null);
      } catch (DuplicateException e) {
         Assert.fail("The COCOA namespace appears more than once.");
      }
   }

   /**
    * <p>
    * Verify the {@link puck.thrifty.NamespaceScope#ALL} namespace. 
    * </p>
    */
   @Test
   public void namespaceAll() throws Exception {
      
      Merger m = new Merger();
      m.setInputFile(new File("src//test//resources//simple.json"));
      m.addNamespace(NamespaceScope.ALL, "hello.there.all");
      String results = m.process();
      List<NamespaceLine> namespaces = this.parseForNamespaces(results);
      Assert.assertEquals("1", NamespaceScope.ALL.getCode(), namespaces.get(0).getNamespaceScope());
      Assert.assertEquals("2", "hello.there.all", namespaces.get(0).getIdentifier());
   }

   /**
    * <p>
    * Verify the {@link puck.thrifty.NamespaceScope#COCOA} namespace. 
    * </p>
    */
   @Test
   public void namespaceCocoa() throws Exception {
      
      Merger m = new Merger();
      m.setInputFile(new File("src//test//resources//simple.json"));
      m.addNamespace(NamespaceScope.COCOA, "hello.there.cocoa");
      String results = m.process();
      List<NamespaceLine> namespaces = this.parseForNamespaces(results);
      Assert.assertEquals("1", NamespaceScope.COCOA.getCode(), namespaces.get(0).getNamespaceScope());
      Assert.assertEquals("2", "hello.there.cocoa", namespaces.get(0).getIdentifier());
   }
   
   /**
    * <p>
    * Verify the {@link puck.thrifty.NamespaceScope#CPP} namespace. 
    * </p>
    */
   @Test
   public void namespaceCpp() throws Exception {
      
      Merger m = new Merger();
      m.setInputFile(new File("src//test//resources//simple.json"));
      m.addNamespace(NamespaceScope.CPP, "hello.there.cpp");
      String results = m.process();
      List<NamespaceLine> namespaces = this.parseForNamespaces(results);
      Assert.assertEquals("1", NamespaceScope.CPP.getCode(), namespaces.get(0).getNamespaceScope());
      Assert.assertEquals("2", "hello.there.cpp", namespaces.get(0).getIdentifier());
   }

   /**
    * <p>
    * Verify the {@link puck.thrifty.NamespaceScope#CSHARP} namespace. 
    * </p>
    */
   @Test
   public void namespaceCsharp() throws Exception {
      
      Merger m = new Merger();
      m.setInputFile(new File("src//test//resources//simple.json"));
      m.addNamespace(NamespaceScope.CSHARP, "hello.there.csharp");
      String results = m.process();
      List<NamespaceLine> namespaces = this.parseForNamespaces(results);
      Assert.assertEquals("1", NamespaceScope.CSHARP.getCode(), namespaces.get(0).getNamespaceScope());
      Assert.assertEquals("2", "hello.there.csharp", namespaces.get(0).getIdentifier());
   }
   
   /**
    * <p>
    * Verify the {@link puck.thrifty.NamespaceScope#JAVA} namespace. 
    * </p>
    */
   @Test
   public void namespaceJava() throws Exception {
      
      Merger m = new Merger();
      m.setInputFile(new File("src//test//resources//simple.json"));
      m.addNamespace(NamespaceScope.JAVA, "hello.there.java");
      String results = m.process();
      List<NamespaceLine> namespaces = this.parseForNamespaces(results);
      Assert.assertEquals("1", NamespaceScope.JAVA.getCode(), namespaces.get(0).getNamespaceScope());
      Assert.assertEquals("2", "hello.there.java", namespaces.get(0).getIdentifier());
   }

   /**
    * <p>
    * Verify the {@link puck.thrifty.NamespaceScope#PERL} namespace. 
    * </p>
    */
   @Test
   public void namespacePerl() throws Exception {
      
      Merger m = new Merger();
      m.setInputFile(new File("src//test//resources//simple.json"));
      m.addNamespace(NamespaceScope.PERL, "hello.there.perl");
      String results = m.process();
      List<NamespaceLine> namespaces = this.parseForNamespaces(results);
      Assert.assertEquals("1", NamespaceScope.PERL.getCode(), namespaces.get(0).getNamespaceScope());
      Assert.assertEquals("2", "hello.there.perl", namespaces.get(0).getIdentifier());
  }

   /**
    * <p>
    * Verify the {@link puck.thrifty.NamespaceScope#PYTHON} namespace. 
    * </p>
    */
   @Test
   public void namespacePython() throws Exception {
      
      Merger m = new Merger();
      m.setInputFile(new File("src//test//resources//simple.json"));
      m.addNamespace(NamespaceScope.PYTHON, "hello.there.python");
      String results = m.process();
      List<NamespaceLine> namespaces = this.parseForNamespaces(results);
      Assert.assertEquals("1", NamespaceScope.PYTHON.getCode(), namespaces.get(0).getNamespaceScope());
      Assert.assertEquals("2", "hello.there.python", namespaces.get(0).getIdentifier());
   }

   /**
    * <p>
    * Verify the {@link puck.thrifty.NamespaceScope#RUBY} namespace. 
    * </p>
    */
   @Test
   public void namespaceRuby() throws Exception {
      
      Merger m = new Merger();
      m.setInputFile(new File("src//test//resources//simple.json"));
      m.addNamespace(NamespaceScope.RUBY, "hello.there.ruby");
      String results = m.process();
      List<NamespaceLine> namespaces = this.parseForNamespaces(results);
      Assert.assertEquals("1", NamespaceScope.RUBY.getCode(), namespaces.get(0).getNamespaceScope());
      Assert.assertEquals("2", "hello.there.ruby", namespaces.get(0).getIdentifier());
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
   
   /**
    * <p>
    * Finds and returns the namespace line within namespaces that has the same
    * scope as provided. If the scope isn't found then null is returned. If
    * there is more than one namespace line that has the given scope then throw
    * an exception.
    * </p>
    * 
    * @param   namespaces A list of namespaces. Required.
    * @param   scope A namespace scope. Required.
    * @return  The namespace line that has a namespaceScope parameter that is
    *          equivalent to the scope parameter. Returns null if no matches
    *          found.
    * @throws  DuplicateException If more than one entry in namespaces has a
    *          namespaceScope equivelent to the scope parameter.
    */
   private NamespaceLine find(List<NamespaceLine> namespaces, NamespaceScope scope) throws DuplicateException {
      
      NamespaceLine matchingNamespace = null;
      for (int index = 0; index < namespaces.size(); index++) {
         if (scope.getCode().equals(namespaces.get(index).getNamespaceScope())) {
            if (matchingNamespace != null) throw new DuplicateException();
            matchingNamespace = namespaces.get(index);
         }
      }
      return matchingNamespace;
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
   
   static class DuplicateException extends Exception {
      private static final long serialVersionUID = 1L;
   }
}