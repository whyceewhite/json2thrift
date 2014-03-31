package puck.thrifty;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import puck.thrifty.datatype.BooleanElement;
import puck.thrifty.datatype.DoubleElement;
import puck.thrifty.datatype.Element;
import puck.thrifty.datatype.IntegerElement;
import puck.thrifty.datatype.ListElement;
import puck.thrifty.datatype.LongElement;
import puck.thrifty.datatype.ObjectElement;
import puck.thrifty.datatype.StringElement;
import puck.thrifty.datatype.UnknownElement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;

/**
 * <p>
 * Given a JSON string, converts it into an {@link puck.thrifty.datatype.ObjectElement}
 * object which represents the structure of given JSON string.
 * </p>
 * <p>
 * To convert a JSON string into an ObjectElement, instantiate the class and
 * provide a name for the root structure. If no name is given then a default
 * root structure name is used.<br>
 * Call the {@link #parse(String)} method with the JSON string that needs to be
 * converted and returned is the JSON as an ObjectElement.
 * </p>
 * <p>
 * The {@link #parse(String)} method may be called repeatedly with new strings.
 * However, because the root structure name is set upon instantiation, all parse
 * calls will result in a root structure with the same name.
 * </p> 
 * 
 * @author ywhite
 */
public class Converter {
   
   private static Logger logger = LoggerFactory.getLogger(Converter.class);
   
   private final static String ROOT_STRUCT_NAME = "root";
   
   private String rootName;
   
   /**
    * <p>
    * Creates an instance of this class. The root structure name will default
    * to "root".
    * </p>
    */
   public Converter() {
      this(ROOT_STRUCT_NAME);
   }
   
   /**
    * <p>
    * Creates an instance of this class. The root structure name will be the
    * value provided by name.
    * </p>
    * 
    * @param   rootName The name of the root structure. If the name is null or
    *          empty then a default structure name is used.
    */
   public Converter(String rootName) {
      this.setRootName(rootName);
   }
   
   /**
    * <p>
    * Parses the given JSON string and converts it into an ObjectElement object
    * that represents the structure of the JSON object.
    * </p>
    * 
    * @param   json The JSON object to parse and convert into an ObjectElement.
    * @return  The JSON string converted into an ObjectElement representation.
    */
   public ObjectElement parse(String json) {
      
      ObjectMapper mapper = new ObjectMapper();
      MapType type = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
      Map<String, Object> datamap = null;
      try {
         datamap = mapper.readValue(json, type);
      } catch (Exception e) {
         logger.error("An error occurred while parsing the JSON string below:\n" + json, e);
      }
      
      return asElement(this.rootName, datamap);
   }
   
   /**
    * <p>
    * Makes an ObjectElement from the map of JSON elements.
    * </p>
    * 
    * @param   name The name of the object structure. This name is used as the
    *          name for the thrift struct that this object map represents.  
    * @param   elementsMap A JSON object represented as a map of elements.
    * @return  The elementsMap as an ObjectElement object.
    */
   protected ObjectElement asElement(String name, Map<String, Object> elementsMap) {
      
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
    * Translates an object value into a corresponding {@link puck.thrifty.datatype.Element}
    * based on its data type. The name of the object will be the key.
    * </p>
    * 
    * @param   key The JSON element name.
    * @param   value The JSON element value that corresponds to the key.
    * @return  The key and value as an Element that represents the data type of
    *          the value.
    */
   @SuppressWarnings({ "unchecked", "rawtypes" })
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
   
   /**
    * <p>
    * The assumption is that for thrift lists, the list elements have the same
    * data type. Thus, if the list of values contains datatypes that are not
    * equivalent then an exception is thrown.
    * </p>
    * 
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
      return new ListElement(listType);
   }
      
   /**
    * <p>
    * Sets the structure name of the root object. If the root name given is
    * null or empty then the default structure name is used.
    * </p>
    *  
    * @param rootName The name of the root structure. If the name is null or
    *       empty then a default structure name is used.
    */
   private void setRootName(String rootName) {
      this.rootName = (rootName == null || rootName.trim().isEmpty()) ? ROOT_STRUCT_NAME : rootName.trim();
   }

}
