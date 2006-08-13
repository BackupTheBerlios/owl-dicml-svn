/*
 * Admin.java
 *
 * Created on 12. August 2006, 17:34
 *
 * See Licence.txt for more information according to this file.
 */

package de.gidoo.owl2.web.reader;

import wicket.Session;
import wicket.markup.html.basic.*;
import wicket.markup.html.link.Link;

/**
 *
 * @author <a href="mailto:krause@informatik.hu-berlin.de">Thomas Krause</a>
 */
public class Admin extends PageWithAdminRightsNeeded {
  
  /** Creates a new instance of Admin */
  public Admin() 
  {
    add(new Label("lblTitle", getString("lblTitle")));
    
    add(new Link("lnkLogout") 
    {
      public void onClick() 
      {
        ((SignInSession) Session.get()).logout();
        setResponsePage(OwlReader.class);
      }
    });
  }
  
}
