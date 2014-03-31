package puck.thrifty.datatype;

import puck.thrifty.MergerException;

public class ListElement extends AbstractElement implements Element {

   private Element listType;
   
   public ListElement(Element listType) {
      this.setListType(listType);
   }
   
   public boolean isList() {
      return true;
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
   
   private void setListType(Element listType) {
      // A Thrift struct cannot have a list of list; therefore, convert the
      // listType to an ObjectElement.
      if (listType.isList()) {
         ObjectElement newListType = new ObjectElement(listType.getName() + "X");
         newListType.add(listType);
         this.listType = newListType;
      } else {
         this.listType = listType;
      }
   }
   
   public String getDatatypeName() {
      return "list";
   }
   
   public Element merge(Element element) throws MergerException {
      if (element == null || this == element || element.isUnknown()) {
         return this;
      } else if (element.getClass().equals(ListElement.class)) {
         Element mergedListTypeElement = this.getListType().merge(((ListElement)element).getListType());
         this.setListType(mergedListTypeElement);
         return this;
      }
      throw new MergerException(this, element);
   }
   
   public boolean equals(Element element) {
      if (!super.equals(element)) return false;
      return this.getListType().equals(((ListElement)element).getListType());
   }
   
}