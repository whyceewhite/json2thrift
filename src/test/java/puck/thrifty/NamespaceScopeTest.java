package puck.thrifty;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import puck.thrifty.NamespaceScope;

/**
 * <p>
 * Tests the {@link org.gee.thrify.NamespaceScope} enum.
 * </p>
 */
public class NamespaceScopeTest {
   
   Logger logger = LoggerFactory.getLogger(getClass());

   /**
    * <p>
    * Verify that {@link org.gee.thrify.NamespaceScope#ALL} returns the
    * expected code value.
    */
   @Test
   public void verifyGetCodeForAll() {
      Assert.assertEquals("*", NamespaceScope.ALL.getCode());
   }

   /**
    * <p>
    * Verify that {@link org.gee.thrify.NamespaceScope#COCOA} returns the
    * expected code value.
    */
   @Test
   public void verifyGetCodeForCocoa() {
      Assert.assertEquals("cocoa", NamespaceScope.COCOA.getCode());
   }
   
   /**
    * <p>
    * Verify that {@link org.gee.thrify.NamespaceScope#CPP} returns the
    * expected code value.
    */
   @Test
   public void verifyGetCodeForCpp() {
      Assert.assertEquals("cpp", NamespaceScope.CPP.getCode());
   }

   /**
    * <p>
    * Verify that {@link org.gee.thrify.NamespaceScope#CSHARP} returns the
    * expected code value.
    */
   @Test
   public void verifyGetCodeForCsharp() {
      Assert.assertEquals("csharp", NamespaceScope.CSHARP.getCode());
   }

   /**
    * <p>
    * Verify that {@link org.gee.thrify.NamespaceScope#JAVA} returns the
    * expected code value.
    */
   @Test
   public void verifyGetCodeForJava() {
      Assert.assertEquals("java", NamespaceScope.JAVA.getCode());
   }

   /**
    * <p>
    * Verify that {@link org.gee.thrify.NamespaceScope#PERL} returns the
    * expected code value.
    */
   @Test
   public void verifyGetCodeForPerl() {
      Assert.assertEquals("perl", NamespaceScope.PERL.getCode());
   }

   /**
    * <p>
    * Verify that {@link org.gee.thrify.NamespaceScope#PYTHON} returns the
    * expected code value.
    */
   @Test
   public void verifyGetCodeForPython() {
      Assert.assertEquals("py", NamespaceScope.PYTHON.getCode());
   }

   /**
    * <p>
    * Verify that {@link org.gee.thrify.NamespaceScope#RUBY} returns the
    * expected code value.
    */
   @Test
   public void verifyGetCodeForRuby() {
      Assert.assertEquals("rb", NamespaceScope.RUBY.getCode());
   }
}
