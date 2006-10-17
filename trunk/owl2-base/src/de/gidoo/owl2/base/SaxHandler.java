/*
 * SaxHandler.java
 *
 * Created on 9. August 2006, 19:02
 *
 * See Licence.txt for more information according to this file.
 */

package de.gidoo.owl2.base;

import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.*;
import java.util.ArrayList;

/**
 * User class for handling events of the SAX-parser
 * @author <a href="mailto:krause@informatik.hu-berlin.de">Thomas Krause</a>
 */
public class SaxHandler extends DefaultHandler {
  
  public List<String> lastLemmaInEntry;
  public String lastIdInEntry;
  
  private boolean _record;
  private String _buffer;
  
  /**
   * Creates a new instance of SaxHandler
   */
  public SaxHandler() 
  {
    lastLemmaInEntry = new ArrayList<String>();
  }

  public void startDocument() throws SAXException {
    _record = false;
  }


  
  public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException 
  {
    if(!_record)
    {
      if(name.equals("orth") || name.equals("orth.alt"))
      {
        _record = true;
        _buffer = "";
      }      
    }
    
    if(name.equals("entry"))
    {
      lastIdInEntry = atts.getValue("id");
    }
  }

  public void endElement(String uri, String name, String qName) throws SAXException 
  {
    if(_record)
    {
      if(name.equals("orth") || name.equals("orth.alt"))
      {
        _record = false;
        lastLemmaInEntry.add(_buffer);
      }
    }
  }
  
  public void characters(char[] c, int start, int length) throws SAXException 
  {
    if(_record)
    {
      for(int i=start; i < start + length; i++)
      {
        _buffer = _buffer + c[i];
      }
    }
  }
  

  

  
    
}
