package puck.thrifty;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

/**
 * <p>
 * The command line interface starting point for the thrifty utility.
 * </p>
 * 
 * @author ywhite
 */
public class Cli {
   
   public static void main(String[] args) {
      
      CliOptions options = new CliOptions();
      CmdLineParser parser = new CmdLineParser(options);
      parser.setUsageWidth(80);
      
      if (args.length == 0) {
         parser.printUsage(System.err);
         System.exit(1);
      }
      
      try {
         
         parser.parseArgument(args);
         
         if (options.isHelp()) {
            parser.printUsage(System.err);
            System.exit(1);
         }
         
         Cli.validate(parser, options);
         Cli.run(options);
         
      } catch (CmdLineException e) {
         System.err.println(e.getMessage());
         System.exit(1);
      } catch (MergerException e) {
         System.err.println(e.getMessage());
         System.exit(1);
      }
      
      System.exit(0);
   }
   
   private static void validate(CmdLineParser parser, CliOptions options) throws CmdLineException {
      
      if (options.getInputFile() == null) {
         throw new CmdLineException(parser, "A file or directory pointing to the JSON data to read is required for -i.");
      }
   }
   
   private static void run(CliOptions options) throws MergerException {
      
      Merger merger = new Merger();
      merger.setInputFile(options.getInputFile());
      merger.setOutputFile(options.getOutputFile());
      merger.setRootStructName(options.getRootStructName());
      merger.addNamespace(NamespaceScope.ALL, options.getNamespaceAll());
      merger.addNamespace(NamespaceScope.COCOA, options.getNamespaceCocoa());
      merger.addNamespace(NamespaceScope.CPP, options.getNamespaceCpp());
      merger.addNamespace(NamespaceScope.CSHARP, options.getNamespaceCsharp());
      merger.addNamespace(NamespaceScope.JAVA, options.getNamespaceJava());
      merger.addNamespace(NamespaceScope.PERL, options.getNamespacePerl());
      merger.addNamespace(NamespaceScope.PYTHON, options.getNamespacePython());
      merger.addNamespace(NamespaceScope.RUBY, options.getNamespaceRuby());
      
      merger.process();
   }

}
