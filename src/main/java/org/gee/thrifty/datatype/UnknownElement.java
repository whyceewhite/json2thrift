package org.gee.thrifty.datatype;

import org.gee.thrifty.exception.MergeException;


public class UnknownElement extends AbstractElement implements Element {
   
   public UnknownElement(String elementName) {
      setName(elementName);
   }
   
   public boolean isUnknown() {
      return true;
   }

   public String getDatatypeName() {
      // TODO: maybe default to "string"?
      return "unknown";
   }
   
   public Element merge(Element element) throws MergeException {
      // This is unknown so always take on the type being compared to.
      return element;
   }

}
