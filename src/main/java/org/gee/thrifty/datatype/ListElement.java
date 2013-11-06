package org.gee.thrifty.datatype;



public class ListElement extends AbstractElement implements Element {

   private Element listType;
   
   public ListElement(Element listType) {
      this.listType = listType;
   }
   
   public String getName() {
      return listType.getName();
   }
   
   public boolean hasObject() {
      return listType.isObject();
   }
   
   public ObjectElement getObject() {
      if (hasObject()) {
         return (ObjectElement)listType;
      }
      return null;
   }
   
   protected String writeTypeAndName() {
      return new StringBuilder()
         .append(getDatatypeName())
         .append('<')
         .append(listType.getDatatypeName())
         .append("> ")
         .append(listType.getName())
         .toString();
   }
   
   public Element getListType() {
      return this.listType;
   }
   
   public void setListType(Element listType) {
      this.listType = listType;
   }
   
   public String getDatatypeName() {
      return "list";
   }

}