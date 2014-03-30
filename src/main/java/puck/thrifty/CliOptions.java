package puck.thrifty;

import java.io.File;

import org.kohsuke.args4j.Option;

/**
 * <p>
 * This represents the options available to the command line interface.
 * </p>
 * 
 * @author ywhite
 */
public class CliOptions {

   @Option(name="-i", aliases="--input-file", usage="A file containing JSON objects to derive a Thrift struct definition. Or, a directory containing many JSON files. Required.")
   private File inputFile;
   
   @Option(name="-o", aliases="--output-file", usage="The file to which the derived Thrift struct is written. If not provided then the struct is written to standard out.")
   private File outputFile;
   
   @Option(name="-n", aliases="--root-name", usage="The name of the root Thrift struct. If not provided then the root struct name is defaulted to Root.")
   private String rootStructName;
      
   @Option(name="-ns", aliases="--set-ns-all", usage="The namespace to apply to all supported languages. Setting this value overrides the namespace setting of any specific language.")
   private String namespaceAll;
   
   @Option(name="--set-ns-cpp", usage="Indicates that the definition should include the given namespace for the CPP language.")
   private String namespaceCpp;
   
   @Option(name="--set-ns-cocoa", usage="Indicates that the definition should include the given namespace for the Cocoa language.")
   private String namespaceCocoa;
   
   @Option(name="--set-ns-csharp", usage="Indicates that the definition should include the given namespace for the Csharp language.")
   private String namespaceCsharp;
   
   @Option(name="--set-ns-java", usage="Indicates that the definition should include the given namespace for the Java language.")
   private String namespaceJava;
   
   @Option(name="--set-ns-perl", usage="Indicates that the definition should include the given namespace for the Perl language.")
   private String namespacePerl;
   
   @Option(name="--set-ns-python", usage="Indicates that the definition should include the given namespace for the Python language.")
   private String namespacePython;
   
   @Option(name="--set-ns-ruby", usage="Indicates that the definition should include the given namespace for the Ruby language.")
   private String namespaceRuby;

   @Option(name="-h", aliases="--help", usage="Displays the usage information.")
   private boolean help;

   
   public File getInputFile() {
      return inputFile;
   }

   public void setInputFile(File inputFile) {
      this.inputFile = inputFile;
   }

   public File getOutputFile() {
      return outputFile;
   }

   public void setOutputFile(File outputFile) {
      this.outputFile = outputFile;
   }
   
   public String getRootStructName() {
      return rootStructName;
   }
   
   public void setRootStructName(String rootStructName) {
      this.rootStructName = rootStructName;
   }

   public String getNamespaceAll() {
      return namespaceAll;
   }

   public void setNamespaceAll(String namespaceAll) {
      this.namespaceAll = namespaceAll;
   }
   
   public String getNamespaceCpp() {
      return namespaceCpp;
   }

   public void setNamespaceCpp(String namespaceCpp) {
      this.namespaceCpp = namespaceCpp;
   }

   public String getNamespaceCocoa() {
      return namespaceCocoa;
   }

   public void setNamespaceCocoa(String namespaceCocoa) {
      this.namespaceCocoa = namespaceCocoa;
   }

   public String getNamespaceCsharp() {
      return namespaceCsharp;
   }
   
   public void setNamespaceCsharp(String namespaceCsharp) {
      this.namespaceCsharp = namespaceCsharp;
   }

   public String getNamespaceJava() {
      return namespaceJava;
   }
   
   public void setNamespaceJava(String namespaceJava) {
      this.namespaceJava = namespaceJava;
   }
   
   public String getNamespacePerl() {
      return namespacePerl;
   }
   
   public void setNamespacePerl(String namespacePerl) {
      this.namespacePerl = namespacePerl;
   }
   
   public String getNamespacePython() {
      return namespacePython;
   }

   public void setNamespacePython(String namespacePython) {
      this.namespacePython = namespacePython;
   }

   public String getNamespaceRuby() {
      return namespaceRuby;
   }
   
   public void setNamespaceRuby(String namespaceRuby) {
      this.namespaceRuby = namespaceRuby;
   }
   
   public boolean isHelp() {
      return help;
   }
   
   public void setHelp(boolean help) {
      this.help = help;
   }
 
}
