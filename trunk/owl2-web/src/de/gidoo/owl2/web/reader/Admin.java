/*
 * Admin.java
 *
 * Created on 12. August 2006, 17:34
 *
 * See Licence.txt for more information according to this file.
 */

package de.gidoo.owl2.web.reader;

import de.gidoo.owl2.base.IDictionaryProvider;
import de.gidoo.owl2.base.SQLiteProvider;
import java.util.ArrayList;
import wicket.Session;
import wicket.extensions.markup.html.tabs.AbstractTab;
import wicket.markup.html.basic.*;
import wicket.markup.html.form.*;
import wicket.markup.html.link.*;
import wicket.markup.html.panel.*;
import wicket.extensions.ajax.markup.html.tabs.*;
import wicket.model.Model;

/**
 * A page to administrate the installation of owl².
 * E.g. you can adjust what dictionaries are imported.
 * @author <a href="mailto:krause@informatik.hu-berlin.de">Thomas Krause</a>
 */
public class Admin extends PageWithAdminRightsNeeded 
{
  private AjaxTabbedPanel tabPanelDics;
  
  /** Creates a new instance of Admin */
  public Admin() 
  {
    
    Link lnkLogout = new Link("lnkLogout") 
    {
      public void onClick() 
      {
        ((SignInSession) Session.get()).logout();
        setResponsePage(OwlReader.class);
      }
    };
    
    add(lnkLogout);
    
    // one tab for each available dictionary-database
    ArrayList<AbstractTab> panels = new ArrayList<AbstractTab>();
    panels.add(new AbstractTab(new Model("imported dictionaries"))
    {
      public Panel getPanel(String id){return new ImportedDicPanel(id);}
    });

    panels.add(new AbstractTab(new Model("dictionaries which may be imported"))
    {
      public Panel getPanel(String id){return new DicsToImportPanel(id);}
    });
    
    tabPanelDics = new AjaxTabbedPanel("tabPanel", panels);
    add(tabPanelDics);
    
  }
  
}
