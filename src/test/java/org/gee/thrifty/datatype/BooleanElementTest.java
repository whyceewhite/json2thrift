package org.gee.thrifty.datatype;

import org.gee.thrifty.exception.MergeException;
import org.junit.Assert;
import org.junit.Test;

/**
 * <p>
 * Unit tests for the {@link org.gee.thrifty.datatype.BooleanElement} class.
 * </p>
 */
public class BooleanElementTest {

   /**
    * <p>
    * Tests the properties inherent to a BooleanElement. All BooleanElement
    * instances will answer the same way.
    * </p>
    */
   @Test
   public void confirmElementBasics() {
      
      BooleanElement elem = new BooleanElement("test");
      Assert.assertEquals("1 - datatypeName", "bool", elem.getDatatypeName());
      Assert.assertFalse("3 - isObject", elem.isObject());
      Assert.assertFalse("4 - isUnknown", elem.isUnknown());
   }

   /**
    * <p>
    * Tests that the name is properly set when instantiated.
    * </p>
    */
   @Test
   public void confirmName() {
      Assert.assertEquals("test", new BooleanElement("test").getName());
   }

   /**
    * <p>
    * Tests the get and set of the order.
    * </p>
    */
   @Test
   public void confirmGetSetOrder() {
      BooleanElement elem = new BooleanElement("test");
      elem.setOrder(20);
      Assert.assertEquals(20, elem.getOrder());
   }
   
   @Test
   public void mergeWithNull() {
      BooleanElement elem = new BooleanElement("test");
      Element mergeElem = elem.merge(null);
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithSameBoolean() {
      BooleanElement elem = new BooleanElement("test");
      Element mergeElem = elem.merge(elem);
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithDifferentBoolean() {
      BooleanElement elem = new BooleanElement("test");
      Element mergeElem = elem.merge(new BooleanElement("test"));
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithUnknown() {
      BooleanElement elem = new BooleanElement("test");
      Element mergeElem = elem.merge(new UnknownElement("test"));
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithDouble() {
      try {
         new BooleanElement("test").merge(new DoubleElement("test"));
      } catch (MergeException e) {
      }
   }
   
   @Test
   public void mergeWithInteger() {
      try {
         new BooleanElement("test").merge(new IntegerElement("test"));
      } catch (MergeException e) {
      }
   }
   
   @Test
   public void mergeWithLong() {
      try {
         new BooleanElement("test").merge(new LongElement("test"));
      } catch (MergeException e) {
      }
   }
   
   @Test
   public void mergeWithString() {
      try {
         new BooleanElement("test").merge(new StringElement("test"));
      } catch (MergeException e) {
      }
   }
   
   @Test
   public void mergeWithList() {
      try {
         new BooleanElement("test").merge(new ListElement(new BooleanElement("test")));
         Assert.fail();
      } catch (MergeException e) {
      }
   }
   
   @Test
   public void mergeWithObject() {
      try {
         new BooleanElement("test").merge(new ObjectElement("test"));
         Assert.fail();
      } catch (MergeException e) {
      }
   }
}
