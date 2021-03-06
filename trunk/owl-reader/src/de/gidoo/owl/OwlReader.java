/**
 * OwlReader.java
 *
 * (c) 2004-2005 by Thomas Krause
 * All rights reserved.
 *
 * http://owl.gidoo.de/source
 */


package de.gidoo.owl;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import javax.swing.event.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.*;
import javax.swing.text.*;
import java.io.*;
import java.text.*;
import javax.xml.transform.*;
import java.net.*;


/**
 * OwlReader represents the main-window of owl
 * @author Thomas Krause
 */
class OwlReader extends JFrame 
                implements ActionListener, ListSelectionListener,
                    DocumentListener, WindowListener
{
    /** The list with the entries of the dictionary. */
    private JList listView;
    /** A textfield to enter the search-text in. */
    private JTextField tSearch;
    /** Parsing and rendering of an entry. */
    private DicmlRead tDicml;
    /** The main menu-bar. */
    private JMenuBar mainMenuBar;
    /** A JSplitPane with panelLeft at the left side and tDicml at the right one. */
    private JSplitPane splitPaneMain;
    /** A  JPanel with the tSearch text-field at the top an the listView containing
     * the entries below.
     */
    private JPanel panelLeft;
            
    /**
     * Contains values about the current state of the application. 
     * Will be loaded at begin and saved at the end.
     */
    private Properties props;
    /** Used to get internationalized strings. */
    private ResourceBundle i18n;
    /** The iso2 language-code used for the user interface. */
    private String uiLanguage;
    /** The Locale used for the user interface. */
    private Locale uiLocale;
    
    /** A menu-item in the options-menu telling wether to load the dictionary at next startup */
    private JCheckBoxMenuItem miLoadAtStartup;
    
    /** Will pop up on exceptions. **/
    private ErrorDialog errorDlg;
    
    private String lemmaName[];
    private long lemmaBegin[], lemmaEnd[];
    
    /** The language of the source-language of the current dictionary. */
    private String sourceLanguage;
    /** The AlphabetOrder for the source-language of the current dictionary. */
    private AlphabetOrder alphOrder;
    /** The dicml-file with the current dictionary for random access. */
    private RandomAccessFile dicmlAccess;
    /** The dicml-file with the current dictionary */
    private File dicmlFile;
    
    private ClassLoader loader;
    
    // for fillToSize and restoreSize
    private int fillDividerSize;
    private int fillHeight;
    private int fillWidth;
    private boolean fillWasFilled = false;
    
    /**
     * Create a new instance of OwlReader
     * @exception java.io.IOException
     * @param loadDicFile Wether to load a dictionary at start-up
     * @param pathToDicFile When loadDicFile is true this is the dictionary to load
     */
    OwlReader(boolean loadDicFile, String pathToDicFile) throws java.io.IOException 
    {
        // ugly workaround for problems under Gnome 2.12.0
        // (seems like a Gnome or JVM problem for me)
        super("owl");
      
        loader = this.getClass().getClassLoader();
        
        //i18n
        changeLanguage("en", null,  null); // first call - fallback
        
        sourceLanguage = "en"; // English by default

        // load properties
        try 
        {                                 
            props = new Properties();
            props.loadFromXML(new FileInputStream(
                    System.getProperty("user.home") + "/.owl/owl-reader.config"));
        }
        catch (Exception e)
        {   
            // only show message, if not first use
            File f = new File(System.getProperty("user.home") + "/.owl/foo");
            if(f.exists())
            {
              errorDlg.showError(i18n.getString("errorLoadConf"), e);
            }
            try
            {
              // create a new property-file
              java.io.File cf = new java.io.File(System.getProperty("user.home") + "/owl-reader.config");
              cf.createNewFile();
                            
            }
            catch(IOException ioE)
            {
              errorDlg.showError(i18n.getString("errorSaveConf"), ioE);
            }
        }
        
        // when first-use, ui-install-language is set (ISO3-Code)
        String installLang = props.getProperty("ui-install-language", "eng");
        if(!installLang.equals("nothing"))
        {
          if(installLang.equals("eng"))
          {
            props.setProperty("ui-language", "en");
          }
          else if(installLang.equals("deu"))
          {
            props.setProperty("ui-language", "de");
          }
          else if(installLang.equals("fra"))
          {
            props.setProperty("ui-language", "fr");
          }
          
          props.setProperty("ui-install-language", "nothing");
        }
        
        changeLanguage(props.getProperty("ui-language", "en"), null, null);
        
        // EVENTS
        this.addWindowListener(this);
        
        // GUI
       
        // the gui mainly has two panels (list and dicml) which are
        // integrated in a split-pane
        
        splitPaneMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        BorderLayout borderLayout = new BorderLayout();
        
        panelLeft = new JPanel(borderLayout);
        
        //init tSearch
        tSearch = new JTextField("");
        tSearch.getDocument().addDocumentListener(this);
        panelLeft.add(tSearch, BorderLayout.NORTH);
                
        //init listView
        String[] LISTDATA = {""};
        listView = new JList(LISTDATA);
        listView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listView.addListSelectionListener(this);
        JScrollPane scrollPane = new JScrollPane(listView);
        panelLeft.add(scrollPane, BorderLayout.CENTER);
        
        splitPaneMain.add(panelLeft);
        
        //init tDicml
        tDicml = new DicmlRead(i18n);
        splitPaneMain.add(tDicml);
        
        add(splitPaneMain);
        int splitPos = Integer.parseInt(props.getProperty("splitterPos", "150"));
        splitPaneMain.setDividerLocation(splitPos);
        splitPaneMain.setDividerSize(5);
        
        //finalize
        pack();
        
        //init menubars
        mainMenuBar = new JMenuBar();
        mainMenuBar.add(createMainMenu());
        mainMenuBar.add(createOptionsMenu());
        
                
        //init window
        setJMenuBar(mainMenuBar);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(i18n.getString("progName") + " " + i18n.getString("progVers"));
        
        if(props.getProperty("window.Maximized", "false").equalsIgnoreCase("true"))
        {
          // window is maximized
          setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        }
        int width = Integer.parseInt(props.getProperty("window.Width", "450"));
        int height = Integer.parseInt(props.getProperty("window.Height", "270"));
        setSize(width, height);
        
        int posX = Integer.parseInt(props.getProperty("windowPos.X", "0"));
        int posY = Integer.parseInt(props.getProperty("windowPos.Y", "0"));
        setLocation(posX, posY);

        // set the icon
        Image icon = Toolkit.getDefaultToolkit().getImage("res/icon16.png");
        setIconImage(icon);
        
        setVisible(true);
                
        try 
        {                                 
          // when first use, show info-dialog
          File file = new File(System.getProperty("user.home") + "/.owl/foo");
          if(!file.exists())
          {
            InfoDialog dlg = new InfoDialog(this, true, i18n);
            dlg.setVisible(true);
            
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.close();
          }
        }
        catch (Exception e)
        {
            errorDlg.showError(i18n.getString("errorLoadConf"), e);
        } 
        
        if(loadDicFile)
        {
          // load a dictionary given at the command-line
          File dicFile = new File(pathToDicFile);
          openDicFile(dicFile);
        }
        else
        {
          // check-out wether a dictionary from the last session should be loaded
          boolean val = Boolean.parseBoolean(props.getProperty("load-at-start-up", "false"));
          if(val)
          {
            File dicFile = new File(props.getProperty("last-dict"), "");
            if(dicFile.exists())
            {
              openDicFile(dicFile);
            }
          }
        }
    }
    
    /**
     * Collect values and store them in the property-file.
     */
    private void saveConfig()
    {
      // get all interesting values
      Rectangle wndRect = this.getBounds();
      props.setProperty("windowPos.X", Integer.toString(wndRect.x));
      props.setProperty("windowPos.Y", Integer.toString(wndRect.y));
      props.setProperty("window.Width", Integer.toString(wndRect.width));
      props.setProperty("window.Height", Integer.toString(wndRect.height));
      if(getExtendedState() == java.awt.Frame.MAXIMIZED_BOTH)
      {
        props.setProperty("window.Maximized", "true");
      }
      else
      {
        props.setProperty("window.Maximized", "false");
      }
      
      props.setProperty("splitterPos", Integer.toString(splitPaneMain.getDividerLocation()));
      props.setProperty("ui-language", uiLanguage);
      
      if(dicmlFile != null)
      {
        props.setProperty("last-dict", dicmlFile.getAbsolutePath());
      }
      
      // store them
      try
      {
        props.storeToXML(new FileOutputStream(
                System.getProperty("user.home") + "/.owl/owl-reader.config"),
                "changes since last exit");
      }
      catch(Exception e)
      {
        errorDlg.showError(i18n.getString("errorSaveConf"), e);
      }
    }
    
    /**
     * Create the main-menu and connect the items to actions.
     */
    private JMenu createMainMenu() {
        
        ImageIcon icon;
      
        // main-menu //
      
        JMenu menu = new JMenu(i18n.getString("menuMenu"));
        menu.setMnemonic(i18n.getString("menuMenuChar").charAt(0));
        JMenuItem mi;

        //open dictionary
        mi = new JMenuItem(i18n.getString("menuOpen"));
        mi.setMnemonic(i18n.getString("menuOpenChar").charAt(0));
        mi.addActionListener(this);
        mi.setActionCommand("opendic");
        icon = new ImageIcon("res/Open16.gif");
        mi.setIcon(icon);
        menu.add(mi);
        
        // printing
        mi = new JMenuItem(i18n.getString("menuPrint"));
        mi.setMnemonic(i18n.getString("menuPrintChar").charAt(0));
        mi.addActionListener(this);
        mi.setActionCommand("print");
        icon = new ImageIcon("res/Print16.gif");
        mi.setIcon(icon);
        menu.add(mi);
        
        //infos
        mi = new JMenuItem(i18n.getString("menuInfo"));
        mi.setMnemonic(i18n.getString("menuInfoChar").charAt(0));
        mi.addActionListener(this);
        mi.setActionCommand("info");
        icon = new ImageIcon("res/Information16.gif");
        mi.setIcon(icon);
        menu.add(mi);
        
        //quit
        mi = new JMenuItem(i18n.getString("menuQuit"));
        mi.setMnemonic(i18n.getString("menuQuitChar").charAt(0));
        mi.addActionListener(this);
        mi.setActionCommand("quit");
        icon = new ImageIcon("res/Stop16.gif");
        mi.setIcon(icon);
        menu.add(mi);
        return menu;
    }
    
    /**
     * Create the options-menu and connect the items to actions.
     */
    private JMenu createOptionsMenu()
    {
        JMenu menu = new JMenu(i18n.getString("menuOptions"));
        menu.setMnemonic(i18n.getString("menuOptionsChar").charAt(0));
        
        // the available languages
        JMenu menuLanguage = new JMenu(i18n.getString("menuLanguage"));
        menuLanguage.setMnemonic(i18n.getString("menuLanguageChar").charAt(0));
        
        // Englisch
        JMenuItem smi = new JMenuItem(i18n.getString("menuChooseEnglish"));
        smi.setMnemonic(i18n.getString("menuChooseEnglishChar").charAt(0));
        smi.addActionListener(this);
        smi.setActionCommand("chooseEnglisch");
        menuLanguage.add(smi);
        
        // German
        smi = new JMenuItem(i18n.getString("menuChooseGerman"));
        smi.setMnemonic(i18n.getString("menuChooseGermanChar").charAt(0));
        smi.addActionListener(this);
        smi.setActionCommand("chooseGerman");
        menuLanguage.add(smi);
                
        // French
        smi = new JMenuItem(i18n.getString("menuChooseFrench"));
        smi.setMnemonic(i18n.getString("menuChooseFrenchChar").charAt(0));
        smi.addActionListener(this);
        smi.setActionCommand("chooseFrench");
        menuLanguage.add(smi);
        
        menu.add(menuLanguage);
        
        // mark the current language
        int n = 0;
        if(uiLanguage.equals("en"))
        {
          n = 0;
        }
        else if(uiLanguage.equals("de"))
        {
          n = 1;
        }
        else if(uiLanguage.equals("fr"))
        {
          n = 2;
        }
        menuLanguage.getMenuComponent(n).setEnabled(false);
        
        // the "force indexing"
        JMenuItem mi = new JMenuItem(i18n.getString("menuForceIndex"));
        mi.setMnemonic(i18n.getString("menuForceIndexChar").charAt(0));
        mi.addActionListener(this);
        mi.setActionCommand("forceindex");
       
        menu.add(mi);
        
        // the "load this dictionary at next start-up"
        miLoadAtStartup = new JCheckBoxMenuItem(i18n.getString("menuLoadAtStartup"));
        miLoadAtStartup.setMnemonic(i18n.getString("menuLoadAtStartupChar").charAt(0));
        miLoadAtStartup.addActionListener(this);
        miLoadAtStartup.setActionCommand("loadAtStartup");
        boolean val = Boolean.parseBoolean(props.getProperty("load-at-start-up", "false"));
        miLoadAtStartup.setSelected(val);
        
        menu.add(miLoadAtStartup);
        
        return menu;
    }
    
    /**
     * Ask the user for the file to open and looks wether this file has to be 
     * indexed. 
     */
    private void openDictionary() {
        File fileDicml;
        String pathId;
        File fileId;
        IndexDicml indexDialog;
        
        //show file-selector
        
        //TODO: add copyright notice to documentation (IMPORTANT!!! :-) )
        ExampleFileFilter filter = new ExampleFileFilter();
        filter.addExtension("dicml");
        filter.setDescription(i18n.getString("filechooseDicml"));
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        
        File parentDir = new File(props.getProperty("last-dir",System.getProperty("user.home")));
        fileChooser.setCurrentDirectory(parentDir);
        
        if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ) {
            fileDicml = fileChooser.getSelectedFile();
            pathId = fileDicml.getAbsolutePath() + ".id";
            fileId = new File(pathId);
            if(fileId.exists()) {
                openDicFile(fileDicml);
                
            } else {
                //ask the user
                if(JOptionPane.showConfirmDialog(this, MessageFormat.format(i18n.getString("filechooseQuestion"), fileDicml.getName()), i18n.getString("progName"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    //show indexing dialog
                    indexDialog = new IndexDicml(fileDicml.getAbsolutePath() ,this, true, i18n);
                    
                    if(!indexDialog.errorOccured)
                    {
                      openDicFile(fileDicml);
                    }
                }
            }
            // save the last dance, eh dir
            props.setProperty("last-dir", fileDicml.getParent());            
        }
    }
    
    /**
     * Open a given file, set the new source-language and show the first entry.
     * @param f the file which shall be opened
     */
    public void openDicFile(File f) {
        LineNumberReader idReader;
        String line;
        int lineCount=0;
        int entryCount=0;
        
        try {
            if(dicmlAccess != null) {
                //close old dicml-file
                dicmlAccess.close();
            }
            //open dicml-file for accessing
            dicmlAccess = new RandomAccessFile(f, "r");
            
            //count the lines
            idReader = new LineNumberReader(new InputStreamReader(new FileInputStream(f.getAbsolutePath() + ".id"), "UTF-8"));

            while(idReader.readLine() != null)
            {
              lineCount++;
            }    
            
            idReader.close();
                        
            idReader = new LineNumberReader(new InputStreamReader(new FileInputStream(f.getAbsolutePath() + ".id"), "UTF-8"));
            
            entryCount = (lineCount-1) / 3;
            
            // get the source language
            sourceLanguage = idReader.readLine().trim();
            
            // reset the title
            setTitle(i18n.getString("progName") + " " + i18n.getString("progVers")
                          + " | " +f.getName());
            
            // init the alphabetical Order
            alphOrder = new AlphabetOrder(new File("res/" + sourceLanguage + ".order.xml"));
            //alphOrder = new AlphabetOrder(new Locale(sourceLanguage));
            //read in the list of entries
            
            //init arrays
            lemmaName = new String[entryCount];
            lemmaBegin = new long[entryCount];
            lemmaEnd = new long[entryCount];
            
            
            for(int x = 0; x < entryCount; x++) {
                
                //add entry to array
                
                line = idReader.readLine();
                lemmaName[x] = line;
                line = idReader.readLine();
                lemmaBegin[x] = Long.parseLong(line);
                line = idReader.readLine();
                lemmaEnd[x] = Long.parseLong(line);
                
            }

            // select first entry and delete old text
            tSearch.setText("");
            listView.clearSelection();
            try
            {
              listView.setListData(lemmaName);
            }
            catch(IndexOutOfBoundsException bE)
            {
              //some kind of selection error, just ignore it
            }
            idReader.close();
            
            listView.setSelectedIndex(0);
            
            this.dicmlFile = f;
                
        } catch(Exception e) {
            errorDlg.showError(i18n.getString("errorLoadWordbook"), e);
        }
    }
    
    
    /**
     * Reacts on various actions performed e.g. by the menu-items.
     * @param event the event that was thrown
     */
    public void actionPerformed(ActionEvent event)
    {
        String cmd = event.getActionCommand();
        if(cmd.equals("opendic")) {
            openDictionary();
        } else if(cmd.equals("print")) {
            printEntry();
        } else if(cmd.equals("info")) {
            //show info dialog
            InfoDialog dlg = new InfoDialog(this, true, i18n);
            dlg.setVisible(true);
            //JOptionPane.showMessageDialog(this, MessageFormat.format(i18n.getString("infoText"),  i18n.getString("progName"), i18n.getString("progVers")), i18n.getString("infoTitle"),JOptionPane.INFORMATION_MESSAGE);
        } else if(cmd.equals("quit")) {
            //quit
            saveConfig();
            System.exit(0);
        }
        else if(cmd.equals("chooseEnglisch")) {
          changeLanguage(null, null, null);
        }
        else if(cmd.equals("chooseGerman")) {
          changeLanguage("de", null, null);
        }        
        else if(cmd.equals("chooseFrench")) {
          changeLanguage("fr", null, null);
        }
        else if(cmd.equals("forceindex")) {
          forceIndex();
        }
        else if(cmd.equals("loadAtStartup"))
        {
          miLoadAtStartup.setSelected(miLoadAtStartup.isSelected());
          props.setProperty("load-at-start-up", "" + miLoadAtStartup.isSelected());
          
        }
    }
    
    /**
     * Changes the language.
     * Will load the new resource-file, re-build the menu and reload DicmlRead-component.
     * @param language the iso2-language code: is needed
     * @param country specifiy a country if you want to, may be null if "variant" is null
     * @param variant specifiy a language-variant if you want to, may be null
     */
    private void changeLanguage(String language, String country, String variant)
    { 
      try {
          
          if(language == null)
          {
            language = "en";
          }
          
          uiLanguage = language;
          
          if(country == null)
          {
            uiLocale = new Locale(language);
          }
          else if(variant == null)
          {
            uiLocale = new Locale(language, country);
          }
          else
          {
            uiLocale = new Locale(language, country, variant);
          }
          i18n = ResourceBundle.getBundle("de.gidoo.owl.OwlReader", uiLocale);
          
        } catch (Exception e) {
            String msg_en =   "Error when reading the language file.\nThe programm will exit, since this is a serious error.";
            String msg_de =   "Beim Einlesen der Sprachdatei ist ein Fehler aufgetreten.\nDa dies ein schwerwiegender Fehler ist, wird das Programm beendet.";
            String msg_line = "----------------------------------------------------------------------------------------------------------------";
            JOptionPane.showMessageDialog(this, msg_en + "\n" + msg_line + "\n" + msg_de + "\n" + msg_line + "\n" + e.getLocalizedMessage() , "ERROR | FEHLER", JOptionPane.ERROR_MESSAGE);
            System.exit(2);
        }
        
        // update the error-dialog
        errorDlg = new ErrorDialog(this, true, i18n);
        
        // update the menu
        if(mainMenuBar != null)
        {
          mainMenuBar.removeAll();
          mainMenuBar.add(createMainMenu());
          mainMenuBar.add(createOptionsMenu());
          mainMenuBar.revalidate();
        } 
        
        // update DicmlRead
        if(tDicml != null)
        {
          int dividerLoc = splitPaneMain.getDividerLocation();
          splitPaneMain.remove(tDicml);
          tDicml = new DicmlRead(i18n);
          splitPaneMain.add(tDicml);
          splitPaneMain.setDividerLocation(dividerLoc);
          // reselect an entry
          if(listView.getSelectedIndex() > -1)
          {
            setEntry(listView.getSelectedIndex(), tDicml);
          }
        }
      
    }
    
    /**
     * Print an entry using the PrintHTML-class. 
     */
    public void printEntry()
    {
      try
      {
        if(dicmlAccess != null)
        {
          PrintHTML printer = new PrintHTML();
          if(printer.print(tDicml.getPage()))
          {
            JOptionPane.showMessageDialog(this, i18n.getString("printingFinished"),
                                      i18n.getString("printingInfoTitle"),
                                      JOptionPane.INFORMATION_MESSAGE);
          }
        }
        else
        {
          JOptionPane.showMessageDialog(this, i18n.getString("errorNotLoadedYet"), i18n.getString("errorTitleWarning"), JOptionPane.WARNING_MESSAGE);
        }
      }
      catch(Exception e)
      {
        errorDlg.showError(i18n.getString("errorPrinting"), e);
      }
    }
    
    /** Will delete an old index-file and re-index the dictionary */
    private void forceIndex()
    {
      if(dicmlFile == null)
      {
        // no dictionary is opened
        JOptionPane.showMessageDialog(this, i18n.getString("errorNotLoadedYet"), i18n.getString("errorTitleWarning"), JOptionPane.WARNING_MESSAGE);
        return;
      }
      // rename old file
      File idFile = new File(dicmlFile.getAbsolutePath() + ".id");
      File idBakFile = new File(dicmlFile.getAbsolutePath() + ".id.bak");
      idFile.renameTo(idBakFile);
      
      // index
      IndexDicml indexDialog = new IndexDicml(dicmlFile.getAbsolutePath() ,this, true, i18n);

      if(!indexDialog.errorOccured)
      {
        // delete backup
        idBakFile.delete();
        // open
        openDicFile(dicmlFile);
      }
      else
      {
        // restore backup
        idBakFile.renameTo(idFile);
      }
    }
    
    /**
     * Reacts on changes in the search-textfield.
     * Will try to find a matching entry in the list and will select it.
     */
    public void changedUpdate(DocumentEvent event)
    {
      searchForFirstEntry();
    }
    
    /**
     * Reacts when text in the search-textfield is removed.
     */
    public void removeUpdate(DocumentEvent event)
    {
      searchForFirstEntry();      
    }
    
    /**
     * Reacts on insert-actions in the search-textfield.
     */
    public void insertUpdate(DocumentEvent event)
    {
      searchForFirstEntry();
    }
    
    /**
     * Searches for the first entry which begins like the searched text.
     * @return wether a matching entry was found
     */
    private boolean searchForFirstEntry()
    {
      String text = tSearch.getText();
      Entry e1 = new Entry();
      Entry e2 = new Entry();
      
      if(text.equals("")) return false;
      
      // binary search - find first matching entry
      boolean found = false;
      int pivot = -1;
      int begin = 0;
      int end = lemmaName.length - 1;
      e1.compare = text;
      int compare;
      
      pivot = listView.getSelectedIndex();
      
      while(!found && begin >= 0 && end < lemmaName.length && begin <= end)
      {       
        e2.compare = lemmaName[pivot];
        compare = alphOrder.compare(e1, e2);
        
        if(compare < 0)
        {
          // search at the left side
          // begin = begin
          end = pivot-1;
        }
        else if(compare > 0)
        {
          // search at the right side
          // end = end
          begin = pivot + 1;
        }
        else
        {
          // found
          found = true;
        }
        
        if(compare != 0)
        {
          pivot = begin + ((end - begin) / 2);
        } 
      }

      listView.setSelectedIndex(pivot);
      listView.ensureIndexIsVisible(pivot);
      return found;
    }
    
    /**
     * Reacts to changes in the selection of the list by showing the new entry.
     */
    public void valueChanged(ListSelectionEvent event) {
        JList callingList;
        
        callingList =  (JList) event.getSource();
        
        setEntry(callingList.getSelectedIndex(), tDicml);
    }
    
    /**
     * Shows a selected entry
     * @param index the index of the selected entry
     * @param target the DicmlRead-component which shall be used for rendering
     */
    private void setEntry(int index, DicmlRead target)
    {
      byte[] stringBuffer;
      String entryString;
      
      if(lemmaBegin == null || index > lemmaBegin.length || index < 0) return;
        
      long begin = lemmaBegin[index];
      long length = (lemmaEnd[index] - begin) + 1;
      if(index > -1) {
          try {
              //slice out the entry
              stringBuffer = new byte[(int) length];
              dicmlAccess.seek(begin);
              dicmlAccess.readFully(stringBuffer, 0, (int) length);
              entryString = new String(stringBuffer, "UTF-8");
              target.parse(entryString);
          } catch (Exception e) {
              errorDlg.showError(i18n.getString("errorEntryInterpretation"), e);
          }
      }
    }

    
    /**
     * Events of WindowListener
     */
    
    public void windowOpened( WindowEvent e ) {}
    
    public void windowClosing( WindowEvent e )
    {
      // save all properties
      saveConfig();
    }
    
    public void windowClosed( WindowEvent e ) {}
    public void windowIconified( WindowEvent e ) {}
    public void windowDeiconified( WindowEvent e ) {}
    public void windowActivated( WindowEvent e ) {}
    public void windowDeactivated( WindowEvent e ) {}
    
    /**
     * The main-function.
     * Starts owl and shows the main-window.
     */
    public static void main(String[] args) throws java.io.IOException
    {
        OwlReader window;
        // set fonts to non-bold by standard
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        
        boolean loadDic = false;
        String pathDic = "";
        
        if(args.length >= 1)
        {
          loadDic = true;
          pathDic = args[0];
        }
        
        window = new OwlReader(loadDic, pathDic);
    }
}


