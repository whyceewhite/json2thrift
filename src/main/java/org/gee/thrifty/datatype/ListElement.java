package org.gee.thrifty.datatype;

import org.gee.thrifty.exception.MergeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ListElement extends AbstractElement implements Element {

   private Logger logger = LoggerFactory.getLogger(getClass());
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
   
   public Element merge(Element element) throws MergeException {
      if (element == null || this == element || element.isUnknown()) {
         return this;
      } else if (element.getClass().equals(ListElement.class)) {
         Element mergedListTypeElement = this.getListType().merge(((ListElement)element).getListType());
         this.setListType(mergedListTypeElement);
         return this;
      }
      logger.debug("ListElement merge failed.");
      logger.debug("___ this.listType = " + this.getListType());
      logger.debug("___ element = " + element);
      if (element.getClass().equals(ListElement.class)) {
         logger.debug("___ element.listType = " + ((ListElement)element).getListType());
      }
      throw new MergeException(this, element);
   }

}