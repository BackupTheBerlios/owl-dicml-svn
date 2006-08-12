/*
 * SignInSession.java
 *
 * Created on 12. August 2006, 17:46
 *
 * See Licence.txt for more information according to this file.
 */

package de.gidoo.owl2.web.reader;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.naming.java.javaURLContextFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import wicket.protocol.http.WebApplication;
import wicket.protocol.http.WebSession;

/**
 * A session that supports authentification for users. Currently only one role 
 * (admin) is supported. This means that every logged in user is an admin.
 * @author <a href="mailto:krause@informatik.hu-berlin.de">Thomas Krause</a>
 */
public class SignInSession extends WebSession {
  
  private String _currentUserName = null;
  
  /** Creates a new instance of SignInSession */
  public SignInSession(final WebApplication application) {
    super(application);
  }
  
  /**
   * Try to login a user for this session.
   * @param username the username used for authentification
   * @param pwd the password as clear text (no hash)
   * @return true if login was successful; false if password and username doesn't match or if user is already logged in
   */
  public boolean login(String username, String pwd)
  {
    if(_currentUserName == null)
    {
      if(username != null && pwd != null)
      {
        javax.xml.parsers.DocumentBuilder db;
        try 
        {
          // prepare password
          String hashVal = new String(
            java.security.MessageDigest.getInstance("MD5").digest(
            pwd.getBytes()));
          
          // read in user.xml          
          db = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder();
          org.w3c.dom.Document doc = db.parse("WEB-INF/loginData/users.xml");
          
          // get all users and check wether username and hash-value of password are equal
          NodeList nodeList = doc.getElementsByTagName("user");
          int i=0;
          while(nodeList.item(i) != null)
          {
            NamedNodeMap atts = nodeList.item(i).getAttributes();
            if(username.equals(atts.getNamedItem("user").getNodeValue())
              && hashVal.equals(atts.getNamedItem("password").getNodeValue()))
            {
              // everything is correct
              _currentUserName = username;              
              return true;
            }
            
            i++;
          }
          
        } 
        catch (Exception ex)
        {
          ex.printStackTrace();
          error(ex.toString());
          return false;
        }
        
      }
    }
      
    return false;
  }
  
  public void logout()
  {
    _currentUserName = null;
  }
  
  /**
   * Wether the user behind this session is logged in as admin.
   * @return true if admin, false if not
   */
  public boolean isAdmin()
  {
    // since we only have not-logged in users and logged-in administrators...
    return (_currentUserName != null);
  }
}
