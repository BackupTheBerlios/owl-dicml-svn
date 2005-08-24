/*
 * UnicodeHelpers.java
 *
 * Created on 16. Februar 2005, 20:56
 */

package dicmlconv;

/**
 *
 * @author thomas
 */
public class UnicodeHelpers {
    
    /** Creates a new instance of UnicodeHelpers */
    public UnicodeHelpers() {
    }
  
  static long[] pow16 = {1l, 16l, 256l, 4096l, 65536l, 1048576l, 16777216l, 268435456l, 4294967296l,
                  68719476736l, 1099511627776l, 17592186044416l, 281474976710656l,
                  4503599627370496l, 72057594037927936l, 1152921504606846976l};
  
  static String unicodeToGlyph(String text)
  {
    String result = "";
    
    for(int i=0; i < text.length(); i++)
    {
      char c = text.charAt(i);
      if((long) c > 127) // then it is a special character which has to be marked
      {
        //String hex = longToHexString((long) c);
        String hex = Long.toHexString((long) c);
        hex = "&#x" + hex + ";";
        result = result + hex;
      }
      else
      {
        result = result + c;
      }
    }
    
    return result;
  }
  
  /** replaces all masked characters in the string with the real character */
  static String glyphToUnicode(String text)
  {
    int foundAmpersand = 0;
    int foundSemicolon = 0;
    int lastSemicolon = -1;
    String hexString = "";
    long hexValue = -1;
    String result = "";
    char c = 'A';
    //find all "&#x"
    while( (foundAmpersand = text.indexOf("&#x", foundAmpersand)) != -1 )
    {
      foundAmpersand += 3;
      //find next semicolon
      foundSemicolon = text.indexOf(";", foundAmpersand);
      //slice out the hexadecimal value
      hexString = text.substring(foundAmpersand, foundSemicolon);
      hexValue = hexStringToLong(hexString);
      //set together to new string
      c = (char)hexValue;
      result = result + text.substring(lastSemicolon+1, foundAmpersand-3) + c;
      lastSemicolon = foundSemicolon;
    }
    result = result + text.substring(lastSemicolon+1);
    return result;
  }
  
  /**
  convert strings with hexadecimal values (e.g. "04E4") to long
  */
  static long hexStringToLong(String text)
  {
		
  text = text.toLowerCase();
  char character;
  long number = 0;
  long result = 0;
	for(int i = 0, n=text.length()-1; i < text.length(); i++, n--)
  {    
		character = text.charAt(i);
		switch(character) 
    {
		case '0':
					number = 0;
					break;
		case '1':
					number = 1;
					break;
		case '2':
					number = 2;
					break;
		case '3':
					number = 3;
					break;
		case '4':
					number = 4;
					break;
		case '5':
					number = 5;
					break;
		case '6':
					number = 6;
					break;
		case '7':
					number = 7;
					break;
		case '8':
					number = 8;
					break;
		case '9':
					number = 9;
					break;
		case 'a':
					number = 10;
					break;
		case 'b':
					number = 11;
					break;
		case 'c':
					number = 12;
					break;
		case 'd':
					number = 13;
					break;
		case 'e':
					number = 14;
					break;
		case 'f':
					number = 15;
					break;
		}
		result = result + (long) (number * Math.pow(16, n));
	}
	return result;
}
  /**
   * converts a long to a string representating this value
   * (reverse of hexStringToLong)
   */
  static String longToHexString(long value)
  {
    
    String result = "";
    long rest = value;
    
    
    while(rest > 0)
    {
      byte power = 1;
      // find the highest power of 16 which is smaller than rest
      while(pow16[power] < rest)
      {
        power++;
      }
      power--;
      int multiply = (int) (rest / pow16[power]);
      rest = rest - (multiply * pow16[power]);
      String digit = "";
      if(multiply < 10)
      {
        digit = String.valueOf(multiply);
      }
      else if(multiply == 10)
      {
        digit = "A";
      }
      else if(multiply == 11)
      {
        digit = "B";
      }
      else if(multiply == 12)
      {
        digit = "C";
      }
      else if(multiply == 13)
      {
        digit = "D";
      }
      else if(multiply == 14)
      {
        digit = "E";
      }
      else if(multiply == 15)
      {
        digit = "F";
      }
      
      result = result + digit;
    }
    return result;
  }
}
