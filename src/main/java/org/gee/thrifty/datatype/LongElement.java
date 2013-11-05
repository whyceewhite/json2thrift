package org.gee.thrifty.datatype;

import org.gee.thrifty.exception.MergeException;



public class LongElement extends AbstractElement implements NumberElement {

   public LongElement(String elementName) {
      setName(elementName);
   }
   
   public String getDatatypeName() {
      return "i64";
   }
   
   public Element merge(Element element) throws MergeException {
      if (element == null) {
         return this;
      } else if (element instanceof DoubleElement) {
         return element;
      } else if (element instanceof NumberElement) {
         return this;
      }
      throw new MergeException("The element type of " + element.getClass().getName() + " is incompatible with " + this.getClass().getName());
   }

}
