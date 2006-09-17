/*
 * DicsToImportPanel.java
 *
 * Created on 7. September 2006, 09:50
 *
 * See Licence.txt for more information according to this file.
 */

package de.gidoo.owl2.web.reader;

import de.gidoo.owl2.base.IDictionaryProvider;
import de.gidoo.owl2.base.SQLiteProvider;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import wicket.markup.html.form.Button;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.Radio;
import wicket.markup.html.form.RadioGroup;
import wicket.markup.html.form.SubmitLink;
import wicket.markup.html.form.TextField;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.*;
import wicket.markup.html.basic.*;
import wicket.markup.html.link.Link;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.util.time.Duration;

/**
 * Presents a list of all dicML files inside a special folder of the server
 * and let the admin import them.
 * @author <a href="mailto:krause@informatik.hu-berlin.de">Thomas Krause</a>
 */
public class DicsToImportPanel extends wicket.markup.html.panel.Panel {
  
  
  private static int _importDone = 0;
  
  private Label _lblProgress;
  
  private WorkerThread _worker;
  private Thread _thread;
  
  private TextField _txtName;      
  private RadioGroup _fileGroup;
  private Link _lnkAbort;
  private final List _dictList = new Vector();
  private final ListView _lstDicts;

  private static int count = 0;
  
  /**
   * Creates a new instance of DicsToImportPanel
   */
  public DicsToImportPanel(String id) 
  {
    super(id);
    
    _dictList.clear();
    
    updateDicList();
    
    Form form = new InputForm("formImport");
    add(form);
    
    _fileGroup = new RadioGroup("rgFile");
    _fileGroup.setModel(new Model());
    
    _lstDicts = new ListView("listView", _dictList) 
    {
      protected void populateItem(ListItem item) 
      {
        final File f = (File) item.getModelObject();
        item.add(new Radio("radFileName", new Model(f.getName())));
        item.add(new Label("lblFileName", new Model(f.getName())));
        
      }
    };
       
    _fileGroup.add(_lstDicts);
    form.add(_fileGroup);
    
    add(new FeedbackPanel("feedback"));
    
    _lblProgress = new Label("lblProgress", new PropertyModel(this, "progress"));
    _lblProgress.setVisible(false);
    add(_lblProgress);
    _lblProgress.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(1)));
    
    _txtName = new TextField("txtName");
    _txtName.setModel(new Model(""));
    form.add(_txtName);
    
    form.add(new Button("btImport", new Model("Import")));
    
    _lnkAbort = new Link("lnkAbort") {
      public void onClick() 
      {
        if(_thread != null && _thread.isAlive())
        {
          _lnkAbort.setVisible(false);
          _lblProgress.setVisible(false);
          _worker.getDictionaryProvider().setStopImporting(true);
        }
      }
    };
    _lnkAbort.setVisible(false);
    add(_lnkAbort);
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
  
  private final void importDict()
  {
    String name = _txtName.getModelObjectAsString();
    String file = _fileGroup.getModelObjectAsString();
    
    boolean e = false;
    if(name == null || "".equals(name))
    {
      error("You have to insert a name for the dictionary.");
      e = true;
    }
    
    
    if(file == null || "".equals(file))
    {
      error("You have to select one of the dictionaries.");
      e = true;
    }
    
    if(e)
    {
       _lnkAbort.setVisible(false);
       _lblProgress.setVisible(false);
      return;
    }
    

    _lnkAbort.setVisible(true);
    _lblProgress.setVisible(true);
     
    _worker = new WorkerThread(OwlApp.getDicProvider(), 
      OwlApp.realPathToContext + "WEB-INF/dicts/" + file, name);
    
    _thread = new Thread(_worker);
    _thread.start();
  }
  
  public String getProgress()
  {
    double p;
    if(_worker != null && _thread.isAlive())
    {
      p = _worker.getDictionaryProvider().getImportingProgress();
      if(p > -1)
      {
        return "progress: " + String.format("%.2f", p) + "%";
      }
    }
    else if(_worker != null && isHeadRendered())
    {
      _lnkAbort.setVisible(false);
      _lblProgress.setVisible(false);
    }
    return "finishing, please wait...";
  }


  protected void finalize()
  {
      // abort if necessary
    if(_thread != null && _thread.isAlive() && _worker != null)
    {
      _worker.getDictionaryProvider().setStopImporting(true);
    }
  
  }
  
  private class InputForm extends Form
  {
  
    public InputForm(String id)
    {
      super(id);                
    }    
    

    protected void onSubmit() 
    {
      if(_thread != null && _thread.isAlive())
      {
        error("You have to wait until the import-process is ready or abort it.");
        _lnkAbort.setVisible(false);
       _lblProgress.setVisible(false);
      }
      else
      {
        importDict();
      }
    }
    
    
  }
  
  
  private class WorkerThread implements Runnable
  {
    String _path;
    String _name;
    IDictionaryProvider _dic;
    
    public WorkerThread(IDictionaryProvider dicProvider, String pathDicToImport, String name)
    {
      _path = pathDicToImport;
      _name = name;
      _dic = dicProvider;
    }
    
    public void run() 
    {
      _dic.setStopImporting(false);
      _dic.importDictionary(_path, _name);
    }
    
    public IDictionaryProvider getDictionaryProvider()
    {
      return _dic;
    }

  }
  
}
