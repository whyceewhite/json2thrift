package puck.thrifty.datatype;

import puck.thrifty.exception.MergeException;


public interface Element {
   
   public boolean isObject();
   public boolean isUnknown();
   
   public boolean hasObject();
   public Element getObject();
   public String getDatatypeName();
   public String getName();
   public void setName(String name);
   public boolean isRequired();
   public void setRequired(boolean isRequired);
   public int getOrder();
   public void setOrder(int order);
   
   public Element merge(Element element) throws MergeException;
   public String write();
}
