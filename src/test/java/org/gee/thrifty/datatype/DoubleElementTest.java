package org.gee.thrifty.datatype;

import org.gee.thrifty.exception.MergeException;
import org.junit.Assert;
import org.junit.Test;


public class DoubleElementTest {

   @Test
   public void confirmElementBasics() {
      
      DoubleElement elem = new DoubleElement("test");
      Assert.assertEquals("1 - datatypeName", "double", elem.getDatatypeName());
      Assert.assertFalse("3 - isObject", elem.isObject());
      Assert.assertFalse("4 - isUnknown", elem.isUnknown());
   }

   @Test
   public void confirmName() {
      Assert.assertEquals("test", new DoubleElement("test").getName());
   }

   @Test
   public void confirmGetSetOrder() {
      DoubleElement elem = new DoubleElement("test");
      elem.setOrder(20);
      Assert.assertEquals(20, elem.getOrder());
   }
   
   @Test
   public void mergeWithNull() {
      DoubleElement elem = new DoubleElement("test");
      Element mergeElem = elem.merge(null);
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithSameDouble() {
      DoubleElement elem = new DoubleElement("test");
      Element mergeElem = elem.merge(elem);
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithDifferentDouble() {
      DoubleElement elem = new DoubleElement("test");
      Element mergeElem = elem.merge(new DoubleElement("test"));
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithUnknown() {
      DoubleElement elem = new DoubleElement("test");
      Element mergeElem = elem.merge(new UnknownElement("test"));
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithBoolean() {
      try {
         new DoubleElement("test").merge(new BooleanElement("test"));
      } catch (MergeException e) {
      }
   }
   
   @Test
   public void mergeWithInteger() {
      DoubleElement elem = new DoubleElement("test");
      Element mergeElem = elem.merge(new IntegerElement("test"));
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithLong() {
      DoubleElement elem = new DoubleElement("test");
      Element mergeElem = elem.merge(new LongElement("test"));
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithString() {
      try {
         new DoubleElement("test").merge(new StringElement("test"));
      } catch (MergeException e) {
      }
   }
   
   @Test
   public void mergeWithList() {
      try {
         new DoubleElement("test").merge(new ListElement(new DoubleElement("test")));
         Assert.fail();
      } catch (MergeException e) {
      }
   }
   
   @Test
   public void mergeWithObject() {
      try {
         new DoubleElement("test").merge(new ObjectElement("test"));
         Assert.fail();
      } catch (MergeException e) {
      }
   }

}
