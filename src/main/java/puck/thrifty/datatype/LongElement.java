package puck.thrifty.datatype;

import puck.thrifty.MergerException;



public class LongElement extends AbstractElement implements NumberElement {

   public LongElement(String elementName) {
      setName(elementName);
   }
   
   public String getDatatypeName() {
      return "i64";
   }
   
   public Element merge(Element element) throws MergerException {
      if (element == null || this == element || element.isUnknown() || this.getClass().equals(element.getClass())) {
         return this;
      } else if (element.getClass().equals(DoubleElement.class)) {
         return element;
      } else if (element instanceof NumberElement) {
         return this;
      }
      throw new MergerException(this, element);
   }

}
