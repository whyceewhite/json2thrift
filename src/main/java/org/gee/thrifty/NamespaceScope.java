package org.gee.thrifty;

/**
 * <p>
 * This enumeration provides the target languages to which a namespace may apply
 * when generating using a Thrift IDL. The {@link #ALL} scope will apply to all
 * the supported languages.
 * </p>
 * <p>
 * The namespace is also known as package or module depending on the language.
 * </p> 
 */
public enum NamespaceScope {
   
   ALL("*"),
   COCOA("cocoa"),
   CPP("cpp"),
   CSHARP("csharp"),
   JAVA("java"),
   PERL("perl"),
   PYTHON("py"),
   RUBY("rb");
   
   private String code;
   
   NamespaceScope(String code) {
      this.code = code;
   }
   
   /**
    * <p>
    * Returns the code name of the namespace language;
    * </p>
    * 
    * @return The code name of the namespace language.
    */
   public String getCode() {
      return this.code;
   }
}
