package puck.thrifty;

import puck.thrifty.datatype.Element;

/**
 * <p>
 * Represents an error that occurred during the merger process.
 * </p>
 * 
 * @author ywhite
 *
 */
public class MergerException extends RuntimeException {

   private static final long serialVersionUID = 1L;
   
   public MergerException() {
      super();
   }
   
   public MergerException(String message) {
      super(message);
   }
   
   /**
    * <p>
    * Create an instance of this class to indicate a merge error. The two
    * parameters represent 1) the element being merged and 2) the element
    * receiving the merge.
    * </p>
    * 
    * @param mergeRecipient The element receiving the merge. Required.
    * @param mergeDonor The element that is being merged into another
    *       element. Required.
    */
   public MergerException(Element mergeRecipient, Element mergeDonor) {
      super("The element type of " + mergeDonor.getClass().getName() + " cannot be merged into " + mergeRecipient.getClass().getName());
   }

}
