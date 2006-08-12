/*
 * SimpleAuthorizationStrategy.java
 *
 * Created on 12. August 2006, 21:12
 *
 * See Licence.txt for more information according to this file.
 */

package de.gidoo.owl2.web.reader;

import wicket.Session;
import wicket.authorization.IAuthorizationStrategy;
import wicket.RestartResponseAtInterceptPageException;

/**
 * Restrict access to web-pages that are derived from PageWithAdminRightRequired.
 * @author <a href="mailto:krause@informatik.hu-berlin.de">Thomas Krause</a>
 */
public class SimpleAuthorizationStrategy implements IAuthorizationStrategy {
  
  /** Creates a new instance of SimpleAuthorizationStrategy */
  public SimpleAuthorizationStrategy()
  {
  }
  
  /** Every action is authorized up to now*/
  public boolean isActionAuthorized(wicket.Component comp, wicket.authorization.Action action)
  {
    return true;
  }
  
  /** Restrict access to web-pages that are derived from PageWithAdminRightRequired. */
  public boolean isInstantiationAuthorized(Class c)
  {
    if(PageWithAdminRightsNeeded.class.isAssignableFrom(c))
    {
      if(((SignInSession) Session.get()).isAdmin())
      {
        return true;
      }
      
      // force login
      //throw new RestartResponseAtInterceptPageException(Login.class);
        
    }
    return true ;
  }
  
}
