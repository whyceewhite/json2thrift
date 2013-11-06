package org.gee.thrifty;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.gee.thrifty.datatype.ObjectElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Merger {
   
   private Logger logger = LoggerFactory.getLogger(getClass());
   private File inputDirectory;;
   private File inputFile;
   private File outputFile;
   
   public Merger() {
   }
   
   public static void main(String[] args) throws Exception {
      Merger m = new Merger();
      m.parseArguments(args);
      m.merge();
   }
   
   public void merge() throws IOException {
      // TODO: too long. modularize.
      ObjectElement element = null;
      if (this.inputDirectory != null) {
         File[] files = this.inputDirectory.listFiles();
         if (files != null) {
            for (File jsonFile : files) {
               logger.info("Processing file " + jsonFile.getAbsolutePath());
               element = this.merge(new FileInputStream(jsonFile), element);
            }
         }
      } else if (this.inputFile != null) {
         logger.info("Processing file " + this.inputFile.getAbsolutePath());
         element = this.merge(new FileInputStream(this.inputFile));
      } else {
         throw new RuntimeException("Either an input directory or file must be specified.");
      }
      
      if (element == null) {
         logger.error("No value was derived from the merge execution. Check that the input files exist and contain data.");
         return;
      }
      
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
   
   private void validate() {
      // check that either inputDirectory or inputFile is given
      // check that inputDirectory exists and is a directory.
      // check that inputFile exists.
   }
   
   ObjectElement merge(InputStream stream, ObjectElement element) throws IOException {
      ObjectElement parsedElement = this.merge(stream);
      if (element == null) {
         element = parsedElement;
      }
      return (ObjectElement)element.merge(parsedElement);
   }
   
   ObjectElement merge(InputStream stream) throws IOException  {
      String contents = IOUtils.toString(stream);
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
   
}
