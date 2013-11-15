package org.gee.thrifty.datatype;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.gee.thrifty.exception.MergeException;

public class ObjectElement extends AbstractElement implements Element {
   
   private String structName;
   private Map<String, Element> elements;
   private int mergeCount;
   
   public ObjectElement(String name) {
      setName(name);
      this.elements = new TreeMap<String, Element>();
   }
   
   public ObjectElement add(Element element) {
      if (element != null) {
         elements.put(element.getName(), element);
      }
      return this;
   }
   
   public void setName(String name) {
      super.setName(name);
      if (this.getName() == null) {
         structName = null;
      } else {
         if (this.getName().length() > 1) {
            structName = this.getName().substring(0, 1).toUpperCase() + 
                  this.getName().substring(1, this.getName().length());
         } else {
            structName = this.getName().toUpperCase();
         }
      }
   }
   
   public boolean isObject() {
      return true;
   }
   
   public ObjectElement getObject() {
      return this;
   }
   
   public String getDatatypeName() {
      return getStructName();
   }
   
   protected String getStructName() {
      return this.structName;
   }
   
   protected Map<String, Element> getElements() {
      return this.elements;
   }
   
   public int getMergeCount() {
      return this.mergeCount;
   }
   
   public Element merge(Element element) throws MergeException {
      if (element == null || this == element || element.isUnknown()) {
         return this;
      }
      if (!this.getClass().getName().equals(element.getClass().getName())) {
         throw new MergeException(this, element);
      }
      Map<String, Element> elementsMap = ((ObjectElement)element).getElements();
      for (String elementKey : elementsMap.keySet()) {
         Element elementValue = elementsMap.get(elementKey);
         if (this.elements.containsKey(elementKey)) {
            Element value =  this.elements.get(elementKey);
            Element mergedValue = value.merge(elementValue);
            if (mergedValue != value) {
               this.elements.put(elementKey, mergedValue);
            }
         } else {
            this.add(elementValue);
         }
      }
      return this;
   }
   
   public void write(OutputStream outstream) throws IOException {
      TreeSet<String> structCollection = new TreeSet<String>();
      this.write(structCollection);
      for (String struct : structCollection) {
         outstream.write(struct.getBytes());
         outstream.write('\n');
      }
   }
   
   protected void write(Collection<String> structList) {
      int order = 1;
      StringBuilder buffer = new StringBuilder();
      buffer.append("struct ")
         .append(this.getStructName())
         .append(" {");
      for (Element element : this.getElements().values()) {
         element.setOrder(order++);
         buffer.append('\n');
         buffer.append("   ");
         buffer.append(element.write());
         if (element.isObject() || element.hasObject()) {
            ((ObjectElement)(element.getObject())).write(structList);
         }
      }
      buffer.append('\n');
      buffer.append('}');
      structList.add(buffer.toString());
   }
   
   /**
    * <p>
    * Answers true if element is equal to this instance and false if it is not.
    * </p>
    * 
    * @param element The element instance that is equated against this instance.
    * @return  true if this and the element instances are equal and false if
    *       they are not.
    */
   public boolean equals(Element element) {
      if (!super.equals(element)) return false;
      
      Map<String, Element> elementMap = ((ObjectElement)element).getElements();
      if (this.getElements().size() != elementMap.size()) return false;
      
      for (String thisKey : this.getElements().keySet()) {
         Element thisValue = this.getElements().get(thisKey);
         if (elementMap.containsKey(thisKey)) {
            Element elementValue = elementMap.get(thisKey);
            if (!thisValue.equals(elementValue)) return false;
         }
      }
      
      return true;
   }
   
}
