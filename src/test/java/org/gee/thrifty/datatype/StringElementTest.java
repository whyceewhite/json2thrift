package org.gee.thrifty.datatype;

import org.gee.thrifty.exception.MergeException;
import org.junit.Assert;
import org.junit.Test;


public class StringElementTest {

   @Test
   public void confirmElementBasics() {
      
      StringElement elem = new StringElement("test");
      Assert.assertEquals("1 - datatypeName", "string", elem.getDatatypeName());
      Assert.assertFalse("2 - isObject", elem.isObject());
      Assert.assertFalse("3 - isUnknown", elem.isUnknown());
   }

   @Test
   public void confirmName() {
      Assert.assertEquals("test", new StringElement("test").getName());
   }

   /**
    * <p>
    * Tests the get and set of the order.
    * </p>
    */
   @Test
   public void confirmGetSetOrder() {
      StringElement elem = new StringElement("test");
      elem.setOrder(20);
      Assert.assertEquals(20, elem.getOrder());
   }
   
   @Test
   public void mergeWithNull() {
      StringElement elem = new StringElement("test");
      Element mergeElem = elem.merge(null);
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithSameString() {
      StringElement elem = new StringElement("test");
      Element mergeElem = elem.merge(elem);
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithDifferentString() {
      StringElement elem = new StringElement("test");
      Element mergeElem = elem.merge(new StringElement("test"));
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithUnknown() {
      StringElement elem = new StringElement("test");
      Element mergeElem = elem.merge(new UnknownElement("test"));
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithDouble() {
      try {
         new StringElement("test").merge(new DoubleElement("test"));
      } catch (MergeException e) {
      }
   }
   
   @Test
   public void mergeWithInteger() {
      try {
         new StringElement("test").merge(new IntegerElement("test"));
      } catch (MergeException e) {
      }
   }
   
   @Test
   public void mergeWithLong() {
      try {
         new StringElement("test").merge(new LongElement("test"));
      } catch (MergeException e) {
      }
   }
   
   @Test
   public void mergeWithBoolean() {
      try {
         new StringElement("test").merge(new BooleanElement("test"));
      } catch (MergeException e) {
      }
   }
   
   @Test
   public void mergeWithList() {
      try {
         new StringElement("test").merge(new ListElement(new StringElement("test")));
         Assert.fail();
      } catch (MergeException e) {
      }
   }
   
   @Test
   public void mergeWithObject() {
      try {
         new StringElement("test").merge(new ObjectElement("test"));
         Assert.fail();
      } catch (MergeException e) {
      }
   }

}
