package org.gee.thrifty;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;

import org.gee.thrifty.datatype.BooleanElement;
import org.gee.thrifty.datatype.Element;
import org.gee.thrifty.datatype.ObjectElement;
import org.junit.Assert;
import org.junit.Test;

/**
 * <p>
 * Unit test for the {@link org.gee.thrifty.Converter} class.
 * </p>
 */
public class ConverterTest {
   
   /**
    * <p>
    * Verify that the absence of a root name assignment results in the default
    * root name.
    * </p>
    */
   @Test
   public void verifyDefaultRootName() {
      Converter c = new Converter();
      ObjectElement obj = c.parse("{}");
      Assert.assertEquals("root", obj.getName());
   }
   
   /**
    * <p>
    * Verify that the assignment of a root name is applied to the parsed object
    * result.
    * </p>
    */
   @Test
   public void verifyOverriddenRootName() {
      Converter c = new Converter("test");
      ObjectElement obj = c.parse("{}");
      Assert.assertEquals("test", obj.getName());
   }
   
   /**
    * <p>
    * Verify that an element with a value of true will translate to a
    * BooleanElement.
    * </p>
    */
   @Test
   public void trueValueGoesToBoolean() {
      Converter c = new Converter();
      ObjectElement obj = c.parse("{ \"boolVal\" : true }");
      Map<String, Element> elemMap = obj.getElements();
      Assert.assertEquals(1, elemMap.size());
      Assert.assertTrue(elemMap.containsKey("boolVal"));
      Assert.assertTrue(elemMap.get("boolVal").getClass().equals(BooleanElement.class));
   }
   
   /**
    * <p>
    * Verify that an element with a value of false will translate to a
    * BooleanElement.
    * </p>
    */
   @Test
   public void falseValueGoesToBoolean() {
      Converter c = new Converter();
      ObjectElement obj = c.parse("{ \"boolVal\" : false }");
      Map<String, Element> elemMap = obj.getElements();
      Assert.assertEquals(1, elemMap.size());
      Assert.assertTrue(elemMap.containsKey("boolVal"));
      Assert.assertTrue(elemMap.get("boolVal").getClass().equals(BooleanElement.class));
   }
   
   /**
    * <p>
    * Reads data from a file and stores it in a string. The string containing
    * the file contents is returned.
    * </p>
    * 
    * @param resourceName The name of the resource that contains the data to
    *       read and return as a string.
    * @return The contents from resourceName file.
    * @throws IOException If an error occurs while reading the resource file.
    */
   private String readDataFromFile(String resourceName) throws IOException {
      
      ClassLoader loader = this.getClass().getClassLoader();
      InputStream configInputStream = loader.getResourceAsStream(resourceName);
      return toString(configInputStream);
   }
   
   /**
    * <p>
    * Reads the given file's contents and returns it as a string.
    * </p>
    * 
    * @param inputStream The input stream of bytes to turn into a string.
    * @return The contents from resourceName file.
    * @throws IOException If an error occurs while reading the resource file.
    */
   private String toString(InputStream inputStream) throws IOException {
      
      if (inputStream == null) return null;
      
      StringBuffer stringBuffer = new StringBuffer();
      InputStreamReader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

      char[] charBuffer = new char[1024];
      int charsRead = reader.read(charBuffer);
      while (charsRead > 0) {
         stringBuffer.append(charBuffer, 0, charsRead);
         charsRead = reader.read(charBuffer);
      }
      reader.close();
      
      return stringBuffer.toString();
   }

}
