/*
 * OwlApp.java
 *
 * Created on July 17, 2006, 9:06 PM
 *
 * See Licence.txt for more information according to this file.
 */

package de.gidoo.owl2.web.reader;

import wicket.ISessionFactory;
import wicket.Session;
import wicket.authorization.IAuthorizationStrategy;
import wicket.protocol.http.WebApplication;

/**
 * The application class for owl2
 * @author <a href="mailto:krause@informatik.hu-berlin.de">Thomas Krause</a>
 */
public class OwlApp extends wicket.protocol.http.WebApplication {
  
    public static String realPathToContext;
      
    /** Creates a new instance of OwlApp */
    public OwlApp() {
    }

    public void init()
    {
      realPathToContext = this.getWicketServlet().getServletContext().getRealPath("/");
      getDebugSettings().setSerializeSessionAttributes(false); 
      
      getSecuritySettings().setAuthorizationStrategy(new SimpleAuthorizationStrategy());
      
    }
    
    public Class getHomePage() {
        // start with OwlReader in test-phase
        return OwlReader.class;
    }
  

  protected ISessionFactory getSessionFactory()
  {
    return new ISessionFactory()
    {
      public Session newSession() 
      {
        return new SignInSession((WebApplication) OwlApp.get());
      }
      
    };
  }

}
