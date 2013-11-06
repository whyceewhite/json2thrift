package org.gee.thrifty.datatype;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.gee.thrifty.exception.MergeException;

public class ObjectElement extends AbstractElement implements Element {
   
   private String structName;
   private Map<String, Element> elements;
   
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
   
   public Element merge(Element element) throws MergeException {
      if (element == null || this == element || element.isUnknown()) {
         return this;
      }
      if (!this.getClass().getName().equals(element.getClass().getName())) {
         throw new MergeException("The element type of " + element.getClass().getName() + " is incompatible with " + this.getClass().getName());
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
      ArrayList<String> structList = new ArrayList<String>();
      this.write(structList);
      for (String struct : structList) {
         outstream.write(struct.getBytes());
         outstream.write('\n');
      }
   }
   
   protected void write(List<String> structList) {
      StringBuilder buffer = new StringBuilder();
      buffer.append("struct ")
         .append(this.getStructName())
         .append(" {");
      for (Element element : this.getElements().values()) {
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
   
}
