package org.gee.thrifty.datatype;

import org.gee.thrifty.exception.MergeException;


public class DoubleElement extends AbstractElement implements NumberElement {
   
   public DoubleElement(String elementName) {
      setName(elementName);
   }
   
   public String getDatatypeName() {
      return "double";
   }
   
   public Element merge(Element element) throws MergeException {
      if (element == null || element instanceof NumberElement) {
         return this;
      }
      throw new MergeException("The element type of " + element.getClass().getName() + " is incompatible with " + this.getClass().getName());
   }

}
