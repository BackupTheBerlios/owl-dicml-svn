/*
 * OwlReader.java
 *
 * Created on July 17, 2006, 9:17 PM
 *
 * See Licence.txt for more information according to this file.
 */

package de.gidoo.owl2.web;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Array;
import java.util.*;
import wicket.extensions.markup.html.tabs.AbstractTab;

import wicket.markup.html.basic.*;
import wicket.markup.html.form.*;
import wicket.markup.html.panel.Panel;
import wicket.model.*;
import wicket.Component;
import wicket.markup.html.link.*;
import wicket.extensions.ajax.markup.html.autocomplete.*;
import wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import wicket.extensions.markup.html.tabs.AbstractTab;

import de.gidoo.owl2.base.*;

/**
 * A page to search in and display items of a dictonary
 *
 * @author <a href="mailto:krause@informatik.hu-berlin.de">Thomas Krause</a>
 */
public class OwlReader extends wicket.markup.html.WebPage {

    private IDictionaryProvider _dic;
    private MessageForm mainForm;
    private AjaxTabbedPanel tabPanel;
        
    /** Creates a new instance of OwlReader */
    public OwlReader() {
     
      _dic = new SQLiteProvider(OwlApp.realPathToContext + "owl.db");
     
      if(!_dic.isImported("de_en"))
        _dic.importDictionary(OwlApp.realPathToContext + "de-en.dicml", "de_en");
      
      _dic.activateDictionary("de_en");
            
      mainForm = new MessageForm("form");
      add(mainForm);
      
      // empty tab-list at begin
      tabPanel = getAvailableTabs();
      add(tabPanel);
      
    }
    
    protected void onAttach()
    {
      tabPanel = getAvailableTabs();
      replace(tabPanel);
    } 
    
    public AjaxTabbedPanel getAvailableTabs()
    {
      List l = new ArrayList();
     
      // get the searched text
      final String search = mainForm.getSearchedText();  
      
      if(search == null || search.equals(""))
      {
        l.add(new AbstractTab(new Model("info"))
          {
            public Panel getPanel(String panelId) {return new ResultTabPanel(panelId, "", "no search text entered");}
          }
        );
        return new AjaxTabbedPanel("tabs", l);
      }
      
      final String[][] results = _dic.getEntry(search);
      
      if(results == null || results.length == 0)
      {
        l.add(new AbstractTab(new Model("error"))
          {
            public Panel getPanel(String panelId) {return new ResultTabPanel(panelId, "", "no entry found");}
          }
        );
        return new AjaxTabbedPanel("tabs", l);
      }
      
      for(int i=0; i < results.length; i++)
      {
        l.add(new ResultAbstractTab(new Model("" + (i+1) + " (" + results[i][1] + ")"), search, results[i][0]));
      }
      
      return new AjaxTabbedPanel("tabs", l);
    }

    /******************/
    /* HELPER CLASSES */
    /******************/
    private final class ResultAbstractTab extends AbstractTab
    {
      private String title;
      private String content;
      
      public ResultAbstractTab(IModel title, String lemma ,String content)
      {
        super(title);
        this.title = lemma;
        this.content = content;
      }
      
      public Panel getPanel(String panelId)
      {
        return new ResultTabPanel(panelId, title, content);
      }
    }
    
    private final class MessageForm extends Form
    {
      private AutoCompleteTextField searchTextField;
      
      public MessageForm(String id)
      {
        super(id);
        
        searchTextField = new AutoCompleteTextField("txtSearch", new Model()) {
          protected Iterator getChoices(String input) {
            List<String> choices = new ArrayList<String>();
            
            if(input.length() < 3)
            {
              return choices.iterator();
            }
            
            List<String[]> matches = _dic.getMatchingLemma(input);
            
            //choices = matches;
            int i=0;
            Iterator<String[]> it = matches.iterator();
            while(it.hasNext() && i < 25)
            {
              choices.add(it.next()[0]);
              i++;
            }
            
            return choices.iterator();
          }
        };
        
        
        add(searchTextField);        
      }
      
      public String getSearchedText()
      {
        return searchTextField.getModelObjectAsString();
      }   
  }
    
}

