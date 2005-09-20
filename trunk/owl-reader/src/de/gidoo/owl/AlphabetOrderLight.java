/*
 * AlphabetOrderLight.java
 *
 * Created on 31. Juli 2005, 14:45
 *
 */

package de.gidoo.owl;

/**
 * Describes the order of characters of a natural language.<br><br>
 *
 * The only differences to AlphabetOrder are, that<br>
 * - the strings to compare will not be processed by getCompareable() nor by
 * glyphToUnicode()<br>
 * (this has to be done manually by the calling-procedure)<br>
 * - compare() does not use Entry.compare, but Entry.compareProcessed or
 * Entry.collectionKey (depending which constructor was used)
 *
 * @author Thomas Krause
 */
public class AlphabetOrderLight extends AlphabetOrder
{
  /**
   * Creates a new instance of AlphabetOrder using an user specified xml-file.
   * @param orderFile the XML-file which contains the information about the order
   */
  public AlphabetOrderLight(java.io.File orderFile) throws java.io.IOException
  {
    super(orderFile);
  }
  
  /**
    * Creates a new instance of AlphabetOrder using the Java-api
    * @param locale defines the language to be used
  */
  public AlphabetOrderLight(java.util.Locale locale)
  {
    super(locale);
  }
  
  /**
   * Compares two entries which each other using the variable "collationKey" or
   * "compareProcessed".
   * @param entry1 entry number one
   * @param entry2 entry number two
   * @return -1 if entry1 comes first, 1 if entry2 comes first or 
   * 0 if both are equal
   */
  public int compare(Entry entry1, Entry entry2)
  {
    if(coll != null)
    {
      return entry1.collationKey.compareTo(entry2.collationKey);
    }
    else
    {
      return compareManually(entry1, entry2);
    }
  }
  
  /**
   * Compares two entries which each other using the variable "compareProcessed".
   * @param entry1 entry number one
   * @param entry2 entry number two
   * @return -1 if entry1 comes first, 1 if entry2 comes first or 
   * 0 if both are equal
   */
  private int compareManually(Entry entry1, Entry entry2)
  {      
      // this will become the comparation strings
      String comp1 = UnicodeHelpers.glyphToUnicode(entry1.compareProcessed);
      String comp2 = UnicodeHelpers.glyphToUnicode(entry2.compareProcessed);
      
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
}
