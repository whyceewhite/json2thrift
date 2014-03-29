package puck.thrifty.datatype;



public class StringElement extends AbstractElement implements Element {

   public StringElement(String elementName) {
      setName(elementName);
   }
   
   public String getDatatypeName() {
      return "string";
   }
}
