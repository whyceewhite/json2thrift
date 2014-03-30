package puck.thrifty.datatype;

import puck.thrifty.MergerException;


public class IntegerElement extends AbstractElement implements NumberElement {
   
   public IntegerElement(String elementName) {
      setName(elementName);
   }
   
   public String getDatatypeName() {
      return "i32";
   }
   
   public Element merge(Element element) throws MergerException {
      if (element == null || this == element || element.isUnknown() || this.getClass().equals(element.getClass())) {
         return this;
      } else if (element instanceof NumberElement) {
         return element;
      }
      throw new MergerException(this, element);
   }

}
