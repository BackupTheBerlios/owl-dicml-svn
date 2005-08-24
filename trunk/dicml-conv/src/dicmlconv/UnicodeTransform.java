/*
 * UnicodeTransform.java
 *
 * Created on 9. August 2005, 11:04
 *
 */

package dicmlconv;

import java.io.*;
/**
 *
 * @author thomas
 */
public class UnicodeTransform {
  
  /** Creates a new instance of UnicodeTransform */
  public UnicodeTransform() {
  }
  
  public void transformToASCII(String inputFile, String outputFile)
  {
    try
    {
      InputStreamReader reader = new InputStreamReader(new FileInputStream(inputFile), "UTF-16");
      OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outputFile), "US-ASCII");
      int c;
      while((c = reader.read()) > -1)
      {
        String str = "" + (char) c;
        // mask them
        String newStr = UnicodeHelpers.unicodeToGlyph(str);
        writer.write(newStr);
      }
      writer.close();
      reader.close();
    }
    catch(Exception e)
    {
      System.out.println("an error occured, please read the stacktrace:\n\n");
      e.printStackTrace();
    }
  }
  
  public void removeMasked(String inputFile, String outputFile, boolean windowsEndOfLine)
  {
    try
    {
      BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"));
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
      String buffer = "";
      while((buffer = reader.readLine()) != null)
      {
        String newStr = UnicodeHelpers.glyphToUnicode(buffer);
        if(windowsEndOfLine)
        {
          newStr = newStr + "\r\n";
        }
        else
        {
          newStr = newStr + "\n";
        }
        writer.write(newStr);
      }
      reader.close();
      writer.close();
    }
    catch(Exception e)
    {
      System.out.println("an error occured, please read the stacktrace:\n\n");
      e.printStackTrace();
    }
  }
  
}
