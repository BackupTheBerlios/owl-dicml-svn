/*
 * AlphabetOrder.java
 *
 * Created on 16. Februar 2005, 19:40
 */

package de.gidoo.owl;

import org.jdom.*;
import java.util.*;

/**
 * Describes the order of characters of a natural language.<br><br>
 * 
 * It uses a XML-file which contains information about which characters (small
 * and capital are handled equal) comes first in the order. Additionally it says
 * which string should be replaced by other strings before compararion. E.g. the
 * German umlaut "&uuml;" is treated as "u".
 *
 * @author Thomas Krause
 */
public class AlphabetOrder implements java.util.Comparator<Entry> {
   
    /** 
     * The xml-document with the information about the order.
     * It's elements are accessable whenever needed.*/
    private Document doc;
    /**
     * Holds the index in the order for every character. 
     * The value of the character is used as key of the hash-map
     */
    private HashMap<Integer, Integer> charOrder;
    
    /**
     * Creates a new instance of AlphabetOrder.
     * @param orderFile the XML-file which contains the information about the order
     */
    public AlphabetOrder(java.io.File orderFile) throws java.io.IOException
    {
      // parse the XML-Content
      try
      {
        org.jdom.input.SAXBuilder builder = new org.jdom.input.SAXBuilder();
        doc = builder.build(orderFile);
        
        // create the order of the chars
                
        Element order = doc.getRootElement().getChild("order");
        List ch = order.getChildren("char");
        int countCh = ch.size();
        
        charOrder = new HashMap<Integer, Integer>(countCh);
        
        int x = 0;
        
        while(x < countCh)
        {
          Element subEl = (Element) ch.get(x);
          // captial letters
          Integer number = new Integer((int) UnicodeHelpers.hexStringToLong(subEl.getAttributeValue("capital")));
          charOrder.put(number, new Integer(x));
          // small letters
          number = new Integer((int) UnicodeHelpers.hexStringToLong(subEl.getAttributeValue("small")));
          charOrder.put(number, new Integer(x)); 
          x++;
        }
        
      }
      catch(java.io.IOException ioE)
      {
          throw(ioE);
      }
      catch(JDOMException jdE)
      {
          throw(new java.io.IOException("JDOM: " + jdE.getMessage()));
      }
      
    }
    
    /**
     * Get a String for comparision.
     * This method will perform all necessary replacements.
     *
     * @param s the original string
     * @return a string which is suitable for comparisions
     */
    public String getCompareable(String s)
    {
      String result = new String(s);
      
      // make all replacements
      Element repl = doc.getRootElement().getChild("replacement");
            
      List group = repl.getChildren("treat.gr");
      int countGroup = group.size();
      for(int x = 0; x < countGroup; x++)
      {
        Element e1 = (Element) group.get(x);
        List treat = e1.getChildren("treat");
        
        Element as = e1.getChild("as");
        String replacement = UnicodeHelpers.glyphToUnicode(as.getTextTrim());
        
        int countTreat = treat.size();
        // replace all
        for(int y = 0; y < countTreat; y++)
        {
          Element e2 = (Element) treat.get(y);
          
          String source = UnicodeHelpers.glyphToUnicode(e2.getTextTrim());
          // change possible regex-characters
          source = java.util.regex.Pattern.quote(source);
          result = result.replaceAll(source, replacement);
        }
      }
      
      return result;
    }
    
    /**
     * Compares two entries which each other using the variable "compare".
     * @param entry1 entry number one
     * @param entry2 entry number two
     * @return -1 if entry1 comes first, 1 if entry2 comes first or 
     * 0 if both are equal
     */
    public int compare(Entry entry1, Entry entry2)
    {

      // this will become the comparation strings
      String comp1 = UnicodeHelpers.glyphToUnicode(entry1.compare);
      String comp2 = UnicodeHelpers.glyphToUnicode(entry2.compare);
      
      // make all replacements
      comp1 = getCompareable(comp1);
      comp2 = getCompareable(comp2);
      
      // and now to s.th completely different:
      // compare!
      
      // go through every char-position
      int minCount = Math.min(comp1.length(), comp2.length());
      int z = 0;
      while(z < minCount)
      {
        char ch1 = comp1.charAt(z);
        char ch2 = comp2.charAt(z);
       
        int result = compareChar(ch1, ch2);
       
        if(result < 0)
        {
          return -1;              
        }
        else if(result > 0)
        {
          return 1;
        }
         
        z++;          
      }
      
      // both seem to be equal up to the minimal length
      // --> decide after the length
      
      if(comp1.length() < comp2.length())
      {
        return -1;
      }
      else if(comp1.length() > comp2.length())
      {
        return 1;         
      }
      
      // the strings are absoutely equal
      
      return 0;
    }
    
    /**
     * Compares two chars which each other.
     * @param comp1 char number one
     * @param comp2 char number two
     * @return -1 if comp1 comes first, 1 if comp2 comes first or 
     * 0 if both are equal
     */
    protected int compareChar(char comp1, char comp2)
    {
      Integer c1 = new Integer((int) comp1);
      Integer c2 = new Integer((int) comp2);
      
      Integer i1 = (Integer) charOrder.get(c1);
      Integer i2 = (Integer) charOrder.get(c2);
      
      if(i1== null)
      {
        // is not in the list --> give it the worst number
          i1 = new Integer(charOrder.size() + 1);
      }
      if(i2 == null)
      {
        // is not in the list --> give it the worst number
          i2 = new Integer(charOrder.size() + 1);
      }
      return i1.compareTo(i2);
    }
}
