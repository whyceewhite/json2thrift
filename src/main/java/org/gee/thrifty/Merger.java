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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gee.thrifty.datatype.ObjectElement;
import org.gee.thrifty.exception.InvalidRuntimeArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Merger {
   
   private Logger logger = LoggerFactory.getLogger(getClass());
   private File inputDirectory;;
   private File inputFile;
   private File outputFile;
   private int fileMergeCount = 0;
   
   public Merger() {
   }
   
   public static void main(String[] args) throws Exception {
      
      Merger m = new Merger();
      try {
         m.parseArguments(args);
         m.merge();
      } catch (InvalidRuntimeArgumentException e) {
         m.printHelp();
      }
   }
   
   public void merge() throws IOException {
      
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
      
      this.write(element);
   }
   
   private void write(ObjectElement element) throws IOException {
      
      if (element == null) {
         logger.error("No thrift element value was derived from the merge execution. Check that the input files exist and contain data.");
         return;
      }
      
      logger.info(fileMergeCount + " files were merged.");
      OutputStream outputStream = null;
      if (this.outputFile == null) {
         outputStream = new ByteArrayOutputStream();
         element.write(outputStream);
         logger.info("json-to-thrift:\n" + outputStream.toString());
      } else {
         outputStream = new FileOutputStream(this.outputFile);
         element.write(outputStream);
         logger.info("Wrote json-to-thrift to file " + this.outputFile.getAbsolutePath() + ".");
         outputStream.close();
      }
   }
   
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
   
   private ObjectElement parse(String json) {
      Converter converter = new Converter();
      converter.parse(json);
      return converter.getRoot();
   }
   
   /**
    * <p>
    * Reads the given input stream's contents and returns it as a string.
    * </p>
    * 
    * @param inputStream The input stream to read. Required.
    * @return The input stream's contents.
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
      
      Pattern pattern = Pattern.compile("-(inputDirectory|inputFile|outputFile)=\"?(.*)\"?");
      for (String argument : args) {
         
         Matcher matcher = pattern.matcher(argument);
         if (matcher.matches()) {

            String argName = matcher.group(1);
            String argValue = matcher.group(2);
            if (argName.equals("inputDirectory")) {
               this.inputDirectory = new File(argValue);
            } else if (argName.equals("inputFile")) {
               this.inputFile = new File(argValue);
            } else if (argName.equals("outputFile")) {
               this.outputFile = new File(argValue);
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
