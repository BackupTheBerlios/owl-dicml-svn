/*
 * PageWithAdminRightsNeeded.java
 *
 * Created on 12. August 2006, 17:34
 *
 * See Licence.txt for more information according to this file.
 */

package de.gidoo.owl2.web.reader;

import wicket.markup.html.WebPage;

/**
 * If a Page is derived from this class, we no that we need Adminstrator rights.<br>
 * Owl uses a very simple approach: every general visitor of our website is a user.
 * Admins have to log in and can manage the application (import dictionaries and so on).
 * @author <a href="mailto:krause@informatik.hu-berlin.de">Thomas Krause</a>
 */
public class PageWithAdminRightsNeeded extends WebPage
{ 
  
}
