package puck.thrifty.datatype;

import puck.thrifty.MergerException;


public class DoubleElement extends AbstractElement implements NumberElement {
   
   public DoubleElement(String elementName) {
      setName(elementName);
   }
   
   public String getDatatypeName() {
      return "double";
   }
   
   public Element merge(Element element) throws MergerException {
      if (element == null || this == element || element.isUnknown() || element instanceof NumberElement) {
         return this;
      }
      throw new MergerException(this, element);
   }

}
