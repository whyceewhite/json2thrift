package puck.thrifty.datatype;

import puck.thrifty.exception.MergeException;


public class DoubleElement extends AbstractElement implements NumberElement {
   
   public DoubleElement(String elementName) {
      setName(elementName);
   }
   
   public String getDatatypeName() {
      return "double";
   }
   
   public Element merge(Element element) throws MergeException {
      if (element == null || this == element || element.isUnknown() || element instanceof NumberElement) {
         return this;
      }
      throw new MergeException(this, element);
   }

}
