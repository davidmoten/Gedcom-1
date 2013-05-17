package org.folg.gedcom.tools;

import org.folg.gedcom.model.EventFact;
import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.model.Visitor;
import org.folg.gedcom.parser.ModelParser;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * User: dallan
 * Date: 5/18/12
 */
public class EventNameCounter extends Visitor {
   @Option(name="-i", required=true, usage="file or directory containing gedcom files to process")
   private File gedcomIn;

   private CountsCollector cc;
   private ModelParser parser;

   public EventNameCounter() {
      parser = new ModelParser();
      cc = new CountsCollector();
   }

   @Override
   public boolean visit(EventFact eventFact) {
      String displayName = eventFact.getDisplayType();
      if (EventFact.OTHER_TYPE.equals(displayName)) {
         if (eventFact.getType() != null && eventFact.getType().length() > 0) {
            displayName = eventFact.getType();
         }
         else if (eventFact.getTag() != null && eventFact.getTag().length() > 0) {
            displayName = eventFact.getTag();
         }
      }
      cc.add(displayName);
      return true;
   }

   public void processGedcom(File file) throws SAXParseException, IOException {
      try {
         Gedcom gedcom = parser.parseGedcom(file);
         gedcom.accept(this);
      }
      catch (Exception e) {
         System.err.println("Error processing file: "+file.getName()+" "+e.getMessage());
      }
   }

   private void doMain() throws IOException, SAXParseException {
      if (gedcomIn.isDirectory()) {
         for (File file : gedcomIn.listFiles()) {
            processGedcom(file);
         }
      }
      else if (gedcomIn.isFile()) {
         processGedcom(gedcomIn);
      }

      cc.writeSorted(false, 100, new PrintWriter(System.out));
   }

   public static void main(String[] args) throws IOException, SAXParseException {
      EventNameCounter self = new EventNameCounter();
      CmdLineParser parser = new CmdLineParser(self);
      try {
         parser.parseArgument(args);
         self.doMain();
      }
      catch (CmdLineException e) {
         System.err.println(e.getMessage());
         parser.printUsage(System.err);
      }
   }

}
