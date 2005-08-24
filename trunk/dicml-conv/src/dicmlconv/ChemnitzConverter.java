/*
 * ChemnitzConverter.java
 *
 * Created on 28. Februar 2005, 17:11
 */

package dicmlconv;

import java.io.*;
import java.util.Hashtable;
import java.util.ArrayList;

/**
 *
 * @author thomas
 */
public class ChemnitzConverter {
  
  public enum TranslationDirection {LEFT_SIDE, RIGHT_SIDE};
  
  File fileIn;
  File fileOut;
  InputStreamReader readerIn;
  OutputStreamWriter writerOut;
  
  int _tickState;
  
  int countEntries;
  
  long writtenBytes;
    
  TranslationDirection direction;
  
  /** Creates a new instance of DicmlConverter */
  public ChemnitzConverter() 
  {
    _tickState = 0;
  }
  
  void convertChemnitz(String In, String Out, String config, TranslationDirection dir)
  { 
      info("convert from \"Chemnitz-Style\": ");
      
      countEntries = 0;
      
      this.direction = dir;
      writtenBytes = 0;
      try
      {
        // open the file
        fileIn = new File(In);
        readerIn = new InputStreamReader(new FileInputStream(fileIn), "UTF-8");
        
        fileOut = new File(Out);
        writerOut = new OutputStreamWriter(new FileOutputStream(fileOut), "UTF-8");
        info("writing header");
        writeHeader("");
        
        int r;
        char ch;
        String entry = "";
        info("read in the file");
      
        System.out.print("\b");
        _tickState = -1;
        int count = 0;
        
        while((r = readerIn.read()) != -1)
        {
          
          if(count % 10000 == 0)
          {
            doTick();
          }
          
          ch = (char) r;
          
          // get the entry
          if(ch == '\n')
          {
            writeEntry(entry);
            entry = "";
          }
          else
          {
            entry += ch;
          }
          count++;
        }
        
        writeFooter();
        
        writerOut.close();
        readerIn.close();

        _tickState = 5;
        doTick();
        
        info("finished!");
        info("wrote " + countEntries + " entries");
      }
      catch(IOException ioE)
      {
        error("file-operation-error " + ioE.getMessage());
      }
      
    }
  
  void writeEntry(String entry) throws IOException
  {
      if(entry.charAt(0) != '#' && entry.charAt(0) != '\u0000')
      {
        // find the lemma (maybe more than one)
        String[] splitEntry = entry.split("::");
        if(splitEntry.length == 2) // only this should be expected, everything else is nonsense
        {
          String[] splitLemma;
          String[] splitSense;
          if(direction == TranslationDirection.LEFT_SIDE)
          {
            splitLemma = splitEntry[0].trim().split("\\u007C"); // they belong together in some kind
            splitSense = splitEntry[1].trim().split("\\u007C");
          }
          else if(direction == TranslationDirection.RIGHT_SIDE)
          {
            splitLemma = splitEntry[1].trim().split("\\u007C"); // they belong together in some kind
            splitSense = splitEntry[0].trim().split("\\u007C");
          }
          else
          {
            error("neither \"left\" nor \"right\" was choosen");
            return; // this would be an error
          }
          
          if(splitLemma.length == splitSense.length) // should be true as well
          {
            for(int i=0; i < splitLemma.length; i++)
            {
              String[] subLemma = splitLemma[i].split(";"); // different lemma with same translation 
              for(int j=0; j < subLemma.length; j++)
              {
                  write("<entry id=\"" + "noID" + "\">\n");
                  writeLemmaGroup(subLemma[j].trim());
                  writeSenseGroup(splitSense[i].trim());
                  write("</entry>\n\n");
                  countEntries++;
              }
            }                    
          }
          else
          {
            error("both sides have a different count of |-divided sectors");
          }
        }
      }
   }
  
  void writeLemmaGroup(String lemma) throws IOException
  {
      write("<lemma.gr>\n");
      
      String result = writeLemma(lemma);
      
      
      write("</lemma.gr>\n");
    }
    
  String writeLemma(String l) throws IOException
  {
    /*
      // find "pos" etc.
      String insideBrake = "";
      
      // search for grammatical hint inside { }      
      int foundBrakeS = l.indexOf("{");
      int foundBrakeE = -1;
      
      if(foundBrakeS > -1)
      {
        foundBrakeE = l.indexOf("}", foundBrakeS);
        if(foundBrakeE > -1)
        {
          insideBrake = l.substring(foundBrakeS + 1, foundBrakeE);
          l = l.substring(0, foundBrakeS);
        }
      }
      */
      String[] gram = grammarHint(l.trim());
      write("<l>" + gram[1] + "</l>\n");
      if(!gram[0].equals(""))
      {
        write("<pos pos=\"" + gram[0] + "\" />\n");
      }
      return l;
    }
    
   void writeSenseGroup(String sense) throws IOException
  {
      // split it
      String[] s = sense.split(";");
      
      for(int x = 0; x < s.length; x++)
      {
        write("<sense.gr>\n");
        String[] gram = grammarHint(s[x].trim());
        if(!gram[0].equals(""))
        {
          write("<p><t><w pos=\"" + gram[0] + "\">" + gram[1] + "</w></t></p>");
        }
        else
        {
          write("<p><t>" + s[x].trim() + "</t></p>\n");
        }
        write("</sense.gr>\n");
      }
      
    }
   
  /** 
   * Search for grammatical hint inside { }.
   * Will cut out this hint from the original string.
   * @return empty string if not existing or the grammatical hint
   *  at index 0 and the rest-string at index 1
   */
  String[] grammarHint(String str)
  {
    String insideBrake = "";
    String[] result = new String[2];
    result[0] = "";
    result[1] = str;
    
    int foundBrakeS = str.indexOf("{");
    int foundBrakeE = -1;
      
    if(foundBrakeS > -1)
    {
      foundBrakeE = str.indexOf("}", foundBrakeS);
      if(foundBrakeE > -1)
      {
        result[0] = str.substring(foundBrakeS + 1, foundBrakeE);
        result[1] = str.substring(0, foundBrakeS).trim();
      }
    }
    
    return result;
  }
   
  void writeHeader(String conf) throws IOException
  {
      String buffer = "";
      buffer += "<dicml>\n";
      buffer += "<head>\n";
      buffer += "<dic.lang-s>de</dic.lang-s>\n";
      // TODO: more infos
      buffer += "</head>\n";
      buffer += "<body>\n";
      
      write(buffer);
    }
  
  void writeFooter() throws IOException
  {
      String buffer = "";
      buffer += "</body>\n";
      buffer += "</dicml>\n";
      
      write(buffer);
    }
  
  void info(String text)
  {
      System.out.println("INFO: " + text);
    }
    
  void doTick()
  {
    switch(_tickState)
    {
      case -1:
              System.out.print("|");
              _tickState = 0;
              break;
      case 0:
              System.out.print("\b/");
              _tickState = 1;
              break;
      case 1:
              System.out.print("\b-");
              _tickState = 2;
              break;
      case 2:
              System.out.print("\b-");
              _tickState = 3;
              break;
      case 3:
              System.out.print("\b\\");
              _tickState = 4;
              break;
      case 4:
              System.out.print("\b|");
              _tickState = 0;
              break;
      case 5:
              System.out.println("\b");
              break;
    }
  }
    
  void error(String text)
  {
      System.out.println("ERROR: " + text);
  }
  
  void write(String str) throws IOException
  {
    writerOut.write(str);
    writtenBytes += str.getBytes().length;
  }
}
