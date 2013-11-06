package org.gee.thrifty;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.gee.thrifty.datatype.BooleanElement;
import org.gee.thrifty.datatype.DoubleElement;
import org.gee.thrifty.datatype.Element;
import org.gee.thrifty.datatype.IntegerElement;
import org.gee.thrifty.datatype.ListElement;
import org.gee.thrifty.datatype.LongElement;
import org.gee.thrifty.datatype.ObjectElement;
import org.gee.thrifty.datatype.StringElement;
import org.gee.thrifty.datatype.UnknownElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;

/**
 * Suggest that if all the objects passed in have an element then that element is required.
 *
 */
public class Converter {
   
   private Logger logger = LoggerFactory.getLogger(getClass());
   private ObjectElement root;
   private String rootName;
   
   /*
   public static void main(String[] args) {
      Converter c = new Converter();
      FileInputStream fileStream = null;
      try {
         File file = new File("/Users/ywhite/Development/workspace/gnip/gnip-parser/src/test/resources/1382906810623.output.json");
         fileStream = new FileInputStream(file);
         c.read(fileStream);
      } catch (Exception e) {
         c.logger.error("", e);
      } finally {
         try {
            fileStream.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }
   */
   
   public Converter() {
      this("root");
   }
   
   public Converter(String name) {
      this.rootName = name;
   }
   
   public ObjectElement getRoot() {
      return this.root;
   }
   
   public static void main(String[] args) throws Exception {
      
//      String json = "{\"isOpen\" : false, \"id\" : 1910032223334, \"order\" : 28, \"person\" : {\"fName\" : \"yvette\", \"lName\" : \"white\"} }";
//      String json = "{\"veggies\" : [\"aubergine\", \"carrot\"], \"ids\" : [1239, 1222, 8], \"amount\" : 28.23}";
//      String json = "{\"veggies\" : [\"aubergine\", \"carrot\"], \"mixed\" : [\"amour\", 1222, false]}";
//      String json = "{\"veggies\" : [ { \"name\" : \"aubergine\", \"color\" : \"purple\" }, { \"name\" : \"carrot\", \"color\" : \"orange\" } ] }";
//      String json1 = "{\"isOpen\" : false, \"id\" : 334, \"order\" : [true, false], \"person\" : {\"fName\" : \"yvette\", \"lName\" : \"white\"} }";
//      String json2 = "{\"isOpen\" : true, \"id\" : 1910032223334, \"person\" : {\"fName\" : \"david\", \"lName\" : \"franklin\", \"mName\" : \"jackman\"}, \"rankings\" : [ { \"rank\" : 2, \"state\" : \"FL\" }, { \"rank\" : 3, \"state\" : \"MD\" } ] }";
   }
   
   public void parse(String json) {
      
      ObjectMapper mapper = new ObjectMapper();
      MapType type = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
      Map<String, Object> datamap = null;
      try {
         datamap = mapper.readValue(json, type);
      } catch (Exception e) {
         logger.error("An error occurred while parsing the given JSON string.", e);
      }
      
      this.root = asElement(this.rootName, datamap);
   }
   
   public ObjectElement asElement(String name, Map<String, Object> elementsMap) {
      
      ObjectElement rootElement = new ObjectElement(name);
      
      for (Iterator<String> i = elementsMap.keySet().iterator(); i.hasNext();) {
         String key = i.next();
         Object value = elementsMap.get(key);
         String classname = (value == null ? "" : value.getClass().getName());
         logger.debug("key = " + key);
         logger.debug("value = " + value);
         logger.debug("class = " + classname);
         
         Element element = asElement(key, value); 
         rootElement.add(element);
      }
      return rootElement;
   }
   
   /**
    * <p>
    * The assumption is that for thrift lists, the list elements have the same
    * data type. Thus, if the list contains datatypes that are not
    * @param key
    * @param values
    */
   @SuppressWarnings("rawtypes")
   private Element asListElement(String key, List values) {
      
      Element listType = new UnknownElement(key);
      Element currentListType = null;
      for (int i = 0; i < values.size(); i++) {
         Object o = values.get(i);
         logger.debug("value = " + o + " | type = " + o.getClass().getName());
         currentListType = asElement(key, o);
         listType = listType.merge(currentListType);
      }
      // If no exception was thrown then our listType is the element type of our list.
      return new ListElement(listType);
   }
   
   @SuppressWarnings("unchecked")
   private Element asElement(String key, Object value) {
      
      if (value != null) {
         if (value instanceof Boolean) {
            return new BooleanElement(key);
         } else if (value instanceof Integer) {
            return new IntegerElement(key);
         } else if (value instanceof Long) {
            return new LongElement(key);
         } else if (value instanceof String) {
            return new StringElement(key);
         } else if (value instanceof List) {
            return asListElement(key, (List)value);
         } else if (value instanceof Map) {
            return asElement(key, (Map<String, Object>) value);
         } else if (value instanceof Double) {
            return new DoubleElement(key);
         }
      } 
      return new UnknownElement(key);
   }
      

}
