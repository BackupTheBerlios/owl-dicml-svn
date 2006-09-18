/*
 * ResultTabPanel.java
 *
 * Created on 9. August 2006, 15:08
 *
 * See Licence.txt for more information according to this file.
 */

package de.gidoo.owl2.web.reader;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import wicket.extensions.ajax.markup.html.WicketAjaxIndicatorAppender;
import wicket.markup.html.WebComponent;
import wicket.markup.html.basic.*;
import wicket.model.*;
import javax.xml.transform.*;

/**
 * A simple "template" for a panel inside a tab with a label. 
 * Will use a XSLT-Script to convert the input.
 * @author <a href="mailto:krause@informatik.hu-berlin.de">Thomas Krause</a>
 */
public class ResultTabPanel extends wicket.markup.html.panel.Panel {

  public final String XSLT_FILE_PATH = "/WEB-INF/dicml_2-0.xsl";
  
  private Label _text;
  
  /** Creates a new instance of ResultTab */
  public ResultTabPanel(String id, String lemma, String entry, boolean doXSLTTransform)
  {
    super(id);
    
    String entryHTML = "";
    TransformerFactory tFactory = TransformerFactory.newInstance();
    
    if(doXSLTTransform)
    {
      try
      {
        File f = new File(OwlApp.realPathToContext + XSLT_FILE_PATH);
        if(f.exists())
        {
          Transformer transformer = tFactory.newTransformer(new StreamSource(OwlApp.realPathToContext + XSLT_FILE_PATH));

          StringWriter w = new StringWriter();

          transformer.transform(new StreamSource(new StringReader(entry)), new StreamResult(w));

          entryHTML = w.toString();
        }
      }
      catch(Exception ex)
      {
        ex.printStackTrace();
        entryHTML = getString("errorXSLT");
      }
    }
    else
    {
      entryHTML = entry;
    }
    
    _text = new Label("label", entryHTML);
    _text.setEscapeModelStrings(false);
    
    add(_text);
    
  }
  
}
