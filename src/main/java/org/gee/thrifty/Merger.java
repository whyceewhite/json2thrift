package org.gee.thrifty;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gee.thrifty.datatype.ObjectElement;
import org.gee.thrifty.exception.InvalidRuntimeArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This class reads a file that contains one or more json strings and derives a
 * Thrift definition that matches the structure of the combined json strings. A
 * directory containing multiple files of json strings may also be used instead
 * of reading in a single file.
 * </p>
 * <p>
 * While only one json string is necessary to derive a Thrift definition, the
 * option to evaluate many json files of the same structure assures that a more
 * relevant Thrift definition can be derived. This is because if a json string
 * does not contain a data element (i.e., it is null or it is missing from the
 * json string) then the datatype or the element itself cannot be determined.
 * However, a subsequent json string of the same structure may contain the
 * element that was missing from the previous json string and, thereby, allow
 * the merger to ascertain its existence and datatype.
 * </p>
 * <p>
 * To execute this class, call the {@link #process()} method. Prior to executing
 * the method, use the attributes listed in the table below to tailor the
 * execution.
 * </p>
 * <p>
 * <table>
 * <tr><td><b>Parameter</b></td><td><b>Required</b></td><td><b>Purpose</b></td></tr>
 * <tr><td>inputDirectory</td><td>Yes, if inputFile is not provided.</td><td>A directory containing files of JSON string to read.</td></tr>
 * <tr><td>inputFile</td><td>Yes, if inputDirectory is not provided.</td><td>A file containing one or more JSON strings. If inputDirectory is provided then inputDirectory takes precedence.</td></tr>
 * <tr><td>outputFile</td><td>No</td><td>The file where to write the definition.</td></tr>
 * <tr><td>namespaceMap</td><td>No</td><td>Provides a language scope and the namespace to assign to the scope.</td></tr>
 * </table>
 * </p>
 */
public class Merger {
   
   private Logger logger = LoggerFactory.getLogger(getClass());
   
   private int fileMergeCount = 0;
   
   private String rootStructName;
   private File inputDirectory;
   private File inputFile;
   private File outputFile;
   private Map<NamespaceScope, String> namespaceMap;
   private String contents;
   
   /**
    * <p>
    * Creates an instance of this class.
    * </p>
    */
   public Merger() {
      namespaceMap = new TreeMap<NamespaceScope, String>();
   }
   
   public static void main(String[] args) throws Exception {
      
      Merger m = new Merger();
      try {
         m.parseArguments(args);
         m.process();
      } catch (InvalidRuntimeArgumentException e) {
         m.printHelp();
      }
   }
   
   /**
    * <p>
    * Eval the set of files provided.
    * </p>
    * 
    * @return  A string that represents a thrift definition based on the json
    *          strings provided during this execution. 
    * @throws  IOException If an error occurs when writing the results.
    */
   public String process() throws IOException {
      
      validate();
      
      ObjectElement element = null;
      try {
         if (this.inputDirectory != null) {
            File[] files = this.inputDirectory.listFiles();
            if (files != null) {
               for (File jsonFile : files) {
                  logger.info("Processing file " + jsonFile.getAbsolutePath());
                  element = this.merge(new FileInputStream(jsonFile), element);
                  fileMergeCount++;
               }
            }
         } else if (this.inputFile != null) {
            logger.info("Processing file " + this.inputFile.getAbsolutePath());
            element = this.merge(new FileInputStream(this.inputFile));
            fileMergeCount++;
         }
      } catch (Exception e) {
         logger.error("An error occurred during processing. " + fileMergeCount + " files were processed prior to the error.", e);
      }
      
      element.setName(this.rootStructName);
      this.write(element);
      return this.contents;
   }
   
   /**
    * <p>
    * Adds a namespace to the Thrift definition file. This namespace, along
    * with other added namespaces, will be written to the final output. Both
    * the scope and namespace are required. If either is missing then the
    * namespace will not be added.
    * </p>
    * 
    * @param scope The language, or scope, to which the namespace name is
    *       applied. Required.
    * @param namespace The name of the namespace. Required.
    * @return true if the namespace is added and false if not. If either the
    *       scope or the namespace is not provided then the namespace will not
    *       be added.
    */
   public boolean addNamespace(NamespaceScope scope, String namespace) {
      
      if (scope == null || namespace == null || namespace.trim().isEmpty()) {
         logger.warn("A namespace was not added because either the scope or the namespace name was not provided. The values given were: scope = '" + scope + "', namespace = '" + namespace + "'.");
         return false;
      }
      this.namespaceMap.put(scope, namespace);
      return true;
   }
   
   /**
    * <p>
    * Returns the directory containing files of json strings which are
    * parsed to derive a Thrift definition.
    * </p>
    * 
    * @return A directory containing files of json strings for this instance to
    *       parse.
    */
   public File getInputDirectory() {
      return this.inputDirectory;
   }
   
   /**
    * <p>
    * Sets the directory that contains files which contain json strings. The
    * json strings are parsed to derive a Thrift definition containing the
    * elements encountered in the json strings.
    * </p>
    * <p>
    * If both {@link #getInputDirectory()} and {@link #getInputFile(File)} are
    * not null then {@link #getInputDirectory()} takes precedence.
    * </p>
    * 
    * @param directoryPath The path of directory containing files which contain
    *       json strings from which a Thrift definition is derived.
    */
   public void setInputDirectory(File inputDirectory) {
      this.inputDirectory = inputDirectory; 
   }
   
   /**
    * <p>
    * Returns the file containing json strings that are parsed to derive a
    * Thrift definition.
    * </p>
    * 
    * @return A file containing json strings for this instance to parse.
    */
   public File getInputFile() {
      return this.inputFile;
   }
   
   /**
    * <p>
    * Sets the file that contains one or more json strings that are parsed to
    * derive a Thrift definition file containing the elements encountered in
    * the json strings.
    * </p>
    * <p>
    * If both {@link #getInputDirectory()} and {@link #getInputFile(File)} are
    * not null then {@link #getInputDirectory()} takes precedence.
    * </p>
    * @param inputFile The file containing json strings from which a Thrift
    *       definition is derived.
    */
   public void setInputFile(File inputFile) {
      this.inputFile = inputFile;
   }
   
   /**
    * <p>
    * Returns the file to which the derived Thrift definition file is written.
    * If this is null then the derived Thrift definition may be obtained as a
    * string from the {@link #process()} method's returned results or from the
    * {@link #getContents()} method.
    * </p>
    * 
    * @return The file to which the derived Thrift definition is written.
    */
   public File getOutputFile() {
      return this.outputFile;
   }
   
   /**
    * <p>
    * Sets the file to where the derived Thrift definition is written.
    * </p>
    * 
    * @param outputFile The file to where the derived Thrift definition is
    *       written.
    */
   public void setOutputFile(File outputFile) {
      this.outputFile = outputFile;
   }
   
   /**
    * <p>
    * Returns the derived Thrift definition that resulted from reading one or
    * more json strings during the {@link #process()} execution.
    * </p>
    * 
    * @return The derived Thrift definition.
    */
   public String getContents() {
      return this.contents;
   }
   
   /**
    * <p>
    * The name of the root structure generated from the merge.
    * </p>
    * 
    * @return The name of the root structure.
    */
   public String getRootStructName() {
      return this.rootStructName;
   }
   
   /**
    * <p>
    * Assigns a name to the root structure that is generated from the merge
    * execution. A null or empty value will result in the default structure
    * name.
    * </p>
    * 
    * @param rootName The name to assign the root structure. A null or empty
    *       value will result in the default structure name.
    */
   public void setRootStructName(String rootName) {
      this.rootStructName = (rootName == null || rootName.isEmpty() ? null : rootName);
   }
   
   /**
    * <p>
    * Writes the Thrift definition that was created based on the set of JSON
    * files used during the merge process. The Thrift definition is written to
    * the file provided at {@link #outputFile} or to the console if no file
    * was given. 
    * </p>
    * 
    * @param element The element that is the composite of the JSON files given
    *       for the merge.
    * @throws IOException If an error occurs while writing the Thrift
    *       definition to the file.
    */
   private void write(ObjectElement element) throws IOException {
      
      if (element == null) {
         logger.error("No thrift element value was derived from the merge execution. Check that the input files exist and contain data.");
         return;
      }
      
      logger.info(fileMergeCount + " file" + (fileMergeCount != 1 ? "s were" : " was") + " merged.");
      
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      writeNamespace(outputStream);
      element.write(outputStream);
      
      this.contents = outputStream.toString();
      if (this.isWriteToFile()) {
         FileOutputStream fileOutputStream = new FileOutputStream(this.outputFile);
         fileOutputStream.write(outputStream.toByteArray());
         logger.info("Wrote json-to-thrift to file " + this.outputFile.getAbsolutePath() + ".");
         fileOutputStream.close();
      }
      logger.info("Thrift Definition:\n" + this.contents);
   }
   
   /**
    * <p>
    * Writes the Thrift namespaces to the output stream. 
    * </p>
    */
   private void writeNamespace(OutputStream outputStream) throws IOException {
      
      if (this.namespaceMap == null || this.namespaceMap.isEmpty()) return;
      
      if (this.namespaceMap.containsKey(NamespaceScope.ALL)) {
         outputStream.write(writeNamespace(NamespaceScope.ALL, this.namespaceMap.get(NamespaceScope.ALL)));
      } else {
         for (NamespaceScope nsScope : this.namespaceMap.keySet()) {
            outputStream.write(writeNamespace(nsScope, this.namespaceMap.get(nsScope)));
         }
      }
   }
   
   /**
    * <p>
    * Creates a Thrift namespace statement based on the namespace scope and the
    * name of the namespace. Returns the string as an array of bytes.
    * </p>
    * 
    * @param nsScope The namespace scope. Required.
    * @param namespace The name of the namespace. Reqired.
    * @return A Thrift IDL statement that defines a namespace.
    */
   private byte[] writeNamespace(NamespaceScope nsScope, String namespace) {
      return new StringBuffer("namespace ")
         .append(nsScope.getCode())
         .append(" ")
         .append(this.namespaceMap.get(nsScope))
         .append(System.getProperty("line.separator"))
         .toString()
         .getBytes();
   }
   
   /**
    * <p>
    * Answers true if the Thrift struct definitions are written to a file and
    * false if written to the console.
    * </p>
    */
   private boolean isWriteToFile() {
      return this.outputFile != null;
   }
   
   /**
    * <p>
    * Confirms that the required parameters for processing the merge are
    * available. If they are not then an exception is thrown.
    * </p>
    * 
    * @throws InvalidRuntimeArgumentException If required parameters are not
    *       available.
    */
   private void validate() throws InvalidRuntimeArgumentException {
      
      if (this.inputDirectory == null && this.inputFile == null) {
         throw new InvalidRuntimeArgumentException("Either the inputDirectory or the inputFile must be specified prior to execution.");
      }
      
      if (this.inputDirectory != null && !this.inputDirectory.isDirectory()) {
         throw new InvalidRuntimeArgumentException("The inputDirectory '" + this.inputDirectory + "' is not a directory.");
      }
      
      if (this.inputDirectory == null && this.inputFile != null && !this.inputFile.isFile()) {
         throw new InvalidRuntimeArgumentException("The inputFile '" + this.inputFile + "' is not a file.");
      }
   }
   
   ObjectElement merge(InputStream stream, ObjectElement element) throws IOException {
      
      ObjectElement parsedElement = this.merge(stream);
      if (element == null) {
         element = parsedElement;
      }
      return (ObjectElement)element.merge(parsedElement);
   }
   
   ObjectElement merge(InputStream stream) throws IOException  {
      
      String contents = toString(stream);
      return merge(contents);
   }

   ObjectElement merge(String jsonStringGroup) throws IOException {
      
      ObjectElement element = null;
      BufferedReader br = new BufferedReader(new StringReader(jsonStringGroup));
      String json = br.readLine();
      while (json != null) {
         if (!json.isEmpty()) {
            logger.debug("Deserializing json: " + json);
            ObjectElement tempElement = parse(json);
            if (element == null) {
               element = tempElement;
            } else {
               logger.debug("Merging " + tempElement + " into " + element);
               element.merge(tempElement);
            }
         }
         json = br.readLine();
      }
      return element;
   }
   
   /**
    * <p>
    * Given a json string, parses it and converts it into an ObjectElement
    * instance.
    * </p>
    * 
    * @param json The json string to parse. Required.
    * @return The json string represented as an ObjectElement.
    */
   private ObjectElement parse(String json) {
      return new Converter().parse(json);
   }
   
   /**
    * <p>
    * Reads the given input stream's contents and returns it as a string.
    * </p>
    * 
    * @param inputStream The input stream to read. Required.
    * @return The input stream's contents as a string.
    * @throws IOException If an error occurs while accessing, reading or
    *       closing the file.
    */
   private String toString(InputStream inputStream) throws IOException {
      
      StringBuffer stringBuffer = new StringBuffer();
      InputStreamReader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

      char[] charBuffer = new char[1024];
      int charsRead = reader.read(charBuffer);
      while (charsRead > 0) {
         stringBuffer.append(charBuffer, 0, charsRead);
         charsRead = reader.read(charBuffer);
      }
      reader.close();
      
      return stringBuffer.toString();
   }
   
   /**
    * <p>
    * Parses the command line for the arguments needed to run this program.
    * </p>
    * 
    * @param args The arguments from the command line.
    */
   private void parseArguments(String[] args)  {
      
      if (args == null) {
         return;
      }
      
      Pattern pattern = Pattern.compile("-(inputDirectory|inputFile|outputFile|rootStructName|nsAll|nsJava|nsCpp|nsPython|nsPerl|nsRuby|nsCocoa|nsCsharp)=\"?(.*)\"?");
      for (String argument : args) {
         Matcher matcher = pattern.matcher(argument);
         if (matcher.matches()) {
            
            String argName = matcher.group(1);
            String argValue = matcher.group(2);
            
            if (argName.equals("inputDirectory")) {
               this.setInputDirectory(new File(argValue));
            } else if (argName.equals("inputFile")) {
               this.setInputFile(new File(argValue));
            } else if (argName.equals("outputFile")) {
               this.setOutputFile(new File(argValue));
            } else if (argName.equals("rootStructName")) {
               this.setRootStructName(argValue);
            } else if (argName.equals("nsAll")) {
               this.addNamespace(NamespaceScope.ALL, argValue);
            } else if (argName.equals("nsJava")) {
               this.addNamespace(NamespaceScope.JAVA, argValue);
            } else if (argName.equals("nsCpp")) {
               this.addNamespace(NamespaceScope.CPP, argValue);
            } else if (argName.equals("nsPython")) {
               this.addNamespace(NamespaceScope.PYTHON, argValue);
            } else if (argName.equals("nsPerl")) {
               this.addNamespace(NamespaceScope.PERL, argValue);
            } else if (argName.equals("nsRuby")) {
               this.addNamespace(NamespaceScope.RUBY, argValue);
            } else if (argName.equals("nsCocoa")) {
               this.addNamespace(NamespaceScope.COCOA, argValue);
            } else if (argName.equals("nsCsharp")) {
               this.addNamespace(NamespaceScope.CSHARP, argValue);
            }
            logger.debug("Runtime argument established | " + argName + " = " + argValue);
         }
      }
   }
   
   /**
    * <p>
    * Prints to System.out the runtime arguments that may be used to execute
    * this utility.
    * </p>
    */
   private void printHelp() {
      // TODO: fill in.
      System.out.println("help goes here. :D");
   }
   
}
