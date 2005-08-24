/*
 * SyncHTMLEditorKit.java
 *
 * Created on 27. Juli 2005, 18:10
 *
 */

package de.gidoo.owl;

import javax.swing.text.*;
import javax.swing.text.html.*;

/**
 * Render HTML-Pages.<br><br>
 *
 * Acts almost like the original HTMLEditorKit except
 * that it will load Documents synchronously (instead
 * of asynchronously like HTMLEditorkit)
 * @author thomas
 */
public class SyncHTMLEditorKit extends HTMLEditorKit {
  
  /** Creates a new instance of SyncHTMLEditorKit */
  public SyncHTMLEditorKit()
  {
    super();
  }
  /**
   * Overriden in order to load the Document synchronously
   */
  public Document createDefaultDocument()
  {
    Document doc = super.createDefaultDocument();
    
    doc.putProperty("load priority", -1);
    
    return doc;
  }
  
}
