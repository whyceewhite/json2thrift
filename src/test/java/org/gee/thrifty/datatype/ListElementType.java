package org.gee.thrifty.datatype;

import org.gee.thrifty.exception.MergeException;
import org.junit.Assert;
import org.junit.Test;

/**
 * <p>
 * Unit tests for the {@link org.gee.thrifty.datatype.ListElement} class.
 * </p>
 * 
 */
// TODO: merge with lists of various types
public class ListElementType {

   @Test
   public void confirmGetSetOrder() {
      ListElement elem = new ListElement(new StringElement("test"));
      elem.setOrder(20);
      Assert.assertEquals(20, elem.getOrder());
   }
   
   public void listOfBoolean() {
      ListElement elem = new ListElement(new BooleanElement("test"));
      Assert.assertEquals("1 - datatypeName", "list", elem.getDatatypeName());
      Assert.assertFalse("2 - isObject", elem.isObject());
      Assert.assertFalse("3 - isUnknown", elem.isUnknown());
      Assert.assertFalse("4 - hasObject", elem.hasObject());
      Assert.assertEquals("5 - getListType", BooleanElement.class, elem.getListType().getClass());
      Assert.assertEquals("6 - name", "test", elem.getName());
      Assert.assertEquals("7 - writeTypeAndName", "list <bool>", elem.writeTypeAndName());
   }
   
   public void listOfDouble() {
      ListElement elem = new ListElement(new DoubleElement("test"));
      Assert.assertEquals("1 - datatypeName", "list", elem.getDatatypeName());
      Assert.assertFalse("2 - isObject", elem.isObject());
      Assert.assertFalse("3 - isUnknown", elem.isUnknown());
      Assert.assertFalse("4 - hasObject", elem.hasObject());
      Assert.assertEquals("5 - getListType", BooleanElement.class, elem.getListType().getClass());
      Assert.assertEquals("6 - name", "test", elem.getName());
      Assert.assertEquals("7 - writeTypeAndName", "list<string>", elem.writeTypeAndName());
   }
   
   public void listOfInteger() {
      ListElement elem = new ListElement(new IntegerElement("test"));
      Assert.assertEquals("1 - datatypeName", "list", elem.getDatatypeName());
      Assert.assertFalse("2 - isObject", elem.isObject());
      Assert.assertFalse("3 - isUnknown", elem.isUnknown());
      Assert.assertFalse("4 - hasObject", elem.hasObject());
      Assert.assertEquals("5 - getListType", IntegerElement.class, elem.getListType().getClass());
      Assert.assertEquals("6 - name", "test", elem.getName());
      Assert.assertEquals("7 - writeTypeAndName", "list<i34>", elem.writeTypeAndName());
   }
   
   public void listOfLong() {
      ListElement elem = new ListElement(new LongElement("test"));
      Assert.assertEquals("1 - datatypeName", "list", elem.getDatatypeName());
      Assert.assertFalse("2 - isObject", elem.isObject());
      Assert.assertFalse("3 - isUnknown", elem.isUnknown());
      Assert.assertFalse("4 - hasObject", elem.hasObject());
      Assert.assertEquals("5 - getListType", LongElement.class, elem.getListType().getClass());
      Assert.assertEquals("6 - name", "test", elem.getName());
      Assert.assertEquals("7 - writeTypeAndName", "list<i64>", elem.writeTypeAndName());
   }
   
   public void listOfObject() {
      ListElement elem = new ListElement(new ObjectElement("test"));
      Assert.assertEquals("1 - datatypeName", "list", elem.getDatatypeName());
      Assert.assertFalse("2 - isObject", elem.isObject());
      Assert.assertFalse("3 - isUnknown", elem.isUnknown());
      Assert.assertTrue("4 - hasObject", elem.hasObject());
      Assert.assertEquals("5 - getListType", ObjectElement.class, elem.getListType().getClass());
      Assert.assertEquals("6 - name", "test", elem.getName());
      Assert.assertEquals("7 - writeTypeAndName", "list<Test>", elem.writeTypeAndName());
   }
   
   public void listOfString() {
      ListElement elem = new ListElement(new StringElement("test"));
      Assert.assertEquals("1 - datatypeName", "list", elem.getDatatypeName());
      Assert.assertFalse("2 - isObject", elem.isObject());
      Assert.assertFalse("3 - isUnknown", elem.isUnknown());
      Assert.assertFalse("4 - hasObject", elem.hasObject());
      Assert.assertEquals("5 - getListType", StringElement.class, elem.getListType().getClass());
      Assert.assertEquals("6 - name", "test", elem.getName());
      Assert.assertEquals("7 - writeTypeAndName", "list<string>", elem.writeTypeAndName());
   }
   
   public void listOfUnknown() {
      ListElement elem = new ListElement(new UnknownElement("test"));
      Assert.assertEquals("1 - datatypeName", "list", elem.getDatatypeName());
      Assert.assertFalse("2 - isObject", elem.isObject());
      Assert.assertTrue("3 - isUnknown", elem.isUnknown());
      Assert.assertFalse("4 - hasObject", elem.hasObject());
      Assert.assertEquals("5 - getListType", UnknownElement.class, elem.getListType().getClass());
      Assert.assertEquals("6 - name", "test", elem.getName());
      Assert.assertEquals("7 - writeTypeAndName", "list<unknown>", elem.writeTypeAndName());
   }

   @Test
   public void mergeWithNull() {
      ListElement elem = new ListElement(new StringElement("test"));
      Element mergeElem = elem.merge(null);
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithSameList() {
      ListElement elem = new ListElement(new StringElement("test"));
      Element mergeElem = elem.merge(elem);
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithDifferentList() {
      ListElement elem = new ListElement(new StringElement("test"));
      Element mergeElem = elem.merge(new ListElement(new StringElement("test")));
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithUnknown() {
      ListElement elem = new ListElement(new StringElement("test"));
      Element mergeElem = elem.merge(new UnknownElement("test"));
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithDouble() {
      try {
         new ListElement(new StringElement("test")).merge(new DoubleElement("test"));
      } catch (MergeException e) {
      }
   }
   
   @Test
   public void mergeWithInteger() {
      try {
         new ListElement(new StringElement("test")).merge(new IntegerElement("test"));
      } catch (MergeException e) {
      }
   }
   
   @Test
   public void mergeWithLong() {
      try {
         new ListElement(new StringElement("test")).merge(new LongElement("test"));
      } catch (MergeException e) {
      }
   }
   
   @Test
   public void mergeWithString() {
      try {
         new ListElement(new StringElement("test")).merge(new StringElement("test"));
      } catch (MergeException e) {
      }
   }
   
   @Test
   public void mergeWithBoolean() {
      try {
         new ListElement(new StringElement("test")).merge(new ListElement(new BooleanElement("test")));
         Assert.fail();
      } catch (MergeException e) {
      }
   }
   
   @Test
   public void mergeWithObject() {
      try {
         new ListElement(new StringElement("test")).merge(new ObjectElement("test"));
         Assert.fail();
      } catch (MergeException e) {
      }
   }
   
   // TODO:  merge diff lists with each other. 
   
   // TODO:  equals
}
