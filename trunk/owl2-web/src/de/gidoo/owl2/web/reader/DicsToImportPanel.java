/*
 * DicsToImportPanel.java
 *
 * Created on 7. September 2006, 09:50
 *
 * See Licence.txt for more information according to this file.
 */

package de.gidoo.owl2.web.reader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import wicket.model.*;
import wicket.markup.html.basic.*;
import wicket.markup.html.link.Link;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;

/**
 * Presents a list of all dicML files inside a special folder of the server
 * and let the admin import them.
 * @author <a href="mailto:krause@informatik.hu-berlin.de">Thomas Krause</a>
 */
public class DicsToImportPanel extends wicket.markup.html.panel.Panel {
  
  private static final List _dictList = new Vector();
  private final ListView lstDicts;
  
  /**
   * Creates a new instance of DicsToImportPanel
   */
  public DicsToImportPanel(String id) 
  {
    super(id);
    
    _dictList.clear();
    
    updateDicList();
        
    lstDicts = new ListView("listView", _dictList) 
    {
      protected void populateItem(ListItem item) 
      {
        final File f = (File) item.getModelObject();
        item.add(new Label("lblFileName", new Model(f.getName())));
        item.add(new Link("lnkImport") {
          public void onClick() 
          {
            importDict(f.getAbsolutePath());
          }
        });
      }
    };
    
    add(lstDicts);
    
  }
  
  private void updateDicList()
  {
    // iterate through the dirctory
    File dictDir = new File(OwlApp.realPathToContext + "WEB-INF/dicts");
    if(!dictDir.exists())
    {
      dictDir.mkdir();
    }
    
    if(dictDir.isDirectory())
    {
      File[] subfiles = dictDir.listFiles();
      for(int i=0; i < subfiles.length; i++)
      {
        if(subfiles[i].getName().endsWith(".dicml"))
        {
          _dictList.add(subfiles[i]);
        }
      }
    }
  }
  
  private final void importDict(String path)
  {
    // do something usefull
  }
  
}
