package puck.thrifty.datatype;

import org.junit.Assert;
import org.junit.Test;

import puck.thrifty.MergerException;
import puck.thrifty.datatype.BooleanElement;
import puck.thrifty.datatype.DoubleElement;
import puck.thrifty.datatype.Element;
import puck.thrifty.datatype.IntegerElement;
import puck.thrifty.datatype.ListElement;
import puck.thrifty.datatype.LongElement;
import puck.thrifty.datatype.ObjectElement;
import puck.thrifty.datatype.StringElement;
import puck.thrifty.datatype.UnknownElement;


public class IntegerElementTest {

   @Test
   public void confirmElementBasics() {
      
      IntegerElement elem = new IntegerElement("test");
      Assert.assertEquals("1 - datatypeName", "i32", elem.getDatatypeName());
      Assert.assertFalse("2 - isObject", elem.isObject());
      Assert.assertFalse("3 - isUnknown", elem.isUnknown());
   }

   @Test
   public void confirmName() {
      Assert.assertEquals("test", new IntegerElement("test").getName());
   }

   @Test
   public void confirmGetSetOrder() {
      IntegerElement elem = new IntegerElement("test");
      elem.setOrder(20);
      Assert.assertEquals(20, elem.getOrder());
   }
   
   @Test
   public void mergeWithNull() {
      IntegerElement elem = new IntegerElement("test");
      Element mergeElem = elem.merge(null);
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithSameInteger() {
      IntegerElement elem = new IntegerElement("test");
      Element mergeElem = elem.merge(elem);
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithDifferentInteger() {
      IntegerElement elem = new IntegerElement("test");
      Element mergeElem = elem.merge(new IntegerElement("test"));
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithUnknown() {
      IntegerElement elem = new IntegerElement("test");
      Element mergeElem = elem.merge(new UnknownElement("test"));
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithDouble() {
      IntegerElement elem = new IntegerElement("test");
      DoubleElement dElem = new DoubleElement("test");
      Element mergeElem = elem.merge(dElem);
      Assert.assertSame(dElem, mergeElem);
   }
   
   @Test
   public void mergeWithBoolean() {
      try {
         new IntegerElement("test").merge(new BooleanElement("test"));
      } catch (MergerException e) {
      }
   }
   
   @Test
   public void mergeWithLong() {
      IntegerElement elem = new IntegerElement("test");
      LongElement lElem = new LongElement("test");
      Element mergeElem = elem.merge(lElem);
      Assert.assertSame(lElem, mergeElem);
   }
   
   @Test
   public void mergeWithString() {
      try {
         new IntegerElement("test").merge(new StringElement("test"));
      } catch (MergerException e) {
      }
   }
   
   @Test
   public void mergeWithList() {
      try {
         new IntegerElement("test").merge(new ListElement(new IntegerElement("test")));
         Assert.fail();
      } catch (MergerException e) {
      }
   }
   
   @Test
   public void mergeWithObject() {
      try {
         new IntegerElement("test").merge(new ObjectElement("test"));
         Assert.fail();
      } catch (MergerException e) {
      }
   }

}
