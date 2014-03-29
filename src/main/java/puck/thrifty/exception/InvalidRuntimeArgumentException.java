package puck.thrifty.exception;


public class InvalidRuntimeArgumentException extends RuntimeException {

   private static final long serialVersionUID = 1L;
   
   public InvalidRuntimeArgumentException() {
      super();
   }
   
   public InvalidRuntimeArgumentException(String message) {
      super(message);
   }

}
