package puck.thrifty.datatype;

import puck.thrifty.exception.MergeException;



public abstract class AbstractElement implements Element {

   private String name;
   private int order;
   private boolean isRequired;
   
   public String write() {
      return new StringBuilder()
         .append(this.getOrder())
         .append(": ")
         .append(this.isRequired() ? "required " : "optional ")
         .append(this.writeTypeAndName())
         .append(";")
         .toString();
   }
   
   public boolean isUnknown() {
      return false;
   }
   
   public Element merge(Element element) throws MergeException {
      if (element == null || this == element || element.isUnknown() || this.getClass().equals(element.getClass())) {
         return this;
      } else if (this.isUnknown()) {
         return element;
      }
      throw new MergeException(this, element);
   }
   
   protected String writeTypeAndName() {
      return getDatatypeName() + " " + getName();
   }
   
   public boolean isObject() {
      return false;
   }
   
   public boolean hasObject() {
      return false;
   }
   
   public ObjectElement getObject() {
      return null;
   }

   public abstract String getDatatypeName();
   
   public String getName() {
      return name;
   }
   
   public void setName(String name) {
      this.name = name;
   }
   
   public int getOrder() {
      return order;
   }
   
   public void setOrder(int order) {
      this.order = order;
   }
   
   public boolean isRequired() {
      return isRequired;
   }
   
   public void setRequired(boolean isRequired) {
      this.isRequired = isRequired;
   }
   
   public boolean equals(Element element) {
      if (this == element) {
         return true;
      }
      if (element == null || !getClass().equals(element.getClass())) {
         return false;
      }
      return this.getName().equals(element.getName());
   }
   
   public String toString() {
      return this.getClass().getName()+"["+ writeTypeAndName()+"]";
   }

}
