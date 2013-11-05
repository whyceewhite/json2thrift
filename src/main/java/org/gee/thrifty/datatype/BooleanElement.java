package org.gee.thrifty.datatype;



public class BooleanElement extends AbstractElement implements Element {

   public BooleanElement(String elementName) {
      setName(elementName);
   }
   
   public String getDatatypeName() {
      return "bool";
   }
   
}
