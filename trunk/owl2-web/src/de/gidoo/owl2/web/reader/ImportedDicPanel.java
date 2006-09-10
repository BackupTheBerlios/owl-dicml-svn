/*
 * ImportedDicPanel.java
 *
 * Created on 4. September 2006, 18:36
 *
 * See Licence.txt for more information according to this file.
 */

package de.gidoo.owl2.web.reader;

import de.gidoo.owl2.base.IDictionaryProvider;
import de.gidoo.owl2.base.SQLiteProvider;
import java.util.*;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.Link;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.model.Model;

/**
 * Display a list of imported dictonaries and let the user delete single entries.
 * @author <a href="mailto:krause@informatik.hu-berlin.de">Thomas Krause</a>
 */
public class ImportedDicPanel extends wicket.markup.html.panel.Panel {
  
  private ListView _lsvImportedDics;
  private Label _lblNothingFound;
  private List<String> _availableDics = new Vector<String>();
  private IDictionaryProvider _dic;
  
  /**
   * Creates a new instance of ImportedDicPanel
   */
  public ImportedDicPanel(String id) 
  {
    super(id);
    
    _dic = new SQLiteProvider(OwlApp.realPathToContext + "WEB-INF/owl.db");
    
    _lblNothingFound = new Label("lblNothingFound", new Model("Nothing imported"));
    _lblNothingFound.setVisible(false);
    add(_lblNothingFound);
    
    updateDicList();
    
    _lsvImportedDics = new ListView("lsvImportedDics")
    {
      protected void populateItem(final ListItem item) 
      {
        final String s = item.getModelObjectAsString();
        item.add(new Label("lblName", s));
        item.add(new Link("lnkDelete") {
          public void onClick() 
          {
            _dic.deleteDictionary(s);
            updateDicList();
            _lsvImportedDics.modelChanged();
          }
        });
      }
    };
    
    _lsvImportedDics.setList(_availableDics);
    
    add(_lsvImportedDics);
    
  }
  
  private void updateDicList()
  {
    // get all imported dictionaries
    _lblNothingFound.setVisible(false);
    _availableDics.clear();
    String[] d = _dic.getAvailableDictionaries();
    for(int i=0; i<d.length; i++)
    {
      _availableDics.add(d[i]);
    }    
    
    if(d.length == 0 && _lblNothingFound != null)
    {
      _lblNothingFound.setVisible(true);
    }
    
  }
  
}
