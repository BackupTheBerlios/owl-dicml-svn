/*
 * OwlReader.java
 *
 * Created on July 17, 2006, 9:17 PM
 *
 * See Licence.txt for more information according to this file.
 */

package de.gidoo.owl2.web.reader;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;
import org.apache.tools.ant.Target;
import wicket.PageParameters;
import wicket.Session;
import wicket.ajax.AbstractAjaxTimerBehavior;
import wicket.ajax.AjaxEventBehavior;
import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import wicket.ajax.calldecorator.AjaxCallDecorator;
import wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import wicket.ajax.form.AjaxFormSubmitBehavior;
import wicket.ajax.form.AjaxFormValidatingBehavior;
import wicket.ajax.markup.html.form.AjaxSubmitButton;
import wicket.extensions.markup.html.tabs.AbstractTab;

import wicket.markup.html.basic.*;
import wicket.markup.html.form.*;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.markup.html.panel.Panel;
import wicket.markup.html.resources.JavaScriptReference;
import wicket.model.*;
import wicket.Component;
import wicket.markup.html.link.*;
import wicket.extensions.ajax.markup.html.autocomplete.*;
import wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import wicket.extensions.markup.html.tabs.AbstractTab;

import de.gidoo.owl2.base.*;
import wicket.protocol.http.ClientProperties;
import wicket.protocol.http.request.WebClientInfo;
import wicket.request.ClientInfo;
import wicket.util.time.Duration;

/**
 * A page to search in and display items of a dictonary
 *
 * @author <a href="mailto:krause@informatik.hu-berlin.de">Thomas Krause</a>
 */
public class OwlReader extends wicket.markup.html.WebPage {

  private IDictionaryProvider _dic;
  private MessageForm _mainForm;
  private AjaxTabbedPanel _tabPanel;
  private DropDownChoice _dropDic;
  private AutoCompleteTextField _txtSearch;
  private Button _btSearch;

  private FeedbackPanel _feedback;
  
  private AbstractDic _curDictionary;

