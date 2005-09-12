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
  
  /** dom */
  Hashtable rplDom;
  /** pos */
  Hashtable rplPos;
  /** niv */
  Hashtable rplNiv;
  
  /** Creates a new instance of DicmlConverter */
  public ChemnitzConverter() 
  {
    _tickState = 0;
    
    rplNiv = new Hashtable(12);
    rplNiv.put("Am.", "en-US");
    rplNiv.put("Br.", "en-UK");
    rplNiv.put("Ös.", "de-AT");
    rplNiv.put("Schw.", "de-CH");
    rplNiv.put("ugs.", "fam");
    rplNiv.put("coll.", "fam");
    rplNiv.put("übtr.", "fig");
    rplNiv.put("fig.", "fig");
    rplNiv.put("pej.", "contp");
    rplNiv.put("vulg.", "vulg");
    rplNiv.put("slang", "fam");
    rplNiv.put("Schw.", "de-CH");
    
    rplDom = new Hashtable();
    rplDom.put("anat.", "anat");
    rplDom.put("arch.", "arch");
    rplDom.put("astron.", "astr");
    rplDom.put("auto", "moto");
    rplDom.put("biol.", "bio");
    rplDom.put("bot.", "bot");
    rplDom.put("chem.", "chem");
    rplDom.put("comp.", "comp");
    rplDom.put("econ.", "comm");
    rplDom.put("electr.", "elet");
    rplDom.put("cook.", "cuis");
    rplDom.put("geogr.", "geog");
    rplDom.put("geol.", "geol");
    rplDom.put("gramm.", "ling");
    rplDom.put("jur.", "lega");
    rplDom.put("math.", "math");
    rplDom.put("med.", "medec");
    rplDom.put("mil.", "mili");
    rplDom.put("min.", "mine");
    rplDom.put("mus.", "musi");
    rplDom.put("naut.", "naut");
    rplDom.put("ornith.", "orni");
    rplDom.put("pharm.", "phar");
    rplDom.put("phil.", "phil");
    rplDom.put("phys.", "phys");
    rplDom.put("pol.", "poli");
    rplDom.put("relig.", "eccl");
    rplDom.put("techn.", "tech");
    rplDom.put("zool.", "zool");
    
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
        /*String[] gram = grammarHint(s[x].trim());
        if(!gram[0].equals(""))
        {
          write("<p><t><w pos=\"" + gram[0] + "\">" + gram[1] + "</w></t></p>");
        }
        else
        {
          write("<p><t>" + s[x].trim() + "</t></p>\n");
        }
         */
        write("<p><t>");
        write(replaceGrammarHint(s[x].trim()));
        write("</t></p>");
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
  
  String replaceGrammarHint(String str)
  {
    String result = "";
    
    String[] split = str.split("[ \t]");
    int i=0;
    String lastDom = "";
    String lastNiv = "";
    String lastPos = "";
    String lastWord = "";
    
    while(i <= split.length)
    {
      if(i < split.length && split[i].startsWith("{"))
      {
        lastPos = split[i].trim();
        lastPos = lastPos.substring(1, lastPos.length() - 1);
      }
      else if(i < split.length && split[i].startsWith("["))
      {
        
      }
      else
      {
        // save the last one
        if(!lastWord.equals(""))
        {
          if(lastPos.equals(""))
          {
            result = result + lastWord + " ";
          }
          else
          {
            result = result + "<w pos=\"" + lastPos + "\">" + lastWord + "</w> ";
          }
        }
        // clear
        if(i < split.length)
        {
          lastWord = split[i].trim();
          lastDom = "";
          lastPos = "";
          lastNiv = "";
        }
      }
      
      i++;
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
