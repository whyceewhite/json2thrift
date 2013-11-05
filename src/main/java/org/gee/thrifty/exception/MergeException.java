package org.gee.thrifty.exception;


public class MergeException extends RuntimeException {

   private static final long serialVersionUID = 1L;
   
   public MergeException() {
      super();
   }
   
   public MergeException(String message) {
      super(message);
   }

}