  /** Creates a new instance of OwlReader */
  public OwlReader() {
  
    _dic = OwlApp.getDicProvider();

    _mainForm = new MessageForm("form");
    add(_mainForm);
    _mainForm.init();

    add(new Label("lblTitle", getString("lblTitle")));

    Link lnkAdmin = new Link("lnkAdmin") 
    {
      public void onClick() 
      {
        setResponsePage(Admin.class);
      }
    };

    lnkAdmin.add(new Label("lblLinkAdmin", getString("lblLinkAdmin")));

    add(lnkAdmin);
    
    Link lnkEnglish = new Link("lnkEnglish")
    {
      public void onClick()
      {
        getSession().setLocale(Locale.ENGLISH);
        setResponsePage(OwlReader.class);
      }
    };
    
    add(lnkEnglish);
    
    Link lnkDeutsch = new Link("lnkDeutsch")
    {
      public void onClick()
      {
        getSession().setLocale(Locale.GERMAN);
        setResponsePage(OwlReader.class);
      }
    };
    
    add(lnkDeutsch);
    _feedback = new FeedbackPanel("feedback");
    add(_feedback);
    
    add(new Label("lblHeadline", new Model(getString("lblHeadline"))));
    
    ClientProperties clientProp = ((WebClientInfo) getRequestCycle().getClientInfo()).getProperties();
    if("false".equals(clientProp.get(ClientProperties.NAVIGATOR_JAVA_ENABLED).toString()))
    {
      error(getString("errorJavaScriptNeeded"));
    }

    warn("pre-beta modus, not for public use!");
  }

  
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
      return new ResultTabPanel(panelId, title, content, true);
    }
  }

  private final class MessageForm extends Form
  {

    public MessageForm(String id)
    {
      super(id);
     
      List dics = new ArrayList();
      String[] s = _dic.getAvailableDictionaries();
      for(int i=0; i<s.length; i++)
      {
        dics.add(new AbstractDic(s[i]));
      }
      
      
      
      _dropDic = new DropDownChoice("dropDic", new PropertyModel(this, "selectedDic", AbstractDic.class), dics);
      _dropDic.setNullValid(false);
      if(s.length > 0)
      {
        _dropDic.setModelObject(dics.get(0));
      }
      
      _dropDic.add(new AjaxFormSubmitBehavior(this, "onchange")
      {
        protected void onSubmit(AjaxRequestTarget target) 
        {
          _txtSearch.setModelValue("");
          target.addComponent(_txtSearch);
        }
      });
             
      _txtSearch = new AutoCompleteTextField("txtSearch", new Model(""))
      {
        protected Iterator getChoices(String input) {
          List<String> choices = new ArrayList<String>();

          if(input.length() < 3)
          {
            return choices.iterator();
          }

          List<String[]> matches = _dic.getMatchingLemma(input, _curDictionary.shortName);

          //choices = matches;
          int i=0;
          Iterator<String[]> it = matches.iterator();
          while(it.hasNext() && i < 30)
          {
            choices.add(it.next()[0]);
            i++;
          }
          
          
          return choices.iterator();
        }
      };
         
//      _txtSearch.add(new AjaxFormSubmitBehavior(this, "onkeydown") 
//      {
//        protected void onSubmit(AjaxRequestTarget target) 
//        {
//          updateResults(target);
//        }
//      });
      
      
      _btSearch = new Button("btSearch", new Model(getString("btSearch")));
                 
   
      // empty tab-list at begin
      _tabPanel = getAvailableTabs();
      add(_tabPanel);
       
      add(_dropDic);
      add(_txtSearch);     
      add(new Label("lblSearchFor", getString("lblSearchFor")));
      add(_btSearch);
     
      AjaxFormSubmitBehavior b = new AjaxFormSubmitBehavior(this, "onkeyup")
      {
        protected void onSubmit(AjaxRequestTarget target)
        {
          updateResults(target);
        }
      };
      
      b.setThrottleDelay(Duration.seconds(1));
      
      this.add(b);
      
      _btSearch.add(new AjaxFormSubmitBehavior(this, "onclick")        
      {
        protected void onSubmit(AjaxRequestTarget target)
        {
          updateResults(target);
        }
      });
    } 

    /** Do initialisations and checks which have to be done after the Form was added to a page*/
    public void init()
    {
      
      // check for a very special error
      if(_dic != null && _dic.getAvailableDictionaries().length == 0)
      {
        error(getString("errorNoDicAvailable"));
      }
    }

    public void updateResults(AjaxRequestTarget target)
    {
      _tabPanel = getAvailableTabs();
      replace(_tabPanel);

      target.addComponent(_tabPanel);
    }
    
    
    public AjaxTabbedPanel getAvailableTabs()
    {      
      List l = new ArrayList();
     
      // get the searched text
      final String search = _txtSearch.getModelObjectAsString(); 
            
      if(search == null || search.equals(""))
      {
        l.add(new AbstractTab(new Model(getString("info")))
          {
            public Panel getPanel(String panelId) {return new ResultTabPanel(panelId, "", getString("noSearchText"), false);}
          }
        );
        
        return new AjaxTabbedPanel("tabs", l);
      }
      
      final String[][] results = _dic.getEntry(search, _curDictionary.shortName);
      
      if(results == null || results.length == 0)
      {
        l.add(new AbstractTab(new Model(getString("error")))
          {
            public Panel getPanel(String panelId) {return new ResultTabPanel(panelId, "", getString("nothingFound"), false);}
          }
        );
        
        return new AjaxTabbedPanel("tabs", l);
      }
      
      for(int i=0; i < results.length; i++)
      {
        l.add(new ResultAbstractTab(new Model("" + (i+1) + ""), search, results[i][0]));
      }
      AjaxTabbedPanel p = new AjaxTabbedPanel("tabs", l);
      return  p;
    }



    public void setSelectedDic(AbstractDic dic)
    {
      _curDictionary = dic;
    }

    public AbstractDic getSelectedDic()
    {
      if(_curDictionary == null)
      {
        String[] d = _dic.getAvailableDictionaries();
        if(d.length > 0)
        {
          AbstractDic dic = new AbstractDic(d[0]);
          _curDictionary = dic;
          return _curDictionary;
        }
        else
        {
          error(getString("errorNoDicAvailable"));
          return null;
        }
      }
      else
      {
        return _curDictionary;
      }
    }
  }
  
  public class AbstractDic
  {
    
//    public AbstractDic()
//    {
//      shortName = "";
//    }
    
    public AbstractDic(String shortName)
    {
      this.shortName = shortName;
    }
    
    public String shortName;

    public String toString() 
    {
      return _dic.getTitle(shortName);
    }
    
  }
}

