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

import wicket.markup.html.basic.*;
import wicket.markup.html.form.*;
import wicket.model.*;
import wicket.Component;
import wicket.markup.html.link.*;
import wicket.extensions.ajax.markup.html.autocomplete.*;

import de.gidoo.owl2.base.*;

/**
 * A page to search in and display items of a dictonary
 *
 * @author <a href="mailto:krause@informatik.hu-berlin.de">Thomas Krause</a>
 */
public class OwlReader extends wicket.markup.html.WebPage {

    private IDictionaryProvider _dic;
    
    /** Creates a new instance of OwlReader */
    public OwlReader() {
     
      _dic = new SQLiteProvider(OwlApp.realPathToContext + "owl.db");
     
      if(!_dic.isImported("test"))
        _dic.importDictionary(OwlApp.realPathToContext + "test.dicml", "test");
      
      _dic.activateDictionary("test");
      
      IModel model = new Model();
      
      add(new MessageForm("form", model));
      
      add(new Label("label", model));
      
        
    }
    
    
    private final class MessageForm extends Form
    {
      public MessageForm(String id, IModel model)
      {
        super(id);
        //add(new TextField("txtSearch", model));
        
        AutoCompleteTextField textField = new AutoCompleteTextField("txtSearch") {
          protected Iterator getChoices(String input) {
            List<String> choices = new ArrayList<String>();
            
            if(input.length() < 3)
            {
              return choices.iterator();
            }
            
            List<String> matches = _dic.getMatchingLemma(input);
            
            choices = matches;
            
            return choices.iterator();
          }
        };
        
        textField.setModel(model);
        
        add(textField);
        
      }
    }
    
}

