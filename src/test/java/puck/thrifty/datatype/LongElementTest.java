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


/**
 * <p>
 * Unit tests for the {@link puck.thrifty.datatype.LongElement} class.
 * </p>
 */
public class LongElementTest {

   @Test
   public void confirmElementBasics() {
      LongElement elem = new LongElement("test");
      Assert.assertEquals("1 - datatypeName", "i64", elem.getDatatypeName());
      Assert.assertFalse("2 - isObject", elem.isObject());
      Assert.assertFalse("3 - isUnknown", elem.isUnknown());
   }

   @Test
   public void confirmName() {
      Assert.assertEquals("test", new LongElement("test").getName());
   }

   @Test
   public void confirmGetSetOrder() {
      LongElement elem = new LongElement("test");
      elem.setOrder(20);
      Assert.assertEquals(20, elem.getOrder());
   }
   
   @Test
   public void mergeWithNull() {
      LongElement elem = new LongElement("test");
      Element mergeElem = elem.merge(null);
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithSameLong() {
      LongElement elem = new LongElement("test");
      Element mergeElem = elem.merge(elem);
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithDifferentLong() {
      LongElement elem = new LongElement("test");
      Element mergeElem = elem.merge(new LongElement("test"));
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithUnknown() {
      LongElement elem = new LongElement("test");
      Element mergeElem = elem.merge(new UnknownElement("test"));
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithDouble() {
      LongElement elem = new LongElement("test");
      DoubleElement dElem = new DoubleElement("test");
      Element mergeElem = elem.merge(dElem);
      Assert.assertSame(dElem, mergeElem);
   }
   
   @Test
   public void mergeWithInteger() {
      LongElement elem = new LongElement("test");
      Element mergeElem = elem.merge(new IntegerElement("test"));
      Assert.assertSame(elem, mergeElem);
   }
   
   @Test
   public void mergeWithBoolean() {
      try {
         new LongElement("test").merge(new BooleanElement("test"));
      } catch (MergerException e) {
      }
   }
   
   @Test
   public void mergeWithString() {
      try {
         new LongElement("test").merge(new StringElement("test"));
      } catch (MergerException e) {
      }
   }
   
   @Test
   public void mergeWithList() {
      try {
         new LongElement("test").merge(new ListElement(new LongElement("test")));
         Assert.fail();
      } catch (MergerException e) {
      }
   }
   
   @Test
   public void mergeWithObject() {
      try {
         new LongElement("test").merge(new ObjectElement("test"));
         Assert.fail();
      } catch (MergerException e) {
      }
      
   }
}
