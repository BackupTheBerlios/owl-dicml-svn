/*
 * ResultTabPanel.java
 *
 * Created on 9. August 2006, 15:08
 *
 * See Licence.txt for more information according to this file.
 */

package de.gidoo.owl2.web;

import wicket.markup.html.basic.*;
import wicket.model.*;

/**
 *
 * @author <a href="mailto:krause@informatik.hu-berlin.de">Thomas Krause</a>
 */
public class ResultTabPanel extends wicket.markup.html.panel.Panel {
  
  /** Creates a new instance of ResultTab */
  public ResultTabPanel(String id, String lemma, String entry)
  {
    super(id);
    add(new Label("label", entry));
  }
  
}
