package puck.thrifty.datatype;


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

}
